package schooling.com.epizy.someone.schooling.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import schooling.com.epizy.someone.schooling.Model.teacher_model;
import schooling.com.epizy.someone.schooling.R;

public class teacher_adapter  extends RecyclerView.Adapter<teacher_adapter.viewholder>{
    public Context context; private List<teacher_model> list;

    public teacher_adapter(Context context, List<teacher_model> model) {
        this.context = context;
        this.list = model;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(context).inflate(R.layout.rec_teacher, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        teacher_model current = list.get(position);
        holder.name.setText(current.name);
        holder.name.setTag(current.id);
        holder.phone.setText(current.phone);
        holder.initial.setText(current.name.substring(0, 1));
    }

    public teacher_model getItem(int position){
        return list.get(position);
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView name, phone, initial;
        viewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_teacher_name);
            phone = itemView.findViewById(R.id.rec_teacher_phone);
            initial = itemView.findViewById(R.id.rec_teacher_initial);
        }
    }
}
