package schooling.com.epizy.someone.schooling.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.eunsiljo.timetablelib.data.TimeData;
import com.github.eunsiljo.timetablelib.data.TimeTableData;
import com.github.eunsiljo.timetablelib.view.TimeTableView;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import java.util.ArrayList;
import schooling.com.epizy.someone.schooling.R;

public class schedule extends Fragment {
    TimeTableView timetable; FloatingActionButton fab;
    String [] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    long today = 0; ArrayList<TimeData> values;
    private static DateTime now = DateTime.now();
    ArrayList<TimeTableData> tableData;
    public schedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.f_schedule, container, false);
        timetable = v.findViewById(R.id.timetable_view);
        fab = v.findViewById(R.id.fab_schedule);
        fab.setImageResource(R.drawable.ic_add);
        values = new ArrayList<>();
        tableData = new ArrayList<>();

        initTimeTable();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(schedule.this.getContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();

//                TODO[Integration] Adding subject to timetable
//                DateTime awal = new DateTime(today).plusHours(9).plusMinutes(30);
//                DateTime akhir = awal.plusHours(3).plusMinutes(45);
//                values.add(new TimeData(1, "Else", R.color.color_table_2, R.color.white, awal.getMillis(), akhir.getMillis()));
//                tableData.set(0, new TimeTableData("Mon", values));
//                timetable.setTimeTable(today, tableData);
            }
        });

        return v;
    }

    private void initTimeTable() {
        today = now.withTimeAtStartOfDay().getMillis();
        Log.w("Date-nya", String.valueOf(today));
        timetable.setStartHour(6);
        timetable.setShowHeader(true);
        timetable.setTableMode(TimeTableView.TableMode.SHORT);
        tableData = getsamples(today, days);
        timetable.setTimeTable(today, tableData);

    }

    private ArrayList<TimeTableData> getsamples(long today, String[] days) {
        ArrayList<TimeTableData> tableData = new ArrayList<>();
        DateTime awal = new DateTime(today);
        ArrayList<TimeData> mon = new ArrayList<>();
        mon.add(new TimeData(0,"Manajemen Layanan", R.color.color_table_1, R.color.white, awal.plusHours(10).getMillis(), awal.plusHours(13).plusMinutes(30).getMillis()));

        ArrayList<TimeData> tue = new ArrayList<>();
        tue.add(new TimeData(0,"Akuntansi", R.color.color_table_1, R.color.white, awal.plusHours(6).plusMinutes(30).getMillis(), awal.plusHours(9).plusMinutes(30).getMillis()));
        tue.add(new TimeData(1,"Kustomisasi ERP", R.color.color_table_1, R.color.white, awal.plusHours(12).plusMinutes(30).getMillis(), awal.plusHours(15).plusMinutes(30).getMillis()));

        ArrayList<TimeData> wed = new ArrayList<>();
        wed.add(new TimeData(0,"Rekayasa Perangkat Lunak", R.color.color_table_1, R.color.white, awal.plusHours(11).plusMinutes(30).getMillis(), awal.plusHours(15).plusMinutes(30).getMillis()));

        ArrayList<TimeData> thu = new ArrayList<>();
        thu.add(new TimeData(0,"Keamanan Sistem Informasi", R.color.color_table_1, R.color.white, awal.plusHours(9).plusMinutes(30).getMillis(), awal.plusHours(11).plusMinutes(30).getMillis()));
        thu.add(new TimeData(1,"Pengembangan Aplikasi Bergerak", R.color.color_table_1, R.color.white, awal.plusHours(13).plusMinutes(30).getMillis(), awal.plusHours(16).plusMinutes(30).getMillis()));

        ArrayList<TimeTableData> tables = new ArrayList<>();
        tables.add(new TimeTableData(days[0], mon));
        tables.add(new TimeTableData(days[1], tue));
        tables.add(new TimeTableData(days[2], wed));
        tables.add(new TimeTableData(days[3], thu));
        tables.add(new TimeTableData(days[4], null));
        tables.add(new TimeTableData(days[5], null));

        tableData.addAll(tables);
        return  tableData;
    }

    private long getmillis(String s) {
        DateTime baru = DateTimeFormat.forPattern("HH:mm:ss").parseDateTime(s);
        Log.w("Time Tables : ",String.valueOf(baru.getMillis()));
        return  baru.getMillis();
    }

}
