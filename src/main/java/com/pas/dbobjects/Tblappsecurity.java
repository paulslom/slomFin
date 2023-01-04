package com.pas.dbobjects;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Tblappsecurity implements Serializable {

    /** identifier field */
    private String userId;

    /** persistent field */
    private String password;

    /** full constructor */
    public Tblappsecurity(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    /** default constructor */
    public Tblappsecurity() {
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userId", getUserId())
            .toString();
    }

}
