package com.alibaba.map;

public class Tiny {
    String gid;
    String bet1;
    String bet2;
    String bet3;
    int askType; //问路的类型，0非问路，1模拟庄问路不显示，2真实问路，闪烁显示

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getBet1() {
        return bet1;
    }

    public void setBet1(String bet1) {
        this.bet1 = bet1;
    }

    public String getBet2() {
        return bet2;
    }

    public void setBet2(String bet2) {
        this.bet2 = bet2;
    }

    public String getBet3() {
        return bet3;
    }

    public void setBet3(String bet3) {
        this.bet3 = bet3;
    }

    public int getAskType() {
        return askType;
    }

    public void setAskType(int askType) {
        this.askType = askType;
    }
}
