package com.pas.dbobjects;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbltaxformulatype implements Serializable {

    /** identifier field */
    private Integer itaxFormulaTypeId;

    /** persistent field */
    private String sformulaDescription;

    /** persistent field */
    private Set<Tbltaxformulas> tbltaxformulas;

    /** full constructor */
    public Tbltaxformulatype(Integer itaxFormulaTypeId, String sformulaDescription, Set<Tbltaxformulas> tbltaxformulas) {
        this.itaxFormulaTypeId = itaxFormulaTypeId;
        this.sformulaDescription = sformulaDescription;
        this.tbltaxformulas = tbltaxformulas;
    }

    /** default constructor */
    public Tbltaxformulatype() {
    }

    public Integer getItaxFormulaTypeId() {
        return this.itaxFormulaTypeId;
    }

    public void setItaxFormulaTypeId(Integer itaxFormulaTypeId) {
        this.itaxFormulaTypeId = itaxFormulaTypeId;
    }

    public String getSformulaDescription() {
        return this.sformulaDescription;
    }

    public void setSformulaDescription(String sformulaDescription) {
        this.sformulaDescription = sformulaDescription;
    }

    public Set<Tbltaxformulas> getTbltaxformulas() {
        return this.tbltaxformulas;
    }

    public void setTbltaxformulas(Set<Tbltaxformulas> tbltaxformulas) {
        this.tbltaxformulas = tbltaxformulas;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxFormulaTypeId", getItaxFormulaTypeId())
            .toString();
    }

}
