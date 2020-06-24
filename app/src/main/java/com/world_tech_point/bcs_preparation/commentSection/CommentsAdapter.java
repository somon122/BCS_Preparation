package com.world_tech_point.bcs_preparation.commentSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.world_tech_point.bcs_preparation.R;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<CommentClass>list;
    private CommentClass commentClass;

    public CommentsAdapter(Context context, List<CommentClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comments_view_model,parent,false);

        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        commentClass  = list.get(position);

        holder.userName.setText("Name: "+commentClass.getmName());
        holder.commentsDesc.setText(commentClass.getmComments());
        holder.commentsDate.setText("Date: "+commentClass.getmData());
        holder.commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, commentsDesc,commentsView,commentsDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.commentsUserName_id);
            commentsDesc = itemView.findViewById(R.id.commentsDescription_id);
            commentsView = itemView.findViewById(R.id.commentsRead_id);
            commentsDate = itemView.findViewById(R.id.commentsDate_id);


        }
    }
}
