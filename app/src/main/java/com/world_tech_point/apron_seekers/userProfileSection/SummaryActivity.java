package com.world_tech_point.apron_seekers.userProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.apron_seekers.R;

import com.world_tech_point.apron_seekers.liveExamSection.LiveExamClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    TextView totalExamNo, participateExamNo, passExamNo, totalQuestionNo, totalRightAnswer, totalWrongAns, notice;

    SaveUserInfo saveUserInfo;

    List<LiveExamClass> liveExamList;
    List<LiveExamClass> liveQList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Toolbar toolbar = findViewById(R.id.summaryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("প্রোফাইল সামারি");

        totalExamNo = findViewById(R.id.summaryTotalExam_id);
        participateExamNo = findViewById(R.id.summaryParticipateExam_id);
        passExamNo = findViewById(R.id.summaryPassExamNo_id);
        totalQuestionNo = findViewById(R.id.summaryTotalQuestion);
        totalRightAnswer = findViewById(R.id.summaryTotalRightAns_id);
        totalWrongAns = findViewById(R.id.summaryTotalWrongAns_id);

        notice = findViewById(R.id.summaryNotice_id);

        saveUserInfo = new SaveUserInfo(this);
        userScoreData(String.valueOf(saveUserInfo.getId()));
        liveExamList = new ArrayList<>();
        liveQList = new ArrayList<>();

        examList();
        examQList();


    }

    private void userScoreData(final String id) {

        String url = getString(R.string.BASS_URL) + "readProfileById";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                        String res = jsonObject.getString("user");

                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        int id = jsonObject2.getInt("id");

                        String t_exam = jsonObject2.getString("t_exam");
                        String t_question = jsonObject2.getString("t_question");

                        String t_participateExam = jsonObject2.getString("t_participateExam");
                        String t_passExam = jsonObject2.getString("t_passExam");
                        String t_rightAns = jsonObject2.getString("t_rightAns");
                        String t_wrongAns = jsonObject2.getString("t_wrongAns");

                      /*  float v1 = Float.parseFloat(t_participateExam);
                        float v2 = Float.parseFloat(t_passExam);
                        float v3 = Float.parseFloat(t_rightAns);
                        float v4 = Float.parseFloat(t_wrongAns);*/


                        saveUserInfo.storeUpdateResult(t_participateExam, t_passExam, t_rightAns, t_wrongAns);
                        participateExamNo.setText(t_participateExam);
                        passExamNo.setText(t_passExam);
                        totalRightAnswer.setText(t_rightAns);
                        totalWrongAns.setText(t_wrongAns);

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

                Params.put("id", id);
                return Params;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void examQList() {

        String url = getString(R.string.BASS_URL) + "liveExamQ";
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
                            LiveExamClass liveExamClass = new LiveExamClass();
                            liveExamClass.setId(i);
                            liveQList.add(liveExamClass);
                        }
                        totalQuestionNo.setText("" + liveQList.size());

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
        RequestQueue queue = Volley.newRequestQueue(SummaryActivity.this);
        queue.add(stringRequest);
    }


    private void examList() {

        String url = getString(R.string.BASS_URL) + "totleE";
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
                            LiveExamClass liveExamClass = new LiveExamClass();
                            liveExamClass.setId(i);
                            liveExamList.add(liveExamClass);
                        }

                        totalExamNo.setText("" + liveExamList.size());

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
        RequestQueue queue = Volley.newRequestQueue(SummaryActivity.this);
        queue.add(stringRequest);
    }

    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
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