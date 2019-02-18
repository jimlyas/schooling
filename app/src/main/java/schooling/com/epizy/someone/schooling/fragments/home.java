package schooling.com.epizy.someone.schooling.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.adapters.TodayClassAdapter;
import schooling.com.epizy.someone.schooling.models.timetable_model;

public class home extends Fragment {
    private static ArrayList<timetable_model> listData = new ArrayList<>();
    private LinearLayout no_class;
    private RecyclerView rec;
    private DBHelper helper;

    public home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_home, container, false);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        no_class = view.findViewById(R.id.no_class);
        rec = view.findViewById(R.id.today_class);
        TextView current_date = view.findViewById(R.id.current_date);
        helper = new DBHelper(getContext());

        current_date.setText(new SimpleDateFormat("EEEE, d MMMM yyyy").format(new Date()));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onStart() {
        super.onStart();
        if (!listData.isEmpty()){
            listData.clear();
            helper.data_timetable_by_day(listData, new SimpleDateFormat("EEEE").format(new Date()));
        }else {
            helper.data_timetable_by_day(listData, new SimpleDateFormat("EEEE").format(new Date()));
        }

        checkTodayClass(listData);
    }

    private void checkTodayClass(ArrayList<timetable_model> data){
        if (data.isEmpty()){
            no_class.setVisibility(View.VISIBLE);
            rec.setVisibility(View.GONE);
        }else {
            no_class.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);
            TodayClassAdapter adapter = new TodayClassAdapter(data, helper);
            rec.setLayoutManager(new LinearLayoutManager(getContext()));
            rec.setHasFixedSize(true);
            rec.addItemDecoration(new DividerItemDecoration(rec.getContext(), DividerItemDecoration.VERTICAL));
            rec.setAdapter(adapter);
        }
    }
}
