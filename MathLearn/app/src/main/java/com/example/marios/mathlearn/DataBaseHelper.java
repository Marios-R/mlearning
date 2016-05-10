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
    private static final String CREATE_VIDEOS="CREATE TABLE IF NOT EXISTS videos(vTitle VARCHAR, vCode VARCHAR, vSelected VARCHAR, vViewed VARCHAR, vSequence INT);";
    private static final String CREATE_ASSIGNMENTS="CREATE TABLE IF NOT EXISTS assignments(assignTitle VARCHAR, assignLink VARCHAR, assignSelected VARCHAR, assignID INT, assignSequence INT);";
    //public static final String TABLE_NAME="videos";
    //private SQLiteDatabase db;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VIDEOS);
        db.execSQL(CREATE_ASSIGNMENTS);
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
            videos[0]= new Video("ΔΕΝ ΥΠΑΡΧΕΙ ΔΙΑΛΕΞΗ", "", false, false,1);
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
                if (c.getString(2).equals("true")){
                    videoSelected=true;
                    if (c.getString(3).equals("true")){
                        videoViewed=true;
                    }
                }
                videos[i]= new Video(videoTitle,videoCode,videoSelected,videoViewed, videoSequence);
                i++;
            }
        }
        return videos;
    }

    public boolean existInVideos(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu=db.rawQuery("SELECT * FROM videos WHERE vCode='" + code + "'", null);
        if (cu.getCount()==0)
            return false;
        else
            return true;
    }

    public void insertInVids(Video video){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO videos VALUES('"+video.videoTitle+"','"+video.videoCode+"','false','false','"+video.videoSequence+"');");
    }

    public void updateSelectedinVids(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vSelected", "true");
        db.update("videos", cv, "vCode='" + code + "'", null);
    }

    public void updateViewedinVids(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vViewed", "true");
        db.update("videos", cv, "vCode='" + code+"'", null);
    }

    public boolean existInAssignments(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cu=db.rawQuery("SELECT * FROM assignments WHERE assignID=" + id + "", null);
        if (cu.getCount()==0)
            return false;
        else
            return true;
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
            assignments[0]= new Assignment("ΔΕΝ ΥΠΑΡΧΕΙ ΦΥΛΛΑΔΙΟ ΑΣΚΗΣΕΩΝ", "", false,0,1);
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

}
