package com.yenti.pencarianjarak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.helper.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private EditText nama;
    private Button Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

        nama = (EditText) findViewById(R.id.editname);
        username = (EditText) findViewById(R.id.edituname);
        password = (EditText) findViewById(R.id.editpass);
        Register = (Button) findViewById(R.id.buttonreg);
        Register.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    public void onClick(View v) {
        if (v.getId() == R.id.buttonreg) {
            // String tname = name.getText().toString();
            // String tuname = username.getText().toString();
            //String tpass = password.getText().toString();
            //addUser();
            //Tulisan yang keluar nantinya
            //Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_SHORT).show();
            //LoginActivity login = new LoginActivity();
            //new Sign(RegisterActivity.this).execute(tname,tuname,tpass);
        }
    }


    /*private void addUser() {

        final String namea = nama.getText().toString().trim();
        final String uname = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();


        class AddUser extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String jsonStr = s;
                //Toast.makeText(context, jsonStr, Toast.LENGTH_SHORT).show();
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String query_result = jsonObj.getString("query_result");
                        if (query_result.equals("SUCCESS")) {
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MenuActivity.class));

                        } else if (query_result.equals("FAILURE")) {
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Error Parsing JSON Data", Toast.LENGTH_SHORT).show();
                        Log.e("JSON Parser", "Error Parsing Data[" + e.getMessage() + "] " + jsonStr);
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Couldn't get JSON data", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.Key_nama, namea);
                params.put(Config.Key_uname, uname);
                params.put(Config.Key_pass, pass);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }/*

        AddUser ab = new AddUser();
        ab.execute();
    }*/

}


