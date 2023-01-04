package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tbldropdown implements Serializable {

    /** identifier field */
    private Integer iddid;

    /** persistent field */
    private String sdddescription;

    /** persistent field */
    private String sddtype;

    /** persistent field */
    private String sddtableName;

    /** persistent field */
    private String sddvalueColName;

    /** persistent field */
    private String sdddescColName;

    /** nullable persistent field */
    private String sdddescColNameShort;

    /** nullable persistent field */
    private String sddsort;

    /** nullable persistent field */
    private String sddwhereCol1;

    /** nullable persistent field */
    private String sddwhereTbl1;

    /** nullable persistent field */
    private String sddwhereAbbr1;

    /** nullable persistent field */
    private String sddwhereCol2;

    /** nullable persistent field */
    private String sddwhereTbl2;

    /** nullable persistent field */
    private String sddwhereAbbr2;

    /** nullable persistent field */
    private String sddwhereCol3;

    /** nullable persistent field */
    private String sddwhereTbl3;

    /** nullable persistent field */
    private String sddwhereAbbr3;

    /** nullable persistent field */
    private String sddwhereCol4;

    /** nullable persistent field */
    private String sddwhereTbl4;

    /** nullable persistent field */
    private String sddwhereAbbr4;

    /** nullable persistent field */
    private String sddwhereCol5;

    /** nullable persistent field */
    private String sddwhereTbl5;

    /** nullable persistent field */
    private String sddwhereAbbr5;

    /** nullable persistent field */
    private String sddwhereText;

    /** full constructor */
    public Tbldropdown(Integer iddid, String sdddescription, String sddtype, String sddtableName, String sddvalueColName, String sdddescColName, String sdddescColNameShort, String sddsort, String sddwhereCol1, String sddwhereTbl1, String sddwhereAbbr1, String sddwhereCol2, String sddwhereTbl2, String sddwhereAbbr2, String sddwhereCol3, String sddwhereTbl3, String sddwhereAbbr3, String sddwhereCol4, String sddwhereTbl4, String sddwhereAbbr4, String sddwhereCol5, String sddwhereTbl5, String sddwhereAbbr5, String sddwhereText) {
        this.iddid = iddid;
        this.sdddescription = sdddescription;
        this.sddtype = sddtype;
        this.sddtableName = sddtableName;
        this.sddvalueColName = sddvalueColName;
        this.sdddescColName = sdddescColName;
        this.sdddescColNameShort = sdddescColNameShort;
        this.sddsort = sddsort;
        this.sddwhereCol1 = sddwhereCol1;
        this.sddwhereTbl1 = sddwhereTbl1;
        this.sddwhereAbbr1 = sddwhereAbbr1;
        this.sddwhereCol2 = sddwhereCol2;
        this.sddwhereTbl2 = sddwhereTbl2;
        this.sddwhereAbbr2 = sddwhereAbbr2;
        this.sddwhereCol3 = sddwhereCol3;
        this.sddwhereTbl3 = sddwhereTbl3;
        this.sddwhereAbbr3 = sddwhereAbbr3;
        this.sddwhereCol4 = sddwhereCol4;
        this.sddwhereTbl4 = sddwhereTbl4;
        this.sddwhereAbbr4 = sddwhereAbbr4;
        this.sddwhereCol5 = sddwhereCol5;
        this.sddwhereTbl5 = sddwhereTbl5;
        this.sddwhereAbbr5 = sddwhereAbbr5;
        this.sddwhereText = sddwhereText;
    }

    /** default constructor */
    public Tbldropdown() {
    }

    /** minimal constructor */
    public Tbldropdown(Integer iddid, String sdddescription, String sddtype, String sddtableName, String sddvalueColName, String sdddescColName) {
        this.iddid = iddid;
        this.sdddescription = sdddescription;
        this.sddtype = sddtype;
        this.sddtableName = sddtableName;
        this.sddvalueColName = sddvalueColName;
        this.sdddescColName = sdddescColName;
    }

    public Integer getIddid() {
        return this.iddid;
    }

    public void setIddid(Integer iddid) {
        this.iddid = iddid;
    }

    public String getSdddescription() {
        return this.sdddescription;
    }

    public void setSdddescription(String sdddescription) {
        this.sdddescription = sdddescription;
    }

    public String getSddtype() {
        return this.sddtype;
    }

    public void setSddtype(String sddtype) {
        this.sddtype = sddtype;
    }

    public String getSddtableName() {
        return this.sddtableName;
    }

    public void setSddtableName(String sddtableName) {
        this.sddtableName = sddtableName;
    }

    public String getSddvalueColName() {
        return this.sddvalueColName;
    }

    public void setSddvalueColName(String sddvalueColName) {
        this.sddvalueColName = sddvalueColName;
    }

    public String getSdddescColName() {
        return this.sdddescColName;
    }

    public void setSdddescColName(String sdddescColName) {
        this.sdddescColName = sdddescColName;
    }

    public String getSdddescColNameShort() {
        return this.sdddescColNameShort;
    }

    public void setSdddescColNameShort(String sdddescColNameShort) {
        this.sdddescColNameShort = sdddescColNameShort;
    }

    public String getSddsort() {
        return this.sddsort;
    }

    public void setSddsort(String sddsort) {
        this.sddsort = sddsort;
    }

    public String getSddwhereCol1() {
        return this.sddwhereCol1;
    }

    public void setSddwhereCol1(String sddwhereCol1) {
        this.sddwhereCol1 = sddwhereCol1;
    }

    public String getSddwhereTbl1() {
        return this.sddwhereTbl1;
    }

    public void setSddwhereTbl1(String sddwhereTbl1) {
        this.sddwhereTbl1 = sddwhereTbl1;
    }

    public String getSddwhereAbbr1() {
        return this.sddwhereAbbr1;
    }

    public void setSddwhereAbbr1(String sddwhereAbbr1) {
        this.sddwhereAbbr1 = sddwhereAbbr1;
    }

    public String getSddwhereCol2() {
        return this.sddwhereCol2;
    }

    public void setSddwhereCol2(String sddwhereCol2) {
        this.sddwhereCol2 = sddwhereCol2;
    }

    public String getSddwhereTbl2() {
        return this.sddwhereTbl2;
    }

    public void setSddwhereTbl2(String sddwhereTbl2) {
        this.sddwhereTbl2 = sddwhereTbl2;
    }

    public String getSddwhereAbbr2() {
        return this.sddwhereAbbr2;
    }

    public void setSddwhereAbbr2(String sddwhereAbbr2) {
        this.sddwhereAbbr2 = sddwhereAbbr2;
    }

    public String getSddwhereCol3() {
        return this.sddwhereCol3;
    }

    public void setSddwhereCol3(String sddwhereCol3) {
        this.sddwhereCol3 = sddwhereCol3;
    }

    public String getSddwhereTbl3() {
        return this.sddwhereTbl3;
    }

    public void setSddwhereTbl3(String sddwhereTbl3) {
        this.sddwhereTbl3 = sddwhereTbl3;
    }

    public String getSddwhereAbbr3() {
        return this.sddwhereAbbr3;
    }

    public void setSddwhereAbbr3(String sddwhereAbbr3) {
        this.sddwhereAbbr3 = sddwhereAbbr3;
    }

    public String getSddwhereCol4() {
        return this.sddwhereCol4;
    }

    public void setSddwhereCol4(String sddwhereCol4) {
        this.sddwhereCol4 = sddwhereCol4;
    }

    public String getSddwhereTbl4() {
        return this.sddwhereTbl4;
    }

    public void setSddwhereTbl4(String sddwhereTbl4) {
        this.sddwhereTbl4 = sddwhereTbl4;
    }

    public String getSddwhereAbbr4() {
        return this.sddwhereAbbr4;
    }

    public void setSddwhereAbbr4(String sddwhereAbbr4) {
        this.sddwhereAbbr4 = sddwhereAbbr4;
    }

    public String getSddwhereCol5() {
        return this.sddwhereCol5;
    }

    public void setSddwhereCol5(String sddwhereCol5) {
        this.sddwhereCol5 = sddwhereCol5;
    }

    public String getSddwhereTbl5() {
        return this.sddwhereTbl5;
    }

    public void setSddwhereTbl5(String sddwhereTbl5) {
        this.sddwhereTbl5 = sddwhereTbl5;
    }

    public String getSddwhereAbbr5() {
        return this.sddwhereAbbr5;
    }

    public void setSddwhereAbbr5(String sddwhereAbbr5) {
        this.sddwhereAbbr5 = sddwhereAbbr5;
    }

    public String getSddwhereText() {
        return this.sddwhereText;
    }

    public void setSddwhereText(String sddwhereText) {
        this.sddwhereText = sddwhereText;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("iddid", getIddid())
            .toString();
    }

}
