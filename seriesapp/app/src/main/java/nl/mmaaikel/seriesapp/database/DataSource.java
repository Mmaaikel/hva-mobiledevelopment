package nl.mmaaikel.seriesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nl.mmaaikel.seriesapp.Serie;
import nl.mmaaikel.seriesapp.Watched;

/**
 * Created by Maikel on 19-09-16.
 */

public class DataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    public String[] seriesAllColumns = { MySQLiteHelper.COLUMN_SERIE_ID, MySQLiteHelper.COLUMN_SERIE_NAME, MySQLiteHelper.COLUMN_SERIE_IMAGE };

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    // Opens the database to use it
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Closes the database when you no longer need it
    public void close() {
        dbHelper.close();
    }


    /*
        *
        * SERIES
        *
     */

    public long createSerie(String name, byte[] image) {
        return createSerie(name, null, image);
    }

    //Before inserting into database, you need to convert your Bitmap image into byte array first then apply it using database query.
    public long createSerie(String name, String description, byte[] image) {
        // If the database is not open yet, open it
        if (!database.isOpen()) {
            open();
        }

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SERIE_NAME, name);
        values.put(MySQLiteHelper.COLUMN_SERIE_DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_SERIE_IMAGE, image);
        long insertId = database.insert(MySQLiteHelper.TABLE_SERIES, null, values);

        // If the database is open, close it
        if (database.isOpen()) {
            close();
        }

        return insertId;
    }

    public void deleteSerie(Serie serie) {
        if (!database.isOpen()) {
            open();
        }

        database.delete(MySQLiteHelper.TABLE_SERIES, MySQLiteHelper.COLUMN_SERIE_ID + " =?", new String[] { Long.toString( serie.getId() ) } );

        if (database.isOpen()) {
            close();
        }
    }

    public void updateSerie(Serie serie) {
        if (!database.isOpen()) {
            open();
        }

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_SERIE_ID, serie.getId());
        args.put(MySQLiteHelper.COLUMN_SERIE_NAME, serie.getName());
        args.put(MySQLiteHelper.COLUMN_SERIE_DESCRIPTION, serie.getDescription());
        args.put(MySQLiteHelper.COLUMN_SERIE_IMAGE, serie.getImage());
        database.update(MySQLiteHelper.TABLE_SERIES, args, MySQLiteHelper.COLUMN_SERIE_ID + "=?", new String[] { Long.toString( serie.getId() ) });

        if (database.isOpen()) {
            close();
        }
    }

    public List<Serie> getAllSeries() {
        if (!database.isOpen()) {
            open();
        }

        List<Serie> series = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + MySQLiteHelper.TABLE_SERIES , null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Serie serie = cursorToSerie(cursor);
            series.add( serie );
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        if (database.isOpen()) {
            close();
        }

        return series;
    }

    public Cursor getAllSeriesCursor() {
        if (!database.isOpen()) {
            open();
        }

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + MySQLiteHelper.TABLE_SERIES , null);

        if( cursor != null ) {
            cursor.moveToFirst();
        }

        // make sure to close the cursor
        if (database.isOpen()) {
            close();
        }

        return cursor;
    }

    public Serie getSerie(long serieId) {
        if (!database.isOpen()) {
            open();
        }

        Cursor cursor = database.rawQuery(
                "SELECT * " +
                        " FROM " + MySQLiteHelper.TABLE_SERIES +
                        " WHERE " + MySQLiteHelper.COLUMN_SERIE_ID + " = " + serieId, null);
        cursor.moveToFirst();
        Serie serie = cursorToSerie(cursor);
        cursor.close();

        if (database.isOpen()) {
            close();
        }

        return serie;
    }

    public void deleteSerie(long id) {
        if (!database.isOpen()) {
            open();
        }

        database.delete(MySQLiteHelper.TABLE_SERIES, MySQLiteHelper.COLUMN_SERIE_ID + " =?", new String[] { Long.toString(id) });

        if (database.isOpen()) {
            close();
        }
    }

    public void updateWatched(Watched watched) {
        if (!database.isOpen()) {
            open();
        }

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_WATCHED_ID, watched.getId());
        args.put(MySQLiteHelper.COLUMN_WATCHED_SERIE_ID, watched.getSerieId());
        args.put(MySQLiteHelper.COLUMN_WATCHED_SEASON, watched.getSeason());
        args.put(MySQLiteHelper.COLUMN_WATCHED_EPISODE, watched.getEpisode());
        database.update(MySQLiteHelper.TABLE_WATCHED, args, MySQLiteHelper.COLUMN_WATCHED_ID + "=?", new String[] { Long.toString( watched.getId() ) });

        if (database.isOpen()) {
            close();
        }
    }

    //When retrieving from database, you certainly have a byte array of image, what you need to do is to convert byte array back to original image. So, you have to make use of BitmapFactory to decode.
    private Serie cursorToSerie(Cursor cursor) {
        try {
            Serie serie = new Serie();
            serie.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIE_ID)));
            serie.setName(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIE_NAME)));
            serie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_SERIE_DESCRIPTION)));
            serie.setImage( cursor.getBlob( cursor.getColumnIndexOrThrow( MySQLiteHelper.COLUMN_SERIE_IMAGE )) );

            return serie;
        } catch(CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /*
        *
        * WATCHED
        *
     */

    public long createWatched(Long serieId, String season, String episode ) {
        // If the database is not open yet, open it
        if (!database.isOpen()) {
            open();
        }

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_WATCHED_SERIE_ID, serieId);
        values.put(MySQLiteHelper.COLUMN_WATCHED_SEASON, season);
        values.put(MySQLiteHelper.COLUMN_WATCHED_EPISODE, episode);
        long insertId = database.insert(MySQLiteHelper.TABLE_WATCHED, null, values);

        // If the database is open, close it
        if (database.isOpen()) {
            close();
        }

        return insertId;
    }

    public void deleteWatched(Watched watched) {
        if (!database.isOpen()) {
            open();
        }

        database.delete(MySQLiteHelper.TABLE_WATCHED, MySQLiteHelper.COLUMN_WATCHED_ID + " =?", new String[] { Long.toString( watched.getId() ) } );

        if (database.isOpen()) {
            close();
        }
    }

    public List<Watched> getAllWatched() {
        if (!database.isOpen()) {
            open();
        }

        List<Watched> watched = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + MySQLiteHelper.TABLE_WATCHED , null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Watched w = cursorToWatched(cursor);
            watched.add( w );
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        if (database.isOpen()) {
            close();
        }

        return watched;
    }

    public List<Watched> getAllWatchedBySerieId(long serieId) {
        if (!database.isOpen()) {
            open();
        }

        List<Watched> watched = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "SELECT * " +
                        " FROM " + MySQLiteHelper.TABLE_WATCHED +
                        " WHERE " + MySQLiteHelper.COLUMN_WATCHED_SERIE_ID + " = " + serieId +
                        " ORDER BY " + MySQLiteHelper.COLUMN_WATCHED_ID + " DESC", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Watched w = cursorToWatched(cursor);
            watched.add( w );
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        if (database.isOpen()) {
            close();
        }

        return watched;
    }

    public Cursor getAllWatchedCursor() {
        if (!database.isOpen()) {
            open();
        }

        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + MySQLiteHelper.TABLE_WATCHED , null);

        if( cursor != null ) {
            cursor.moveToFirst();
        }

        // make sure to close the cursor
        if (database.isOpen()) {
            close();
        }

        return cursor;
    }

    public Cursor getAllWatchedBySerieIdCursor( long serieId ) {
        if (!database.isOpen()) {
            open();
        }

        Cursor cursor = database.rawQuery(
                "SELECT * " +
                        " FROM " + MySQLiteHelper.TABLE_WATCHED +
                        " WHERE " + MySQLiteHelper.COLUMN_WATCHED_SERIE_ID + " = " + serieId, null);

        if( cursor != null ) {
            cursor.moveToFirst();
        }

        // make sure to close the cursor
        if (database.isOpen()) {
            close();
        }

        return cursor;
    }

    public Watched getWatched(long watchedId) {
        if (!database.isOpen()) {
            open();
        }

        Cursor cursor = database.rawQuery(
                "SELECT * " +
                        " FROM " + MySQLiteHelper.TABLE_WATCHED +
                        " WHERE " + MySQLiteHelper.COLUMN_WATCHED_ID + " = " + watchedId, null);
        cursor.moveToFirst();
        Watched watched = cursorToWatched(cursor);
        cursor.close();

        if (database.isOpen()) {
            close();
        }

        return watched;
    }

    public void deleteWatched(long id) {
        if (!database.isOpen()) {
            open();
        }

        database.delete(MySQLiteHelper.TABLE_WATCHED, MySQLiteHelper.COLUMN_WATCHED_ID + " =?", new String[] { Long.toString(id) });

        if (database.isOpen()) {
            close();
        }
    }

    //When retrieving from database, you certainly have a byte array of image, what you need to do is to convert byte array back to original image. So, you have to make use of BitmapFactory to decode.
    private Watched cursorToWatched(Cursor cursor) {
        try {
            Watched watched = new Watched();
            watched.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_WATCHED_ID)));
            watched.setSerieId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_WATCHED_SERIE_ID)));
            watched.setSeason(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_WATCHED_SEASON)));
            watched.setEpisode(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_WATCHED_EPISODE)));

            return watched;
        } catch(CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
