package com.world_tech_point.bcs_preparation.ArchiveSection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.world_tech_point.bcs_preparation.R;
import com.world_tech_point.bcs_preparation.liveExamSection.ExamActivity;
import com.world_tech_point.bcs_preparation.liveExamSection.ResultActivity;

import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ViewHolder> {

    private Context context;
    private List<ArchiveClass>list;
    private ArchiveClass archiveClass;

    public ArchiveAdapter(Context context, List<ArchiveClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.archive_view_model,parent,false);

        return new ArchiveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        archiveClass  = list.get(position);
        holder.archiveSubject.setText("বিষয়: "+archiveClass.getArchiveSubject());
        holder.archiveCategory.setText(""+archiveClass.getArchiveCategory());
        holder.archiveId.setText("নাম্বার: "+archiveClass.getArchiveId());
        holder.archiveDate.setText("তারিখ: "+archiveClass.getArchiveDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archiveClass  = list.get(position);

                String dec = "আপনি পরীক্ষা দিতে পারবেন আথবা প্রশ্ন পত্র দেখতে পারবেন।";
               examConfirmAlert(dec,archiveClass.getArchiveDate(),archiveClass.getArchiveSubject(),archiveClass.getArchiveCategory());

            }
        });

    }

    private void examConfirmAlert(String dec, final String date , final String subject , final String category) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.exam_alert_model,null);
        //View view1 = context.getLayoutInflater().inflate(R.layout.exam_alert_model, null);

        final TextView description = view1.findViewById(R.id.alertDescription);
        final Button seeQ = view1.findViewById(R.id.alertSeeQ);
        final Button enterExam = view1.findViewById(R.id.alertEnterExam);
        description.setText(dec);
        seeQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("subject",subject);
                intent.putExtra("status","old");
                intent.putExtra("category",category);
                intent.putExtra("date_time",date);
                context.startActivity(intent);

            }
        });

        enterExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ExamActivity.class);
                intent.putExtra("subject",subject);
                intent.putExtra("status","old");
                intent.putExtra("category",category);
                intent.putExtra("date_time",date);
                context.startActivity(intent);

            }
        });

        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.show();


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
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView archiveSubject, archiveCategory, archiveDate, archiveId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            archiveSubject = itemView.findViewById(R.id.archiveSubject_id);
            archiveCategory = itemView.findViewById(R.id.archiveCategory_id);
            archiveDate = itemView.findViewById(R.id.archiveDate_id);
            archiveId = itemView.findViewById(R.id.archiveId_id);


        }
    }
}
