package com.world_tech_point.apron_seekers.liveExamSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.apron_seekers.ArchiveSection.ArchiveActivity;
import com.world_tech_point.apron_seekers.R;
import com.world_tech_point.apron_seekers.result_publish.ResultPublishActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExamDashboardActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    Button goToLiveExamBtn, syllabusBtn, routineBtn,archiveBtn, resultBtn, meritBtn;
    TextView notification;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_dashboad);

        Toolbar toolbar = findViewById(R.id.liveExamToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
        category = bundle.getString("category");
       titleSet(category);

        }

        notificationLoad();
        goToLiveExamBtn = findViewById(R.id.examGoToLiveExam_id);
        syllabusBtn = findViewById(R.id.examSyllabus_id);
        routineBtn = findViewById(R.id.examRoutine_id);
        archiveBtn = findViewById(R.id.examArchive_id);
        resultBtn = findViewById(R.id.examResult_id);
        meritBtn = findViewById(R.id.examMeritList_id);
        notification = findViewById(R.id.examNotification_id);


        goToLiveExamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ExamActivity.class));


            }
        });

        syllabusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SyllabusActivity.class);
                intent.putExtra("category",category);
                startActivity(intent);

            }
        });

        routineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                intent.putExtra("category",category);
                startActivity(intent);

            }
        });

        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                intent.putExtra("category",category);
                startActivity(intent);

            }
        });


        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResultPublishActivity.class);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

        meritBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeritListActivity.class);
                startActivity(intent);

            }
        });

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


    private void notificationLoad(){

        String url = getString(R.string.BASS_URL)+"notification";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")){
                        String res = jsonObject.getString("notify");
                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                        String title = jsonObject2.getString("title");
                        String des = jsonObject2.getString("description");
                        notification.setText(title+"\n"+des);

                    }else {

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
                Params.put("category",category );
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void notificationAlert(String title, String des){

        AlertDialog.Builder builder = new AlertDialog.Builder(ExamDashboardActivity.this);

        builder.setTitle(title)
                .setMessage(des)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamDashboardActivity.this);
        builder.setTitle("Alert!")
                .setMessage("No data found")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamDashboardActivity.this);
        builder.setTitle("Alert!")
                .setMessage("Please make sure your net connection")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }




}