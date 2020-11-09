package com.world_tech_point.apron_seekers.videoSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.apron_seekers.ArchiveSection.ArchiveActivity;
import com.world_tech_point.apron_seekers.ArchiveSection.ArchiveAdapter;
import com.world_tech_point.apron_seekers.ArchiveSection.ArchiveClass;
import com.world_tech_point.apron_seekers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoShowActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



    ProgressBar progressBar;
    TextView noDataAlertTV;
    RecyclerView recyclerView;
    List<VideoShowClass>videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);

        Toolbar toolbar = findViewById(R.id.videoShowToolBar_id);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.videoShowProgressBar);
        noDataAlertTV = findViewById(R.id.videoShowAlertTV);
        recyclerView= findViewById(R.id.videoShowRecyclerView);
        noDataAlertTV.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        videoList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String category = bundle.getString("category");
            videoShowList(category);

        }


    }

    private void videoShowList(final String category) {

        String url = getString(R.string.BASS_URL) + "videoList";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        progressBar.setVisibility(View.GONE);

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                           int id = dataobj.getInt("id");
                           String title = dataobj.getString("title");
                           String description = dataobj.getString("description");
                           String videoId = dataobj.getString("videoId");
                           String videoImageUrl = dataobj.getString("videoImageUrl");
                            VideoShowClass videoShowClass = new VideoShowClass(id,title,description,videoImageUrl,videoId);
                            videoList.add(videoShowClass);
                        }

                        VideoShowAdapter videoShowAdapter = new VideoShowAdapter(VideoShowActivity.this, videoList);
                        recyclerView.setAdapter(videoShowAdapter);
                        videoShowAdapter.notifyDataSetChanged();


                    } else {

                        noDataAlertTV.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    noDataAlertTV.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                noDataAlertTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("category", category);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(VideoShowActivity.this);
        queue.add(stringRequest);
    }


}