package com.alibaba.map;

public class Tiny2 {
    String gid;
    String bet1;
    boolean hePre;
    boolean heAft;
    int heAmount;
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

    public boolean isHePre() {
        return hePre;
    }

    public void setHePre(boolean hePre) {
        this.hePre = hePre;
    }

    public boolean isHeAft() {
        return heAft;
    }

    public void setHeAft(boolean heAft) {
        this.heAft = heAft;
    }

    public int getHeAmount() {
        return heAmount;
    }

    public void setHeAmount(int heAmount) {
        this.heAmount = heAmount;
    }

    public int getAskType() {
        return askType;
    }

    public void setAskType(int askType) {
        this.askType = askType;
    }
}
