package com.world_tech_point.bcs_preparation.liveExamSection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.world_tech_point.bcs_preparation.Database.DB_Manager;
import com.world_tech_point.bcs_preparation.Database.LinkClass;
import com.world_tech_point.bcs_preparation.R;

import java.util.List;

class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private Context context;
    private List<RoutineClass> routineClassList;
    private RoutineClass routineClass;
    LiveExamControl examControl;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    int i=0;

    DB_Manager db_manager;


    public RoutineAdapter(Context context, List<RoutineClass> routineClassList) {
        this.context = context;
        this.routineClassList = routineClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.routine_model_view,parent,false);
        examControl=new LiveExamControl(context);
        db_manager = new DB_Manager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {



        routineClass = routineClassList.get(position);
        holder.source.setText(routineClass.getrSourceDetails());
        holder.subject.setText(""+routineClass.getrSubject());
        holder.status.setText(""+routineClass.getrStatus());
        holder.date.setText(""+routineClass.getrDate());

        try {

            if (routineClass.getrStatus().contains("Running")){
                holder.gotoLiveExamBtn.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.status.setTextColor(context.getColor(R.color.colorRed));
                }else {
                    holder.status.setTextColor(context.getResources().getColor(R.color.colorRed));
                }
            }else if (routineClass.getrStatus().contains("Upcoming")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.status.setTextColor(context.getColor(R.color.blue));
                }else {
                    holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                }

            }
        }catch (Exception e){
        }
        holder.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i>0){
                    holder.showBtn.setText("সিলেবাস");
                    i=0;
                    holder.source.setVisibility(View.GONE);
                }else {
                    i=i+1;
                    holder.showBtn.setText("Hide");
                    holder.source.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.gotoLiveExamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routineClass = routineClassList.get(position);
                    String check = routineClass.getrCategory()+routineClass.getrSubject()+routineClass.getrDate();
                    String test = db_manager.getData(check);

                    if (test==null){

                            Intent intent = new Intent(context, ExamActivity.class);
                            intent.putExtra("subject",routineClass.getrSubject());
                            intent.putExtra("status","new");
                            intent.putExtra("category",routineClass.getrCategory());
                            intent.putExtra("date_time",routineClass.getrDate());
                            context.startActivity(intent);

                    }else {
                        examRepeatAlert("আপনি ইতিমধ্যে এই পরীক্ষায় অংশ নিয়েছেন। পরবর্তী পরীক্ষার জন্য রুটিন দেখুন।");
                    }
                }

        });

        
      /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
              boolean  isDelete = db_manager.removeAll();
                if (isDelete){
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                }
                
            }
        });*/

    }


    private void examRepeatAlert(String dec) {

        builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.exam_alert_model,null);
        //View view1 = context.getLayoutInflater().inflate(R.layout.exam_alert_model, null);

        final TextView description = view1.findViewById(R.id.alertDescription);
        final Button seeQ = view1.findViewById(R.id.alertSeeQ);
        final Button enterExam = view1.findViewById(R.id.alertEnterExam);
        description.setText(dec);
        seeQ.setVisibility(View.GONE);
        enterExam.setText("ok");
        enterExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view1);
        dialog = builder.create();
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
        return routineClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        Button showBtn,gotoLiveExamBtn;
        TextView subject, status,source, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showBtn = itemView.findViewById(R.id.routineSourceShow_id);
            gotoLiveExamBtn = itemView.findViewById(R.id.routineEnterExam_id);

            subject = itemView.findViewById(R.id.routineSubject_id);
            status = itemView.findViewById(R.id.routineStatus_id);
            source = itemView.findViewById(R.id.routineSourceDetails_id);
            date = itemView.findViewById(R.id.routineDate_id);

        }
    }
}
