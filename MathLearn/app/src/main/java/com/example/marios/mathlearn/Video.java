package com.example.marios.mathlearn;

/**
 * Created by Marios on 5/9/2016.
 */
public class Video {
    public String videoTitle = "";
    public String videoCode = "";
    public boolean videoSelected = false;
    public boolean videoViewed = false;
    public int videoSequence;

    public Video(String vTitle, String vCode, boolean vSelected, boolean vViewed, int vSequence){
        videoTitle=vTitle;
        videoCode=vCode;
        videoSelected=vSelected;
        videoViewed=vViewed;
        videoSequence=vSequence;
    }
}
