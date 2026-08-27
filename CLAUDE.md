# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

SlomFin — a personal finance web app (accounts, investment transactions, paychecks, portfolio/capital-gains reports) for a single user. Server-rendered JSF/PrimeFaces UI on top of a Spring Boot host, with DynamoDB as the primary datastore.

## Build & run

Maven project (`pom.xml`), Java 17, packaged as `war` but launched via the Spring Boot embedded-Tomcat plugin.

```
mvn clean package          # builds target/slomfin.war (finalName: slomfin)
mvn spring-boot:run        # runs the app directly
```

There are no tests in this repo (`src/test/java` is empty) and no lint/format tooling configured — don't invent a `mvn test` workflow.

Runtime requires two env vars for the TLS listener (see `src/main/resources/application.yml`): `KEYSTORE_LOCATION` and `KEYSTORE_PASS`, pointing at a `TomcatJava17.jks` keystore. The app serves HTTPS only, on port 8443, under context path `/slomfin`.

DynamoDB access uses a hard-coded local AWS named profile (`PaulsAmazon`, region `us-east-1` — see `DynamoUtil`), not env-based credentials, so this app effectively only runs fully against real data on the author's machine.

## Architecture

This is two web frameworks stacked in one app, and it's important to know which one owns what:

- **Spring Boot** (`com.pas.spring`, `Application.java`) hosts the servlet container, wires **Spring Security** (`SecurityConfig`) for form-login auth, and is the entry point. It does **not** provide Spring MVC controllers for the app's pages — `AppConfig`'s component scan explicitly excludes `@Controller` classes.
- **JSF (Jakarta Faces) + PrimeFaces**, bootstrapped manually by `MyWebAppInitializer` (a `ServletContextInitializer` that starts Weld/CDI and `FacesInitializer` by hand), owns the actual pages. Views are the `.xhtml` files directly under `src/main/webapp/` (one per screen: `transactionList.xhtml`, `accountAddUpdate.xhtml`, `reportCapitalGains.xhtml`, etc.), all wrapped by `template.xhtml`/`menubar.xhtml`.
- The two are bridged deliberately: `ELResolverInitializerServlet` wires JSF's EL resolver to Spring, and `SpringBean` / `SpringConfig`-style beans exist only to expose Spring-managed values (e.g. context path) into the JSF/CDI world.

**`SlomFinMain`** (`com.pas.beans.SlomFinMain`, ~4000 lines, `@Named("pc_SlomFinMain") @ApplicationScoped`) is the single application-scoped JSF managed bean backing nearly every page — form state, dropdown lists, action handlers, and render-flags (`renderTrx...` booleans) that drive conditional field visibility in the `.xhtml` forms all live here. When changing UI behavior, this is almost always the file to look in first, and grep for the specific `render*`/`selected*` field before assuming new state is needed.

**Persistence is DynamoDB**, accessed via the AWS SDK v2 enhanced client, not Spring Data:
- `DynamoUtil` — plain-Java (no Spring) singleton initializer for the DynamoDB clients, used because JSF startup happens outside the Spring context and can't read `application.yml`.
- `DynamoClients` — holds the `DynamoDbClient`/`DynamoDbEnhancedClient` pair.
- `com.pas.slomfin.dao.*DAO` classes (`AccountDAO`, `InvestmentDAO`, `TransactionDAO`, `PaydayDAO`, `PortfolioHistoryDAO`) — hand-written DAOs, each owning one DynamoDB table (table names hard-coded per DAO, e.g. `slomFinAccounts`), typically caching the full table in an in-memory `Map`/`List` since data volume is small.
- `@DynamoDbBean`-annotated entities (`DynamoTransaction`, and the beans in `com.pas.beans`) map directly to table items.
- `com.pas.dynamodb.archived.*` are one-off, no-longer-run migration scripts that seeded the DynamoDB tables from the old MySQL schema — historical reference only, not part of the live app.

**MySQL/Spring JDBC remnants**: `spring-jdbc` and a MySQL driver are still dependencies, and `*RowMapper` classes exist alongside the DAOs, but `DataSourceAutoConfiguration` is explicitly excluded in `Application.java` — there is no active datasource. Treat MySQL-related code as legacy from before the DynamoDB migration unless you find live wiring proving otherwise.

**Reports/exports**: capital gains, cost basis, portfolio history, dividends, etc. are rendered both as JSF views (`report*.xhtml`) and exported via `org.apache.poi` (Excel) and `openpdf` (PDF) — see `com.pas.pdfstuff` and usages in `SlomFinMain`.

## Working in this codebase

- New UI-visible state almost always belongs as a field on `SlomFinMain`, following its existing `selectedX` / `renderTrxX` / `xDropdownList` naming conventions.
- New persisted fields go on the relevant `@DynamoDbBean` entity plus the corresponding DAO; there's no schema migration step — DynamoDB is schemaless, so a new attribute just starts being written/read.
- `.xhtml` view files are plain JSF/PrimeFaces (`p:` components), not Thymeleaf/Facelets-with-Spring — action methods referenced via EL (`#{pc_SlomFinMain.someMethod}`) resolve straight onto `SlomFinMain`.
