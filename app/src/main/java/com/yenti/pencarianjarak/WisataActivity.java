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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

public class WisataActivity extends AppCompatActivity {
    private ArrayList<String> dataKabupaten = new ArrayList<>();
    private ArrayList<Wisata> dataWisata = new ArrayList<>();

    RecyclerViewWisataAdapter wisataAdapter;
    SpinnerKabupatenAdapter spinnerAdapter;

    private OnRecyclerItemClickListener<Wisata> wisataListener = new OnRecyclerItemClickListener<Wisata>() {
        @Override
        public void onRecyclerItemClickListener(Wisata param) {
            Intent nextPage =new Intent(WisataActivity.this, DetailWisataActivity.class);
            nextPage.putExtra("data", param);
            startActivity(nextPage);
        }
    };

    class SpinnerKabupatenAdapter extends BaseAdapter{
        List<String> kabupatenList;

        SpinnerKabupatenAdapter(ArrayList<String> kabupatenList){
            this.kabupatenList = kabupatenList ;
        }

        @Override
        public int getCount() {
            return kabupatenList.size();
        }

        @Override
        public Object getItem(int position) {
            return kabupatenList.get(position);
        } 

        @Override
        public long getItemId(int position) {
            return position + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, null);
            }

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(kabupatenList.get(position));

            return convertView;
        }
    }

    class getKabupaten extends AsyncTask<String, Void, String> {
        private Context contexts;

        public getKabupaten(Context contexts) {
            this.contexts = contexts;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequest(Config.URL_Kab);
            return s;
        }

        protected void onPostExecute(String result) {
            String jsonStr = result;
            //Toast.makeText(context, jsonStr, Toast.LENGTH_SHORT).show();
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);

                    JSONArray a = object.getJSONArray("server_response");


                    a.length();
                    for (int i = 0; i < a.length(); i++) {
                        dataKabupaten.add(a.getJSONObject(i).getString("nama_kabupaten"));

                    }
                    if (spinnerAdapter != null) {
                        spinnerAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(contexts, "Error Parsing JSON Data", Toast.LENGTH_SHORT).show();
                    Log.e("JSON Parser", "Error Parsing Data[" + e.getMessage() + "] " + jsonStr);
                }

            } else {
                Toast.makeText(contexts, "Couldn't get JSON data", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class getWisata extends AsyncTask<String, Void, String> {

        private Context context;
        private String category_id;
        private String kabupaten_id;
        private ProgressDialog dialog;

        public getWisata(Context context, String category_id, String kabupaten_id) {
            this.context = context;
            this.category_id = category_id;
            this.kabupaten_id = kabupaten_id;
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

            //https://bangkatourism.000webhostapp.com/getEvent.php?bulan=1
            //LOCALHOST/GET
            String urls = Config.URL_Wisata + "?category_id=" + category_id + "&kabupaten_id=" + kabupaten_id;
            String s = rh.sendGetRequest(urls);

            return s;
        }

        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {
                try {
                    //{"server_response":[{"id":"1","nama_event":"HUT Bangka Selatan","waktu":"02 Maret 2018","tempat":"Toboali, Bangka Selatan"}]}
                    /*
                    {
                      "server_response": [
                        {
                          "id": "1",
                          "nama_event": "HUT Bangka Selatan",
                          "waktu": "02 Maret 2018",
                          "tempat": "Toboali, Bangka Selatan"
                        }
                      ]
                    }
                    */
                    JSONObject object = new JSONObject(jsonStr);
                    JSONArray a = object.getJSONArray("server_response");

                    Gson gson = new Gson();
                    String x = a.toString();
                    dataWisata.clear();
                    List<Wisata> temp = gson.fromJson(a.toString(), new TypeToken<List<Wisata>>(){}.getType());
                    dataWisata.addAll(temp);

                    if (wisataAdapter != null) {
                        wisataAdapter.notifyDataSetChanged();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);


        dataKabupaten.add("All");



        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setTitle("Wisata");
        }

        final String wisata_id = getIntent().getStringExtra("wisata_id");


        wisataAdapter = new RecyclerViewWisataAdapter(dataWisata, wisataListener);
        spinnerAdapter = new SpinnerKabupatenAdapter(this.dataKabupaten);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerKabupaten);
        if (spinner != null) {
            spinner.setAdapter(new SpinnerKabupatenAdapter(dataKabupaten));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    new getWisata(WisataActivity.this, wisata_id, String.valueOf(position)).execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

        });
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listWisata);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            recyclerView.setAdapter(wisataAdapter);
        }
        new getKabupaten(this).execute();

    }

}
