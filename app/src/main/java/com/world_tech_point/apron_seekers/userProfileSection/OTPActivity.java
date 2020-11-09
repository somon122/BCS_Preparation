package com.world_tech_point.apron_seekers.userProfileSection;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.apron_seekers.R;
import com.world_tech_point.apron_seekers.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    SaveUserInfo saveUserInfo;
    Button  btn;
    LinearLayout welcomeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        btn = findViewById(R.id.welcomeBtn2);
        welcomeBox = findViewById(R.id.welcomeBox2);
        saveUserInfo = new SaveUserInfo(this);
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

    // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });

    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    saveUserInfo = new SaveUserInfo(this);
                    String phoneNumber = null;
                    if (user != null) {
                        phoneNumber = user.getPhoneNumber();
                    }
                    if (phoneNumber != null) {
                        phoneNumber = phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
                        save(phoneNumber);

                    }

                } else {

                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        }

    private void save(String phoneNumber) {

        saveUserInfo.storeNumber(phoneNumber);

        if (saveUserInfo.getNumber() != null){

            checkUserInfo(phoneNumber);

        }else {
            logout();

        }

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivity.this);
        builder.setTitle("সতর্কতা!")
                .setMessage("আপনার নেট সংযোগ নিশ্চিত করুন")
                .setCancelable(false)
                .setPositiveButton("আবার চেষ্টা করোন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void checkUserInfo(final String number) {

        String url = getString(R.string.BASS_URL) + "checkUserInfo";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {

                      welcomeBox.setVisibility(View.VISIBLE);

                    } else if (!jsonObject.getBoolean("success")){

                        startActivity(new Intent(OTPActivity.this, MainActivity.class));
                        finish();

                    }else {
                        logout();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    logout();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               logout();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();

                Params.put("number", number);
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void logout(){

        AuthUI.getInstance()
                .signOut(OTPActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        netAlert();
                    }
                });
    }

}