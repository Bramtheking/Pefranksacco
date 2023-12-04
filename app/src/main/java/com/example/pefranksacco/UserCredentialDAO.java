package com.example.pefranksacco;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserCredentialDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public UserCredentialDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertUserCredential(UserCredential userCredential) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMAIL, userCredential.getEmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, userCredential.getPassword());

        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public void deleteUserCredential(long userId) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = " + userId, null);
    }

    public UserCredential getUserCredentialById(long userId) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null,
                DatabaseHelper.COLUMN_ID + " = " + userId, null, null, null, null);
        UserCredential userCredential = null;

        if (cursor != null && cursor.moveToFirst()) {
            userCredential = cursorToUserCredential(cursor);
            cursor.close();
        }

        return userCredential;
    }

    private UserCredential cursorToUserCredential(Cursor cursor) {
        UserCredential userCredential = new UserCredential();
        userCredential.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        userCredential.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
        userCredential.setPassword(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD)));

        return userCredential;
    }
    public UserCredential getSavedUserCredential() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        UserCredential userCredential = null;

        if (cursor != null && cursor.moveToFirst()) {
            userCredential = cursorToUserCredential(cursor);
            cursor.close();
        }

        return userCredential;
    }
    public void clearLoginDetails() {
        database.delete(DatabaseHelper.TABLE_NAME, null, null);
    }
}
