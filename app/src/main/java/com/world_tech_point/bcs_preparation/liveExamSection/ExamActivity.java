package com.world_tech_point.bcs_preparation.liveExamSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.world_tech_point.bcs_preparation.Database.DB_Manager;
import com.world_tech_point.bcs_preparation.Database.LinkClass;
import com.world_tech_point.bcs_preparation.MainActivity;
import com.world_tech_point.bcs_preparation.R;
import com.world_tech_point.bcs_preparation.userProfileSection.ProfileSubmitActivity;
import com.world_tech_point.bcs_preparation.userProfileSection.SaveUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExamActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            String check = category+subject+date_time;
            String test = db_manager.getData(check);
            if (test==null){
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        String check = category+subject+date_time;
        String test = db_manager.getData(check);
        if (test==null){
            this.finish();
        }
    }

    RecyclerView recyclerView;
    TextView timeLeftTV;
    Button submitBtn;

    String category;
    String subject;
    String status;
    String newStatus;

    private CountDownTimer countDownTimer;
    private long timeLeft;
    private boolean timeRunning;
    private String timeText;
    private String date_time;
    ProgressBar progressBar;


    String examTQ;
    String examRightAns;
    String examWrongAns;
    String passStatus;
    String result;
    int  fCounter=0;

    private List<LiveExamClass> liveExamList;
    LiveExamCountSave examCountSave;
    LiveExamAdapter LiveExamAdapter;
    SaveUserInfo saveUserInfo;

    DB_Manager db_manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Toolbar toolbar = findViewById(R.id.examToolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db_manager = new DB_Manager(this);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.strock_design));
        recyclerView = findViewById(R.id.examRecyclerView_id);
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(trailersLayoutManager);
        recyclerView.setHasFixedSize(true);
        timeLeftTV = findViewById(R.id.examTime_id);
        submitBtn = findViewById(R.id.examSubmit_id);
        submitBtn.setVisibility(View.GONE);
        timeLeftTV.setVisibility(View.GONE);
        liveExamList = new ArrayList<>();
        progressBar = findViewById(R.id.liveExamProgressBar);
        examCountSave = new LiveExamCountSave(this);
        saveUserInfo = new SaveUserInfo(this);
        recyclerView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getString("status");
            category = bundle.getString("category");
            date_time = bundle.getString("date_time");
            subject = bundle.getString("subject");

            if (status.equals("old")) {
                if (subject.equals("All_subject")) {
                    titleSet(subject);
                    examQList(date_time, category,subject);
                    newStatus = "oldExam";

                } else {
                    titleSet(subject);
                    oldExamQList(date_time, category, subject);
                    newStatus = "oldExam";
                }

            } else {
                titleSet(subject);
                examQList(date_time, category,subject);
                newStatus = "newExam";
            }
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (category.contains("CentralTest")){
                    examFinishAlert();
                }else {
                    examFinishAlert2();

                }
            }
        });
    }

    private void resetTimer() {
        timeLeft = 0;
        updateTimer();
        countDownTimer.cancel();
    }


    private void startTime() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timeLeftTV.setText("TimeUp");
                if (category.equals("CentralTest")) {
                    centralResult();
                } else {
                    result();
                }
            }
        }.start();
        timeRunning = true;

    }

    private void updateTimer() {

        int minutes = (int) (timeLeft / 60000);
        int seconds = (int) (timeLeft % 60000 / 1000);
        timeText = "" + minutes;
        timeText += ":";
        if (seconds < 10) timeText += "0";
        timeText += seconds;

        if (minutes < 5) {
            timeLeftTV.setTextColor(getResources().getColor(R.color.colorRed));
            timeLeftTV.setText("" + timeText);
        } else {
            timeLeftTV.setText("" + timeText);
        }
    }


    private void examConfirmAlert(int tQ, long time) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        int hours = (int) (time / 3600000);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time % 60000 / 1000);
        String setTime = ""+hours+":"+minutes+":"+seconds;

        builder.setTitle("কনফার্মেশন এলার্ট!")
                .setMessage("পরীক্ষার মোট প্রশ্ন "+tQ+ " এবং সময় "+setTime+"।"+" আপনি পরীক্ষা শুরু করতে প্রস্তুত?")
                .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String check = category+subject+date_time;
                        LinkClass linkClass = new LinkClass(check);
                        boolean isInsert = db_manager.Save_All_Data(linkClass);
                        if (isInsert){
                            startTime();
                            recyclerView.setVisibility(View.VISIBLE);
                            timeLeftTV.setVisibility(View.VISIBLE);
                            submitBtn.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(ExamActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                }).setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void examFinishAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);

        builder.setTitle("সতর্কতা!")
                .setMessage("আপনি "+examCountSave.getTotalQ()+" টি প্রশ্নের মধ্যে উওর করেছেন "+
                        examCountSave.getUserTotalAnsC()+" টি।"+" আপনি এখানে পরীক্ষা শেষ করতে ইচ্ছুক?")
                .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitBtn.setEnabled(false);
                        resetTimer();
                        centralResult();

                    }
                }).setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


 private void examFinishAlert2() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);

     builder.setTitle("সতর্কতা!")
             .setMessage("আপনি "+examCountSave.getTotalQ()+" টি প্রশ্নের মধ্যে উওর করেছেন "+
                     examCountSave.getUserTotalAnsC()+" টি।"+" আপনি এখানে পরীক্ষা শেষ করতে ইচ্ছুক?")
             .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {

                     submitBtn.setEnabled(false);
                     resetTimer();
                     result();

                 }
             }).setNegativeButton("না", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void finishAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);

        builder.setTitle("সময় বিজ্ঞপ্তি।")
                .setMessage("আপনার পরীক্ষার সময় শেষ")
                .setPositiveButton("ওকে", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


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
                        progressBar.setVisibility(View.GONE);
                        fCounter=fCounter+1;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            LiveExamClass liveExamClass = new LiveExamClass();
                            liveExamClass.setId(i + 1);
                            liveExamClass.setQuestion(dataobj.getString("question"));
                            liveExamClass.setOption1(dataobj.getString("option1"));
                            liveExamClass.setOption2(dataobj.getString("option2"));
                            liveExamClass.setOption3(dataobj.getString("option3"));
                            liveExamClass.setOption4(dataobj.getString("option4"));
                            liveExamClass.setAnswer(dataobj.getString("answer"));
                            liveExamClass.setDateTime(dataobj.getString("date_time"));
                            liveExamList.add(liveExamClass);
                        }
                        LiveExamAdapter = new LiveExamAdapter(ExamActivity.this, category, date_time, liveExamList);
                        recyclerView.setAdapter(LiveExamAdapter);
                        timeLeft = LiveExamAdapter.getItemCount() * 36000;
                        LiveExamAdapter.notifyDataSetChanged();
                        int tQ = LiveExamAdapter.getItemCount();
                        examCountSave.storeTotalQ(tQ);

                        examConfirmAlert(tQ,timeLeft);

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
        RequestQueue queue = Volley.newRequestQueue(ExamActivity.this);
        queue.add(stringRequest);
    }


    private void oldExamQList(final String date_time, final String category, final String subject) {

        String url = getString(R.string.BASS_URL) + "oldLiveExam";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("success")) {

                        String res = obj.getString("list");
                        JSONArray jsonArray = new JSONArray(res);
                        progressBar.setVisibility(View.GONE);
                        fCounter=fCounter+1;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);
                            LiveExamClass liveExamClass = new LiveExamClass();
                            liveExamClass.setId(i + 1);
                            liveExamClass.setQuestion(dataobj.getString("question"));
                            liveExamClass.setOption1(dataobj.getString("option1"));
                            liveExamClass.setOption2(dataobj.getString("option2"));
                            liveExamClass.setOption3(dataobj.getString("option3"));
                            liveExamClass.setOption4(dataobj.getString("option4"));
                            liveExamClass.setAnswer(dataobj.getString("answer"));
                            liveExamClass.setDateTime(dataobj.getString("date_time"));
                            liveExamList.add(liveExamClass);
                        }
                        LiveExamAdapter = new LiveExamAdapter(ExamActivity.this, category, date_time, liveExamList);
                        recyclerView.setAdapter(LiveExamAdapter);
                        LiveExamAdapter.notifyDataSetChanged();
                        timeLeft = LiveExamAdapter.getItemCount() * 36000;
                        int tQ = LiveExamAdapter.getItemCount();
                        examCountSave.storeTotalQ(tQ);
                        examConfirmAlert(tQ,timeLeft);

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
                Params.put("ExamCategory", category);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ExamActivity.this);
        queue.add(stringRequest);
    }

    private void result() {
        examRightAns = String.valueOf(examCountSave.getRightAns());
        examWrongAns = String.valueOf(examCountSave.getWrongAns());
        examTQ = String.valueOf(examCountSave.getTotalQ());
        String id = String.valueOf(saveUserInfo.getId());
        double marks = Double.parseDouble(examCountSave.getMarks());


        int f = (examCountSave.getTotalQ() * 40) / 100;
        int p = (examCountSave.getTotalQ() * 40) % 100;
        String t = String.valueOf(f);
        String t1 = String.valueOf(p);
        String total = t+"."+t1;
        double pass = Double.parseDouble(total);
        String passStatus;
        if (marks >= pass) {
            passStatus = "Pass";
            result = String.valueOf(marks);
        } else {
            passStatus = "Fail";
            result = String.valueOf(marks);
        }


        if (newStatus.equals("newExam")) {
            resultSubmit(id, result, examTQ, passStatus, examRightAns, examWrongAns);

        } else {

            Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
            intent.putExtra("date_time", date_time);
            intent.putExtra("category", category);
            intent.putExtra("status", newStatus);
            intent.putExtra("subject", subject);

            intent.putExtra("result", result);
            intent.putExtra("examTQ", examTQ);
            intent.putExtra("passStatus", passStatus);
            intent.putExtra("examRightAns", examRightAns);
            intent.putExtra("examWrongAns", examWrongAns);
            startActivity(intent);
            finish();

        }

    }

    private void centralResult() {

        examRightAns = String.valueOf(examCountSave.getRightAns());
        examWrongAns = String.valueOf(examCountSave.getWrongAns());
        examTQ = String.valueOf(examCountSave.getTotalQ());
        double academicR = Double.parseDouble(saveUserInfo.getTotalResult());
        double marks = Double.parseDouble(examCountSave.getMarks());

        int f = (examCountSave.getTotalQ() * 40) / 100;
        int p = (examCountSave.getTotalQ() * 40) % 100;
        String t = String.valueOf(f);
        String t1 = String.valueOf(p);
        String total = t+"."+t1;
        double pass = Double.parseDouble(total);

        String id = String.valueOf(saveUserInfo.getId());
        if (marks >= pass) {
            passStatus = "Pass";
            result = String.valueOf(marks + academicR);
            centralSubmit(id, result, examTQ, passStatus, examRightAns, examWrongAns);

        } else {
            passStatus = "Fail";
            result = String.valueOf(marks + academicR);
            centralSubmit(id, result, examTQ, passStatus, examRightAns, examWrongAns);
        }

    }

    private void centralSubmit(String id, String result, String examTQ, String passStatus, String examRightAns, String examWrongAns) {


        if (newStatus.equals("newExam")) {
            resultSubmit(id, result, examTQ, passStatus, examRightAns, examWrongAns);

        } else {

            Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
            intent.putExtra("date_time", date_time);
            intent.putExtra("category", category);
            intent.putExtra("status", newStatus);
            intent.putExtra("subject", subject);

            intent.putExtra("result", result);
            intent.putExtra("examTQ", examTQ);
            intent.putExtra("passStatus", passStatus);
            intent.putExtra("examRightAns", examRightAns);
            intent.putExtra("examWrongAns", examWrongAns);
            startActivity(intent);
            finish();

        }



    }


    private void resultSubmit(final String userId, final String result, final String tQuestion, final String examStatus,
                              final String tRightAns, final String tWrongAns) {

        String url = getString(R.string.BASS_URL) + "insertResultStatus";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {

                        int pExam = Integer.parseInt(saveUserInfo.getT_participateExam())+1;
                        String t_participateExam = String.valueOf(pExam);

                        String t_passExam;
                        if (examStatus.equals("Pass")) {

                            int passExam = Integer.parseInt(saveUserInfo.getT_passExam())+1;
                            t_passExam = String.valueOf(passExam);
                            //t_passExam = String.valueOf(saveUserInfo.getT_passExam() + 1);

                        } else {
                            t_passExam = saveUserInfo.getT_passExam();

                        }

                        int d = Integer.parseInt(saveUserInfo.getT_rightAns())+Integer.parseInt(tRightAns);
                        int  d1 = Integer.parseInt(saveUserInfo.getT_wrongAns())+Integer.parseInt(tWrongAns);

                        String t_rightAns = String.valueOf(d);
                        String t_wrongAns = String.valueOf(d1);
                        updateSubmit(userId, t_participateExam, t_passExam, t_rightAns, t_wrongAns);

                    } else {
                        if (examCountSave.getUserTotalAnsC()== 0){
                            if (fCounter > 0){
                                worning();
                            }else {
                                noDataAlert();
                            }
                        }

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

                Params.put("userId", userId);

                Params.put("userN", saveUserInfo.getUserName());
                Params.put("sCategory", saveUserInfo.getStudentCategory());
                Params.put("collegeN", saveUserInfo.getCollegeName());

                Params.put("category", category);
                Params.put("subject", subject);
                Params.put("result", result);
                Params.put("tQuestion", tQuestion);
                Params.put("examStatus", examStatus);
                Params.put("tRightAns", tRightAns);
                Params.put("tWrongAns", tWrongAns);

                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void worning() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
        builder.setTitle("Alert!")
                .setMessage("Please answer Some Question")
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


    private void updateSubmit(final String id, final String t_participateExam, final String t_passExam, final String t_rightAns, final String t_wrongAns) {

        String url = getString(R.string.BASS_URL) + "updatePResult";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {

                        Toast.makeText(ExamActivity.this, "Submit success", Toast.LENGTH_SHORT).show();
                        examCountSave.delete();
                        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
                        intent.putExtra("date_time", date_time);
                        intent.putExtra("category", category);
                        intent.putExtra("status", newStatus);
                        intent.putExtra("subject", subject);
                        startActivity(intent);
                        finish();


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
                Params.put("t_participateExam", t_participateExam);
                Params.put("t_passExam", t_passExam);
                Params.put("t_rightAns", t_rightAns);
                Params.put("t_wrongAns", t_wrongAns);

                return Params;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
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