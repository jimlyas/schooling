package schooling.com.epizy.someone.schooling.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import schooling.com.epizy.someone.schooling.Model.subject_model;
import schooling.com.epizy.someone.schooling.R;

public class subject_adapter  extends RecyclerView.Adapter<subject_adapter.ViewHolder>{
    public Context context; private List<subject_model> list;

    public subject_adapter(Context context, List<subject_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rec_subject, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        subject_model current = list.get(position);
        holder.name.setText(current.name);
        holder.room.setText(current.room);
        holder.teacher.setText(current.teacher);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public subject_model getItem(int position){
//        return list.get(position);
//    }

//    public void removeItem(int position){
//        list.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, list.size());
//    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, room, teacher;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_subject_name);
            room = itemView.findViewById(R.id.rec_subject_room);
            teacher = itemView.findViewById(R.id.rec_subject_teacher);
        }
    }
}
