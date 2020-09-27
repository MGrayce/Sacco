package com.grayapps.saccodemo.Demo.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EntitiesOnlineObj {
    private String member, amount, action, date;
    public EntitiesOnlineObj(){}

    public EntitiesOnlineObj(String member, String amount, String action, String date) {
        this.member = member;
        this.amount = amount;
        this.action = action;
        this.date = date;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
