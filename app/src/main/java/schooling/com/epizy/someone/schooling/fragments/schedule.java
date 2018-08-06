package schooling.com.epizy.someone.schooling.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.eunsiljo.timetablelib.data.TimeData;
import com.github.eunsiljo.timetablelib.data.TimeGridData;
import com.github.eunsiljo.timetablelib.data.TimeTableData;
import com.github.eunsiljo.timetablelib.view.TimeTableView;
import com.github.eunsiljo.timetablelib.viewholder.TimeTableItemViewHolder;

import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Objects;

import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.models.timetable_model;
import schooling.com.epizy.someone.schooling.R;

import static android.app.Activity.RESULT_OK;

public class schedule extends Fragment {
    TimeTableView timetable;
    String [] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    long today = 0; DBHelper db; int colorData = 0; int row = 0;
    private static DateTime now = DateTime.now();
    ArrayList<TimeTableData> tableData;
    ArrayList<timetable_model> listModel;

    ArrayList<TimeData> mon = new ArrayList<>();
    ArrayList<TimeData> tue = new ArrayList<>();
    ArrayList<TimeData> wed = new ArrayList<>();
    ArrayList<TimeData> thu = new ArrayList<>();
    ArrayList<TimeData> fri = new ArrayList<>();
    ArrayList<TimeData> sat = new ArrayList<>();

