package com.akin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    //private static String DATABASE_PATH;
    private static final int DATABASE_VERSION = 1;
    public static final String LOGTAG = "FAVORITE";
    public static ArrayList<byte[]> entries= new ArrayList<>();
    public static int total=0;

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //DATABASE_PATH = context.getDatabasePath("favorite.db").getPath();
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteContract.FavoriteEntry.COLUMN_QUOTEID + " VARCHAR UNIQUE, " +
                FavoriteContract.FavoriteEntry.COLUMN_IMAGE + " BLOB " +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addFavorite(String quote_id, byte[] bytes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_QUOTEID, quote_id);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_IMAGE, bytes);

        if(entries.indexOf(bytes)==-1){
            db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
            entries.add(bytes);
        }
        db.close();
    }

    public void deleteFavorite(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry.COLUMN_QUOTEID+ "=" + id, null);
    }

    public Bitmap[] getAllFavorite(){
        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_QUOTEID,
                FavoriteContract.FavoriteEntry.COLUMN_IMAGE,
        };
        String sortOrder =
                FavoriteContract.FavoriteEntry._ID + " ASC";
        Bitmap []favoriteList = new Bitmap[20];
        SQLiteDatabase db = this.getReadableDatabase();
        byte bytes[];
        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);
        if (cursor.moveToFirst()){
            do {
                //SelfMade favs=new SelfMade();
                //Famous favf=new Famous();
                String qid=cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_QUOTEID));

                //if(qid.charAt(qid.length()-1)=='s'){
                //favs.quote_id=qid;
                bytes=(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_IMAGE)));
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                favoriteList[total]=bmp;
                total++;
                //}
                //if(qid.charAt(qid.length()-1)=='f'){
                    //favf.quote_id=(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_QUOTEID)));
                //bytes=(cursor.getBlob(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_IMAGE)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoriteList;
    }
}