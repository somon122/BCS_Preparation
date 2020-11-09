package com.world_tech_point.apron_seekers.liveExamSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.world_tech_point.apron_seekers.R;

import java.util.List;

public class MeritAdapter extends RecyclerView.Adapter<MeritAdapter.ViewHolder>{

    private Context context;
    private List<MeritClass>meritClassList;
    private MeritClass meritClass;

    public MeritAdapter(Context context, List<MeritClass> meritClassList) {
        this.context = context;
        this.meritClassList = meritClassList;
    }

    @NonNull
    @Override
    public MeritAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.marit_model,parent,false);
        return new MeritAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeritAdapter.ViewHolder holder, int position) {

        meritClass = meritClassList.get(position);
        holder.serialNo.setText(""+meritClass.getSerialNo()+".");
        holder.name.setText(meritClass.getName());
        holder.clgName.setText(meritClass.getClgName());

        if (meritClass.getCategory().equals("Medical College Admitted")){

            holder.category.setText("Second Timer");

        }else {
            holder.category.setText(meritClass.getCategory());

        }


        holder.meritResult.setText(meritClass.getResult());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return meritClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView serialNo, name, clgName, category,meritResult;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serialNo = itemView.findViewById(R.id.meritSerialNo);
            name = itemView.findViewById(R.id.meritUserName);
            clgName = itemView.findViewById(R.id.meritUserClgName);
            category = itemView.findViewById(R.id.meritUserCategory);
            meritResult = itemView.findViewById(R.id.meritResult);

        }
    }
}
