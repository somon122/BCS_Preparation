package com.world_tech_point.apron_seekers.userProfileSection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.world_tech_point.apron_seekers.MainActivity;
import com.world_tech_point.apron_seekers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout number, password;
    Button submitBtn;
    TextView forgerPass, createAccount;
    private CountryCodePicker countryCodePicker;
    ProgressBar progressBar;
    SaveUserInfo saveUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        number = findViewById(R.id.loginNumber_id);
        password = findViewById(R.id.loginPassword_id);

        submitBtn = findViewById(R.id.loginSubmitBtn);
        forgerPass = findViewById(R.id.loginForgetPassword_id);
        createAccount = findViewById(R.id.loginCreateAccount_id);
        countryCodePicker = findViewById(R.id.loginCountryCodePicker_Id);

        progressBar= findViewById(R.id.loginProgressBar);
        saveUserInfo = new SaveUserInfo(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num = number.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();

                if (num.isEmpty()){

                    number.getEditText().setError("Please enter your number");

                }else if (pass.isEmpty()){
                    password.getEditText().setError("Please enter your password");

                }else {

                    String countryCode = countryCodePicker.getSelectedCountryCode();
                    String authNumber = countryCode+num;
                    CheckUserForLogin(authNumber,pass);
                    submitBtn.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                }


            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,ProfileSubmitActivity.class);
                startActivity(intent);

            }
        });


        forgerPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });


    }

    private void CheckUserForLogin(final String number, final String confirmPassword) {

        String url = getString(R.string.BASS_URL) + "checkForLogin";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {

                       userAlreadyExits("",number);

                    } else {

                        Toast.makeText(LoginActivity.this, "Enter Valid Number and password", Toast.LENGTH_SHORT).show();

                        submitBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    submitBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                submitBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();

                Params.put("number", number);
                Params.put("password", confirmPassword);
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


    private void userAlreadyExits(final String email, final String number) {

        String url = getString(R.string.BASS_URL) + "readProfileByEmailAndNumber";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                        String res = jsonObject.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        int id = jsonObject2.getInt("id");
                        String name = jsonObject2.getString("userName");
                        String password = jsonObject2.getString("password");
                        String studentCategory = jsonObject2.getString("studentCategory");
                        String email = jsonObject2.getString("emailAddress");
                        String number = jsonObject2.getString("number");
                        String userClgName = jsonObject2.getString("CollegeName");
                        String hscRoll = jsonObject2.getString("hscRoll");
                        String schoolName = jsonObject2.getString("schoolName");
                        String sscRoll = jsonObject2.getString("sscRoll");
                        String userBloodGroup = jsonObject2.getString("bloodGroup");
                        String userAddress = jsonObject2.getString("address");
                        String regNo = jsonObject2.getString("regNo");
                        String hscGpa = jsonObject2.getString("hscGPA");
                        String sscGpa = jsonObject2.getString("sscGPA");
                        String imageUrl = jsonObject2.getString("imageUrl");
                        saveUserInfo.dataStore(id, name, email,imageUrl, password, studentCategory, number, userClgName, hscRoll, hscGpa, schoolName, sscRoll,
                                sscGpa, regNo, userBloodGroup, userAddress);
                        resultCount(studentCategory, hscGpa, sscGpa);

                        Toast.makeText(LoginActivity.this, "Login Success ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else {

                       noDataAlert();
                        progressBar.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    netAlert();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netAlert();
                progressBar.setVisibility(View.GONE);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();

                Params.put("emailAddress", email);
                Params.put("number", number);
                return Params;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void resultCount(String category, String hscGpa, String sscGpa) {

        try {
            float hscG = Float.parseFloat(hscGpa) * 25;
            float sscG = Float.parseFloat(sscGpa) * 15;

            if (category.equals("First Timer")) {
                float totalAResult = hscG + sscG;
                saveUserInfo.storeResult(String.valueOf(totalAResult));

            } else if (category.equals("Second Timer")) {
                float totalAResult = (hscG + sscG) - 5;
                saveUserInfo.storeResult(String.valueOf(totalAResult));

            } else if (category.equals("Medical College Admitted")) {

                float totalAResult = (float) ((hscG + sscG) - 7.5);
                saveUserInfo.storeResult(String.valueOf(totalAResult));

            } else {
               /* float totalAResult = hscG + sscG;
                saveUserInfo.storeResult(String.valueOf(totalAResult));*/
            }

        } catch (Exception e) {

           /* float hscG = Float.parseFloat(hscGpa) * 25;
            float sscG = Float.parseFloat(sscGpa) * 15;
            float totalAResult = hscG + sscG;
            saveUserInfo.storeResult(String.valueOf(totalAResult));*/

        }

    }
    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Alert!")
                .setMessage("No data found")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        submitBtn.setEnabled(true);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("সতর্কতা!")
                .setMessage("আপনার নেট সংযোগ নিশ্চিত করুন")
                .setCancelable(false)
                .setPositiveButton("আবার চেষ্টা করোন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        submitBtn.setEnabled(true);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}