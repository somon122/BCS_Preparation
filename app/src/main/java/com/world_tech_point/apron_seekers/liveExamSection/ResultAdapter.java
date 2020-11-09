package com.world_tech_point.apron_seekers.liveExamSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.world_tech_point.apron_seekers.R;

import java.util.List;

class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder>{

    private Context context;
    private List<ResultClass> resultClassList;
    private ResultClass resultClass;
    private int status;

    public ResultAdapter(Context context, List<ResultClass> resultClassList) {
        this.context = context;
        this.resultClassList = resultClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_model,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        resultClass = resultClassList.get(position);

        holder.numberTV.setText(resultClass.getId()+")");
        holder.questionTV.setText(resultClass.getQuestion());
        holder.optionButtonNo1.setText(resultClass.getOption1());
        holder.optionButtonNo2.setText(resultClass.getOption2());
        holder.optionButtonNo3.setText(resultClass.getOption3());
        holder.optionButtonNo4.setText(resultClass.getOption4());
        holder.optionButtonNo1.setEnabled(false);
        holder.optionButtonNo2.setEnabled(false);
        holder.optionButtonNo3.setEnabled(false);
        holder.optionButtonNo4.setEnabled(false);

        holder.explainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status == 0){
                    status = status+1;
                    resultClass = resultClassList.get(position);
                    holder.detailsTV.setVisibility(View.VISIBLE);
                    holder.detailsTV.setText("ব্যাখ্যা: "+resultClass.getExplain());
                }else {
                    status=0;
                    holder.detailsTV.setVisibility(View.GONE);
                }

            }
        });

          holder.ansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0){
                    status = status+1;
                    resultClass = resultClassList.get(position);
                    holder.detailsTV.setVisibility(View.VISIBLE);
                    holder.detailsTV.setText("উত্তর: "+resultClass.getAnswer());
                }else {
                    status=0;
                    holder.detailsTV.setVisibility(View.GONE);
                }

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
        return resultClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RadioButton optionButtonNo1, optionButtonNo2, optionButtonNo3, optionButtonNo4;
        private RadioGroup radioGroup;
        private RadioButton radioButton;
        private Button ansButton, explainButton;

        private TextView questionTV,numberTV, detailsTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            radioGroup = itemView.findViewById(R.id.resultExamOptionGroup);
            optionButtonNo1 = itemView.findViewById(R.id.resultExamOptionNo1_id);
            optionButtonNo2 = itemView.findViewById(R.id.resultExamOptionNo2_id);
            optionButtonNo3 = itemView.findViewById(R.id.resultExamOptionNo3_id);
            optionButtonNo4 = itemView.findViewById(R.id.resultExamOptionNo4_id);
            questionTV = itemView.findViewById(R.id.resultExamQuestion_id);
            numberTV = itemView.findViewById(R.id.resultExamNumber_id);

            ansButton = itemView.findViewById(R.id.resultAns_id);
            explainButton = itemView.findViewById(R.id.resultExplain_id);
            detailsTV = itemView.findViewById(R.id.resultDetailsView_id);


        }
    }
}
