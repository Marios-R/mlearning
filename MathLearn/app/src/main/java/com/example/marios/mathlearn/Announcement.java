package com.example.marios.mathlearn;

/**
 * Created by Marios on 5/11/2016.
 */
public class Announcement {
    public String annDate="";
    public String annBody = "";
    public boolean annSelected = false;
    public int annID;
    public int annSequence;

    public Announcement(String pannDate, String pannBody, boolean pannSelected, int pannID, int pannSequence) {
        annDate=pannDate;
        annBody=pannBody;
        annSelected=pannSelected;
        annID = pannID;
        annSequence= pannSequence;
    }
}
