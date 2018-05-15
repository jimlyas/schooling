package schooling.com.epizy.someone.schooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import schooling.com.epizy.someone.schooling.Model.teacher_model;

public class DBHelper extends SQLiteOpenHelper {
    public Context context; SQLiteDatabase database;
    public static final String database_name = "school.db";
    public static final String teacher_table = "teacher";
    public static final String column_one_teacher = "name";
    public static final String column_two_teacher = "phone";

    public DBHelper(Context context) {
        super(context, database_name, null, 1);
        this.context = context;
        database = this.getWritableDatabase();
        database.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, name varchar(50), phone varchar(15))");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists "+teacher_table+" (id integer primary key autoincrement, name varchar(50), phone varchar(15))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+teacher_table);
        onCreate(sqLiteDatabase);
    }

    public boolean add_teacher(teacher_model model){
        ContentValues values = new ContentValues();
        values.put(column_one_teacher, model.name);
        values.put(column_two_teacher, model.phone);
        long result = database.insert(teacher_table, null, values);
        if(result==-1){
            return false;
        }else{
            return  true;
        }
    }

    public void data_teachers(List<teacher_model> list){
        Cursor cursor = this.getReadableDatabase().rawQuery("select * from "+teacher_table, null);
        while (cursor.moveToNext()){
            teacher_model current = new teacher_model(cursor.getString(1), cursor.getString(2));
            current.setId(String.valueOf(cursor.getInt(0)));
            list.add(current);
        }
    }
}
