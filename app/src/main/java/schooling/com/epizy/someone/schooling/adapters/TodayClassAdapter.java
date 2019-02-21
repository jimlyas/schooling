package schooling.com.epizy.someone.schooling.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.models.timetable_model;

public class TodayClassAdapter extends RecyclerView.Adapter<TodayClassAdapter.ViewHolder>{
    private List<timetable_model> data;
    private DBHelper helper;

    public TodayClassAdapter(List<timetable_model> data, DBHelper helper) {
        this.helper = helper;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_today_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        String hour = data.get(position).start_Hour+"-"+data.get(position).end_Hour;
        holder.hour.setText(hour);
        holder.location.setText(helper.subject_location(data.get(position).name));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, location, hour;
        private ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.today_class_name);
            location = itemView.findViewById(R.id.today_class_location);
            hour = itemView.findViewById(R.id.today_class_hour);
        }
    }
}
