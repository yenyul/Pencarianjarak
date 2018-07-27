package com.yenti.pencarianjarak;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TravelMode;
import com.yenti.pencarianjarak.algorithm.Haversine;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Wisata data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        data = getIntent().getParcelableExtra("data");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Bangka and move the camera
        //LatLng bangka = new LatLng(-1.621435, 105.7794438);
        LatLng bangka = new LatLng(-1.9846153, 106.129835);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangka, 12f));
        mMap.setTrafficEnabled(true);

        if(data != null){
            //TARGET -> TARGET WISATA?
            List<LatLng> decodedPath = PolyUtil.decode(getIntent().getStringExtra("encodedPath"));
            mMap.addMarker(new MarkerOptions().position(decodedPath.get(0)).title("START POINT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLattitude()), Double.parseDouble(data.getLongtitude()))).title(data.getNama_wisata()));
        }

        mMap.addPolyline(new PolylineOptions().color(R.color.colorAccent).addAll(PolyUtil.decode(getIntent().getStringExtra("encodedPath"))));


        //new OtherTask( new LatLng(-2.318434, 106.186541), new LatLng(Double.parseDouble(data.getLattitude()), Double.parseDouble(data.getLongtitude())), new WeakReference<>(MapsActivity.this), mMap).execute();

    }

    private static class OtherTask extends AsyncTask<Void, Void, DirectionsResult> {
        private LatLng startPoint;
        private LatLng endPoint;

        private WeakReference<MapsActivity> appReference;
        GoogleMap map;


        public OtherTask(LatLng startPoint, LatLng endPoint, WeakReference<MapsActivity> appReference, GoogleMap map) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.appReference = appReference;
            this.map = map;
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
                        .mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(startPoint.latitude, startPoint.longitude))
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

            //JALURNYA DI TERIMA NAMANYA directionResult
            MutableDirectedWeightedGraph<LatLng, Double> graph = MutableDirectedWeightedGraph.create();
            // Let's get the shortest paths of all pairs.
            // Johnson's algorithm is a combination of Bellman Ford's and Dijkstra's algorithm.

            if(directionsResult != null) {
                //JALURNYA DI LOOP PAKE FOR
                for (DirectionsRoute route : directionsResult.routes) {

                    //JALURNYA DIBUAT JADI TITIK2
                    List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
                    //TITIK NYA DISIMPAN
                    graph.insertVertex(startPoint);
                    //TITIKNYA DITARIK GARIS KE TITIK BERIKUTNYA
                    graph.addEdge(startPoint, decodedPath.get(0), Haversine.getInstance().getDistanceFromLatLonInKm(startPoint, decodedPath.get(0)));

                    for(int i = 0; i < decodedPath.size(); i++){

                        //TITIK NYA DISIMPAN
                        graph.insertVertex(decodedPath.get(i));
                        //TAMPILIN TITIKNYA                     LAT LONG NYA                    JUDULNYA                                        ICON MARKERNYA WARNANYA OREN
                        map.addMarker(new MarkerOptions().position(decodedPath.get(i)).title("Marker in Bangka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                        //TITIKNYA DITARIK GARIS KE TITIK BERIKUTNYA KALAU BERIKUTNYA MASI ADA
                        if(i + 1 != decodedPath.size())
                            graph.addEdge(decodedPath.get(i), decodedPath.get(i+1), Haversine.getInstance().getDistanceFromLatLonInKm(decodedPath.get(i), decodedPath.get(i+1)) );
                    }

                    graph.insertVertex(endPoint);
                    graph.addEdge(decodedPath.get(decodedPath.size() - 1), endPoint, Haversine.getInstance().getDistanceFromLatLonInKm(decodedPath.get(decodedPath.size() - 1), endPoint));

                }

                AllPairShortestPath johnson = GoodJohnsonAlgorithm.getInstance();
                AllPairShortestPathResult<LatLng, Double, DirectedWeightedEdge<LatLng, Double>> res = johnson.calc(graph, DoubleNumberSystem.getInstance());

                double distanceAToB = res.getDistance(startPoint, endPoint); // must be 15
                List<LatLng> decodedPath = new ArrayList<>();
                for(DirectedWeightedEdge<LatLng, Double> tes : res.getPath(startPoint, endPoint)){
                    decodedPath.add(tes.from());
                }
                map.addPolyline(new PolylineOptions().color(R.color.colorAccent).addAll(decodedPath));
                Toast.makeText(appReference.get(), String.valueOf(distanceAToB), Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(directionsResult);
        }

    }



}
