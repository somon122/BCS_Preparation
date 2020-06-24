package com.world_tech_point.bcs_preparation.commentSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.world_tech_point.bcs_preparation.R;

import org.json.JSONException;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView commentsAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.commentRecyclerView_id);
        commentsAdd = findViewById(R.id.commentsAdd_id);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        commentsRetrieveMethod();

        commentsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });


    }

    private void commentsRetrieveMethod(){

        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



/*

               JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    JSONArray dataArray  = obj.getJSONArray("Success");

                    dataAlertTV.setVisibility(View.GONE);

                    for (int i = 0; i < dataArray.length(); i++) {

                        dataClass = new ShowDataClass();
                        JSONObject dataobj = dataArray.getJSONObject(i);
                        dataClass.setCategory(dataobj.getString("category"));
                        dataClass.setImage_link(dataobj.getString("image_link"));
                        dataClass.setVideo_title(dataobj.getString("title"));
                        dataClass.setVideo_link(dataobj.getString("link"));
                        dataClass.setLove_count(dataobj.getString("love_count"));
                        dataClass.setView_count(dataobj.getString("view_count"));
                        dataClass.setDate_time(dataobj.getString("date_time"));
                        data_list.add(dataClass);

                    }

                    dataAdapter = new ShowDataAdapter(getContext(),data_list);
                    recyclerView.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }




}