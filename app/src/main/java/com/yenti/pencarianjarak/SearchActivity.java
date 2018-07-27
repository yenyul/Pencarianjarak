package com.yenti.pencarianjarak;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yenti.pencarianjarak.adapter.RecyclerViewWisataAdapter;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.helper.RequestHandler;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;
import com.yenti.pencarianjarak.model.Wisata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerViewWisataAdapter wisataAdapter;
    private ArrayList<Wisata> dataWisata = new ArrayList<>();
    private OnRecyclerItemClickListener<Wisata> wisataListener = new OnRecyclerItemClickListener<Wisata>() {
        @Override
        public void onRecyclerItemClickListener(Wisata param) {
            Intent nextPage =new Intent(SearchActivity.this, DetailWisataActivity.class);
            nextPage.putExtra("data", param);
            startActivity(nextPage);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        wisataAdapter = new RecyclerViewWisataAdapter(dataWisata, wisataListener);


        SearchView a = (SearchView) findViewById(R.id.searchView);
        a.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                wisataAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    wisataAdapter.filter("");
                }
                return false;
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listView);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(wisataAdapter);
        }
        new getWisata(this).execute();
    }


    class getWisata extends AsyncTask<String, Void, String> {
        private Context context;
        private ProgressDialog dialog;

        public getWisata(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestHandler rh = new RequestHandler();
            String urls = Config.URL_Wisata + "?category_id=0&kabupaten_id=0";
            String s = rh.sendGetRequest(urls);

            return s;
        }

        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);
                    JSONArray a = object.getJSONArray("server_response");

                    Gson gson = new Gson();
                    String x = a.toString();
                    dataWisata.clear();
                    List<Wisata> temp = gson.fromJson(a.toString(), new TypeToken<List<Wisata>>(){}.getType());
                    dataWisata.addAll(temp);

                    if (wisataAdapter != null) {
                        wisataAdapter.notifyDataSetChanged();
                        wisataAdapter.invalidateSearchDataSet();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error Parsing JSON Data", Toast.LENGTH_SHORT).show();
                    Log.e("JSON Parser", "Error Parsing Data[" + e.getMessage() + "] " + jsonStr);
                }

            } else {
                Toast.makeText(context, "Couldn't get JSON data", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        }
    }

}
