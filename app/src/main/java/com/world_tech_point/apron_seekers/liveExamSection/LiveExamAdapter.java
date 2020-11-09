package com.world_tech_point.apron_seekers.liveExamSection;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.world_tech_point.apron_seekers.R;
import java.util.List;

 class LiveExamAdapter extends RecyclerView.Adapter<LiveExamAdapter.ViewHolder> {

    private Context context;
    private String date;
    private String category;
    private List<LiveExamClass>examClassList;
    private LiveExamClass liveExamClass;
    private long correctAns;
    private long wrongAns;
     double tc;

    LiveExamCountSave examCountSave;



    public LiveExamAdapter(Context context,String category,String date, List<LiveExamClass> examClassList) {
        this.context = context;
        this.date = date;
        this.category = category;
        this.examClassList = examClassList;

    }

    @NonNull
    @Override
    public LiveExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_exam_model_view,parent,false);
        examCountSave = new LiveExamCountSave(context);

        return new LiveExamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LiveExamAdapter.ViewHolder holder, final int position) {

        liveExamClass = examClassList.get(position);
        holder.numberTV.setText(""+liveExamClass.getId()+") ");
        holder.questionTV.setText(liveExamClass.getQuestion());
        String ans = liveExamClass.getAnswer();

        holder.optionButtonNo1.setText(liveExamClass.getOption1());
        holder.optionButtonNo2.setText(liveExamClass.getOption2());
        holder.optionButtonNo3.setText(liveExamClass.getOption3());
        holder.optionButtonNo4.setText(liveExamClass.getOption4());

        holder.optionButtonNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                liveExamClass = examClassList.get(position);
                String ans = liveExamClass.getAnswer();
                String option = holder.optionButtonNo1.getText().toString();
                notificationAlert(holder,ans,option);
            }
        });

        holder.optionButtonNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                liveExamClass = examClassList.get(position);
                String ans = liveExamClass.getAnswer();
                String option = holder.optionButtonNo2.getText().toString();
                notificationAlert(holder,ans,option);
            }
        });

        holder.optionButtonNo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                liveExamClass = examClassList.get(position);
                String ans = liveExamClass.getAnswer();
                String option = holder.optionButtonNo3.getText().toString();
                notificationAlert(holder,ans,option);

            }
        });

        holder.optionButtonNo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                liveExamClass = examClassList.get(position);
                String ans = liveExamClass.getAnswer();
                String option = holder.optionButtonNo4.getText().toString();
                notificationAlert(holder,ans,option);

            }
        });

    }

     private void notificationAlert(final LiveExamAdapter.ViewHolder holder , final String ans, final String option){

         AlertDialog.Builder builder = new AlertDialog.Builder(context);

         builder.setTitle("Confirm alert!")
                 .setMessage("100% Sure is it right?")
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         int cAns = examCountSave.getUserTotalAnsC()+1;
                         examCountSave.setUserTotalAnsC(cAns);
                         optionHide(holder, ans, option);

                     }
                 }).setNegativeButton("No", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 holder.optionButtonNo1.setChecked(false);
                 holder.optionButtonNo2.setChecked(false);
                 holder.optionButtonNo3.setChecked(false);
                 holder.optionButtonNo4.setChecked(false);
                 dialogInterface.dismiss();

             }
         });

         AlertDialog dialog = builder.create();
         dialog.show();


     }

     private void optionHide(LiveExamAdapter.ViewHolder holder, String ans, String option) {

         int wr = examCountSave.getWrongAns();
         int co = examCountSave.getRightAns();
         double marks = Double.parseDouble(examCountSave.getMarks());

         if (option.equals(ans)){
             int tc = co+1;
             double m = (double)marks+1;
             examCountSave.storeRightAns(tc);
             examCountSave.storeMarks(String.valueOf(m));
         }else {

             int twr = wr+1;
             if (!category.contains("ModelTest")){
                  tc = (double) (marks -.25);
                 examCountSave.storeMarks(String.valueOf(tc));
             }
             examCountSave.storeWrongAns(twr);
         }

         holder.optionButtonNo1.setEnabled(false);
         holder.optionButtonNo2.setEnabled(false);
         holder.optionButtonNo3.setEnabled(false);
         holder.optionButtonNo4.setEnabled(false);


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
        return examClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton optionButtonNo1, optionButtonNo2, optionButtonNo3, optionButtonNo4;
        private RadioGroup radioGroup;
        private RadioButton radioButton;

        private TextView questionTV,numberTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            radioGroup = itemView.findViewById(R.id.liveExamOptionGroup);
            optionButtonNo1 = itemView.findViewById(R.id.liveExamOptionNo1_id);
            optionButtonNo2 = itemView.findViewById(R.id.liveExamOptionNo2_id);
            optionButtonNo3 = itemView.findViewById(R.id.liveExamOptionNo3_id);
            optionButtonNo4 = itemView.findViewById(R.id.liveExamOptionNo4_id);
            questionTV = itemView.findViewById(R.id.liveExamQuestion_id);
            numberTV = itemView.findViewById(R.id.liveExamNumber_id);


        }
    }
}
