package com.example.marios.mathlearn;

/**
 * Created by Marios on 5/10/2016.
 */
public class Assignment {

    public String assignTitle="";
    public String assignLink="";
    public boolean assignSelected=false;
    public int assignID;
    public int assignSequence;

    public Assignment(String t, String l, boolean s, int i, int seq){
        assignTitle=t;
        assignLink=l;
        assignSelected=s;
        assignID=i;
        assignSequence=seq;
    }

}
