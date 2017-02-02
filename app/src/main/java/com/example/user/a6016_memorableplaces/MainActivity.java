package com.example.user.a6016_memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> listOfLocationNames;
    static ArrayAdapter<String> adapterElementow;
    static ArrayList<Address> listOfAddresses;
    private String addNewCoordinate, addNewString;
    private Geocoder defaultGeocode;
    private  ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewString="Press to add new coordinates..";
        LatLng defaultLocalization = new LatLng(50f,50f);
        defaultGeocode = new Geocoder(getApplicationContext(), Locale.getDefault());
            listOfLocationNames = new ArrayList<>();
            listOfAddresses = new ArrayList<>();
            listOfLocationNames.add(("Press to add new coordinates.."));
       adapterElementow = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listOfLocationNames);
        list = (ListView) findViewById(R.id.listView);
            list.setAdapter(adapterElementow);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent it = new Intent(getApplicationContext(),MapsActivity.class);
                    it.putExtra("Position info",i);
                    startActivity(it);
                }else {
                    for(Address a:listOfAddresses){
                        Log.i("Address",a.getAddressLine(0));
                    }

                    Intent it = new Intent(getApplicationContext(),MapsActivity.class);
                    it.putExtra("Position info",i);
                    startActivity(it);
                }

            }
        });


    }

}
