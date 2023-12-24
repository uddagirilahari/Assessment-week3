//User

package com.accolite.TransactionManagement.entity;
import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User{


    @Id
    int userId;
    String userName;
    String userCategory;
    String userSecret;

    Boolean userStatus;

    Boolean userEnrolled;

    Boolean userEnrollApproved;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Wallet wallet;

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public Boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getUserEnrolled() {
        return userEnrolled;
    }

    public void setUserEnrolled(Boolean userEnrolled) {
        this.userEnrolled = userEnrolled;
    }

    public Boolean getUserEnrollApproved() {
        return userEnrollApproved;
    }

    public void setUserEnrollApproved(Boolean userEnrollApproved) {
        this.userEnrollApproved = userEnrollApproved;
    }
//    Double user_latitude;
//
//    Double user_longitude;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}

