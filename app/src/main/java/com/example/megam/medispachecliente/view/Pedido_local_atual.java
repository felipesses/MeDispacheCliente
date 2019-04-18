package com.example.megam.medispachecliente.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.megam.medispachecliente.Main4Activity;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Pedido_local_atual extends AppCompatActivity {

    Button dispachar;
    EditText local_atual;
    TextView local_texto;
    private static final int MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION = 1 ;
    private FusedLocationProviderClient mFusedLocationClient;
    TextView localizacao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_local_atual);
        eventoBotao();
        inicializarcomponentes();

        try {
            checkGps();
        }catch (Exception e){
            createNoGpsDialog();
        }

    }



    public void onResume(){
        super.onResume();

        localizacao = findViewById(R.id.tvLocalizacao);
    }

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
        if (ContextCompat.checkSelfPermission(Pedido_local_atual.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Pedido_local_atual.this,
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

                                ActivityCompat.requestPermissions(Pedido_local_atual.this,
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
                ActivityCompat.requestPermissions(Pedido_local_atual.this,
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



    private void loc_func(Location location){



        try {

            Geocoder geocoder = new Geocoder(this);

            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String cidade = addresses.get(0).getLocality();
            String pais = addresses.get(0).getCountryName();
            String estado = addresses.get(0).getAdminArea();
            String n1 = addresses.get(0).getSubAdminArea();
            String bairro = addresses.get(0).getSubLocality();
            String Rua = addresses.get(0).getThoroughfare();
            String casa_numero = addresses.get(0).getSubThoroughfare();



            localizacao.setText("País: "+pais+"; Cidade: "+cidade+"; Estado: "+estado+"; Bairro: "+bairro+"; Rua: "+Rua+"; Numero da casa: "+casa_numero);




        } catch (IOException f) {
            f.printStackTrace();
        }

    }


    private void eventoBotao(){
                Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
    }

    @SuppressLint("WrongViewCast")
    private void inicializarcomponentes(){
        local_texto = findViewById(R.id.text_local);
        dispachar = findViewById(R.id.btn_dispache);
        local_atual = findViewById(R.id.endereco_pessoa_agora);

    }
}