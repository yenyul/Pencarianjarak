package com.yenti.pencarianjarak;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    /*public static String[] nama_event = new String[10];
    public static String[] waktu = new String[10];
    public static String[] tempat = new String[10];
    public static String[] id = new String[10];*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //new getEvent(this).execute();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(SplashScreen.this, MenuActivity.class);
                startActivity(homeIntent);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }

/*   static class getEvent extends AsyncTask<Void, Void, String>{
        WeakReference<Context> context;

        getEvent(Context context){
            this.context = new WeakReference<>(context);
        }
        @Override
        protected String doInBackground(Void... voids) {
           // return new ConnectorHelper().get("https://bangkatourism.000webhostapp.com/getEvent.php");
        }

        //
        @Override
        protected void onPostExecute(String s){
            try{
                JSONObject a = new JSONObject(s);
                //String pumps = a.optString("server_response").toString();
                JSONArray childrenArray = new JSONArray("server_response");
                for(int i = 0;i<childrenArray.length();i++){
                    JSONObject childObject = childrenArray.getJSONObject(i);
                    String pump = childObject.getString("id");
                    id[i] = pump;
                    String p2 = childObject.optString("nama_event").toString();
                    nama_event[i] = p2;
                    String p3 = childObject.optString("waktu").toString();
                    waktu[i] = p3;
                    String p4 = childObject.optString("tempat").toString();
                    tempat[i] = p4;
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            //tampilin array
//            for(int j=0;j<10;j++){
//                System.out.println(id[j]);
//                System.out.println(nama_event[j]);
//                System.out.println(waktu[j]);
//                System.out.println(tempat[j]);
//            }
           System.out.println(s);
      }
    }*/
}

