package schooling.com.epizy.someone.schooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import schooling.com.epizy.someone.schooling.Model.subject_model;
import schooling.com.epizy.someone.schooling.Model.teacher_model;
import schooling.com.epizy.someone.schooling.Model.timetable_model;

public class DBHelper extends SQLiteOpenHelper {
    public Context context; private SQLiteDatabase database;
    private static final String database_name = "school.db";
    private static final String teacher_table = "teacher";
    private static final String teacher_column_one = "teachername";
    private static final String teacher_column_two = "phone";

    private static final String subject_table = "subject";
    private static final String subject_column_one = "name";
    private static final String subject_column_two = "room";
    private static final String subject_column_three = "teachername";
    private static final String subject_column_four = "note";

    private static final String schedule_table = "schedule";
    private static final String schedule_column_one = "name";
    private static final String schedule_column_two = "type";
    private static final String schedule_column_three = "day";
    private static final String schedule_column_four = "startTime";
    private static final String schedule_column_five = "endTime";


    public DBHelper(Context context) {
        super(context, database_name, null, 1);
        this.context = context;
        database = this.getWritableDatabase();
        //TODO[On Delete Default] buat kolom teacher di tabel subject belum berhasil
        database.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, teachername varchar(50) unique, phone varchar(15) not null)");
        database.execSQL("create table if not exists "+subject_table+" (id integer primary key autoincrement," +
                " name varchar(50) unique not null, room varchar(10) not null, teachername varchar(50) default 'Not Defined', note varchar(100) not null, foreign key(teachername) references "+teacher_table+"(teachername)" +
                " on update cascade on delete set null)");
        database.execSQL("create table if not exists "+schedule_table+"(id integer primary key autoincrement, name varchar(50), type varchar(50), day varchar(50)" +
                ", startTime varchar(5), endTime varchar(5), foreign key (name) references "+subject_table+"(name) on update cascade on delete cascade)");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, teachername varchar(50) unique, phone varchar(15) not null)");
        sqLiteDatabase.execSQL("create table if not exists "+subject_table+" (id integer primary key autoincrement," +
                " name varchar(50) unique not null, room varchar(10) not null, teachername varchar(50) default 'Not Defined', note varchar(100) not null, foreign key(teachername) references "+teacher_table+"(teachername)" +
                " on update cascade on delete set null)");
        sqLiteDatabase.execSQL("create table if not exists "+schedule_table+"(id integer primary key autoincrement, name varchar(50), type varchar(50), day varchar(50)" +
                ", startTime varchar(5), endTime varchar(5), foreign key (name) references "+subject_table+"(name) on update cascade on delete cascade)");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+teacher_table);
        sqLiteDatabase.execSQL("drop table if exists "+subject_table);
        sqLiteDatabase.execSQL("drop table if exists "+schedule_table);
        onCreate(sqLiteDatabase);
    }

    public boolean add_teacher(teacher_model model){
        ContentValues values = new ContentValues();
        values.put(teacher_column_one, model.name);
        values.put(teacher_column_two, model.phone);
        long result = database.insert(teacher_table, null, values);

        return result != -1;
    }

    public boolean update_teacher(teacher_model model){
        ContentValues values = new ContentValues();
        values.put(teacher_column_one, model.name);
        values.put(teacher_column_two, model.phone);
        long result = database.update(teacher_table, values, "id="+model.id, null);

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
        Cursor cursor = this.getReadableDatabase().rawQuery("select teachername from "+teacher_table, null);
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

    public boolean update_subject(subject_model model){
        ContentValues values = new ContentValues();
        values.put(subject_column_one, model.name);
        values.put(subject_column_two, model.room);
        values.put(subject_column_three, model.teacher);
        values.put(subject_column_four, model.note);
        long result = database.update(subject_table, values, "id="+Integer.valueOf(model.id), null);
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

    public void subject_name(List<String> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select name from "+subject_table, null);
        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
    }

    public boolean deleteSubject(int index){
        return database.delete(subject_table, "id="+index, null)>0;
    }

    public boolean add_timetable(timetable_model model){
        ContentValues values = new ContentValues();
        values.put(schedule_column_one, model.name);
        values.put(schedule_column_two, model.type);
        values.put(schedule_column_three, model.day);
        values.put(schedule_column_four, model.start_Hour);
        values.put(schedule_column_five, model.end_Hour);
        long result = database.insert(schedule_table, null, values);
        return result != -1;
    }

    public void data_timetable(List<timetable_model> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from "+schedule_table, null);
        while (cursor.moveToNext()){
            timetable_model current = new timetable_model(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            current.setId(String.valueOf(cursor.getInt(0)));
            list.add(current);
        }
        cursor.close();
    }

    public int getTimeTableId(String The_name, String The_type){
        int id = 0;
        Cursor cursor = this.getReadableDatabase().rawQuery("select id from "+schedule_table+" where name='"+The_name+"' and type='"+The_type+"'", null);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public int getSubjectId(String The_name){
        int id = 0;
        Cursor cursor = this.getReadableDatabase().rawQuery("select id from "+subject_table+" where name='"+The_name+"'", null);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public int getTeacherId(String The_name){
        int id = 0;
        Cursor cursor = this.getReadableDatabase().rawQuery("select id from "+teacher_table+" where teachername='"+The_name+"'", null);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public boolean deleteTimetable(int number){
        return database.delete(schedule_table, "id="+number+"", null)>0;
    }

    public boolean deleteTeacher(int number){
        return database.delete(teacher_table, "id="+number+"", null)>0;
    }
}
