package com.world_tech_point.apron_seekers.splashScreen;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.world_tech_point.apron_seekers.MainActivity;
import com.world_tech_point.apron_seekers.R;
import com.world_tech_point.apron_seekers.userProfileSection.LoginActivity;
import com.world_tech_point.apron_seekers.userProfileSection.OTPActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class SplashFragment extends Fragment {


    private ProgressBar progressBar;
    private int progress;
    TextView refressTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View root =  inflater.inflate(R.layout.fragment_splash, container, false);


        progressBar = root.findViewById(R.id.progressBar);
        refressTV = root.findViewById(R.id.reload_id);
        refressTV.setVisibility(View.GONE);

        init();
        return root;
    }

    private void init() {

        if (HaveNetwork()) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doTheWork();
                    startApp();
                }
            });
            thread.start();

        } else {

            Toast.makeText(getContext(), "Please connect your Internet first", Toast.LENGTH_SHORT).show();
            refressTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
        refressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OpenActivity.class));
            }
        });


    }

    private void startApp() {
        Intent intent = new Intent(getContext(), MainActivity.class);

        startActivity(intent);


    }

    private void doTheWork() {

        for (progress = 25; progress <= 100; progress = progress + 25) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private boolean HaveNetwork() {
        boolean have_WiFi = false;
        boolean have_Mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfo) {

            if (info.getTypeName().equalsIgnoreCase("WIFI")) {
                if (info.isConnected()) {
                    have_WiFi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (info.isConnected()) {
                    have_Mobile = true;
                }
            }

        }
        return have_WiFi || have_Mobile;

    }


}