package com.world_tech_point.bcs_preparation;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.bcs_preparation.liveExamSection.ExamDashboardActivity;
import com.world_tech_point.bcs_preparation.pdfSection.PDF_CategoryActivity;
import com.world_tech_point.bcs_preparation.subjectivePracticeSection.SubjectCategoryActivity;
import com.world_tech_point.bcs_preparation.userProfileSection.DashboardActivity;
import com.world_tech_point.bcs_preparation.userProfileSection.LoginActivity;
import com.world_tech_point.bcs_preparation.userProfileSection.ProfileSubmitActivity;
import com.world_tech_point.bcs_preparation.userProfileSection.SaveUserInfo;
import com.world_tech_point.bcs_preparation.userProfileSection.SummaryActivity;
import com.world_tech_point.bcs_preparation.videoSection.VideoCategoryActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navigationView;
    TextView smartBook, videoTutorials, subjectivePractice;
    TextView sideBarEditProfile, sideBarUserId, sideBarUserEmail;
    BottomNavigationView bottomNavigationView;
    Button centralTestBtn, subjectiveTestBtn, chapterBassTestBtn, weeklyTestBtn, dailyTestBtn, modelTest;

    SaveUserInfo saveUserInfo;
    int totalAResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("এপ্রোন সিকার্স");

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        notificationLoad();
        saveUserInfo = new SaveUserInfo(this);

        centralTestBtn = findViewById(R.id.centralTest_id);
        subjectiveTestBtn = findViewById(R.id.subjectiveTest_id);
        chapterBassTestBtn = findViewById(R.id.chapterBassTest_id);
        weeklyTestBtn = findViewById(R.id.weeklyTest_id);
        dailyTestBtn = findViewById(R.id.dailyTest_id);
        modelTest = findViewById(R.id.modelTest_id);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        centralTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("CentralTest");

            }
        });
        subjectiveTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("SubjectiveTest");

            }
        });
        chapterBassTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("ChapterBassTest");

            }
        });
        weeklyTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("WeeklyTest");

            }
        });

        dailyTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("DailyTest");

            }
        });

        modelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enterExam("ModelTest");

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userProfile();


        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.bottomHome_id:

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                        break;

                    case R.id.bottomProfile_id:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        break;

                    case R.id.bottom_summary_id:
                        startActivity(new Intent(getApplicationContext(), SummaryActivity.class));
                        break;

                    case R.id.bottom_Comment_id:

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (ActivityNotFoundException e) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                        }
                        break;

                    default:
                        break;
                }
            }
        });


        sideBarEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileSubmitActivity.class));
            }
        });

        smartBook = findViewById(R.id.smartBook_id);
        videoTutorials = findViewById(R.id.videoTutorials_id);
        subjectivePractice = findViewById(R.id.subjectivePractice_id);

        smartBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, PDF_CategoryActivity.class));
            }
        });

        videoTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, VideoCategoryActivity.class));
            }
        });

        subjectivePractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SubjectCategoryActivity.class));
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_privacyPolicy) {

                    Toast.makeText(MainActivity.this, "nav_privacyPolicy", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_rules) {

                    Toast.makeText(MainActivity.this, "Rules", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_Help) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                    }


                } else if (id == R.id.nav_share) {

                    shareApp();


                } else if (id == R.id.nav_send) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                    }


                } else if (id == R.id.nav_logout) {

                    logoutAlert();


                } else if (id == R.id.nav_exits) {

                    exitAlert();


                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


                return false;

            }
        });

    }

    private void shareApp() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "App link : " + "\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
        String shareSub = "MCQ Online Exam by Android App";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Live Exam"));
    }

    private void enterExam(String category) {
        Intent intent = new Intent(MainActivity.this, ExamDashboardActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);

    }

    private void notificationLoad() {

        String url = getString(R.string.BASS_URL) + "notification";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                        String res = jsonObject.getString("notify");
                        JSONArray jsonArray = new JSONArray(res);
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);

                        String title = jsonObject2.getString("title");
                        String des = jsonObject2.getString("description");

                        if (title != null) {

                            notificationAlert(title, des);
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
                Params.put("category", "home");
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
                        saveUserInfo.dataStore(id, name, email, password, studentCategory, number, userClgName, hscRoll, hscGpa, schoolName, sscRoll,
                                sscGpa, regNo, userBloodGroup, userAddress);
                        resultCount(studentCategory, hscGpa, sscGpa);

                    } else {

                        Intent intent = new Intent(getApplicationContext(), ProfileSubmitActivity.class);
                        intent.putExtra("status", "new");
                        startActivity(intent);

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

    private void notificationAlert(String title, String des) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(title)
                .setMessage(des)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();


    }


    private void userProfile() {

        View userProfileUD = navigationView.getHeaderView(0);
        sideBarEditProfile = userProfileUD.findViewById(R.id.sideBarEditProfile_id);
        sideBarUserId = userProfileUD.findViewById(R.id.sideBarUserId);
        sideBarUserEmail = userProfileUD.findViewById(R.id.sideBarUserEmail);
        sideBarUserId.setText("আইডি: " + saveUserInfo.getId());
        sideBarUserEmail.setText("ইমেইল: " + saveUserInfo.getEmail());

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {

            if (saveUserInfo.checkUser()) {
                userAlreadyExits(saveUserInfo.getEmail(), saveUserInfo.getNumber());

            } else {

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_Notification) {
            notificationLoad();

        } else if (id == R.id.action_share) {
            shareApp();

        } else if (id == R.id.action_refresh) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            exitAlert();
        } else {
            exitAlert();
        }

    }

    private void exitAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exits alert!")
                .setMessage("Are you sure to Exits?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void noDataAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


    private void logoutAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Logout alert!")
                .setMessage("Are you sure to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        saveUserInfo.delete();
                                        Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}