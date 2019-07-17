package com.example.megam.medispachecliente;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationChangeListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback

{


    int PLACE_PICKER_REQUEST = 1;
    private Location meulocal;
    Geocoder geocoder;
    String city;
    String state;
    String country;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    private void compra(Location local) {

        try {
            List<Address> addresses = geocoder.getFromLocation(local.getLatitude(), local.getLongitude(), 1);

            if  (addresses != null && addresses.size() > 0){

                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {

        meulocal = location;

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}