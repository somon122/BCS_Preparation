package com.world_tech_point.apron_seekers.liveExamSection;

import android.content.Context;
import android.content.SharedPreferences;

public class LiveExamCountSave {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LiveExamCountSave(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LiveExamCounter", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public void storeMarks (String marks){
        editor.putString("marks",marks);
        editor.commit();
    }

    public String getMarks(){
        String marks = sharedPreferences.getString("marks","0");
        return marks;
    }


  public void setUserTotalAnsC (int userAnsC){
        editor.putInt("userAnsCount",userAnsC);
        editor.commit();
    }

    public int getUserTotalAnsC(){
        int userAnsC = sharedPreferences.getInt("userAnsCount",0);
        return userAnsC;
    }


    public void storeRightAns (int number){
        editor.putInt("rightAns",number);
        editor.commit();

    }
    public void storeWrongAns (int wrongAns){
        editor.putInt("wrongAns",wrongAns);
        editor.commit();

    }

    public void storeTotalQ (int totalQ){
        editor.putInt("totalQ",totalQ);
        editor.commit();

    }


    public int getTotalQ(){
        int totalQ = sharedPreferences.getInt("totalQ",0);
        return totalQ;
    }
    public int getRightAns  (){

        int rightAns = sharedPreferences.getInt("rightAns",0);
        return rightAns;
    }
    public int getWrongAns (){

        int wrongAns = sharedPreferences.getInt("wrongAns",0);
        return wrongAns;
    }

    public void delete (){
        editor.clear();
        editor.commit();

    }


}
