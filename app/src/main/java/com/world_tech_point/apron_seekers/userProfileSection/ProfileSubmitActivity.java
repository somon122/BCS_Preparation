package com.world_tech_point.apron_seekers.userProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.world_tech_point.apron_seekers.MainActivity;
import com.world_tech_point.apron_seekers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSubmitActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    EditText userName, userEmail, userNumber, userPassword, userConfirmPassword, userRegNo, userAddress;
    EditText userClgName, userHSCRoll, userHSCGPA, userSchoolName, userSSCRoll, userSSCGPA;
    Spinner categorySpinner, bloodGroupSpinner;
    Button submitButton;
    String category, blood;
    List<String> mainCategoryValue;
    List<String> bloodGroupValue;
    String status;
    int option;

    SaveUserInfo saveUserInfo;
    CircleImageView profileImage;
    Button pickImage;
    Uri imageURI = null;
    Bitmap imagePath = null;
    String image=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_submit);
        Toolbar toolbar = findViewById(R.id.editProfileToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        submitButton = findViewById(R.id.editProfileSubmitButton);
        categorySpinner = findViewById(R.id.editProfileStudentCategorySpinner_id);
        bloodGroupSpinner = findViewById(R.id.editProfileBloodGroupsNamesSpinner);

        userName = findViewById(R.id.editProfileUserName);
        userEmail = findViewById(R.id.editProfileUserEmail);
        userNumber = findViewById(R.id.editProfileUserNumber);
        userNumber.setEnabled(false);

        profileImage = findViewById(R.id.profileImage);
        pickImage = findViewById(R.id.picProfileImage);

        userPassword = findViewById(R.id.editProfileUserPassword_id);
        userConfirmPassword = findViewById(R.id.editProfileConfirmPassword_id);
        userRegNo = findViewById(R.id.editProfileRegNo);
        userAddress = findViewById(R.id.editProfileAddress);
        userClgName = findViewById(R.id.editProfileUserClgName);
        userHSCRoll = findViewById(R.id.editProfileHSCRoll);
        userHSCGPA = findViewById(R.id.editProfileHSCGPA);
        userSchoolName = findViewById(R.id.editProfileSchoolName);
        userSSCRoll = findViewById(R.id.editProfileSSCRoll);
        userSSCGPA = findViewById(R.id.editProfileSSC_GPA);
        saveUserInfo = new SaveUserInfo(this);
        userNumber.setText(saveUserInfo.getNumber());


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            status = bundle.getString("status");
            if (status.equals("new")) {
                setTitle("এডিট প্রোফাইল");
                setNewData();
            } else if (status.equals("old")) {

                setOldValue();

            } else {
                Toast.makeText(this, "Not define", Toast.LENGTH_SHORT).show();
            }

        }

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileSubmitActivity.this);

            }
        });

        mainCategoryValue = new ArrayList<String>();
        mainCategoryValue.add("Select Student Category");
        mainCategoryValue.add("HSC");
        mainCategoryValue.add("First Timer");
        mainCategoryValue.add("Second Timer");
        mainCategoryValue.add("Medical College Admitted");
        ArrayAdapter<String> mainDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainCategoryValue);
        mainDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(mainDataAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                category = categorySpinner.getSelectedItem().toString();
                if (category.equals("HSC")){
                    option=1;
                    userHSCGPA.setVisibility(View.GONE);
                    userHSCRoll.setVisibility(View.GONE);
                }else {
                    option=0;
                    userHSCGPA.setVisibility(View.VISIBLE);
                    userHSCRoll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bloodGroupValue = new ArrayList<String>();
        bloodGroupValue.add("Select Blood Group");
        bloodGroupValue.add("A+");
        bloodGroupValue.add("A-");
        bloodGroupValue.add("B+");
        bloodGroupValue.add("B-");
        bloodGroupValue.add("O+");
        bloodGroupValue.add("AB+");
        bloodGroupValue.add("AB-");
        bloodGroupValue.add("O-");
        bloodGroupValue.add("others");

        ArrayAdapter<String> bloodGroupDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodGroupValue);
        bloodGroupDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupDataAdapter);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                blood = bloodGroupSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Objects.equals(getFileData(imagePath), "")){
                    editProfile();
                }else {
                    Toast.makeText(ProfileSubmitActivity.this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public String getFileData(Bitmap bitmap) {

        if (bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes,Base64.DEFAULT);
        }

        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageURI = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                     imagePath = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    profileImage.setImageBitmap(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();

            }
        }
    }



    private void setNewData() {

        userNumber.setText(saveUserInfo.getNumber());
        userEmail.setText(saveUserInfo.getEmail());

    }

    private void setOldValue() {


        setTitle("আপডেট প্রোফাইল");
        submitButton.setText("আপডেট প্রোফাইল");

        userName.setText(saveUserInfo.getUserName());
        userNumber.setText(saveUserInfo.getNumber());
        userEmail.setText(saveUserInfo.getEmail());
        userRegNo.setText(saveUserInfo.getRegNo());
        userAddress.setText(saveUserInfo.getAddress());
        userClgName.setText(saveUserInfo.getCollegeName());
        userHSCGPA.setText(saveUserInfo.getHscGPA());
        userSSCGPA.setText(saveUserInfo.getSscGPA());

        userPassword.setText(saveUserInfo.getPassword());
        userConfirmPassword.setText(saveUserInfo.getPassword());
        userHSCRoll.setText(saveUserInfo.getHscRoll());
        userSSCRoll.setText(saveUserInfo.getSscRoll());
        userSchoolName.setText(saveUserInfo.getSchoolName());


    }

    private void editProfile() {

        String name = userName.getText().toString();
        String email = userEmail.getText().toString().trim();
        String number = userNumber.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPassword = userConfirmPassword.getText().toString().trim();
        String regNo = userRegNo.getText().toString().trim();
        String address = userAddress.getText().toString();
        String collegeName = userClgName.getText().toString();
        String hscRoll = userHSCRoll.getText().toString().trim();
        String hscGPA = userHSCGPA.getText().toString().trim();
        String schoolName = userSchoolName.getText().toString();
        String sscRoll = userSSCRoll.getText().toString().trim();
        String sscGPA = userSSCGPA.getText().toString().trim();


        if (name.isEmpty()) {
            userName.setError("Please enter name");
        } else if (email.isEmpty()) {
            userEmail.setError("Please enter Email");
        } else if (number.isEmpty()) {
            userNumber.setError("Please enter Number");
        } else if (password.isEmpty()) {
            userPassword.setError("Please enter password");
        } else if (confirmPassword.isEmpty()) {

            userConfirmPassword.setError("Please enter valid password ");
        } else if (regNo.isEmpty()) {
            userRegNo.setError("Please enter Registration number");

        } else if (address.isEmpty()) {
            userAddress.setError("Please enter Address");

        } else if (collegeName.isEmpty()) {
            userClgName.setError("Please enter collegeName");

        }

        else if (hscRoll.isEmpty() && option == 0) {
            userHSCRoll.setError("Please enter HSC Roll");

        } else if (hscGPA.isEmpty() && option == 0) {
            userHSCGPA.setError("Please enter HSC GPA");
        }

        else if (schoolName.isEmpty()) {
            userSchoolName.setError("Please enter School Name");

        } else if (sscRoll.isEmpty()) {
            userSSCRoll.setError("Please enter SSC Roll");

        } else if (sscGPA.isEmpty()) {
            userSSCGPA.setError("Please enter SSC GPA");

        } else {

            if (confirmPassword.equals(password) && category.equals("Select Student Category") && blood.equals("Select Blood Group")) {

                userConfirmPassword.setError("Please carefully completed all information");

            } else {

                if (status.equals("new")) {

                    submit(name, email, number, confirmPassword, category, blood, regNo, address, collegeName, hscRoll, hscGPA, schoolName, sscRoll, sscGPA);
                    submitButton.setEnabled(false);
                } else if (status.equals("old")) {
                    String id = String.valueOf(saveUserInfo.getId());
                    update(id, name, email, number, confirmPassword, category, blood, regNo, address, collegeName, hscRoll, hscGPA, schoolName, sscRoll, sscGPA);
                    submitButton.setEnabled(false);
                } else {
                    Toast.makeText(this, "Not define", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void submit(final String name, final String email, final String number, final String confirmPassword, final String category, final String blood, final String regNo, final String address, final String collegeName, final String hscRoll, final String hscGPA, final String schoolName, final String sscRoll, final String sscGPA) {


        String url = getString(R.string.BASS_URL) + "writeProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ProfileSubmitActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("success").equals("success")) {

                        Toast.makeText(ProfileSubmitActivity.this, "Registration complete ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else if (jsonObject.getString("success").equals("image not move")){

                        Toast.makeText(ProfileSubmitActivity.this, "image not move", Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);

                    } else if (jsonObject.getString("success").equals("image not found")){

                        Toast.makeText(ProfileSubmitActivity.this, "image not found", Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);

                    }else {

                        Toast.makeText(ProfileSubmitActivity.this, "Other p", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    submitButton.setEnabled(true);
                    Toast.makeText(ProfileSubmitActivity.this, "Please Try Againggggggg"+e, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProfileSubmitActivity.this, "Please Try Again problem" +error, Toast.LENGTH_SHORT).show();
                submitButton.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();

                Params.put("userName", name);
                Params.put("emailAddress", email);
                Params.put("number", number);
                Params.put("imageUrl",getFileData(imagePath));
                Params.put("password", confirmPassword);
                Params.put("studentCategory", category);
                Params.put("CollegeName", collegeName);
                Params.put("hscRoll", hscRoll);
                Params.put("hscGPA", hscGPA);
                Params.put("schoolName", schoolName);
                Params.put("sscRoll", sscRoll);
                Params.put("sscGPA", sscGPA);
                Params.put("regNo", regNo);
                Params.put("bloodGroup", blood);
                Params.put("address", address);
                Params.put("t_exam", "0");
                Params.put("t_participateExam", "0");
                Params.put("t_passExam", "0");
                Params.put("t_question", "0");
                Params.put("t_rightAns", "0");
                Params.put("t_wrongAns", "0");

                return Params;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void update(final String id, final String name, final String email, final String number, final String confirmPassword, final String category, final String blood, final String regNo, final String address, final String collegeName, final String hscRoll, final String hscGPA, final String schoolName, final String sscRoll, final String sscGPA) {

        String url = getString(R.string.BASS_URL) + "updateUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {

                        securityAlert();

                    } else if (jsonObject.getString("success").equals("image not move")){

                        Toast.makeText(ProfileSubmitActivity.this, "image not move", Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);

                    } else if (jsonObject.getString("success").equals("image not found")){

                        Toast.makeText(ProfileSubmitActivity.this, "image not found", Toast.LENGTH_SHORT).show();
                        submitButton.setEnabled(true);

                    } else {
                        submitButton.setEnabled(true);
                        Toast.makeText(ProfileSubmitActivity.this, "You have to no  change", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileSubmitActivity.this, " problem", Toast.LENGTH_SHORT).show();
                    submitButton.setEnabled(true);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                submitButton.setEnabled(true);
                Toast.makeText(ProfileSubmitActivity.this, "Update link problem", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();

                Params.put("id", id);

                Params.put("userName", name);
                Params.put("emailAddress", email);
                Params.put("number", number);
                Params.put("imageUrl", getFileData(imagePath));
                Params.put("password", confirmPassword);

                Params.put("studentCategory", category);
                Params.put("CollegeName", collegeName);
                Params.put("hscRoll", hscRoll);
                Params.put("hscGPA", hscGPA);

                Params.put("schoolName", schoolName);
                Params.put("sscRoll", sscRoll);
                Params.put("sscGPA", sscGPA);
                Params.put("regNo", regNo);
                Params.put("bloodGroup", blood);
                Params.put("address", address);

                return Params;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }



    private void securityAlert() {


        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSubmitActivity.this);

        builder.setTitle("Re-decoration Alert!")
                .setMessage("Please press ok for re-login")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AuthUI.getInstance()
                                .signOut(ProfileSubmitActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        saveUserInfo.delete();
                                        Toast.makeText(ProfileSubmitActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProfileSubmitActivity.this, LoginActivity.class));
                                        finish();

                                    }
                                });

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }




}
