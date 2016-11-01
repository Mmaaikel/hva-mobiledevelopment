package nl.mmaaikel.seriesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maikel on 19-09-16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "mmaaikel_seriesapp.db";
    private static final int DATABASE_VERSION = 1;

    // Series
    public static final String TABLE_SERIES = "series";
    public static final String COLUMN_SERIE_ID = "_id";
    public static final String COLUMN_SERIE_NAME = "name";
    public static final String COLUMN_SERIE_DESCRIPTION = "description";
    public static final String COLUMN_SERIE_IMAGE = "image";

    // Watched
    public static final String TABLE_WATCHED = "watched";
    public static final String COLUMN_WATCHED_ID = "_id";
    public static final String COLUMN_WATCHED_SERIE_ID = "serie_id";
    public static final String COLUMN_WATCHED_SEASON = "season";
    public static final String COLUMN_WATCHED_EPISODE = "episode";

    // Creating the table
    private static final String DATABASE_CREATE_SERIES =
            "CREATE TABLE " + TABLE_SERIES +
                    "(" +
                    COLUMN_SERIE_ID + " integer primary key autoincrement, " +
                    COLUMN_SERIE_NAME + " text not null, " +
                    COLUMN_SERIE_DESCRIPTION + " text not null, " +
                    COLUMN_SERIE_IMAGE +" BLOB " +
                    ");";

    // Creating the table
    private static final String DATABASE_CREATE_WATCHED =
            "CREATE TABLE " + TABLE_WATCHED +
                    "(" +
                    COLUMN_WATCHED_ID + " integer primary key autoincrement, " +
                    COLUMN_WATCHED_SERIE_ID + " integer not null, " +
                    COLUMN_WATCHED_SEASON + " text not null, " +
                    COLUMN_WATCHED_EPISODE +" text not null " +
                    ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_SERIES);
        database.execSQL(DATABASE_CREATE_WATCHED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
        onCreate(db);
    }
}
