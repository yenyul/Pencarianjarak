package com.yenti.pencarianjarak;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yenti.pencarianjarak.adapter.RecyclerViewEventAdapter;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.helper.RequestHandler;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;
import com.yenti.pencarianjarak.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    private ArrayList<Event> dataEvent = new ArrayList<>();

    private OnRecyclerItemClickListener eventListener = new OnRecyclerItemClickListener<Event>() {
        @Override
        public void onRecyclerItemClickListener(Event param) {
            Intent a = new Intent(EventActivity.this, DetailEventActivity.class);
            a.putExtra("data", param);
           startActivity(a);
        }
    };

    class getEvent extends AsyncTask<String, Void, String> {
        private Context context;
        private String bulan;
        private ProgressDialog dialog;


        public getEvent(Context context, String bulan) {
            this.context = context;
            this.bulan = bulan;
        }


        @Override
        protected String doInBackground(String... params) {

            RequestHandler rh = new RequestHandler();
            String urls = Config.URL_Event + "?bulan=" + bulan;
            String s = rh.sendGetRequest(urls);
            return s;
        }

        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);

                    JSONArray a = object.getJSONArray("server_response");
                    Gson gson = new Gson();
                    List<Event> temp = gson.fromJson(a.toString(), new TypeToken<List<Event>>(){}.getType());

                    dataEvent.addAll(temp);

                    if (eventAdapter != null) {
                        eventAdapter.notifyDataSetChanged();
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

    RecyclerViewEventAdapter eventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setTitle("BALADO");
        }


        eventAdapter = new RecyclerViewEventAdapter(dataEvent, eventListener);




        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listEvent);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(eventAdapter);
        }

       new getEvent(this, getIntent().getStringExtra("bulan")).execute();

    }
}


