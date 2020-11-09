package com.world_tech_point.apron_seekers.userProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.world_tech_point.apron_seekers.MainActivity;
import com.world_tech_point.apron_seekers.R;
import com.world_tech_point.apron_seekers.liveExamSection.ExamActivity;
import com.world_tech_point.apron_seekers.liveExamSection.ResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgetPasswordActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private EditText otpET;
    private Button sentCodeButton;
    private CardView verifyingShow;
    private TextView resentTimeShow;
    private String verificationId;
    private FirebaseAuth auth;

    String finalNumber;
    String work;

    private static final long START_TIME_IN_MILLIS = 20000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private int value;
    String numberWithPlus;
    String numberWithoutPlus;

    private CountryCodePicker countryCodePicker;
    private TextInputLayout numberTIL;
    private Button forgetSentBtn;
    private ConstraintLayout forgetWholeBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Toolbar toolbar = findViewById(R.id.forgetPasswordToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        forgetWholeBody = findViewById(R.id.forgetWholeBody_id);
        countryCodePicker = findViewById(R.id.forgetCountryCodePicker_Id);
        numberTIL = findViewById(R.id.forgetEnterNumber_id);
        forgetSentBtn = findViewById(R.id.forgetSentButton);

        otpET =findViewById(R.id.otpCode_id);
        sentCodeButton = findViewById(R.id.otpSentButton_id);
        resentTimeShow = findViewById(R.id.resentTimeShow_id);
        verifyingShow = findViewById(R.id.verifyingOtpCodeSection_id);
        auth = FirebaseAuth.getInstance();

        otpET.setVisibility(View.GONE);
        sentCodeButton.setVisibility(View.GONE);
        verifyingShow.setVisibility(View.GONE);
        resentTimeShow.setVisibility(View.GONE);

        forgetSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num = numberTIL.getEditText().getText().toString().trim();
                if (num.isEmpty()){

                    numberTIL.getEditText().setError("Please Enter Valid Number");

                }else {

                    String countryCodeWithPlus = countryCodePicker.getSelectedCountryCodeWithPlus();
                    String countryCodeWithoutPlus = countryCodePicker.getSelectedCountryCode();
                    numberWithPlus = countryCodeWithPlus+num;
                    numberWithoutPlus = countryCodeWithoutPlus+num;
                    checkUserInfo(numberWithoutPlus,numberWithPlus);
                    verifyingShow.setVisibility(View.VISIBLE);
                }

            }
        });
        sentCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sentCodeButton.equals("Resent code")){

                    phoneAuthentication(finalNumber);
                    sentCodeButton.setVisibility(View.GONE);
                    resentTimeShow.setVisibility(View.VISIBLE);
                    startTimer();
                }else {

                    String code = otpET.getText().toString().trim();

                    if (code.isEmpty()){
                        otpET.setError("Enter code");

                    }else {
                        verifyCode(code);
                        resentTimeShow.setVisibility(View.GONE);
                    }

                }

            }
        });

    }
    private void phoneAuthentication(String number) {
        sentVerificationCode(number);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                           passwordAlert(numberWithoutPlus);
                           verifyingShow.setVisibility(View.GONE);
                           otpET.setVisibility(View.GONE);

                        } else {
                            sentCodeButton.setVisibility(View.VISIBLE);
                            Toast.makeText(ForgetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    private void sentVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            Toast.makeText(ForgetPasswordActivity.this, "Code sent", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpET.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
        verifyingShow.setVisibility(View.VISIBLE);
        sentCodeButton.setVisibility(View.GONE);


    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                resentTimeShow.setVisibility(View.GONE);
                sentCodeButton.setVisibility(View.VISIBLE);
                verifyingShow.setVisibility(View.GONE);
                if (value>0){
                    sentCodeButton.setText("Resent code");
                }else {
                    value++;
                }
                resetTimer();

            }
        }.start();

        mTimerRunning = true;
    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d",minutes,seconds);
        resentTimeShow.setText("Resent code remaining : "+timeLeftFormatted);
    }

    private void passwordAlert(final String number) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builder.setTitle("Alert")
                .setMessage("Choose any one")
                .setCancelable(false)
                .setPositiveButton("see old password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        userInfo("s@gmail",number);

                    }
                }).setNeutralButton("change old password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               changePassword(number);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePassword(final String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        View view1 = LayoutInflater.from(ForgetPasswordActivity.this).inflate(R.layout.password_change,null);


        final EditText password = view1.findViewById(R.id.changePassword_id);
        final EditText confirmPassword = view1.findViewById(R.id.changeConfirmPassword_id);
        final Button submit = view1.findViewById(R.id.changePasswordBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass = password.getText().toString().trim();
                String conFirmPass = confirmPassword.getText().toString().trim();

                if (pass.isEmpty()){

                    password.setError("Enter New password");

                }else if (conFirmPass.isEmpty()){

                    confirmPassword.setError("Enter New confirm password");

                }else {

                    changeOldPassword(number,conFirmPass);

                }

            }
        });

        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkUserInfo(final String number,final String numberWithPlus) {

        String url = getString(R.string.BASS_URL) + "checkUserInfo";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {

                        resentTimeShow.setVisibility(View.VISIBLE);
                        startTimer();
                        phoneAuthentication(numberWithPlus);
                        forgetSentBtn.setVisibility(View.GONE);
                        sentCodeButton.setVisibility(View.VISIBLE);
                        otpET.setVisibility(View.VISIBLE);
                        numberTIL.setVisibility(View.GONE);
                        countryCodePicker.setVisibility(View.GONE);
                        verifyingShow.setVisibility(View.GONE);

                    } else {

                        Toast.makeText(ForgetPasswordActivity.this, "Enter Valid Number and password", Toast.LENGTH_SHORT).show();
                        verifyingShow.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    verifyingShow.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ForgetPasswordActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                verifyingShow.setVisibility(View.GONE);
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
    private void changeOldPassword(final String number,final String password) {

        String url = getString(R.string.BASS_URL) + "updatePasswordChange";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                       successAlert();

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

                Params.put("number", number);
                Params.put("password", password);
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void userInfo(final String email, final String number) {

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

                        String name = jsonObject2.getString("userName");
                        String password = jsonObject2.getString("password");

                        if (!password.equals("")){

                            userPasswordShow(name,password);
                        }


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

                Params.put("emailAddress", email);
                Params.put("number", number);
                return Params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
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

 private void userPasswordShow(final String name, final String password) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builder.setTitle("Password Info!")
                .setMessage("Take Screenshot for Remember\n\nName: "+name+"\nPassword: "+password)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                      finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

 private void successAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
        builder.setTitle("Congratulation")
                .setMessage("Successfully Change your password\nPress Ok for Login")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                      finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}