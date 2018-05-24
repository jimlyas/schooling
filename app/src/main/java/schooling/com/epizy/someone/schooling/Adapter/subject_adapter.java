package schooling.com.epizy.someone.schooling.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import schooling.com.epizy.someone.schooling.DBHelper;
import schooling.com.epizy.someone.schooling.Model.subject_model;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.add_subject;
import schooling.com.epizy.someone.schooling.fragments.subjects;

public class subject_adapter  extends RecyclerView.Adapter<subject_adapter.ViewHolder>{
    public Context context; private List<subject_model> list;
    private MaterialDialog.Builder mdb; private DBHelper database;

    public subject_adapter(Context context, List<subject_model> list) {
        this.context = context;
        this.list = list;
        database = new DBHelper(context);
        mdb = new MaterialDialog.Builder(context);
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
        holder.name.setTag(current.note);
        holder.room.setText(current.room);
        holder.room.setTag(current.id);
        holder.teacher.setText(current.teacher);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public subject_model getItem(int position){
        return list.get(position);
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, room, teacher; CardView cd;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_subject_name);
            room = itemView.findViewById(R.id.rec_subject_room);
            teacher = itemView.findViewById(R.id.rec_subject_teacher);
            cd = itemView.findViewById(R.id.root_rec_subject);


            final View v = LayoutInflater.from(context).inflate(R.layout.d_subjects, null);
            cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index = getAdapterPosition();
                    final subject_model current = list.get(index);
                    ((TextView)v.findViewById(R.id.d_subject_note)).setText(current.note);
                    ((TextView)v.findViewById(R.id.d_subject_room)).setText(current.room);
                    ((TextView)v.findViewById(R.id.d_subject_teacher)).setText(current.teacher);
                    mdb.title(name.getText().toString()).customView(v, true)
                            .negativeText("Delete").positiveText("Edit").neutralText("Close").autoDismiss(true);

                    mdb.callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            Intent edit = new Intent(context, add_subject.class);
                            edit.putExtra("RequestCode", "12");
                            edit.putExtra("name", current.name);
                            edit.putExtra("room", current.room);
                            edit.putExtra("teacher", current.teacher);
                            edit.putExtra("note", current.note);
                            edit.putExtra("id", current.id);
                            edit.putExtra("index", String.valueOf(index));
                            ((Activity)context).startActivityForResult(edit, 12);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            if(database.deleteSubject(Integer.valueOf(current.id))){
                                subject_adapter.this.removeItem(index);
                            }else{
                                Toast.makeText(context, "Delete subject Failed!", Toast.LENGTH_SHORT).show();
                            }
                            super.onNegative(dialog);
                        }

                        @Override
                        public void onNeutral(MaterialDialog dialog) {
                            super.onNeutral(dialog); dialog.dismiss();
                        }
                    });

                    mdb.build().show();
                }
            });
        }
    }
}
