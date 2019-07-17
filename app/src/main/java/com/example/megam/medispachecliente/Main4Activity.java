package com.example.megam.medispachecliente;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.design.*;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.fragments.UsersFragment;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.Splash;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class Main4Activity extends AppCompatActivity implements LocationListener {
    FirebaseUser user;
    FirebaseAuth auth;
    TextView username;
    DatabaseReference reference;
    ImageView restaurante, agua, lanchonete, bebidas;
    private LatLng currentLocationLatLong;
    String cidade = "cazaquistao";
    Location locale;
    Address meuEndereco;
    String MinhaCidadeAtual = "cazaquistao";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;

        ////////////////////////////////////////////////////////////////////////////////////////////////
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;

        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        ///////////////////////////////////////////////////////////////////////////////////////////////

        if (!isGPS || !isNetwork) {
            showSettingsAlert();
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            //Checks if FINE LOCATION and COARSE Location were granted
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
             }
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        lm.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


                locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    try {
                        locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                        if (addresses.size() != 0) {
                            if (addresses.size() > 0) {
                                meuEndereco = addresses.get(0);
                                cidade = meuEndereco.getSubAdminArea();
                                Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            while(cidade.equalsIgnoreCase("cazaquistao")) {
                try {
                    locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                    if (addresses.size() != 0) {
                        if (addresses.size() > 0) {
                            meuEndereco = addresses.get(0);
                            cidade = meuEndereco.getSubAdminArea();
                            Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
            }
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                if (addresses.size() != 0) {
                    if (addresses.size() > 0) {
                        meuEndereco = addresses.get(0);
                        cidade = meuEndereco.getSubAdminArea();
                        Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(cidade.equalsIgnoreCase("cazaquistao")) {
                try {
                    locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                    if (addresses.size() != 0) {
                        if (addresses.size() > 0) {
                            meuEndereco = addresses.get(0);
                            cidade = meuEndereco.getSubAdminArea();
                            Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if(!isGPS && !isNetwork){
            MinhaCidadeAtual = "cazaquistao";
            Toast.makeText(this, MinhaCidadeAtual, Toast.LENGTH_SHORT).show();
        }else{
            MinhaCidadeAtual = cidade;
            Toast.makeText(this, MinhaCidadeAtual, Toast.LENGTH_SHORT).show();
        }



        restaurante = findViewById(R.id.restaurante);
        agua = findViewById(R.id.agua);
        lanchonete = findViewById(R.id.lanchonete);
        bebidas = findViewById(R.id.bebidas);

        username = findViewById(R.id.nomeuser);
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        if (user == null) {
            username.setText("USUÁRI@");
        } else {
            reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Usuarios u = dataSnapshot.getValue(Usuarios.class);
                        username.setText(u.getName());
                        username.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        restaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main4Activity.this, Main5Activity.class);
                i.putExtra("cidade", MinhaCidadeAtual);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        agua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main4Activity.this, Main7Activity.class);
                i.putExtra("cidade", MinhaCidadeAtual);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main4Activity.this, Main6Activity.class);
                i.putExtra("cidade", MinhaCidadeAtual);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        lanchonete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main4Activity.this, Main8Activity.class);
                i.putExtra("cidade", MinhaCidadeAtual);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        try{
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            cidade = addresses.get(0).getLocality();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Address buscarEndereco(double lat, double lon) throws IOException{
        Geocoder geocoder;
        Address address = null;
        List<Address> addressList;
        geocoder = new Geocoder(getApplicationContext());
        addressList = geocoder.getFromLocation(lat,lon,1);
        if(addressList.size() > 0){
            address = addressList.get(0);
        }
        return address;
    }

    public ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                recreate();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        alertDialog.show();
    }


    private void startGettingLocations() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }
        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }
        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
        } else {
            Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
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

