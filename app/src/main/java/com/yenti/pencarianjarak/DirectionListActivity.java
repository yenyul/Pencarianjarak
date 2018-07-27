package com.yenti.pencarianjarak;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;
import com.yenti.pencarianjarak.adapter.RecyclerViewListDirectionAdapter;
import com.yenti.pencarianjarak.algorithm.Haversine;
import com.yenti.pencarianjarak.listener.OnRecyclerItemClickListener;
import com.yenti.pencarianjarak.model.Wisata;

import org.joda.time.DateTime;
import org.psjava.algo.graph.shortestpath.AllPairShortestPath;
import org.psjava.algo.graph.shortestpath.AllPairShortestPathResult;
import org.psjava.ds.graph.DirectedWeightedEdge;
import org.psjava.ds.graph.MutableDirectedWeightedGraph;
import org.psjava.ds.numbersystrem.DoubleNumberSystem;
import org.psjava.goods.GoodJohnsonAlgorithm;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DirectionListActivity extends AppCompatActivity implements OnRecyclerItemClickListener<DirectionsRoute> {

    private RecyclerView carRecyclerView;
    private ProgressBar progressBar;
    private RecyclerViewListDirectionAdapter listDirectionAdapter;
    private List<DirectionsRoute> directionsResult = new ArrayList<>();
    private String encodedPath = "";
    private Wisata dataWisata;
    private ProgressDialog dialog;

    private AsyncTask<Void, Void, DirectionsResult> directionTask;
    // private dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_list);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("List Direction");
        }

        listDirectionAdapter = new RecyclerViewListDirectionAdapter(directionsResult, this);

        carRecyclerView = (RecyclerView) findViewById(R.id.carDirectionListRecyclerView);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        carRecyclerView.setAdapter(listDirectionAdapter);



        if(getIntent().hasExtra("data")) {
            dataWisata = getIntent().getParcelableExtra("data");
            //new OtherTask( new LatLng(-1.621435, 105.7794438), new LatLng(Double.parseDouble(data.getLattitude()), Double.parseDouble(data.getLongtitude())), new WeakReference<>(this)).execute();
        }

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait, getting your location...");
        dialog.setCancelable(false);
        dialog.show();

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRecyclerItemClickListener(DirectionsRoute param) {
        Intent nextPage = new Intent(DirectionListActivity.this, MapsActivity.class);
        nextPage.putExtra("data", getIntent().getParcelableExtra("data"));
        nextPage.putExtra("encodedPath", param.overviewPolyline.getEncodedPath());
        startActivity(nextPage);
    }


    private static class OtherTask extends AsyncTask<Void, Void, DirectionsResult> {
        private LatLng startPoint;
        private LatLng endPoint;
        private WeakReference<DirectionListActivity> appReference;
        private AsyncTask<Void, Void, DirectionsResult> directionTask;

        OtherTask(LatLng startPoint, LatLng endPoint, WeakReference<DirectionListActivity> appReference) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.appReference = appReference;
        }

        GeoApiContext getGeoContext() {
            GeoApiContext geoApiContext = new GeoApiContext();
            return geoApiContext.setQueryRateLimit(20)
                    .setApiKey(appReference.get().getString(R.string.google_maps_key))
                    .setConnectTimeout(10, TimeUnit.SECONDS)
                    .setReadTimeout(10, TimeUnit.SECONDS)
                    .setWriteTimeout(10, TimeUnit.SECONDS);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appReference.get().dialog.setMessage("Now we counting the distance...");

        }

        @Override
        protected DirectionsResult doInBackground(Void... voids) {
            DirectionsResult result = null;
            DateTime now = new DateTime();
            try {
                //MINTA SAMA GOOGLE JALURNYA
                result = DirectionsApi.newRequest(getGeoContext())
                        .avoid(DirectionsApi.RouteRestriction.TOLLS)
                        .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                        .avoid(DirectionsApi.RouteRestriction.FERRIES)
                        .alternatives(true)
                        //.mode(TravelMode.DRIVING).mode(TravelMode.WALKING)
                        .mode(TravelMode.DRIVING)
                        .origin(new com.google.maps.model.LatLng(startPoint.latitude, startPoint.longitude))
                        .destination(new com.google.maps.model.LatLng(endPoint.latitude, endPoint.longitude)).departureTime(now)
                        .await();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(DirectionsResult directionsResult) {
         /*   DirectionsRoute a = new DirectionsRoute();
            a.legs = new DirectionsLeg[]{new DirectionsLeg()};
            a.legs[0].distance = new Distance();
            a.legs[0].duration = new Duration();
            List<com.google.maps.model.LatLng> listDirection = new ArrayList<>();
            for(DirectionsStep step : directionsResult.routes[0].legs[0].steps){
                listDirection.add(step.startLocation);
                listDirection.add(step.endLocation);
                a.legs[0].distance.inMeters += Haversine.getInstance().getDistanceFromLatLonInKm(new LatLng(step.startLocation.lat, step.startLocation.lng), new LatLng(step.endLocation.lat, step.endLocation.lng)) * 1000 ;
                a.legs[0].duration.inSeconds += step.duration.inSeconds;
            }
            a.legs[0].distance.humanReadable = String.valueOf(a.legs[0].distance.inMeters / 1000) + " km";
            double axd = (a.legs[0].distance.inMeters * 60 / 1000 / 55);
            a.legs[0].duration.humanReadable =  String.valueOf(axd) + " mins";
            a.overviewPolyline = new EncodedPolyline(listDirection);
            appReference.get().directionsResult.add(a);*/


            appReference.get().directionsResult.addAll(new ArrayList<>(Arrays.asList(directionsResult.routes)));

            for (int i = 0; i < appReference.get().directionsResult.size(); i++) {
                double axds = (appReference.get().directionsResult.get(i).legs[0].distance.inMeters * 60 / 1000 / 45);
                appReference.get().directionsResult.get(i).legs[0].durationInTraffic.humanReadable = String.valueOf(axds) + " mins";
            }

            appReference.get().listDirectionAdapter.notifyDataSetChanged();


            //JALURNYA DI TERIMA NAMANYA directionResult
            MutableDirectedWeightedGraph<LatLng, Double> graph = MutableDirectedWeightedGraph.create();
            // Let's get the shortest paths of all pairs.
            // Johnson's algorithm is a combination of Bellman Ford's and Dijkstra's algorithm.

            if (directionsResult != null) {

                //jalur di looping
                if (directionsResult.routes.length > 0) {
                    for (DirectionsRoute route : directionsResult.routes) {
                        String time = route.legs[0].durationInTraffic.humanReadable;
/*

                    LatLng firstLegPoint = PolyUtil.decode(route.legs[0].steps[0].polyline.getEncodedPath()).get(0);
                    List<LatLng> lastLegPoints = PolyUtil.decode(route.legs[0].steps[route.legs[0].steps.length - 1].polyline.getEncodedPath());
                    LatLng lastLegPoint = lastLegPoints.get(lastLegPoints.size() -1);
                    graph.insertVertex(startPoint);
                    graph.addEdge(startPoint, firstLegPoint, Haversine.getInstance().getDistanceFromLatLonInKm(startPoint, firstLegPoint));
                    List<LatLng> decodedPath = null;

                    for(int i = 0; i < route.legs[0].steps.length; i++){
                        decodedPath = PolyUtil.decode(route.legs[0].steps[i].polyline.getEncodedPath());
                        for(int j = 0; j < decodedPath.size(); j++){

                            //TITIK NYA DISIMPAN
                            graph.insertVertex(decodedPath.get(j));

                            //TITIKNYA DITARIK GARIS KE TITIK BERIKUTNYA KALAU BERIKUTNYA MASI ADA
                            if(j + 1 != decodedPath.size())
                                graph.addEdge(decodedPath.get(j), decodedPath.get(j+1), Haversine.getInstance().getDistanceFromLatLonInKm(decodedPath.get(j), decodedPath.get(j+1)) );
                        }
                    }
                    graph.insertVertex(endPoint);
                    graph.addEdge(lastLegPoint, endPoint, Haversine.getInstance().getDistanceFromLatLonInKm(lastLegPoint, endPoint));
*/


                        List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
                        graph.insertVertex(startPoint);
                        graph.addEdge(startPoint, decodedPath.get(0), Haversine.getInstance().getDistanceFromLatLonInKm(startPoint, decodedPath.get(0)));

                        for (int i = 0; i < decodedPath.size(); i++) {

                            //TITIK NYA DISIMPAN
                            graph.insertVertex(decodedPath.get(i));

                            //Titik ditarik garis ke titik berikutnya kalo masih ada
                            if (i + 1 != decodedPath.size())
                                graph.addEdge(decodedPath.get(i), decodedPath.get(i + 1), Haversine.getInstance().getDistanceFromLatLonInKm(decodedPath.get(i), decodedPath.get(i + 1)));
                        }

                        graph.insertVertex(endPoint);
                        graph.addEdge(decodedPath.get(decodedPath.size() - 1), endPoint, Haversine.getInstance().getDistanceFromLatLonInKm(decodedPath.get(decodedPath.size() - 1), endPoint));


                    }
                    //ctrl+B
                    AllPairShortestPath johnson = GoodJohnsonAlgorithm.getInstance();
                    AllPairShortestPathResult<LatLng, Double, DirectedWeightedEdge<LatLng, Double>> res = johnson.calc(graph, DoubleNumberSystem.getInstance());

                    //double distanceAToB = res.getDistance(startPoint, endPoint); // must be 15
                    List<LatLng> decodedPath = new ArrayList<>();
                    for (DirectedWeightedEdge<LatLng, Double> tes : res.getPath(startPoint, endPoint)) {
                        decodedPath.add(tes.from());
                    }
                    appReference.get().encodedPath = PolyUtil.encode(decodedPath);
                }
                super.onPostExecute(directionsResult);
                appReference.get().dialog.dismiss();
            }

        }


    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();
            if(directionTask == null) {
                directionTask = new OtherTask(new LatLng(loc.getLatitude(), loc.getLongitude()), new LatLng(Double.parseDouble(dataWisata.getLattitude()), Double.parseDouble(dataWisata.getLongtitude())), new WeakReference<>(DirectionListActivity.this));
                directionTask.execute();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
