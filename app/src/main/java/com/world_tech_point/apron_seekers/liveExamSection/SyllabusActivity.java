package com.world_tech_point.apron_seekers.liveExamSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
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

import java.util.HashMap;
import java.util.Map;

public class SyllabusActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    WebView pdfView;
    String category;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        Toolbar toolbar = findViewById(R.id.syllabusToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.syllabusProgressBar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
            syllabusLoad(category);
            titleSet(category);

        } else {
            Intent intent = new Intent(getApplicationContext(), ExamDashboardActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
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


    private void syllabusLoad(final String category) {
        String url = getString(R.string.BASS_URL) + "syllabus";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                        String res = jsonObject.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        String subject = jsonObject2.getString("subject");
                        String fileLink = jsonObject2.getString("fileLink");

                        if (subject != null) {

                            String path = getString(R.string.BASS_URL_FOR_SYLLABUS) + "public/files/pdf_file/" + fileLink;
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                            startActivity(browserIntent);
                            finish();
                        }

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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SyllabusActivity.this);
        builder.setTitle("এলার্ট!")
                .setMessage("এখনও সিলেবাস আপলোড করা হয় নি")
                .setCancelable(false)
                .setPositiveButton("ওকে", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SyllabusActivity.this);
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