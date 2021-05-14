package com.uodreams.tmgutils.model;

public class Sop {
    public int id;
    public int value;
    public String skill;
    public int days;
    public int serial;
   // public int current;

    /*public Sop() {
        this.current = 0;
    }*/

    @Override
    public String toString() {
        return "Sop{" +
                "userId=" + id +
                ", value=" + value +
                ", skill='" + skill + '\'' +
                ", days=" + days +
                ", serial=" + serial +
                '}';
    }
}
