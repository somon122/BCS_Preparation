package com.world_tech_point.apron_seekers.subjectivePracticeSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

public class SubjectPracticeActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private List<SubjectClass> liveExamList;
    private int mQuestionsLength;
    private Random random;
    private int qCount=0;

    private RadioButton optionButtonNo1, optionButtonNo2, optionButtonNo3, optionButtonNo4;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView questionTV,numberTV;
    private Button subjectiveP_NextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_practice);

        Toolbar toolbar = findViewById(R.id.subjectPracticeShowToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        random = new Random();
        liveExamList = new ArrayList<>();
        optionButtonNo1 = findViewById(R.id.subjectOptionNo1_id);
        optionButtonNo2 = findViewById(R.id.subjectOptionNo2_id);
        optionButtonNo3 = findViewById(R.id.subjectOptionNo3_id);
        optionButtonNo4 = findViewById(R.id.subjectOptionNo4_id);
        questionTV = findViewById(R.id.subjectQuestion_id);
        numberTV = findViewById(R.id.subjectNumber_id);
        subjectiveP_NextBtn = findViewById(R.id.subjectiveP_NextBtn);

        examQList("2020-08-30","DailyTest","General_Knowledge");

        subjectiveP_NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num = random.nextInt(mQuestionsLength);
                qCount = qCount+1;

                Toast.makeText(SubjectPracticeActivity.this,  ""+num, Toast.LENGTH_SHORT).show();
                updateData(num,qCount);

            }
        });

    }


    private void updateData(int i, int qCount) {

        if (!liveExamList.isEmpty()){
            SubjectClass subjectClass = liveExamList.get(i);

            numberTV.setText(""+qCount+") ");
            optionButtonNo1.setText(subjectClass.getOption1());
            optionButtonNo2.setText(subjectClass.getOption2());
            optionButtonNo3.setText(subjectClass.getOption3());
            optionButtonNo4.setText(subjectClass.getOption4());
            questionTV.setText(subjectClass.getQuestion());


        }else {
            Toast.makeText(this, "Data is not available", Toast.LENGTH_SHORT).show();
        }


    }


    private void examQList(final String date_time, final String category , final String subject) {

        String url = getString(R.string.BASS_URL) + "liveExam";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        //progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            SubjectClass subjectClass = new SubjectClass();
                            subjectClass.setId(i + 1);
                            subjectClass.setQuestion(dataobj.getString("question"));
                            subjectClass.setOption1(dataobj.getString("option1"));
                            subjectClass.setOption2(dataobj.getString("option2"));
                            subjectClass.setOption3(dataobj.getString("option3"));
                            subjectClass.setOption4(dataobj.getString("option4"));
                            subjectClass.setAnswer(dataobj.getString("answer"));
                            subjectClass.setDateTime(dataobj.getString("date_time"));
                            liveExamList.add(subjectClass);
                        }
                        mQuestionsLength = liveExamList.size();
                        int num = random.nextInt(mQuestionsLength);
                        qCount = qCount+1;
                        updateData(num,qCount);


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
                Params.put("date_time", date_time);
                Params.put("examCategory", category);
                Params.put("subject", subject);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(SubjectPracticeActivity.this);
        queue.add(stringRequest);
    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SubjectPracticeActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(SubjectPracticeActivity.this);
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