package com.example.megam.medispachecliente;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.Error404;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main4Activity extends AppCompatActivity implements LocationListener {
    FirebaseUser user;
    FirebaseAuth auth;
    TextView username;
    DatabaseReference reference;
    ImageView restaurante, agua, lanchonete, bebidas;
    String cidade = "cazaquistao";
    Location locale;
    Address meuEndereco;
    String MinhaCidadeAtual = "cazaquistao";
    Criteria criteria;
    public String bestProvider;


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
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();


        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        ///////////////////////////////////////////////////////////////////////////////////////////////
        isOnline();
        //////////////////////////////////////////////////////////////////////////////////////////////
       /* if(!isOnline()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(Main4Activity.this, Error404.class);
                    startActivity(i);
                    finish();
                }
            }, 5000);
                }else if(!isGPS){ // REAVALIAR AQUI
                 MinhaCidadeAtual = "cazaquistao";
                 Toast.makeText(this, MinhaCidadeAtual, Toast.LENGTH_SHORT).show();
                }else{*/
             ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permissão não garantida", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(isOnline()){
                        criteria = new Criteria();
                        bestProvider = String.valueOf(lm.getBestProvider(criteria,true));

                        locale = lm.getLastKnownLocation(bestProvider);
                        if(locale!=null){
                            do{
                                try {
                                    addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                                    if (addresses.size() != 0) {
                                        meuEndereco = addresses.get(0);
                                        cidade = meuEndereco.getSubAdminArea();
                                        Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                lm.requestLocationUpdates(bestProvider,MIN_TIME_BW_UPDATES,10,this);
                            }while (cidade.equalsIgnoreCase("cazaquistao"));
                        }else{
                            lm.requestLocationUpdates(bestProvider,MIN_TIME_BW_UPDATES,10,this);
                        }
                    }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    /*lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);*/

                    /*while(cidade.equalsIgnoreCase("cazaquistao")) {
                        try {
                            locale = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            addresses = geocoder.getFromLocation(locale.getLatitude(), locale.getLongitude(), 1);
                            if (addresses.size() != 0) {
                                meuEndereco = addresses.get(0);
                                cidade = meuEndereco.getSubAdminArea();
                                Toast.makeText(this, cidade, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "TUDO ERRADO", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                //}


            MinhaCidadeAtual = cidade;
            //Toast.makeText(this, MinhaCidadeAtual, Toast.LENGTH_SHORT).show();

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

    /////////////////////////////////////////////////////////////////////////////////////
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    /////////////////////////////////////////////////////////////////////////////////////

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
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

