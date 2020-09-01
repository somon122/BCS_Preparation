package com.world_tech_point.bcs_preparation.result_publish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.bcs_preparation.ArchiveSection.ArchiveActivity;
import com.world_tech_point.bcs_preparation.ArchiveSection.ArchiveAdapter;
import com.world_tech_point.bcs_preparation.ArchiveSection.ArchiveClass;
import com.world_tech_point.bcs_preparation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultPublishActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    List<PublishClass>publishList;
    PublishClass publishClass;
    ProgressBar progressBar;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_publish);

        Toolbar toolbar = findViewById(R.id.publishToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.publishProgressBar);
        recyclerView = findViewById(R.id.publishRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        publishList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
            titleSet(category);
           resultPublishList(category);
        }

    }

    private void titleSet(String value){

        if (value.equals("CentralTest")){

            setTitle("সেন্ট্রাল টেস্ট");

        }else if (value.equals("SubjectiveTest")){
            setTitle("বিষয়ভিত্তিক টেস্ট");

        }else if (value.equals("ChapterBassTest")){
            setTitle("অধায়ভিত্তিক টেস্ট");

        }else if (value.equals("WeeklyTest")){
            setTitle("সাপ্তহিক টেস্ট");

        }else if (value.equals("DailyTest")){
            setTitle("ডেইলি টেস্ট");

        }else if (value.equals("ModelTest")){
            setTitle("মডেল টেস্ট");

        }else {

            setTitle(value);
        }

    }

    private void resultPublishList(final String category) {

        String url = getString(R.string.BASS_URL) + "publishExamResult";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                             publishClass = new PublishClass();
                            publishClass.setCategory(dataobj.getString("category"));
                            publishClass.setSubject(dataobj.getString("subject"));
                            publishClass.setSyllabus(dataobj.getString("syllabus"));
                            publishClass.setDate(dataobj.getString("date_time"));

                            publishList.add(publishClass);
                        }
                        PublishAdapter publishAdapter = new PublishAdapter(ResultPublishActivity.this, publishList);
                        recyclerView.setAdapter(publishAdapter);
                        publishAdapter.notifyDataSetChanged();

                    } else {

                        noDataAlert();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("category", category);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ResultPublishActivity.this);
        queue.add(stringRequest);
    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultPublishActivity.this);
        builder.setTitle("Alert!")
                .setMessage("No data found")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultPublishActivity.this);
        builder.setTitle("Alert!")
                .setMessage("Please make sure your net connection")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}