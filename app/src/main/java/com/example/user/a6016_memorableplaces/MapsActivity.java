package com.example.user.a6016_memorableplaces;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.location.Address;
//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener {

    private GoogleMap mMap;
    private Intent i;
    private int position;
    private Geocoder koder;
    private LocationManager manager;
    private String provider;
    private static int MY_REQUESTED_PERMISSIONS;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = manager.getBestProvider(new Criteria(), false);
        i = getIntent();
        position = i.getIntExtra("Position info", 0);
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
        if (position == 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40f, 40f), 10));
            mMap.setOnMapLongClickListener(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUESTED_PERMISSIONS);
                return;
            }
            manager.requestLocationUpdates(provider, 1, 1, this);
        } else {
            Log.i("Position", String.valueOf(position));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.listOfAddresses.get(position - 1).getLatitude(), MainActivity.listOfAddresses.get(position - 1).getLongitude()), 10f));
            mMap.setOnMapLongClickListener(this);
            Log.i("Position", String.valueOf(position));
            LatLng adr = new LatLng(MainActivity.listOfAddresses.get(position - 1).getLatitude(), MainActivity.listOfAddresses.get(position - 1).getLongitude());

            mMap.addMarker(new MarkerOptions()
                    .position(adr)
                    .title(MainActivity.listOfLocationNames.get(position))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUESTED_PERMISSIONS);
                }
                manager.removeUpdates(this);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        koder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<android.location.Address> lista = new ArrayList<>();
        String locationInfo;
        try {
            lista = koder.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lista.size() != 0) {
            locationInfo = lista.get(0).getAddressLine(0);
        } else {
            locationInfo = new Date().toString();
        }
        MainActivity.listOfLocationNames.add(locationInfo);
        MainActivity.adapterElementow.notifyDataSetChanged();
        MainActivity.listOfAddresses.add(lista.get(0));
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(locationInfo)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }

    @Override
    public void onLocationChanged(Location userLocation) {
        Log.i("Location changed",userLocation.toString());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUESTED_PERMISSIONS);
            return;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),10f));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
