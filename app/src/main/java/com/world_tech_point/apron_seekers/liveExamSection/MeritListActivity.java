package com.world_tech_point.apron_seekers.liveExamSection;

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
import com.world_tech_point.apron_seekers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeritListActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerView recyclerView;
    private List<MeritClass> meritClassList;
    private MeritClass meritClass;
    private ProgressBar progressBar;
    private MeritAdapter meritAdapter;
    String category;
    String subject;
    String date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merit_list);

        Toolbar toolbar = findViewById(R.id.meritToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("মেধা তালিকা");
        recyclerView = findViewById(R.id.meritRecyclerView);
        progressBar = findViewById(R.id.meritProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        meritClassList = new ArrayList<>();
        meritClass = new MeritClass();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
            subject = bundle.getString("subject");
            date_time = bundle.getString("date_time");
            examMeritListBySubjectAndDate(subject,date_time);
        }else {
            allExamMeritList();

        }


    }

    private void examMeritListBySubjectAndDate(final String subject, final String date_time) {

        String url = getString(R.string.BASS_URL)+"examMeritListBySubjectAndDate";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            meritClass =  new MeritClass();
                            meritClass.setSerialNo(i + 1);
                            meritClass.setName(dataobj.getString("userN"));
                            meritClass.setClgName(dataobj.getString("collegeN"));
                            meritClass.setCategory(dataobj.getString("sCategory"));
                            meritClass.setResult(dataobj.getString("result"));
                            meritClassList.add(meritClass);
                        }
                        meritAdapter = new MeritAdapter(MeritListActivity.this, meritClassList);
                        recyclerView.setAdapter(meritAdapter);
                        meritAdapter.notifyDataSetChanged();

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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("subject", subject);
                Params.put("date_time", date_time);
                return Params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(MeritListActivity.this);
        queue.add(stringRequest);
    }


 private void allExamMeritList() {

        String url = getString(R.string.BASS_URL)+"examMeritList";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            meritClass =  new MeritClass();
                            meritClass.setSerialNo(i + 1);
                            meritClass.setName(dataobj.getString("userName"));
                            meritClass.setClgName(dataobj.getString("CollegeName"));
                            meritClass.setCategory(dataobj.getString("studentCategory"));
                            meritClass.setResult(dataobj.getString("t_rightAns"));
                            meritClassList.add(meritClass);
                        }
                        meritAdapter = new MeritAdapter(MeritListActivity.this, meritClassList);
                        recyclerView.setAdapter(meritAdapter);
                        meritAdapter.notifyDataSetChanged();

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
        });
        RequestQueue queue = Volley.newRequestQueue(MeritListActivity.this);
        queue.add(stringRequest);
    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MeritListActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(MeritListActivity.this);
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