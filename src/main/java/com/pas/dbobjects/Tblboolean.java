package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblboolean implements Serializable {

    /** identifier field */
    private Integer iboolean;

    /** nullable persistent field */
    private String ibooleanDesc;

    /** full constructor */
    public Tblboolean(Integer iboolean, String ibooleanDesc) {
        this.iboolean = iboolean;
        this.ibooleanDesc = ibooleanDesc;
    }

    /** default constructor */
    public Tblboolean() {
    }

    /** minimal constructor */
    public Tblboolean(Integer iboolean) {
        this.iboolean = iboolean;
    }

    public Integer getIboolean() {
        return this.iboolean;
    }

    public void setIboolean(Integer iboolean) {
        this.iboolean = iboolean;
    }

    public String getIbooleanDesc() {
        return this.ibooleanDesc;
    }

    public void setIbooleanDesc(String ibooleanDesc) {
        this.ibooleanDesc = ibooleanDesc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("iboolean", getIboolean())
            .toString();
    }

}
