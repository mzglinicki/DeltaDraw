package com.project.mzglinicki96.deltaDraw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.project.mzglinicki96.deltaDraw.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mzglinicki.96 on 28.03.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(final Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        PictureTable.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        PictureTable.onUpgrade(db);
    }

    public void insertData(final String name, final String author, final String points) {

        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues contentValues = new ContentValues();
        contentValues.put(PictureTable.COLUMN_NAME, name);
        contentValues.put(PictureTable.COLUMN_AUTHOR, author);
        contentValues.put(PictureTable.COLUMN_POINTS, points);
        contentValues.put(PictureTable.COLUMN_DATE, getDate());

        db.insert(PictureTable.TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateAllData(final String name, final String author, final String points, final int rowId){

        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues contentValues = new ContentValues();
        contentValues.put(PictureTable.COLUMN_NAME, name);
        contentValues.put(PictureTable.COLUMN_AUTHOR, author);
        contentValues.put(PictureTable.COLUMN_POINTS, points);
        contentValues.put(PictureTable.COLUMN_DATE, getDate());

        db.update(PictureTable.TABLE_NAME, contentValues, PictureTable.COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    public Cursor getAllData() {

        final SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from " + PictureTable.TABLE_NAME, null);
    }

    public Bundle getDataFromRow(final int rowPosition) {

        final SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery("select * from " + PictureTable.TABLE_NAME, null);
        cursor.moveToPosition(rowPosition);

        final String jsonPoints = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_POINTS));
        final String pictureName = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_NAME));
        final String pictureAuthor = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_AUTHOR));
        final int pictureId = cursor.getInt(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_ID));

        cursor.close();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.POINTS_IN_JASON, jsonPoints);
        bundle.putString(Constants.KEY_NAME, pictureName);
        bundle.putString(Constants.KEY_AUTHOR, pictureAuthor);
        bundle.putInt(Constants.KEY_POSITION, pictureId);
        return bundle;
    }

    public Bundle getLastSavedRow(){

        final SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery("select * from " + PictureTable.TABLE_NAME, null);
        cursor.moveToLast();

        final String jsonPoints = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_POINTS));
        final String pictureName = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_NAME));
        final String pictureAuthor = cursor.getString(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_AUTHOR));
        final int pictureId = cursor.getInt(cursor.getColumnIndexOrThrow(PictureTable.COLUMN_ID));

        cursor.close();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.POINTS_IN_JASON, jsonPoints);
        bundle.putString(Constants.KEY_NAME, pictureName);
        bundle.putString(Constants.KEY_AUTHOR, pictureAuthor);
        bundle.putInt(Constants.KEY_POSITION, pictureId);

        return bundle;
    }

    public void deleteAllData() {
        getWritableDatabase().delete(PictureTable.TABLE_NAME, null, null);
    }

    public void deleteRecord(final int rowId) {
        getWritableDatabase().delete(PictureTable.TABLE_NAME, PictureTable.COLUMN_ID + "=" + rowId, null);
    }

    public List<PictureModel> getPicturesData() {

        final List<PictureModel> pictureModelList = new ArrayList<>();

        final Cursor result = getAllData();

        while (result.moveToNext()) {
            final PictureModel pictureModel = new PictureModel();

            pictureModel.setId(result.getInt(result.getColumnIndexOrThrow(PictureTable.COLUMN_ID)));
            pictureModel.setName(result.getString(result.getColumnIndexOrThrow(PictureTable.COLUMN_NAME)));
            pictureModel.setAuthor(result.getString(result.getColumnIndexOrThrow(PictureTable.COLUMN_AUTHOR)));
            pictureModel.setPoints(result.getString(result.getColumnIndexOrThrow(PictureTable.COLUMN_POINTS)));
            pictureModel.setDate(result.getString(result.getColumnIndexOrThrow(PictureTable.COLUMN_DATE)));
            pictureModelList.add(pictureModel);
        }
        result.close();
        return pictureModelList;
    }

    private String getDate() {
        final SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        return formatter.format(Calendar.getInstance().getTime());
    }
}