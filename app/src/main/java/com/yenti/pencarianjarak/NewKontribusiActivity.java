package com.yenti.pencarianjarak;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yenti.pencarianjarak.helper.Config;
import com.yenti.pencarianjarak.helper.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NewKontribusiActivity extends AppCompatActivity {
    private ArrayList<String> dataKabupaten = new ArrayList<>();
    private ArrayList<String> dataKategori = new ArrayList<>();

    private MapView mMapView;
    private GoogleMap mMap;
    private Marker tapMarker;
    private LatLng location;
    private EditText mEditTextAddress;
    private EditText nama_wisata;
    private EditText no_telp;
    private EditText keterangan;
    private Button Save;
    SpinnerAdapter spinnerkabuAdapter;
    SpinnerAdapter spinnerKategoriAdapter;

    Spinner spinnerKategori;
    Spinner spinnerKabupaten;

    class SpinnerAdapter extends BaseAdapter {
        List<String> listData;

        SpinnerAdapter(ArrayList<String> listData){
            this.listData = listData;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
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
            text.setText(listData.get(position));

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
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);

                    JSONArray a = object.getJSONArray("server_response");


                    a.length();
                    for (int i = 0; i < a.length(); i++) {
                        dataKabupaten.add(a.getJSONObject(i).getString("nama_kabupaten"));

                    }
                    if (spinnerkabuAdapter != null) {
                        spinnerkabuAdapter.notifyDataSetChanged();
                        spinnerKabupaten.setClickable(true);
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

    class getKategori extends AsyncTask<String, Void, String> {
        private Context contexts;

        public getKategori(Context contexts) {
            this.contexts = contexts;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequest(Config.URL_Cat);
            return s;
        }

        protected void onPostExecute(String result) {
            String jsonStr = result;
            if (jsonStr != null) {
                try {
                    JSONObject object = new JSONObject(jsonStr);

                    JSONArray a = object.getJSONArray("server_response");


                    a.length();
                    for (int i = 0; i < a.length(); i++) {
                        dataKategori.add(a.getJSONObject(i).getString("nama_category"));
                    }
                    if (spinnerKategoriAdapter != null) {
                        spinnerKategoriAdapter.notifyDataSetChanged();
                        spinnerKategori.setClickable(true);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newkontribusi);

        mMapView = (MapView) findViewById(R.id.contributeMapView);
        mMapView.onCreate(savedInstanceState);

        configureMaps();
        mEditTextAddress = (EditText) findViewById(R.id.editTextAlamat);
        nama_wisata = (EditText) findViewById(R.id.editTextNama);
        no_telp = (EditText) findViewById(R.id.editTextTelp);
        keterangan = (EditText) findViewById(R.id.editTextKeterangan);

        dataKabupaten.add("-");
        dataKategori.add("-");
        spinnerkabuAdapter = new SpinnerAdapter(this.dataKabupaten);
        spinnerKabupaten = (Spinner) findViewById(R.id.spinnerkabu);
        spinnerKabupaten.setAdapter(spinnerkabuAdapter);

        spinnerKategoriAdapter = new SpinnerAdapter(this.dataKategori);
        spinnerKategori = (Spinner) findViewById(R.id.spinnercate);
        spinnerKategori.setAdapter(spinnerKategoriAdapter);

        Save = (Button) findViewById(R.id.buttonSimpan);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.buttonSimpan) {
                    // String tname = name.getText().toString();
                    // String tuname = username.getText().toString();
                    //String tpass = password.getText().toString();
                    addWisata();
                    //Tulisan yang keluar nantinya
                    //Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_SHORT).show();
                    //LoginActivity login = new LoginActivity();
                    //new Sign(RegisterActivity.this).execute(tname,tuname,tpass);
                }
            }
        });
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.contributeToggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMapView.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        new getKabupaten(this).execute();
        new getKategori(this).execute();
    }





    /*public void onClick(View v) {
        if (v.getId() == R.id.buttonSimpan) {
            // String tname = name.getText().toString();
            // String tuname = username.getText().toString();
            //String tpass = password.getText().toString();
            addWisata();
            //Tulisan yang keluar nantinya
            //Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_SHORT).show();
            //LoginActivity login = new LoginActivity();
            //new Sign(RegisterActivity.this).execute(tname,tuname,tpass);
        }
    }*/

    private void addWisata() {

        final String namawisata = nama_wisata.getText().toString().trim();
        final String notelp = no_telp.getText().toString().trim();
        final String ket = keterangan.getText().toString().trim();


        class AddWisata extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(NewKontribusiActivity.this, "Adding...", "Wait...", false, false);
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
                            Toast.makeText(NewKontribusiActivity.this, s, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewKontribusiActivity.this, MenuActivity.class));

                        } else if (query_result.equals("FAILURE")) {
                            loading.dismiss();
                            Toast.makeText(NewKontribusiActivity.this, s, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(NewKontribusiActivity.this, "Error Parsing JSON Data", Toast.LENGTH_SHORT).show();
                        Log.e("JSON Parser", "Error Parsing Data[" + e.getMessage() + "] " + jsonStr);
                    }

                } else {
                    Toast.makeText(NewKontribusiActivity.this, "Couldn't get JSON data", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.Key_nama, namawisata);
                params.put(Config.Key_telp, notelp);
                params.put(Config.Key_ket, ket);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_AddWisata, params);
                return res;
            }
        }

        AddWisata ab = new AddWisata();
        ab.execute();
    }

    private void configureMaps() {
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(NewKontribusiActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewKontribusiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Here, thisActivity is the current activity
                    //Toast.makeText(getContext(), "Mohon mengaktifkan", Toast.LENGTH_SHORT).show();
                    return;
                }
                mMap = googleMap;
                googleMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if(tapMarker != null){
                            tapMarker.remove();
                        }
                        tapMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                        new getAddress(latLng, NewKontribusiActivity.this, new OnMapsAction() {
                            @Override
                            public void onMapsAction(String address, LatLng latLng) {
                                NewKontribusiActivity.this.location = latLng;
                                mEditTextAddress.setText(address);

                            }
                        }).execute();

                        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(mMap.getCameraPosition().zoom).target(latLng).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
                //LatLng customPoint = new LatLng(-6.230233, 106.811603);
                LatLng customPoint = new LatLng(-2.1159944, 106.0855072);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(customPoint).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
    }

    interface OnMapsAction {
        void onMapsAction(String address, LatLng latLng);
    }

    private static class getAddress extends AsyncTask<Void,Void,List<Address>> {
        LatLng latLng;
        private Geocoder geocoder;
        private OnMapsAction listenerOnMapsAction;

        private getAddress(LatLng latLng, Context context, OnMapsAction listener) {
            this.latLng = latLng;
            geocoder = new Geocoder(context, Locale.getDefault());
            listenerOnMapsAction = listener;
        }

        @Override
        protected List<Address> doInBackground(Void... voids) {
            List<Address> addresses = null; //1 num of possible location returned
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
        @Override
        protected void onPostExecute(List<Address> addresses) {
            String address = "";
            if(addresses != null) {
                if(addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                }else{
                    address = "Mohon Coba lagi";
                }
            }else {
                address = "-";
            }
            listenerOnMapsAction.onMapsAction(address,latLng);
            super.onPostExecute(addresses);
        }
    }

}
