package com.yenti.pencarianjarak;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.edituname);
        password = (EditText) findViewById(R.id.editpass);
        Login = (Button) findViewById(R.id.buttonlogin);
        Login.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.textreghere);
        SpannableString content = new SpannableString("Register Here!");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    @Override
    public void onClick(View v) {
        if (v == Login) {
            String tuname = username.getText().toString();
            String tpass = password.getText().toString();

            //Tulisan yang keluar nantinya
            Toast.makeText(LoginActivity.this, "Logging In", Toast.LENGTH_SHORT).show();
            new Sign(LoginActivity.this).execute(tuname, tpass);
        }
    }

    //kelas
    public class Sign extends AsyncTask<String, Void, String> {
        private Context context;

        public Sign(Context context) {this.context = context;}

        protected void onPreExecute() {
            //  System.out.println("Tunggu ya");

        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String pass = params[1];

            String link;
            String getdata;

            BufferedReader bufferedReader;
            String result;
            try {
                getdata = "?username=" + URLEncoder.encode(id, "UTF-8");
                getdata += "&password=" + URLEncoder.encode(pass, "UTF-8");

            //10.0.2.2 localhost
                link = "http://192.168.0.22/skripsi/login1.php" + getdata;
                //link = "http://10.5.50.253/skripsi/login1.php" + getdata;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.toString());
            }
        }

        protected void onPostExecute(String result) {
            String jsonStr = result;
            //Toast.makeText(context, jsonStr, Toast.LENGTH_SHORT).show();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String query_result = jsonObj.getString("query_result");
                    if (query_result.equals("SUCCESS")) {
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));

                    } else if (query_result.equals("FAILURE")) {
                        Toast.makeText(context, "Login Gagal", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error Parsing JSON Data", Toast.LENGTH_SHORT).show();
                    Log.e("JSON Parser", "Error Parsing Data[" + e.getMessage() + "] " + jsonStr);
                }

            } else {
                Toast.makeText(context, "Couldn't get JSON data", Toast.LENGTH_SHORT).show();


            }

        }
    }

    public void click(View v) {
        Intent intent = new Intent();
        switch(v.getId()) {
            case R.id.textreghere: // R.id.textView1
                intent = new Intent(this, RegisterActivity.class);
                break;
        }
        startActivity(intent);
    }



}
