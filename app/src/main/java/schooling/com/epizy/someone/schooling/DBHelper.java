package schooling.com.epizy.someone.schooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import schooling.com.epizy.someone.schooling.Model.subject_model;
import schooling.com.epizy.someone.schooling.Model.teacher_model;

public class DBHelper extends SQLiteOpenHelper {
    public Context context; private SQLiteDatabase database;
    private static final String database_name = "school.db";
    private static final String teacher_table = "teacher";
    private static final String teacher_column_one = "name";
    private static final String teacher_column_two = "phone";

    private static final String subject_table = "subject";
    private static final String subject_column_one = "name";
    private static final String subject_column_two = "room";
    private static final String subject_column_three = "teacher";
    private static final String subject_column_four = "note";

    public DBHelper(Context context) {
        super(context, database_name, null, 1);
        this.context = context;
        database = this.getWritableDatabase();
        database.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, name varchar(50), phone varchar(15))");
        database.execSQL("create table if not exists "+subject_table+" (id integer primary key autoincrement," +
                " name varchar(50), room varchar(10), teacher varchar(50), note varchar(100))");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, name varchar(50), phone varchar(15))");
        sqLiteDatabase.execSQL("create table if not exists "+subject_table+" (id integer primary key autoincrement," +
                " name varchar(50), room varchar(10), teacher varchar(50), note varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+teacher_table);
        sqLiteDatabase.execSQL("drop table if exists "+subject_table);
        onCreate(sqLiteDatabase);
    }

    public boolean add_teacher(teacher_model model){
        ContentValues values = new ContentValues();
        values.put(teacher_column_one, model.name);
        values.put(teacher_column_two, model.phone);
        long result = database.insert(teacher_table, null, values);

        return result != -1;
    }

    public void data_teachers(List<teacher_model> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from "+teacher_table, null);
        while (cursor.moveToNext()){
            teacher_model current = new teacher_model(cursor.getString(1), cursor.getString(2));
            current.setId(String.valueOf(cursor.getInt(0)));
            list.add(current);
        }
        cursor.close();
    }

    public void teachers_name(List<String> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select name from "+teacher_table, null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
    }

    public boolean add_subject(subject_model model){
        ContentValues values = new ContentValues();
        values.put(subject_column_one, model.name);
        values.put(subject_column_two, model.room);
        values.put(subject_column_three, model.teacher);
        values.put(subject_column_four, model.note);
        long result = database.insert(subject_table, null, values);
        return result != -1;
    }

    public void data_subjects(List<subject_model> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from "+subject_table, null);
        while (cursor.moveToNext()){
            subject_model current = new subject_model(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            current.setId(String.valueOf(cursor.getInt(0)));
            list.add(current);
        }
        cursor.close();
    }

    public boolean deletesubject(String name){
        return database.delete(subject_table, subject_column_one+"=\""+name+"\"", null)>0;
    }
}
