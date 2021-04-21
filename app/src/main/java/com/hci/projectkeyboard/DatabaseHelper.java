package com.hci.projectkeyboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.cert.CertificateRevokedException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_project_keyboard.db";
    private static final String TABLE_SESSIONS = "tb_sessions";

    private static final String KEY_KEYBOARD = "keyboard";

    private static final String KEY_PHRASE_NO = "phrase_no";

    private static final String KEY_ORIGINAL_PHRASE = "original_phrase";
    private static final String KEY_TRANSCRIBED_PHRASE = "transcribed_phrase";
    private static final String KEY_TIME_TAKEN = "time_taken";
    private static final String KEY_ERROR_RATE = "error_rate";

    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_MSD = "msd";

    private static final String KEY_ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
                + KEY_KEYBOARD + " TEXT,"

                + KEY_PHRASE_NO + " INTEGER,"

                + KEY_ORIGINAL_PHRASE + " TEXT,"
                + KEY_TRANSCRIBED_PHRASE + " TEXT,"
                + KEY_TIME_TAKEN + "INTEGER,"
                + KEY_ERROR_RATE + "REAL,"

                + KEY_START_TIME + "INTEGER,"
                + KEY_END_TIME + "INTEGER,"
                + KEY_MSD + "INTEGER,"

                + KEY_ID + " INTEGER PRIMARY KEY"
                + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        // Create tables again
        onCreate(db);
    }

    void addSessionDataEntry(SessionDataEntry sessionDataEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KEYBOARD, sessionDataEntry.keyboard);
        values.put(KEY_PHRASE_NO, sessionDataEntry.phrase_no);
        values.put(KEY_ORIGINAL_PHRASE, sessionDataEntry.original_phrase);
        values.put(KEY_TRANSCRIBED_PHRASE, sessionDataEntry.transcribed_phrase);
        values.put(KEY_TIME_TAKEN, sessionDataEntry.time_taken);
        values.put(KEY_ERROR_RATE, sessionDataEntry.error_rate);
        values.put(KEY_START_TIME, sessionDataEntry.start_time);
        values.put(KEY_END_TIME, sessionDataEntry.end_time);
        values.put(KEY_MSD, sessionDataEntry.msd);

        // Inserting Row
        db.insert(TABLE_SESSIONS, null, values);
        //2nd argument is String containing nullColumnHack

        db.close(); // Closing database connection
    }

    public List<SessionDataEntry> getAllTrialDataEntry() {
        List<SessionDataEntry> sessionList = new ArrayList<SessionDataEntry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SESSIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SessionDataEntry sessionDataEntry = new SessionDataEntry(
                        cursor.getString(0),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        Long.parseLong(cursor.getString(4)),
                        Double.parseDouble(cursor.getString(5)),
                        Long.parseLong(cursor.getString(6)),
                        Long.parseLong(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)),
                        Integer.parseInt(cursor.getString(9))
                );
                // Adding contact to list
                sessionList.add(sessionDataEntry);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return sessionList;
    }

    public void deleteAllTrialDataEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SESSIONS);
        db.close();
    }
}
