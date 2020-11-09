package com.world_tech_point.apron_seekers.userProfileSection;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveUserInfo {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SaveUserInfo(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
        public void dataStore (int id,String userName, String email, String imageUrl, String password, String studentCategory, String number,
                               String CollegeName,String hscRoll, String hscGPA, String schoolName,
                               String sscRoll,String sscGPA, String regNo, String bloodGroup, String address){

            editor.putInt("id",id);
            editor.putString("number",number);
            editor.putString("email",email);
            editor.putString("imageUrl",imageUrl);
            editor.putString("userName",userName);
            editor.putString("password",password);
            editor.putString("studentCategory",studentCategory);
            editor.putString("CollegeName",CollegeName);
            editor.putString("hscRoll",hscRoll);
            editor.putString("hscGPA",hscGPA);
            editor.putString("schoolName",schoolName);
            editor.putString("sscRoll",sscRoll);
            editor.putString("sscGPA",sscGPA);
            editor.putString("regNo",regNo);
            editor.putString("bloodGroup",bloodGroup);
            editor.putString("address",address);

            editor.commit();

        }

        public void storeUpdateResult (String t_participateExam, String t_passExam, String t_rightAns, String t_wrongAns ){

            editor.putString("t_participateExam",t_participateExam);
            editor.putString("t_passExam",t_passExam);
            editor.putString("t_rightAns",t_rightAns);
            editor.putString("t_wrongAns",t_wrongAns);
            editor.commit();

        }

      public String getT_participateExam (){

          String t_participateExam = sharedPreferences.getString("t_participateExam","0");
          return t_participateExam;
        }
     public String getT_passExam (){

        String t_passExam = sharedPreferences.getString("t_passExam","0");
        return t_passExam;
        }
     public String getT_rightAns (){

         String t_rightAns = sharedPreferences.getString("t_rightAns","0");
        return t_rightAns;
        }
     public String getT_wrongAns (){

         String t_wrongAns = sharedPreferences.getString("t_wrongAns","0");
        return t_wrongAns;
        }




    public void storeResult (String tResult){

            editor.putString("result",tResult);
            editor.commit();

        }

    public String getTotalResult (){

        String result = sharedPreferences.getString("result","0");
        return result;
    }

        public void storeNumber (String number){

            editor.putString("number",number);
            editor.commit();

        }

          public void storeEmail (String email){

            editor.putString("email",email);
            editor.commit();

        }


        public String getImageUrl (){

            String imageUrl = sharedPreferences.getString("imageUrl","");
            return imageUrl;
        }
        public int getId (){

            int id = sharedPreferences.getInt("id",0);
            return id;
        }


    public String getPassword (){

            String password = sharedPreferences.getString("password","");
            return password;
        }
   public String getStudentCategory (){

            String studentCategory = sharedPreferences.getString("studentCategory","");
            return studentCategory;
        }
   public String getHscRoll (){

            String hscRoll = sharedPreferences.getString("hscRoll","");
            return hscRoll;
        }
   public String getSscRoll (){

            String sscRoll = sharedPreferences.getString("sscRoll","");
            return sscRoll;
        }


   public String getNumber (){

            String number = sharedPreferences.getString("number","");
            return number;
        }

        public String getUserName (){

            String userName = sharedPreferences.getString("userName","");
            return userName;
        }
        public String getEmail (){

            String password = sharedPreferences.getString("email","");
            return password;
        }


          public String getCollegeName (){

            String CollegeName = sharedPreferences.getString("CollegeName","");
            return CollegeName;
        }
          public String getHscGPA  (){

            String hscGPA = sharedPreferences.getString("hscGPA","");
            return hscGPA;
        }
          public String getSchoolName (){

            String schoolName = sharedPreferences.getString("schoolName","");
            return schoolName;
        }
          public String getSscGPA  (){

            String sscGPA = sharedPreferences.getString("sscGPA","");
            return sscGPA;
        }
          public String getRegNo (){

            String regNo = sharedPreferences.getString("regNo","");
            return regNo;
        }
          public String getBloodGroup (){

            String bloodGroup = sharedPreferences.getString("bloodGroup","");
            return bloodGroup;
        }
          public String getAddress (){

            String address = sharedPreferences.getString("address","");
            return address;
        }



        public boolean checkUser(){
            boolean number = sharedPreferences.getString("number","").isEmpty();
            boolean email = sharedPreferences.getString("email","").isEmpty();
            return number || email;
        }

        public void delete (){

            editor.clear();
            editor.commit();

        }


    }
