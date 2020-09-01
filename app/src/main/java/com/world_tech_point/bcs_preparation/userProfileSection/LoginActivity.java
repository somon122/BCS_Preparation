package com.world_tech_point.bcs_preparation.userProfileSection;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.bcs_preparation.MainActivity;
import com.world_tech_point.bcs_preparation.R;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    SaveUserInfo saveUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else {
            AuthUI.getInstance()
                    .signOut(LoginActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                          netAlert();
                        }
                    });


        }

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
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}