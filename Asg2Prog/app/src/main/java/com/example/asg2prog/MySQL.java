package com.example.asg2prog;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MySQL extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private Context context=null;

    public MySQL(@Nullable Context context){
        super(context,Config.DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }
// creating two table as a databases one for the profile and other access
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + Config.TABLE_PROFILE + " ("
                + Config.COLUMN_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_FIRST + " TEXT NOT NULL, "
                + Config.COLUMN_LAST + " TEXT NOT NULL, "
                + Config.COLUMN_GPA + " REAL NOT NULL, "
                + Config.COLUMN_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);

        String CREATE_ACCESS_TABLE = "CREATE TABLE " + Config.TABLE_ACCESS + " ("
                + Config.COLUMN_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PROFILE_ID + " INTEGER NOT NULL, "
                + Config.COLUMN_ACCESS_TYPE + " TEXT NOT NULL, "
                + Config.COLUMN_ACCESS_TIME + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_ACCESS_TABLE);
    }

    // insert method for the Profile Table as seen in the tutorial
public int insertStudent(Profile student){
        int ids=-1;
        SQLiteDatabase db= this.getWritableDatabase();
    ContentValues contentValues= new ContentValues();
    contentValues.put(Config.COLUMN_FIRST, student.getFirst());
    contentValues.put(Config.COLUMN_LAST,student.getLast());
    contentValues.put(Config.COLUMN_PROFILE_ID, student.getPid());
    contentValues.put(Config.COLUMN_GPA, student.getGPA());
    contentValues.put(Config.COLUMN_TIME, student.getTime());
    try {
        ids=(int) db.insertOrThrow(Config.TABLE_PROFILE,null,contentValues);
    }catch (SQLiteException e){
        Toast.makeText(context,"addStudent Error"+e.getMessage(),Toast.LENGTH_LONG).show();
    }finally {
        db.close();
    }
    return ids;
}
// method to read all the student that are inserted in the database in the profile table as seen in the tutorial
    public List<Profile> getAllStudents(){
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor c=null;
        List<Profile> studentList = new ArrayList<>();
        try {
            c=db.query(Config.TABLE_PROFILE,null,null,null,null,null,null,null);
            if(c!=null){
                if(c.moveToFirst()){
                    do{
                        @SuppressLint("Range") String first=c.getString(c.getColumnIndex(Config.COLUMN_FIRST));
                        @SuppressLint("Range") String last=c.getString(c.getColumnIndex(Config.COLUMN_LAST));
                        @SuppressLint("Range") int Pid=c.getInt(c.getColumnIndex((Config.COLUMN_PROFILE_ID)));
                        @SuppressLint("Range") float gpa=c.getFloat(c.getColumnIndex((Config.COLUMN_GPA)));
                        @SuppressLint("Range") String time=c.getString(c.getColumnIndex((Config.COLUMN_TIME)));
                        Profile student=new Profile(Pid,first,last,gpa,time);
                        studentList.add(student);
                    }while (c.moveToNext());
                    return studentList;
                }
            }
        }catch(SQLiteException e){
            Toast.makeText(context,"getAllStudent Error"+e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return Collections.emptyList();
    }
// using collections.sort to sort the the profile by increading iD for the toggle
    public List<Profile> getAllStudentsSortedByID() {
        List<Profile> studentList = getAllStudents();
        Collections.sort(studentList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return Integer.compare(profile1.getPid(), profile2.getPid());
            }
        });
        return studentList;
    }
// using collections sort to sort the profile by firstname in alphabatecial way for the toggle
    public List<Profile> getAllStudentsSortedByFirstName() {
        List<Profile> studentList = getAllStudents();
        Collections.sort(studentList, new Comparator<Profile>() {
            @Override
            public int compare(Profile profile1, Profile profile2) {
                return profile1.getFirst().compareTo(profile2.getFirst());
            }
        });
        return studentList;
    }
// helping me getting the total amount of row in the table to make it seen in the mainactivity
    public int getTotalNumberOfProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM " + Config.TABLE_PROFILE;
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


// getting the profile wanted based on the PID that are used in the profileactivity function
    public Profile getProfile(int pid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Profile profile = null;

        try {
            String selectQuery = "SELECT * FROM " + Config.TABLE_PROFILE + " WHERE " + Config.COLUMN_PROFILE_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(pid)});

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String first = cursor.getString(cursor.getColumnIndex(Config.COLUMN_FIRST));
                @SuppressLint("Range") String last = cursor.getString(cursor.getColumnIndex(Config.COLUMN_LAST));
                @SuppressLint("Range") int profileId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROFILE_ID));
                @SuppressLint("Range")  float gpa = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_GPA));
                @SuppressLint("Range") String time=cursor.getString(cursor.getColumnIndex((Config.COLUMN_TIME)));

                profile = new Profile(profileId, first, last, gpa,time);
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "getProfile Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return profile;
    }
// deleting the profile based on the request in the profile table
    public void deleteProfile(int pid) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(Config.TABLE_PROFILE, Config.COLUMN_PROFILE_ID + " = ?", new String[]{String.valueOf(pid)});
        } catch (SQLiteException e) {
            Toast.makeText(context, "deleteProfile Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }


// method for the Access table to insert and to getALlaccess



    public int insertStudentAccess(Access access){
        int ids=-1;
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        //contentValues.put(Config.COLUMN_ACCESS_ID, access.getId());
        contentValues.put(Config.COLUMN_PROFILE_ID, access.getPid());
        contentValues.put(Config.COLUMN_ACCESS_TYPE, access.getType());
        contentValues.put(Config.COLUMN_ACCESS_TIME, access.getTime());
        try {
            ids=(int) db.insertOrThrow(Config.TABLE_ACCESS,null,contentValues);
        }catch (SQLiteException e){
            Toast.makeText(context,"addStudentAccess Error"+e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return ids;
    }

    public List<Access> getAllStudentsAccess1(int Pid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        List<Access> accessList = new ArrayList<>();

        try {
            String selection = Config.COLUMN_PROFILE_ID + "=?";
            String[] selectionArgs = {String.valueOf(Pid)};
            c = db.query(Config.TABLE_ACCESS, null, selection, selectionArgs, null, null, Config.COLUMN_ACCESS_ID + " DESC");

            if (c != null && c.moveToFirst()) {
                do {
                    @SuppressLint("Range") String type = c.getString(c.getColumnIndex(Config.COLUMN_ACCESS_TYPE));
                    @SuppressLint("Range") String time = c.getString(c.getColumnIndex(Config.COLUMN_TIME));
                    Access access = new Access(Pid, type, time);
                    accessList.add(access);
                } while (c.moveToNext());
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "getAllStudentsAccess Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return accessList;
    }

// method used to get the timestamped based on the requirement

    public String getTime(){
        SimpleDateFormat s = new SimpleDateFormat
                ("yyyy-MM-dd @ HH:mm:ss");
        String format = s.format(new Date());
        return format;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+Config.TABLE_PROFILE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+Config.TABLE_ACCESS);
        onCreate(sqLiteDatabase);
    }
}

