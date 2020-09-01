package com.world_tech_point.bcs_preparation.liveExamSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.world_tech_point.bcs_preparation.MainActivity;
import com.world_tech_point.bcs_preparation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<RoutineClass> routineClassList;
    ProgressBar progressBar;
    String category;
    RoutineAdapter routineAdapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        Toolbar toolbar = findViewById(R.id.routineToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.routineRecyclerView_id);
        progressBar = findViewById(R.id.routineProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        routineClassList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
            titleSet(category);
            routineQList(category);
        }


    }

    private void routineQList(final String category) {

        String url = getString(R.string.BASS_URL) + "routine";
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
                            RoutineClass routineClass = new RoutineClass();
                            routineClass.setrSubject(dataobj.getString("subject"));
                            routineClass.setrCategory(dataobj.getString("category"));
                            routineClass.setrSourceDetails(dataobj.getString("source"));
                            routineClass.setrStatus(dataobj.getString("status"));
                            routineClass.setrDate(dataobj.getString("date_time"));

                            routineClassList.add(routineClass);
                        }
                        routineAdapter = new RoutineAdapter(RoutineActivity.this, routineClassList);
                        recyclerView.setAdapter(routineAdapter);
                        routineAdapter.notifyDataSetChanged();
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
        RequestQueue queue = Volley.newRequestQueue(RoutineActivity.this);
        queue.add(stringRequest);
    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(RoutineActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(RoutineActivity.this);
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



}


