package com.world_tech_point.bcs_preparation.liveExamSection;

import android.content.Context;
import android.content.SharedPreferences;

public class LiveExamControl {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LiveExamControl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("liveExamControl", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public void setStoreExam ( String date){
        editor.putString("date",date);
        editor.commit();
    }

    public String getStoreDate(){
        String date = sharedPreferences.getString("date","0");
        return date;
    }




    public void delete (){
        editor.clear();
        editor.commit();

    }


}
