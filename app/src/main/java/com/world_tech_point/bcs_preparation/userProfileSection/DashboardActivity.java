package com.world_tech_point.bcs_preparation.userProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    TextView userId, userName, userEmail, userNumber,userSSCR, userHSCR,userTotalR,userClgName,userBloodGroup,userAddress;
    TextView updateProfile;

    SaveUserInfo saveUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.dashBoardToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("প্রোফাইল");


        userId = findViewById(R.id.profileUserId);
        userName = findViewById(R.id.profileUserName_id);
        userEmail = findViewById(R.id.profileUserEmail_id);
        userNumber = findViewById(R.id.profileUserNumber);
        userSSCR = findViewById(R.id.profileSSC_GPA_id);
        userHSCR = findViewById(R.id.profileHSC_GPA_id);
        userTotalR = findViewById(R.id.profileResults_id);
        userClgName = findViewById(R.id.profileCollegeName_id);
        userBloodGroup = findViewById(R.id.profileBloodGroup);
        userAddress = findViewById(R.id.profileAddress_id);
        updateProfile = findViewById(R.id.profileUpdateButton_id);

        saveUserInfo = new SaveUserInfo(this);

        userId.setText(""+saveUserInfo.getId());
        userName.setText(saveUserInfo.getUserName());
        userEmail.setText(saveUserInfo.getEmail());
        userNumber.setText(saveUserInfo.getNumber());
        userSSCR.setText(saveUserInfo.getSscGPA());
        userHSCR.setText(saveUserInfo.getHscGPA());
        userClgName.setText(saveUserInfo.getCollegeName());
        userBloodGroup.setText(saveUserInfo.getBloodGroup());
        userAddress.setText(saveUserInfo.getAddress());
        userTotalR.setText(saveUserInfo.getTotalResult());

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileSubmitActivity.class);
                intent.putExtra("status","old");
                startActivity(intent);
            }
        });

    }




}