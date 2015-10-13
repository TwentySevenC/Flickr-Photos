package com.android.liujian.flichrphotos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.liujian.flichrphotos.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 15/10/12.
 * DataBase for saving user favourite photos
 */
public class FlickrDataBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "user.sqlite";


    private static final String TABLE_NAME = "user_fav_photos";
    private static final String PHOTO_ID = "id";
    private static final String PHOTO_TITLE = "title";
    private static final String PHOTO_OWNER = "owner";
    private static final String PHOTO_URL = "url";

    public FlickrDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PHOTO_ID + " TEXT NOT NULL, " + PHOTO_TITLE + " TEXT, " + PHOTO_OWNER +
                " TEXT, " + PHOTO_URL + " TEXT, UNIQUE(photo_id));";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    /**
     * Insert a photo into the table
     * @param photo a photo
     */
    public void insertPhoto(Photo photo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PHOTO_ID, photo.getId());
        cv.put(PHOTO_TITLE, photo.getTitle());
        cv.put(PHOTO_OWNER, photo.getOwnerId());
        cv.put(PHOTO_URL, photo.getUrl());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }


    /**
     * Delete a photo from table
     * @param photo photo
     */
    public void deletePhoto(Photo photo){
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = PHOTO_ID + " = ?";
        String[] whereArgs = {photo.getId()};

        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }


    /**
     * Get the favourite photo's count
     * @return count
     */
    public int getPhotoCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor _cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        int _count = _cursor.getCount();

        if(_cursor != null) _cursor.close();
        db.close();

        return _count;
    }


    /**
     * Query a list of photos
     * @return a list of favourite photos
     */
    public List<Photo> queryPhotos(){
        SQLiteDatabase db = getReadableDatabase();
        List<Photo> _photos = new ArrayList<>();
        Cursor _cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(_cursor != null){
            _cursor.moveToFirst();

            while(!(_cursor.isBeforeFirst()&&_cursor.isAfterLast())){
                Photo _photo = new Photo();
                _photo.setId(_cursor.getString(1));
                _photo.setTitle(_cursor.getString(2));
                _photo.setOwnerId(_cursor.getString(3));
                _photo.setUrl(_cursor.getString(4));

                _photos.add(_photo);

                if(_cursor.isLast()){
                    break;
                }else{
                    _cursor.moveToNext();
                }
            }

        }

        if(_cursor != null) _cursor.close();

        db.close();

        return _photos;
    }




}
