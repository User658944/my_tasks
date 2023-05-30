package fi.heina.mytasks;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String DEADLINE = "deadline";
    private static final String COMPLETED = "completed";
    private static final String NOTEICON = "noteicon";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTaskTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + DEADLINE + " DATE,"
                + COMPLETED + " DATE,"
                + NOTEICON + " TEXT"
                + ")";
        db.execSQL(createTaskTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertTask(ToDoModel task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, task.getTitle());
        values.put(DESCRIPTION, task.getDescription());
        values.put(DEADLINE, task.getDeadline());
        values.put(COMPLETED, task.getCompleted());
        values.put(NOTEICON, task.getNoteicon());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(taskId)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void completeTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMPLETED, getCurrentDate());
        values.put(NOTEICON, "notedone");
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(taskId)};
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public List<ToDoModel> getIncompletedTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, NOTEICON};
        String selection = "(" + COMPLETED + " IS NULL OR " + COMPLETED + " = '')";
        String orderBy = DEADLINE + " ASC";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, orderBy);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ToDoModel task = new ToDoModel();

                int columnIndexId = cursor.getColumnIndex(ID);
                int columnIndexTitle = cursor.getColumnIndex(TITLE);
                int columnIndexDescription = cursor.getColumnIndex(DESCRIPTION);
                int columnIndexDeadline = cursor.getColumnIndex(DEADLINE);
                int columnIndexCompleted = cursor.getColumnIndex(COMPLETED);
                int columnIndexNoteicon = cursor.getColumnIndex(NOTEICON);

                if (columnIndexId >= 0) {
                    task.setId(cursor.getInt(columnIndexId));
                }

                if (columnIndexTitle >= 0) {
                    task.setTitle(cursor.getString(columnIndexTitle));
                }

                if (columnIndexDescription >= 0) {
                    task.setDescription(cursor.getString(columnIndexDescription));
                }

                if (columnIndexDeadline >= 0) {
                    task.setDeadline(cursor.getString(columnIndexDeadline));
                }

                if (columnIndexCompleted >= 0) {
                    task.setCompleted(cursor.getString(columnIndexCompleted));
                }

                if (columnIndexNoteicon >= 0) {
                    task.setNoteicon(cursor.getString(columnIndexNoteicon));
                }

                taskList.add(task);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return taskList;
    }

    public List<ToDoModel> getCompletedTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ID, TITLE, DESCRIPTION, DEADLINE, COMPLETED, NOTEICON};
        String selection = "(" + COMPLETED + " IS NOT NULL OR " + COMPLETED + " != '')";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ToDoModel task = new ToDoModel();

                int columnIndexId = cursor.getColumnIndex(ID);
                int columnIndexTitle = cursor.getColumnIndex(TITLE);
                int columnIndexDescription = cursor.getColumnIndex(DESCRIPTION);
                int columnIndexDeadline = cursor.getColumnIndex(DEADLINE);
                int columnIndexCompleted = cursor.getColumnIndex(COMPLETED);
                int columnIndexNoteicon = cursor.getColumnIndex(NOTEICON);

                if (columnIndexId >= 0) {
                    task.setId(cursor.getInt(columnIndexId));
                }

                if (columnIndexTitle >= 0) {
                    task.setTitle(cursor.getString(columnIndexTitle));
                }

                if (columnIndexDescription >= 0) {
                    task.setDescription(cursor.getString(columnIndexDescription));
                }

                if (columnIndexDeadline >= 0) {
                    task.setDeadline(cursor.getString(columnIndexDeadline));
                }

                if (columnIndexCompleted >= 0) {
                    task.setCompleted(cursor.getString(columnIndexCompleted));
                }

                if (columnIndexNoteicon >= 0) {
                    task.setNoteicon(cursor.getString(columnIndexNoteicon));
                }

                taskList.add(task);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return taskList;
    }
}
