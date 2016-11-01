package nl.mmaaikel.homework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maikel on 19-09-16.
 */

public class DataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] assignmentAllColumns = { MySQLiteHelper.COLUMN_ASSIGNMENT_ID, MySQLiteHelper.COLUMN_ASSIGNMENT };

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

    public long createAssignment(String assignment) {
        // If the database is not open yet, open it
        if (!database.isOpen())
            open();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ASSIGNMENT, assignment);
        long insertId = database.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null, values);
        // If the database is open, close it
        if (database.isOpen())
            close();
        return insertId;
    }

    public void deleteAssignment(Assignment assignment) {
        if (!database.isOpen())
            open();
        database.delete(MySQLiteHelper.TABLE_ASSIGNMENTS, MySQLiteHelper.COLUMN_ASSIGNMENT_ID + " =?", new String[] { Long.toString(assignment.getId())});
        if (database.isOpen())
            close();
    }

    public void updateAssignment(Assignment assignment) {
        if (!database.isOpen())
            open();
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_ASSIGNMENT, assignment.getAssignment());
        args.put(MySQLiteHelper.COLUMN_COURSE_ID, assignment.getCourse().getId());
        database.update(MySQLiteHelper.TABLE_ASSIGNMENTS, args, MySQLiteHelper.COLUMN_ASSIGNMENT_ID + "=?", new String[] { Long.toString(assignment.getId()) });
        if (database.isOpen())
            close();
    }

    public Cursor getAllAssignments() {
        if (!database.isOpen())
            open();
        List<Assignment> assignments = new ArrayList<Assignment>();
        Cursor cursor = database.rawQuery(
                "SELECT assignments.*, courses.*" +
                        " FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " assignments" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_COURSES + " courses" +
                        " ON assignments." + MySQLiteHelper.COLUMN_COURSE_ID + " = courses." + MySQLiteHelper.COLUMN_COURSE_ID , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Assignment assignment = cursorToAssignment(cursor);
            assignments.add(assignment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        if (database.isOpen())
            close();
        return cursor;
    }

    public Cursor getAllAssignmentsCursor() {
        if (!database.isOpen())
            open();
        Cursor cursor = database.rawQuery(
                "SELECT" +
                        " assignments." + MySQLiteHelper.COLUMN_ASSIGNMENT_ID + " AS _id," +
                        " assignments." + MySQLiteHelper.COLUMN_ASSIGNMENT + "," +
                        " courses." + MySQLiteHelper.COLUMN_COURSE +
                        " FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " assignments" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_COURSES + " courses" +
                        " ON assignments." + MySQLiteHelper.COLUMN_COURSE_ID + " = courses." + MySQLiteHelper.COLUMN_COURSE_ID, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (database.isOpen())
            close();
        return cursor;
    }

    private Assignment cursorToAssignment(Cursor cursor) {
        try {
            Assignment assignment = new Assignment();
            Course course = new Course();
            assignment.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ASSIGNMENT_ID)));
            assignment.setAssignment(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ASSIGNMENT)));
            course.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_COURSE_ID)));
            course.setCourse(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_COURSE)));
            assignment.setCourse(course);
            return assignment;
        } catch(CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public Assignment getAssignment(long columnId) {
        if (!database.isOpen())
            open();
        Cursor cursor = database.rawQuery(
                "SELECT assignments.*, courses.*" +
                        " FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " assignments" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_COURSES + " courses" +
                        " ON assignments." + MySQLiteHelper.COLUMN_COURSE_ID + " = courses." + MySQLiteHelper.COLUMN_COURSE_ID +
                        " WHERE assignments." + MySQLiteHelper.COLUMN_ASSIGNMENT_ID + " = " + columnId, null);
        cursor.moveToFirst();
        Assignment assignment = cursorToAssignment(cursor);
        cursor.close();
        if (database.isOpen())
            close();
        return assignment;
    }

    public void deleteAssignment(long id) {
        if (!database.isOpen())
            open();
        database.delete(MySQLiteHelper.TABLE_ASSIGNMENTS, MySQLiteHelper.COLUMN_ASSIGNMENT_ID + " =?", new String[] { Long.toString(id)});
        if (database.isOpen())
            close();
    }

    public long createAssignment(String assignment, long courseId) {
        // If the database is not open yet, open it
        if (!database.isOpen())
            open();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ASSIGNMENT, assignment);
        values.put(MySQLiteHelper.COLUMN_COURSE_ID, courseId);
        long insertId = database.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null, values);
        // If the database is open, close it
        if (database.isOpen())
            close();
        return insertId;
    }

    public long createCourse(String course) {
        if (!database.isOpen())
            open();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COURSE, course);
        long insertId = database.insert(MySQLiteHelper.TABLE_COURSES, null, values);
        if (database.isOpen())
            close();
        return insertId;
    }

    public void deleteCourse(Course course) {
        if (!database.isOpen())
            open();
        Cursor cursor = database.rawQuery(
                "SELECT" +
                        " assignments.*," + " courses.*" +
                        " FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS + " assignments" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_COURSES + " courses" +
                        " ON assignments." + MySQLiteHelper.COLUMN_COURSE_ID + " = courses." + MySQLiteHelper.COLUMN_COURSE_ID +
                        " WHERE assignments." + MySQLiteHelper.COLUMN_COURSE_ID + " = " + course.getId(), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Assignment assignment = cursorToAssignment(cursor);
            deleteAssignment(assignment.getId());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        database.delete(MySQLiteHelper.TABLE_COURSES, MySQLiteHelper.COLUMN_COURSE_ID + " =?", new String[]{Long.toString(course.getId())});
        if (database.isOpen())
            close();
    }

    public Cursor getAllCoursesCursor() {
        if (!database.isOpen())
            open();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        MySQLiteHelper.COLUMN_COURSE_ID + " AS _id," +
                        MySQLiteHelper.COLUMN_COURSE +
                        " FROM " + MySQLiteHelper.TABLE_COURSES, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (database.isOpen())
            close();
        return cursor;
    }
}
