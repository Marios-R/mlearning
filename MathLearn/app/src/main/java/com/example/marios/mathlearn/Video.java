package com.example.marios.mathlearn;

/**
 * Created by Marios on 5/9/2016.
 */
public class Video {
    public String videoTitle = "";
    public String videoLink = "";
    public boolean videoSelected = false;
    public boolean videoViewed = false;
    public int videoSequence;
    public int videoID;

    public Video(String vTitle, String vCode, boolean vSelected, boolean vViewed, int vSequence, int vID){
        videoTitle=vTitle;
        videoLink=vCode;
        videoSelected=vSelected;
        videoViewed=vViewed;
        videoSequence=vSequence;
        videoID=vID;
    }
}
