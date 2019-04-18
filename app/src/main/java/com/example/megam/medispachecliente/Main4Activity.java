package com.example.megam.medispachecliente;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.Splash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Main4Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION = 1 ;
    private FusedLocationProviderClient mFusedLocationClient;

    FirebaseUser user;
    FirebaseAuth auth;
    TextView username;
    GridLayout maingrid;
    DatabaseReference reference;
    ImageView restaurante, agua, lanchonete, bebidas;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();


        restaurante = findViewById(R.id.restaurante);
        agua = findViewById(R.id.agua);
        lanchonete = findViewById(R.id.lanchonete);
        bebidas = findViewById(R.id.bebidas);

/*       Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");*/

        //maingrid = (GridLayout)findViewById(R.id.maingrid);
        username = findViewById(R.id.nomeuser);
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        if(user == null){
            username.setVisibility(View.GONE);
        }else {
            reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
            reference.addValueEventListener( new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
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
        //setSingleEvent(maingrid);

        restaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        agua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), MapsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), Splash.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        lanchonete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), Splash.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        try {
            checkGps();
        }catch (Exception e){
            createNoGpsDialog();
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createNoGpsDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog mNoGpsDialog = builder.setMessage("Por favor ative o seu gps").
                setPositiveButton("Ativar", dialogClickListener).
                create();

        mNoGpsDialog.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fetchLocation() {
        if (ContextCompat.checkSelfPermission(Main4Activity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main4Activity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("É necessário a permissão da sua localização")
                        .setMessage("Você precisa desta permissão para acessar o conteúdo")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(Main4Activity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Main4Activity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==  MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION )
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {

            }
        }
    }
    public void checkGps() throws Exception {
        LocationManager manager; manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            throw new Exception("gps off"); } }



}

