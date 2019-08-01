package com.example.megam.medispachecliente.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.megam.medispachecliente.Main4Activity;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Splash extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseUser user;
    final  int REQUEST_PERMISSIONS_CODE = 128;
    int ALL_PERMISSIONS_RESULT = 101;
    private static final int REQUEST_CHECK_SETTINGS = 613;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /////////////////////////////////////////////////////////
        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;
        ////////////////////////////////////////////////////////////////////
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean canGetLocation = true;
        ///////////////////////////////////////////////////////////////
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        /////////////////////////////////////////////////////////

        user = FirebaseAuth.getInstance().getCurrentUser();

        //askForLocationChange();
        ////////////////////////////////////////////////////////////
        isOnline();
        ////////////////////////////////////////////////////////////////
        createLocationRequest();
        /////////////////////////////////////////////////////////////////
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(permissionsToRequest.size()>0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);

                if(!isOnline()){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent i = new Intent(Splash.this, Error404.class);
                            startActivity(i);
                            finish();
                        }
                    }, 5000);
                }else if(!isGPS){
                    showSettingsAlert();
                }else{
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (user != null) {
                                alert("Conectando: " + user.getEmail());
                                Intent i = new Intent(getApplicationContext(), Main4Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            } else {
                                startActivity(new Intent(getBaseContext(), Main4Activity.class));
                                finish();
                            }
                        }
                    }, 3000);
                }
            }
        }
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissão não garantida", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isOnline()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(Splash.this, Error404.class);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        }else if(!isGPS){
            showSettingsAlert();
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user != null) {
                        alert("Conectando: " + user.getEmail());
                        Intent i = new Intent(getApplicationContext(), Main4Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    } else {
                        startActivity(new Intent(getBaseContext(), Main4Activity.class));
                        finish();
                    }
                }
            }, 3000);
        }

    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
   /////////////////////////////////////////////////////////////////////////////////////////
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                //recreate();
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
    /////////////////////////////////////////////////////////////////////////////////////////////

    protected void createLocationRequest(){
     mLocationRequest = new LocationRequest();
     mLocationRequest.setInterval(10000);
     mLocationRequest.setFastestInterval(5000);
     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

/*    private void askForLocationChange(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(Splash.this,"Location is already on",Toast.LENGTH_SHORT);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    try{
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(Splash.this, REQUEST_CHECK_SETTINGS);
                    }catch (IntentSender.SendIntentException ignored){

                    }
                }
            }
        });
    }*/

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_CHECK_SETTINGS){
            switch (resultCode){
                case Activity.RESULT_OK:
                Toast.makeText(this, "location is now on", Toast.LENGTH_SHORT).show();
                break;
                case Activity.RESULT_CANCELED:
                Toast.makeText(this, "User didn't allowed to change location settings", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }*/
    ///////////////////////////////////////////////////////////////////
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
    /////////////////////////////////////////////////////////////////////////////////////
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    /////////////////////////////////////////////////////////////////////////////////////
}

