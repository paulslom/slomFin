package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblyear implements Serializable {

    /** identifier field */
    private Integer itaxYear;

    /** full constructor */
    public Tblyear(Integer itaxYear) {
        this.itaxYear = itaxYear;
    }

    /** default constructor */
    public Tblyear() {
    }

    public Integer getItaxYear() {
        return this.itaxYear;
    }

    public void setItaxYear(Integer itaxYear) {
        this.itaxYear = itaxYear;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("itaxYear", getItaxYear())
            .toString();
    }

}
