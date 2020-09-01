package com.world_tech_point.bcs_preparation.result_publish;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.world_tech_point.bcs_preparation.R;
import com.world_tech_point.bcs_preparation.liveExamSection.ResultActivity;

import java.util.List;

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {

    private Context context;
    private List<PublishClass>publishClassList;
    private PublishClass publishClass;

    public PublishAdapter(Context context, List<PublishClass> publishClassList) {
        this.context = context;
        this.publishClassList = publishClassList;
    }

    @NonNull
    @Override
    public PublishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.result_publish_model,parent,false);

       return new PublishAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishAdapter.ViewHolder holder, final int position) {

        publishClass = publishClassList.get(position);
        holder.category.setText(publishClass.getCategory());
        holder.subject.setText(publishClass.getSubject());
        holder.date.setText(publishClass.getDate());
        holder.syllabus.setText(publishClass.getSyllabus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishClass = publishClassList.get(position);
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("status","new");
                intent.putExtra("category",publishClass.getCategory());
                intent.putExtra("date_time",publishClass.getDate());
                intent.putExtra("subject",publishClass.getSubject());
                context.startActivity(intent);
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
        return publishClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category, subject,date,syllabus;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.publishCategory_id);
            subject = itemView.findViewById(R.id.publishSubject_id);
            date = itemView.findViewById(R.id.publishDate_id);
            syllabus = itemView.findViewById(R.id.publishSyllabus_id);
            button = itemView.findViewById(R.id.publishSeeButton);

        }
    }
}
