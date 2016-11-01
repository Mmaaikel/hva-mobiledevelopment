package nl.mmaaikel.homework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maikel on 19-09-16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "myhomeworktutorial.db";
    private static final int DATABASE_VERSION = 2;

    // Assignments
    public static final String TABLE_ASSIGNMENTS = "assignments";
    public static final String COLUMN_ASSIGNMENT_ID = "assignment_id";
    public static final String COLUMN_ASSIGNMENT = "assignment";

    // Courses
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE = "course";

    // Creating the table
    private static final String DATABASE_CREATE_COURSES =
            "CREATE TABLE " + TABLE_COURSES +
                    "(" +
                    COLUMN_COURSE_ID + " integer primary key autoincrement, " +
                    COLUMN_COURSE + " text not null" +
                    ");";

    private static final String DATABASE_CREATE_ASSIGNMENTS =
            "CREATE TABLE " + TABLE_ASSIGNMENTS +
                    "(" +
                    COLUMN_ASSIGNMENT_ID + " integer primary key autoincrement, " +
                    COLUMN_ASSIGNMENT + " text not null, " +
                    COLUMN_COURSE_ID + " integer, " +
                    "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + ") ON DELETE CASCADE" +
                    ");";

    // Mandatory constructor which passes the context, database name and database version and passes it to the parent
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Execute the sql to create the table assignments
        database.execSQL(DATABASE_CREATE_COURSES);
        database.execSQL(DATABASE_CREATE_ASSIGNMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*
             * When the database gets upgraded you should handle the update to make sure there is no data loss.
             * This is the default code you put in the upgrade method, to delete the table and call the oncreate again.
             */
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        onCreate(db);

        if(oldVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
            onCreate(db);
        }
    }
}
