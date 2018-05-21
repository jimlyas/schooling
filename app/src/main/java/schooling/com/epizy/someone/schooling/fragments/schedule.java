package schooling.com.epizy.someone.schooling.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import org.joda.time.format.DateTimeFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.Model.timetable_model;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.add_schedule;

import static android.app.Activity.RESULT_OK;

public class schedule extends Fragment {
    TimeTableView timetable; FloatingActionButton fab;
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
        fab = v.findViewById(R.id.fab_schedule);
        fab.setImageResource(R.drawable.ic_add);
        tableData = new ArrayList<>();
        listModel = new ArrayList<>();
        db.data_timetable(listModel);

        initTimeTable();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(schedule.this.getContext(), add_schedule.class), 0);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            Toast.makeText(this.getContext(), data.getStringExtra("type"), Toast.LENGTH_SHORT).show();
            if(db.add_timetable(new timetable_model(data.getStringExtra("name"), data.getStringExtra("type"), data.getStringExtra("day"), data.getStringExtra("start"), data.getStringExtra("end")))){
                listModel.add(new timetable_model(data.getStringExtra("name"), data.getStringExtra("type"), data.getStringExtra("day"), data.getStringExtra("start"), data.getStringExtra("end")));
                ++row;
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
                switch (data.getStringExtra("day")){
                    case "Monday":
                        mon.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(0, new TimeTableData(days[0], mon));
                        break;
                    case "Tuesday":
                        tue.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(1, new TimeTableData(days[1], tue));
                        break;
                    case "Wednesday":
                        wed.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(2, new TimeTableData(days[2], wed));
                        break;
                    case "Thursday":
                        thu.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(3, new TimeTableData(days[3], thu));
                        break;
                    case "Friday":
                        fri.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(4, new TimeTableData(days[4], fri));
                        break;
                    case "Saturday":
                        sat.add(new TimeData(0, data.getStringExtra("name"), colorData, R.color.white, startHour, endHour));
                        tableData.set(5, new TimeTableData(days[5], sat));
                        break;
                }

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
        Log.w("Date-nya", String.valueOf(today));
        timetable.setStartHour(6);
        timetable.setShowHeader(true);
        timetable.setTableMode(TimeTableView.TableMode.SHORT);
        tableData = getTimeTableData(today, days);
        timetable.setTimeTable(today, tableData);
        final View v = getLayoutInflater().inflate(R.layout.d_timetable, null);
        timetable.setOnTimeItemClickListener(new TimeTableItemViewHolder.OnTimeItemClickListener() {
            @Override
            public void onTimeItemClick(View view, int i, TimeGridData timeGridData) {
                TimeData data = timeGridData.getTime();
                timetable_model current = listModel.get((int) data.getKey());
                ((TextView)v.findViewById(R.id.d_start_hour)).setText(current.jamawal);
                ((TextView)v.findViewById(R.id.d_end_hour)).setText(current.jamakhir);
                ((TextView)v.findViewById(R.id.d_type)).setText(current.type);
                MaterialDialog.Builder mdb = new MaterialDialog.Builder(schedule.this.getContext());
                mdb.title(data.getTitle()).customView(v, true).negativeText("Close");
                mdb.build().show();
            }
        });

    }

    private ArrayList<TimeTableData> getTimeTableData(long today, String[] days) {
        DateTime awal = new DateTime(today);

        for(timetable_model model : listModel){
            if(model.type.equals("Praktikum")){
                colorData = R.color.color_table_3;
            }else{
                colorData = R.color.color_table_1;
            }
            String [] start = model.jamawal.split(":");
            String [] end = model.jamakhir.split(":");
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
        return  tableData;
    }

    private long getmillis(String s) {
        DateTime baru = DateTimeFormat.forPattern("HH:mm:ss").parseDateTime(s);
        Log.w("Time Tables : ",String.valueOf(baru.getMillis()));
        return  baru.getMillis();
    }

}
