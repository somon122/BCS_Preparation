package com.world_tech_point.apron_seekers.ArchiveSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button subjectiveFilter;
    String category;
    private List<ArchiveClass> archiveList;

    TextView filterCancel;
    Button filterAllSub, Biology, Zoology, Chemistry1, Chemistry2, Physics1, physics2, English, General_Knowledge;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    long currentMilliseconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        Toolbar toolbar = findViewById(R.id.archiveToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
           titleSet(category);
            archiveQList(category);
        }


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm a");
        String withdraw_date = sdf1.format(new Date());
        try {
            Date mDate = sdf1.parse(withdraw_date);
            currentMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.archiveRecyclerView_id);
        progressBar = findViewById(R.id.archiveProgressBar);
        subjectiveFilter = findViewById(R.id.subjectiveFilter_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        archiveList = new ArrayList<>();

        subjectiveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                examConfirmAlert();

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


    private void examConfirmAlert() {

        builder = new AlertDialog.Builder(ArchiveActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.subjective_filter_model, null);
        builder.setView(view1);

        filterAllSub = view1.findViewById(R.id.filterAllSubject);
        Biology = view1.findViewById(R.id.filterBiology);
        Zoology = view1.findViewById(R.id.filterByZoology);
        Chemistry1 = view1.findViewById(R.id.filterChemistry1);
        Chemistry2 = view1.findViewById(R.id.filterChemistry2);
        Physics1 = view1.findViewById(R.id.filterPhysics1);
        physics2 = view1.findViewById(R.id.filterPhysics2);
        English = view1.findViewById(R.id.filterEnglish);
        General_Knowledge = view1.findViewById(R.id.filterGeneralKnowledge);

        dialog = builder.create();
        dialog.show();
    }

    public void filter(View view) {

        int id = view.getId();

        if (id == R.id.filterCancel) {
            dialog.dismiss();

        } else if (id == R.id.filterAllSubject) {
            Intent intent = new Intent(getApplicationContext(), ArchiveActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
            finish();
        } else if (id == R.id.filterBiology) {
            searchExamBySubject("Biology");
            subjectiveFilter.setText("উদ্ভিদবিজ্ঞান");
            dialog.dismiss();
        } else if (id == R.id.filterByZoology) {
            searchExamBySubject("Zoology");
            subjectiveFilter.setText("প্রাণীবিজ্ঞান");
            dialog.dismiss();

        } else if (id == R.id.filterChemistry1) {
            searchExamBySubject("Chemistry1");
            subjectiveFilter.setText("রসায়ন প্রথম পত্র");
            dialog.dismiss();
        } else if (id == R.id.filterChemistry2) {
            searchExamBySubject("Chemistry2");
            subjectiveFilter.setText("রসায়ন দ্বিতীয় পত্র");
            dialog.dismiss();
        } else if (id == R.id.filterPhysics1) {
            searchExamBySubject("Physics1");
            subjectiveFilter.setText("পদার্থবিজ্ঞান প্রথম পত্র");
            dialog.dismiss();
        } else if (id == R.id.filterPhysics2) {
            searchExamBySubject("Physics2");
            subjectiveFilter.setText("পদার্থবিজ্ঞান দ্বিতীয় পত্র");
            dialog.dismiss();
        } else if (id == R.id.filterEnglish) {
            searchExamBySubject("English");
            subjectiveFilter.setText("ইংরেজি");
            dialog.dismiss();
        } else if (id == R.id.filterGeneralKnowledge) {
            searchExamBySubject("General_Knowledge");
            subjectiveFilter.setText("সাধারণ জ্ঞান");
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchBar_id);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchQuestion(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchQuestion(newText);

                return false;
            }
        });


        return true;
    }

    private void searchQuestion(String recherche) {
        if (recherche.length() > 0)
            recherche = recherche.substring(0, 1).toUpperCase() + recherche.substring(1).toLowerCase();

        List<ArchiveClass> results = new ArrayList<>();
        for (ArchiveClass comPojoClass : archiveList) {
            if (comPojoClass.getArchiveDate() != null && comPojoClass.getArchiveDate().contains(recherche)) {
                results.add(comPojoClass);
            }
        }
        updateListUsers(results);
    }


    private void updateListUsers(List<ArchiveClass> listQuestion) {

        // Sort the list by date

        Collections.sort(listQuestion, new Comparator<ArchiveClass>() {
            @Override
            public int compare(ArchiveClass o1, ArchiveClass o2) {
                int res = 1;
                if (o1.getArchiveDate() == (o2.getArchiveDate())) {
                    res = -1;
                }
                return res;
            }
        });

        ArchiveAdapter archiveAdapter = new ArchiveAdapter(ArchiveActivity.this, listQuestion,currentMilliseconds);
        recyclerView.setAdapter(archiveAdapter);
        archiveAdapter.notifyDataSetChanged();

    }


    private void searchExamBySubject(String recherche) {
        if (recherche.length() > 0)
            recherche = recherche.substring(0, 1).toUpperCase() + recherche.substring(1).toLowerCase();

        List<ArchiveClass> results = new ArrayList<>();
        for (ArchiveClass comPojoClass : archiveList) {
            if (comPojoClass.getArchiveSubject() != null && comPojoClass.getArchiveSubject().contains(recherche)) {
                results.add(comPojoClass);
            }
        }
        updateListBySubject(results);
    }


    private void updateListBySubject(List<ArchiveClass> listQuestion) {

        // Sort the list by date

        Collections.sort(listQuestion, new Comparator<ArchiveClass>() {
            @Override
            public int compare(ArchiveClass o1, ArchiveClass o2) {
                int res = 1;
                if (o1.getArchiveSubject() == (o2.getArchiveSubject())) {
                    res = -1;
                }
                return res;
            }
        });

        ArchiveAdapter archiveAdapter = new ArchiveAdapter(ArchiveActivity.this, listQuestion,currentMilliseconds);
        recyclerView.setAdapter(archiveAdapter);
        archiveAdapter.notifyDataSetChanged();

    }

    private void archiveQList(final String category) {

        String url = getString(R.string.BASS_URL) + "archiveExam";
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
                            ArchiveClass archiveClass = new ArchiveClass();
                            archiveClass.setArchiveId(i + 1);
                            archiveClass.setArchiveCategory(dataobj.getString("syllabus"));
                            archiveClass.setArchiveSubject(dataobj.getString("subject"));
                            archiveClass.setArchiveDate(dataobj.getString("date_time"));

                            archiveList.add(archiveClass);
                        }
                        updateListUsers(archiveList);

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
        RequestQueue queue = Volley.newRequestQueue(ArchiveActivity.this);
        queue.add(stringRequest);
    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(ArchiveActivity.this);
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