    public schedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.f_schedule, container, false);
        timetable = v.findViewById(R.id.timetable_view);

        db = new DBHelper(this.getContext());
        tableData = new ArrayList<>();
        listModel = new ArrayList<>();

        initTimeTable();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!listModel.isEmpty()||!tableData.isEmpty()){
            row = 0;
            listModel.clear(); tableData.clear();
            mon.clear(); tue.clear(); wed.clear();
            thu.clear(); fri.clear(); sat.clear();
            getTimeTableData(now.withTimeAtStartOfDay().getMillis(), days);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2&&resultCode==RESULT_OK){

            if(db.add_timetable(new timetable_model(data.getStringExtra("name"), data.getStringExtra("type"), data.getStringExtra("day"), data.getStringExtra("start"), data.getStringExtra("end")))){
                Log.d("Database Operation :", "Adding timetable");
                timetable_model newest = new timetable_model(data.getStringExtra("name"), data.getStringExtra("type"), data.getStringExtra("day"), data.getStringExtra("start"), data.getStringExtra("end"));
                newest.setId(String.valueOf(db.getTimeTableId(newest.name, newest.type)));
                listModel.add(newest);

                if(data.getStringExtra("type").equals("Praktikum")){
                    colorData = R.color.color_table_3;
                }else{
                    colorData = R.color.color_table_1;
                }
                String [] start = data.getStringExtra("start").split(":");
                String [] end = data.getStringExtra("end").split(":");
                DateTime cur = new DateTime(today);
                long startHour = cur.plusHours(Integer.valueOf(start[0])).plusMinutes(Integer.valueOf(start[1])).getMillis();
                long endHour = cur.plusHours(Integer.valueOf(end[0])).plusMinutes(Integer.valueOf(end[1])).getMillis();

                //Algorithm for inserting data to timetable
                switch (data.getStringExtra("day")){
                    case "Monday":
                        mon.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(0, new TimeTableData(days[0], mon));
                        break;
                    case "Tuesday":
                        tue.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(1, new TimeTableData(days[1], tue));
                        break;
                    case "Wednesday":
                        wed.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(2, new TimeTableData(days[2], wed));
                        break;
                    case "Thursday":
                        thu.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(3, new TimeTableData(days[3], thu));
                        break;
                    case "Friday":
                        fri.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(4, new TimeTableData(days[4], fri));
                        break;
                    case "Saturday":
                        sat.add(new TimeData(row, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(5, new TimeTableData(days[5], sat));
                        break;
                }
                ++row;
                timetable.setTimeTable(today, tableData);

            }else{
                Toast.makeText(this.getContext(), data.getStringExtra("Adding timetable error!"), Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this.getContext(), "Adding Timetable canceled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initTimeTable() {
        today = now.withTimeAtStartOfDay().getMillis();
        timetable.setStartHour(6);
        timetable.setShowHeader(true);
        timetable.setTableMode(TimeTableView.TableMode.SHORT);
        getTimeTableData(today, days);
        timetable.setTimeTable(today, tableData);
        @SuppressLint("InflateParams") final View v = getLayoutInflater().inflate(R.layout.d_timetable, null);

        //When TimeItem Clicked, it's show dialog which contain data and option to delete and update
        timetable.setOnTimeItemClickListener(new TimeTableItemViewHolder.OnTimeItemClickListener() {
            @Override
            public void onTimeItemClick(View view, int i, final TimeGridData timeGridData) {
                final TimeData data = timeGridData.getTime();
                final timetable_model current = listModel.get((int) data.getKey());
                ((TextView)v.findViewById(R.id.d_start_hour)).setText(current.start_Hour);
                ((TextView)v.findViewById(R.id.d_end_hour)).setText(current.end_Hour);
                ((TextView)v.findViewById(R.id.d_type)).setText(current.type);
                ((TextView)v.findViewById(R.id.d_date)).setText(current.day);
                final MaterialDialog.Builder mdb = new MaterialDialog.Builder(Objects.requireNonNull(schedule.this.getContext()));
                mdb.title(data.getTitle()).customView(v, true).negativeText("Close").positiveText("Delete");
                mdb.callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if(db.deleteTimetable(Integer.valueOf(current.id))){
                            Toast.makeText(schedule.this.getContext(), "Delete Success", Toast.LENGTH_SHORT).show();

                            //Algorithm for deleting data from timetable
                            switch (current.day){
                                case "Monday":
                                    for(TimeData newest : mon){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            mon.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(0, new TimeTableData(days[0], mon));
                                    break;
                                case "Tuesday":
                                    for(TimeData newest : tue){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            tue.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(1, new TimeTableData(days[1], tue));
                                    break;
                                case "Wednesday":
                                    for(TimeData newest : wed){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            wed.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(2, new TimeTableData(days[2], wed));
                                    break;
                                case "Thursday":
                                    for(TimeData newest : thu){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            thu.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(3, new TimeTableData(days[3], thu));
                                    break;
                                case "Friday":
                                    for(TimeData newest : fri){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            fri.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(4, new TimeTableData(days[4], fri));
                                    break;
                                case "Saturday":
                                    for(TimeData newest : sat){
                                        if(newest.getKey().equals(data.getKey())&&newest.getTitle().equals(data.getTitle())&&newest.getColorRes()==data.getColorRes()){
                                            sat.remove(newest);
                                            break;
                                        }
                                    }
                                    tableData.set(5, new TimeTableData(days[5], sat));
                                    break;
                            }
                            listModel.remove(current);
                            timetable.setTimeTable(today, tableData);
                        }else{
                            Toast.makeText(schedule.this.getContext(), "Delete Failed!", Toast.LENGTH_SHORT).show();
                        }
                        super.onPositive(dialog);
                    }
                });
                mdb.build().show();
            }
        });

    }

    private void getTimeTableData(long today, String[] days) {
        DateTime awal = new DateTime(today);
        db.data_timetable(listModel);
        for(timetable_model model : listModel){
            if(model.type.equals("Praktikum")){
                colorData = R.color.color_table_3;
            }else{
                colorData = R.color.color_table_1;
            }
            String [] start = model.start_Hour.split(":");
            String [] end = model.end_Hour.split(":");
            long startHour = awal.plusHours(Integer.valueOf(start[0])).plusMinutes(Integer.valueOf(start[1])).getMillis();
            long endHour = awal.plusHours(Integer.valueOf(end[0])).plusMinutes(Integer.valueOf(end[1])).getMillis();
            switch (model.day){
                case "Monday":
                    mon.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
                case "Tuesday":
                    tue.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
                case "Wednesday":
                    wed.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
                case "Thursday":
                    thu.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
                case "Friday":
                    fri.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
                case "Saturday":
                    sat.add(new TimeData(row,model.name, colorData, R.color.white, startHour, endHour));
                    break;
            }
            ++row;
        }

        ArrayList<TimeTableData> tables = new ArrayList<>();
        tables.add(new TimeTableData(days[0], mon));
        tables.add(new TimeTableData(days[1], tue));
        tables.add(new TimeTableData(days[2], wed));
        tables.add(new TimeTableData(days[3], thu));
        tables.add(new TimeTableData(days[4], fri));
        tables.add(new TimeTableData(days[5], sat));

        tableData.addAll(tables);
    }
}
