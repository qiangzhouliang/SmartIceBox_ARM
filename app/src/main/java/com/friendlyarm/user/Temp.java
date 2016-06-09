package com.friendlyarm.user;

import cn.bmob.v3.BmobObject;

/**
 * 温度表
 * Created by Q on 2016-05-16.
 */
public class Temp extends BmobObject {
    private String temp;
    private String flagtemp;
    private String openflag;

    public String getOpenflag() {
        return openflag;
    }

    public void setOpenflag(String openflag) {
        this.openflag = openflag;
    }

    public String getFlagtemp() {
        return flagtemp;
    }

    public void setFlagtemp(String flagtemp) {
        this.flagtemp = flagtemp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
