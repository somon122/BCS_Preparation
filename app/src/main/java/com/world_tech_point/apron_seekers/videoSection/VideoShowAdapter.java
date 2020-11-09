package com.world_tech_point.apron_seekers.videoSection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.world_tech_point.apron_seekers.R;

import java.util.List;

public class VideoShowAdapter extends RecyclerView.Adapter<VideoShowAdapter.ViewHolder> {

    private Context context;
    private List<VideoShowClass>videoShowClassList;
    private VideoShowClass videoShowClass;

    public VideoShowAdapter(Context context, List<VideoShowClass> videoShowClassList) {
        this.context = context;
        this.videoShowClassList = videoShowClassList;
    }

    @NonNull
    @Override
    public VideoShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.video_show_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoShowAdapter.ViewHolder holder, final int position) {

        videoShowClass =videoShowClassList.get(position);
        holder.titleTV.setText(videoShowClass.getTitle());
        Picasso.get().load(videoShowClass.getImageUrl()).fit().into(holder.videoImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoShowClass =videoShowClassList.get(position);
                String videoId = videoShowClass.getVideoId();
                String title = videoShowClass.getTitle();
                String desc = videoShowClass.getDescription();
                videoPlayAlert(title,desc,videoId);

            }
        });


    }

    private void videoPlayAlert(String title, String desc, final String videoId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(desc)
                .setCancelable(false)
                .setPositiveButton("Watch now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context,VideoPlayActivity.class);
                        intent.putExtra("videoId",videoId);
                        context.startActivity(intent);

                    }
                }).setNegativeButton("watch later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

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
        return videoShowClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoImage;
        TextView titleTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoImage = itemView.findViewById(R.id.videoShowModelImage);
            titleTV = itemView.findViewById(R.id.videoShowModelTV);
        }
    }
}
