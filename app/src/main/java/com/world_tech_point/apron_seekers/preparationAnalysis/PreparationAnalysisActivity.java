package com.world_tech_point.apron_seekers.preparationAnalysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.world_tech_point.apron_seekers.R;

import java.util.ArrayList;
import java.util.List;

public class PreparationAnalysisActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    BarChart barChart;
    List<BarEntry>visitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peparation_analysis);

        Toolbar toolbar = findViewById(R.id.analysisToolBart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barChart = findViewById(R.id.analysisBarChart);

        visitors= new ArrayList<>();
        visitors.add(new BarEntry(2011,200));
        visitors.add(new BarEntry(2012,300));
        visitors.add(new BarEntry(2013,400));
        visitors.add(new BarEntry(2014,500));
        visitors.add(new BarEntry(2015,600));

        BarDataSet barDataSet = new BarDataSet(visitors,"Test");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar chart test");
        barChart.animateX(2000);




    }
}