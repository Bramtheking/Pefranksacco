package com.example.pefranksacco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserCredentials.db";
    private static final int DATABASE_VERSION = 1;

    // Define your table and column names
    static final String TABLE_NAME = "user_credentials";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_PASSWORD = "password";

    // Create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    // Define a new table for LoanData
    // Define a new table for LoanData
    static final String LOAN_DATA_TABLE_NAME = "loan_data";
    static final String COLUMN_LOAN_ID = "_id";
    static final String COLUMN_NUMBER = "number"; // Add this line for the number column
    static final String COLUMN_API_RESPONSE = "api_response";

    private static final String LOAN_DATA_TABLE_CREATE =
            "CREATE TABLE " + LOAN_DATA_TABLE_NAME + " (" +
                    COLUMN_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NUMBER + " TEXT, " +  // Add this line for the number column
                    COLUMN_API_RESPONSE + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the user_credentials table
        db.execSQL(TABLE_CREATE);

        // Create the LoanData table
        db.execSQL(LOAN_DATA_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate the user_credentials table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        // Drop and recreate the LoanData table
        db.execSQL("DROP TABLE IF EXISTS " + LOAN_DATA_TABLE_NAME);
        onCreate(db);
    }
}
