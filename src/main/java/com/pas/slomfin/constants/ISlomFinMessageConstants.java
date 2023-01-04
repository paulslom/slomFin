package com.pas.slomfin.constants;

public interface ISlomFinMessageConstants
{
    //all these keys need to exist in the ApplicationResources file
	public static String LOGIN_USERLOGIN_FAILED = "login.userlogin.failed";
	public static String INVALID_TRANSACTION_DATE = "transaction.date.invalid";
	public static String INVALID_TRANSACTION_POSTED_DATE = "transactionPosted.date.invalid";
	public static String INVALID_FROM_DATE = "from.date.invalid";
	public static String INVALID_TO_DATE = "to.date.invalid";
	public static String NO_RECORDS_FOUND = "records.nonefound";
	
	//InvestmentsOwnedUpdateForm
	public static String INVOWNEDUPDATEFORM_PRICE_INVALID="investmentsOwnedUpdateform.PriceInvalid";
	public static String INVOWNEDUPDATEFORM_PRICE_MISSING="investmentsOwnedUpdateform.PriceMissing";
	
	//JobUpdateForm fields
	public static String JOBFORM_STARTDATE_FIELD_INVALID = "jobform.startdate.field.invalid";
	public static String JOBFORM_ENDDATE_FIELD_INVALID = "jobform.enddate.field.invalid";
	
	//MortgageUpdateForm fields
	public static String MTGFORM_STARTDATE_FIELD_INVALID = "mtgform.startdate.field.invalid";
	public static String MTGFORM_ENDDATE_FIELD_INVALID = "mtgform.enddate.field.invalid";	
		
	//MortgagePaymentUpdateForm fields
	public static String MTGPMTFORM_PAYMENTDATE_FIELD_INVALID = "mtgpmtform.paymentdate.field.invalid";
	
	//PaycheckAddForm fields
	public static String PAYCHECKADDFORM_NO_OUTFLOW_SELECTED = "paycheckaddform.nothingselected";
	public static String PAYCHECKADDFORM_DATE_FIELD_MISSING = "paycheckaddform.trxDateMissing";
	public static String PAYCHECKADDFORM_DATE_FIELD_INVALID = "paycheckaddform.trxDateInvalid";
	public static String PAYCHECKADDFORM_AMOUNT_FIELD_MISSING = "paycheckaddform.trxAmountMissing";
	public static String PAYCHECKADDFORM_AMOUNT_FIELD_INVALID = "paycheckaddform.trxAmountInvalid";
	public static String PAYCHECKADDFORM_CHECKNO_FIELD_MISSING = "paycheckaddform.trxCheckNoMissing";
	public static String PAYCHECKADDFORM_CHECKNO_FIELD_INVALID = "paycheckaddform.trxCheckNoInvalid";
	public static String PAYCHECKADDFORM_DESCRIPTION_FIELD_MISSING = "paycheckaddform.trxDescriptionMissing";
	
	//PaycheckHistoryForm fields		
	public static String PAYCHECKHISTORYFORM_DATE_FIELD_INVALID = "paycheckhistoryform.date.field.invalid";
	public static String PAYCHECKHISTORYFORM_PAYCHECKTYPEID_MISSING = "paycheckhistoryform.paycheckTypeID";
		
	//PaycheckOutflowForm fields		
	public static String PAYCHECKOUTFLOWFORM_XFERACCT_FIELD_MISSING = "paycheckoutflowform.xferAccountID";
	public static String PAYCHECKOUTFLOWFORM_WDCATEGORY_FIELD_MISSING = "paycheckoutflowform.wdCategoryID";
	public static String PAYCHECKOUTFLOWFORM_CASHDEPTYPE_FIELD_MISSING = "paycheckoutflowform.cashDepositTypeID";
	
	//TaxPaymentUpdateForm fields
	public static String TAXPMTFORM_PAYMENTDATE_FIELD_INVALID = "taxPaymentUpdateForm.taxPaymentDate";
	
	//TaxGroupYear
	public static String TGYFORM_OTHERITEMIZED_MISSING = "taxGroupYearUpdateForm.otherItemizedDesc";
	public static String TGYFORM_OTHERINCOME_MISSING = "taxGroupYearUpdateForm.otherIncomeDesc";
	
	//TrxSearchForm fields
	public static String TRXSEARCHFORM_SEARCHTEXT_FIELD_MISSING = "trxsearchform.searchtext.field.missing";
	
	//TrxUpdateForm fields
	public static String TRXFORM_CASHDESCRIPTION_FIELD_MISSING = "trxform.cashdescription.field.missing";
	public static String TRXFORM_CASHDEPOSITTYPE_FIELD_MISSING = "trxform.cashdeposittype.field.missing";
	public static String TRXFORM_CHECKNUMBER_FIELD_INVALID = "trxform.checknumber.field.invalid";
	public static String TRXFORM_CHECKNUMBER_FIELD_MISSING = "trxform.checknumber.field.missing";
	public static String TRXFORM_COSTPROCEEDS_FIELD_INVALID = "trxform.costproceeds.field.invalid";
	public static String TRXFORM_COSTPROCEEDS_FIELD_MISSING = "trxform.costproceeds.field.missing";
	public static String TRXFORM_DIVIDENDYEAR_FIELD_INVALID = "trxform.dividendyear.field.invalid";
	public static String TRXFORM_DIVIDENDYEAR_FIELD_MISSING = "trxform.dividendyear.field.missing";
	public static String TRXFORM_EXPIRATIONDATE_FIELD_INVALID = "trxform.expirationdate.field.invalid";
	public static String TRXFORM_INVESTMENT_FIELD_MISSING = "trxform.investment.field.missing";
	public static String TRXFORM_OPTIONTYPE_FIELD_MISSING = "trxform.optiontype.field.missing";
	public static String TRXFORM_PRICE_FIELD_INVALID = "trxform.price.field.invalid";
	public static String TRXFORM_PRICE_FIELD_MISSING = "trxform.price.field.missing";
	public static String TRXFORM_STRIKEPRICE_FIELD_INVALID = "trxform.strikeprice.field.invalid";
	public static String TRXFORM_STRIKEPRICE_FIELD_MISSING = "trxform.strikeprice.field.missing";
	public static String TRXFORM_UNITS_FIELD_INVALID = "trxform.units.field.invalid";
	public static String TRXFORM_UNITS_FIELD_MISSING = "trxform.units.field.missing";	
	public static String TRXFORM_WDCATEGORY_FIELD_MISSING = "trxform.wdcategory.field.missing";
	public static String TRXFORM_XFERACCOUNT_FIELD_MISSING = "trxform.xferaccount.field.missing";
	

}