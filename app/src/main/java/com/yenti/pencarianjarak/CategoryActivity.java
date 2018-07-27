package com.yenti.pencarianjarak;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.yenti.pencarianjarak.adapter.RecyclerViewCategoryAdapter;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.helper.RequestHandler;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ArrayList<String> dataCategory = new ArrayList<>();
    private OnRecyclerItemClickListener<String> categoryListener = new OnRecyclerItemClickListener<String>() {
        @Override
        public void onRecyclerItemClickListener(String param) {
            Intent a = new Intent(CategoryActivity.this, WisataActivity.class);
            a.putExtra("wisata_id", param);
            startActivity(a);
        }
    };

    public class getCategory extends AsyncTask<String, Void, String> {
        private Context context;

        public getCategory(Context context) {this.context = context;}

        @Override
        protected String doInBackground(String... params) {

            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequest(Config.URL_Cate);
            return s;

           // return new ConnectorHelper().get("https://bangkatourism.000webhostapp.com/getCategory.php");
        }

        protected void onPostExecute(String result) {

            String jsonStr = result;

            //Toast.makeText(context, jsonStr, Toast.LENGTH_SHORT).show();
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);

                    JSONArray a = object.getJSONArray("server_response");
                    //JSONArray a = new JSONArray(jsonStr);
                    a.length();
                    for(int i = 0; i < a.length(); i++) {
                        dataCategory.add(a.getJSONObject(i).getString("nama_category"));
                    }
                    if(adapter != null){
                        adapter.notifyDataSetChanged();
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



    RecyclerViewCategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setTitle("BALADO");
        }

        adapter = new RecyclerViewCategoryAdapter(dataCategory, categoryListener);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(adapter);
        }

        new getCategory(this).execute();

    }
}
