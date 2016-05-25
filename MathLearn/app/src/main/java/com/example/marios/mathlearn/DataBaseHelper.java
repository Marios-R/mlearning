package com.example.marios.mathlearn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marios on 5/9/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Local_DB";
    private static final String CREATE_VIDEOS="CREATE TABLE IF NOT EXISTS videos(vTitle VARCHAR, vCode VARCHAR, vSelected VARCHAR, vViewed VARCHAR, vSequence INT, vID INT);";
    private static final String CREATE_ASSIGNMENTS="CREATE TABLE IF NOT EXISTS assignments(assignTitle VARCHAR, assignLink VARCHAR, assignSelected VARCHAR, assignID INT, assignSequence INT);";
    private static final String CREATE_ANNOUNCEMENTS="CREATE TABLE IF NOT EXISTS announcements(annDate VARCHAR, annBody VARCHAR, annSelected VARCHAR, annID INT, annSequence INT);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VIDEOS);
        db.execSQL(CREATE_ASSIGNMENTS);
        db.execSQL(CREATE_ANNOUNCEMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Video[] getVideos(){
        Video[] videos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM videos ORDER BY vSequence DESC", null);
        if (c.getCount()==0){
            videos = new Video[1];
            videos[0]= new Video("Δεν υπάρχει διάλεξη...\nΔοκιμάστε να ανανεώσετε τη λίστα.", "", false, false,1,0);
        }
        else {
            videos= new Video[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                String videoTitle = c.getString(0);
                String videoCode = c.getString(1);
                boolean videoSelected = false;
                boolean videoViewed = false;
                int videoSequence = c.getInt(4);
                int videoID=c.getInt(5);
                if (c.getString(2).equals("true")){
                    videoSelected=true;
                    if (c.getString(3).equals("true")){
                        videoViewed=true;
                    }
                }
                videos[i]= new Video(videoTitle,videoCode,videoSelected,videoViewed, videoSequence, videoID);
                i++;
            }
        }
        return videos;
    }

    public Video getVideo(int id){
        Video video;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu=db.rawQuery("SELECT * FROM videos WHERE vID=" + id + "", null);
        cu.moveToFirst();
        String videoTitle = cu.getString(0);
        String videoCode = cu.getString(1);
        boolean videoSelected = false;
        boolean videoViewed = false;
        int videoSequence = cu.getInt(4);
        int videoID=cu.getInt(5);
        if (cu.getString(2).equals("true")){
            videoSelected=true;
            if (cu.getString(3).equals("true")){
                videoViewed=true;
            }
        }
        video= new Video(videoTitle,videoCode,videoSelected,videoViewed, videoSequence, videoID);
        return video;
    }

    public void insertInVids(Video video){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO videos VALUES('"+video.videoTitle+"','"+video.videoLink+"','false','false',"+video.videoSequence+","+video.videoID+");");
    }

    public void updateSelectedinVids(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vSelected", "true");
        db.update("videos", cv, "vID=" + id + "", null);
    }

    public void updateViewedinVids(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vViewed", "true");
        db.update("videos", cv, "vID='" + id+"'", null);
    }

    public void deleteFromVidsIn(String array){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM videos WHERE vID IN (" + array + ");");
    }

    public void insertInAssignments(Assignment assignment){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO assignments VALUES('"+assignment.assignTitle+"','"+assignment.assignLink+"','false',"+assignment.assignID+","+assignment.assignSequence+");");
    }

    public Assignment[] getAssignments(){
        Assignment[] assignments;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM assignments ORDER BY assignSequence DESC", null);
        if (c.getCount()==0){
            assignments = new Assignment[1];
            assignments[0]= new Assignment("Δεν υπάρχει φυλλάδιο ασκήσεων...\nΔοκιμάστε να ανανεώσετε τη λίστα.", "", false,0,1);
        }
        else {
            assignments= new Assignment[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                String assignTitle = c.getString(0);
                String assignLink = c.getString(1);
                boolean assignSelected = false;
                int assignID = c.getInt(3);
                int assignSequence = c.getInt(4);
                if (c.getString(2).equals("true")){
                    assignSelected=true;
                }
                assignments[i]= new Assignment(assignTitle,assignLink,assignSelected,assignID, assignSequence);
                i++;
            }
        }
        return assignments;
    }


    public void updateSelectedinAssignments(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("assignSelected", "true");
        db.update("assignments", cv, "assignID='" + id + "'", null);
    }


    public void deleteFromAssignments(String array){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM assignments WHERE assignID IN (" + array + ");");
    }


    public void insertInAnnouncements(Announcement announcement){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO announcements VALUES('"+announcement.annDate+"','"+announcement.annBody+"','false',"+announcement.annID+","+announcement.annSequence+");");
    }

    public Announcement[] getAnnouncements(){
        Announcement[] announcements;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM announcements ORDER BY annSequence DESC", null);
        if (c.getCount()==0){
            announcements = new Announcement[1];
            announcements[0]= new Announcement("Δεν υπάρχει ανακοίνωση...\nΔοκιμάστε να ανανεώσετε τη λίστα.", "", false,0,1);
        }
        else {
            announcements= new Announcement[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                String annDate = c.getString(0);
                String annBody = c.getString(1);
                boolean annSelected = false;
                int annID = c.getInt(3);
                int annSequence = c.getInt(4);
                if (c.getString(2).equals("true")){
                    annSelected=true;
                }
                announcements[i]= new Announcement(annDate,annBody,annSelected,annID, annSequence);
                i++;
            }
        }
        return announcements;
    }


    public void updateSelectedinAnnouncements(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("annSelected", "true");
        db.update("announcements", cv, "annID='" + id + "'", null);
    }


    public void deleteFromAnnouncements(String array){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM announcements WHERE annID IN (" + array + ");");
    }


}
