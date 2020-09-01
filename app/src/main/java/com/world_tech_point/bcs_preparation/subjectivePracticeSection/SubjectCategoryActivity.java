package com.world_tech_point.bcs_preparation.subjectivePracticeSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.world_tech_point.bcs_preparation.R;
import com.world_tech_point.bcs_preparation.pdfSection.PDF_CategoryActivity;

public class SubjectCategoryActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){

            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        Toolbar toolbar = findViewById(R.id.subjectivePracticeSectionToolBar_id);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("বিষয়ভিত্তিক প্রস্ততি");
        alert();


    }

    private void alert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(SubjectCategoryActivity.this);
        builder.setTitle("Notice")
                .setMessage("This service coming Soon.....")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}