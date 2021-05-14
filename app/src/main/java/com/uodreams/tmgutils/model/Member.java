package com.uodreams.tmgutils.model;

public class Member {
    public int id;
    public String user;
    public String alias;
    public int rank;
    public int userid;
    public String roles;

    public Member() {
        this.roles = "000";
        this.rank = 1;
    }
}
