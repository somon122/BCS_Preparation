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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.world_tech_point.bcs_preparation.MainActivity;
import com.world_tech_point.bcs_preparation.R;
import com.world_tech_point.bcs_preparation.userProfileSection.SaveUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ResultActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            new LiveExamCountSave(this).delete();
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    ResultAdapter resultAdapter;
    ResultClass resultClass;
    List<ResultClass> resultClassList;
    TextView tExamQ, examStatus, rightAns, wrongAns, result;
    Button meritButton;
    SaveUserInfo saveUserInfo;
    LinearLayout linearLayout;

    String status;
    String category;
    String date_time;
    String subject;

    String examWrongAns;
    String examTQ;
    String passStatus;
    String examRightAns;
    String examResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = findViewById(R.id.resultToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Result");
        resultClass = new ResultClass();
        resultClassList = new ArrayList<>();

        tExamQ = findViewById(R.id.resultTQ_id);
        examStatus = findViewById(R.id.resultExamStatus_id);
        rightAns = findViewById(R.id.resultRightAns_id);
        wrongAns = findViewById(R.id.resultWrongAns_id);
        result = findViewById(R.id.examResult_id);
        meritButton = findViewById(R.id.resultMeritList);
        linearLayout = findViewById(R.id.linearLayouteeee);
        saveUserInfo = new SaveUserInfo(this);
        recyclerView = findViewById(R.id.resultRecyclerView_id);
        LinearLayoutManager dramaLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(dramaLayoutManager);
        recyclerView.setHasFixedSize(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getString("status");
            category = bundle.getString("category");
            date_time = bundle.getString("date_time");
            subject = bundle.getString("subject");

            examResult = bundle.getString("result");
            examTQ = bundle.getString("examTQ");
            passStatus = bundle.getString("passStatus");
            examRightAns = bundle.getString("examRightAns");
            examWrongAns = bundle.getString("examWrongAns");

            if (status.equals("old")) {
                if (subject.equals("All_subject")) {
                    titleSet(subject);
                    linearLayout.setVisibility(View.GONE);
                    resultRetrieve(date_time);
                } else {
                    titleSet(subject);
                    linearLayout.setVisibility(View.GONE);
                    oldExamQList(date_time, subject);
                }

            } else if (status.equals("oldExam")) {

                if (subject.equals("All_subject")) {
                    titleSet(subject);
                    resultRetrieve(date_time);
                    setR(examTQ,examWrongAns,examRightAns,examResult,passStatus);
                } else {
                    titleSet(subject);
                    oldExamQList(date_time, subject);
                    setR(examTQ,examWrongAns,examRightAns,examResult,passStatus);
                }

            } else if (status.equals("newExam")) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                liveExamAlert();

            } else if (status.equals("new")){
                if (subject.equals("All_subject")) {
                    titleSet(subject);
                    resultRetrieve(date_time);
                    liveExamResultRetrieve(date_time, String.valueOf(saveUserInfo.getId()));
                } else {
                    titleSet(subject);
                    oldExamQList(date_time, subject);
                    liveExamResultRetrieve(date_time, String.valueOf(saveUserInfo.getId()));
                }

            }else {

                Toast.makeText(this, "No find", Toast.LENGTH_SHORT).show();
            }
        }

        meritButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MeritListActivity.class);
                intent.putExtra("subject",subject);
                intent.putExtra("date_time",date_time);
                startActivity(intent);
            }
        });


    }

    private void liveExamAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        builder.setTitle("Notification")
                .setMessage("অপেক্ষা করুন। রেজাল্ট পাবলিশ করা হবে।")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ResultActivity.this,MainActivity.class));
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new LiveExamCountSave(this).delete();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new LiveExamCountSave(this).delete();
    }

    private void setR(String examTQ, String examWrongAns, String examRightAns, String examResult, String passStatus ) {

        tExamQ.setText("মোট প্রশ্ন: " + examTQ);
        wrongAns.setText("ভুল উত্তর: " + examWrongAns);
        rightAns.setText("সঠিক উত্তর: " + examRightAns);
        examStatus.setText("মার্কস: " + examResult);

        if (passStatus.equals("Pass")){
            result.setText("রেজাল্টঃ " + "কৃতকার্য");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result.setBackgroundColor(getColor(R.color.colorPrimary));
                result.setTextColor(getColor(R.color.colorBlack));
            }else {
                result.setBackgroundColor( Color.parseColor("#03DAC5"));
                result.setTextColor( Color.parseColor("#000000"));
            }

        }else {

            result.setText("রেজাল্টঃ " + "অকৃতকার্য");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result.setBackgroundColor(getColor(R.color.colorRed));
                result.setTextColor(getColor(R.color.colorWhite));
            }else {
                result.setBackgroundColor( Color.parseColor("#FF0000"));
                result.setTextColor( Color.parseColor("#ffffff"));
            }
        }
    }

    private void resultRetrieve(final String date) {

        String url = getString(R.string.BASS_URL) + "liveExam";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            resultClass = new ResultClass();
                            resultClass.setId(i + 1);
                            resultClass.setQuestion(dataobj.getString("question"));
                            resultClass.setOption1(dataobj.getString("option1"));
                            resultClass.setOption2(dataobj.getString("option2"));
                            resultClass.setOption3(dataobj.getString("option3"));
                            resultClass.setOption4(dataobj.getString("option4"));
                            resultClass.setAnswer(dataobj.getString("answer"));
                            resultClass.setExplain(dataobj.getString("explain"));
                            resultClass.setDateTime(dataobj.getString("date_time"));

                            resultClassList.add(resultClass);
                        }

                        resultAdapter = new ResultAdapter(ResultActivity.this, resultClassList);
                        recyclerView.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();


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
                Params.put("date_time", date);
                return Params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }

    private void liveExamResultRetrieve(final String date_time, final String userId) {

        String url = getString(R.string.BASS_URL) + "getResultStatus";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        int id = jsonObject2.getInt("id");
                        String t_examQ = jsonObject2.getString("tQuestion");
                        String examResult = jsonObject2.getString("result");
                        String eStatus = jsonObject2.getString("examStatus");
                        String t_rightAns = jsonObject2.getString("tRightAns");
                        String t_wrongAns = jsonObject2.getString("tWrongAns");
                        String date = jsonObject2.getString("date_time");

                        setR(t_examQ,t_wrongAns,t_rightAns,examResult,eStatus);

                    } else {
                        noDataAlert();
                        recyclerView.setVisibility(View.GONE);
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
                Params.put("userId", userId);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void oldExamQList(final String date_time, final String subject) {

        String url = getString(R.string.BASS_URL) + "oldLiveExam";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            resultClass = new ResultClass();
                            resultClass.setId(i + 1);
                            resultClass.setQuestion(dataobj.getString("question"));
                            resultClass.setOption1(dataobj.getString("option1"));
                            resultClass.setOption2(dataobj.getString("option2"));
                            resultClass.setOption3(dataobj.getString("option3"));
                            resultClass.setOption4(dataobj.getString("option4"));
                            resultClass.setAnswer(dataobj.getString("answer"));
                            resultClass.setExplain(dataobj.getString("explain"));
                            resultClass.setDateTime(dataobj.getString("date_time"));

                            resultClassList.add(resultClass);
                        }

                        resultAdapter = new ResultAdapter(ResultActivity.this, resultClassList);
                        recyclerView.setAdapter(resultAdapter);
                        resultAdapter.notifyDataSetChanged();


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
                Params.put("subject", subject);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ResultActivity.this);
        queue.add(stringRequest);
    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        builder.setTitle("এলার্ট!")
                .setMessage("আপনি এখনও কোন পরীক্ষায় অংশগ্রহন করেন নি।")
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
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

        if (value.equals("Botany")){

            setTitle("উদ্ভিদবিজ্ঞান");

        }else if (value.equals("Zoology")){
            setTitle("প্রাণীবিজ্ঞান");

        }else if (value.equals("Chemistry1")){
            setTitle("রসায়ন প্রথম পত্র");

        }else if (value.equals("Chemistry2")){
            setTitle("রসায়ন দ্বিতীয় পত্র");

        }else if (value.equals("Physics1")){
            setTitle("পদার্থবিজ্ঞান প্রথম পত্র");

        }else if (value.equals("Physics2")){
            setTitle("পদার্থবিজ্ঞান দ্বিতীয় পত্র");

        }else if (value.equals("English")){
            setTitle("ইংরেজি");

        }else if (value.equals("General_Knowledge")){
            setTitle("সাধারণ জ্ঞান");

        }else if (value.equals("All_Subject")){
            setTitle("সব বিষয়");

        }else {

            setTitle(value);
        }

    }


}

