package schooling.com.epizy.someone.schooling.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import schooling.com.epizy.someone.schooling.models.library_source;
import schooling.com.epizy.someone.schooling.R;
import schooling.com.epizy.someone.schooling.activities.browser;

public class libraries_adapter  extends RecyclerView.Adapter<libraries_adapter.ViewHolder>{
    private Context context; private List<library_source> list;

    public libraries_adapter(Context context, List<library_source> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rec_open_source, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        library_source current = list.get(position);
        holder.name.setText(current.name);
        holder.name.setTag(current.website);
        holder.creator.setText("By "+ current.creator);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, creator;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.library_name);
            creator = itemView.findViewById(R.id.library_creator);
            itemView.findViewById(R.id.root_rec_open_source).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent open_browser = new Intent(context, browser.class);
                    open_browser.putExtra("url", name.getTag().toString());
                    context.startActivity(open_browser);
                }
            });
        }
    }
}
