package com.example.megam.medispachecliente;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.fragments.ChatsFragment;
import com.example.megam.medispachecliente.fragments.PedidosFragment;
import com.example.megam.medispachecliente.fragments.Produto_fragment;
import com.example.megam.medispachecliente.fragments.UsersFragment;
import com.example.megam.medispachecliente.model.Chat;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;




public class MainActivity extends AppCompatActivity  {


/////////////////////////////////////////////////////////////////////////////////

    private static final int MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION = 1 ;
    private FusedLocationProviderClient mFusedLocationClient;

    CircleImageView profilie_image;
    TextView username;
    FirebaseUser user;
    FirebaseAuth auth;
    String imageurl;
    DatabaseReference reference;
    Usuarios u;
    TextView user_location;
    boolean foi=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


///////////////////////////////////////////////////////////////////////////////////

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);




        fetchLocation();







        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");

        profilie_image = findViewById(R.id.imagemperfil);
        username = findViewById(R.id.nomeuser);
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        if(user==null){
            final TabLayout tabLayout = findViewById(R.id.tab_layout);
            ViewPager viewPager = findViewById(R.id.view_pager);
            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
            username.setVisibility(View.GONE);
            viewPageAdapter.addFragment(new UsersFragment(), "Estabelecimentos");
            // viewPageAdapter.addFragment(new PedidosFragment(), "Pedidos");
            viewPager.setAdapter(viewPageAdapter);
            tabLayout.setupWithViewPager(viewPager);
            class ViewPageAdapter extends FragmentStatePagerAdapter {
                private ArrayList<Fragment> fragments;
                private ArrayList<String> titles;
                ViewPageAdapter(FragmentManager fm){
                    super(fm);
                    this.fragments = new ArrayList<>();
                    this.titles = new ArrayList<>();
                }
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }
                @Override
                public int getCount() {
                    return fragments.size();
                }
                public void addFragment(Fragment fragment, String title){
                    fragments.add(fragment);
                    titles.add(title);
                }
                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return titles.get(position);
                }
            }
        }else{
            reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
            // eventoclick();
            reference.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        u = dataSnapshot.getValue(Usuarios.class);

                        username.setText(u.getName());
                        if(foi ==true){
                            status("online");
                            foi =false;
                        }
                        if (u.getImageUrl() == null) {
                            profilie_image.setImageResource(R.drawable.ic_launcher_background);
                        } else {
                            Glide.with(getApplicationContext()).load(u.getImageUrl()).into(profilie_image);

                        }
                    }else{
                        deslogars2();
                    }}

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            final TabLayout tabLayout = findViewById(R.id.tab_layout);
            ViewPager viewPager = findViewById(R.id.view_pager);
            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
            viewPageAdapter.addFragment(new UsersFragment(), "Estabelecimentos");
            viewPageAdapter.addFragment(new PedidosFragment(), "Pedidos");

            viewPager.setAdapter(viewPageAdapter);
            tabLayout.setupWithViewPager(viewPager);
            reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int unread=0;
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Chat chat = snapshot.getValue(Chat.class);
                            if(chat.getReceiver().equals(user.getUid()) && !chat.isIsseen()){
                                unread++;
                            }
                        }
                        if(unread == 0){
                            tabLayout.getTabAt(0).setText("Estabelecimentos");

                        }else{
                            tabLayout.getTabAt(0).setText("("+unread+") Estabelecimentos");
                        }

                    }}

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        profilie_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mconta();
            }
        });
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void fetchLocation() {


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
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

                                ActivityCompat.requestPermissions(MainActivity.this,
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
                ActivityCompat.requestPermissions(MainActivity.this,
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


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==  MY_PERMISSIONS_REQUEST_ACESS_COARSE_LOCATION )
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {

            }
        }
    }

    /*private void eventoclick() {
        de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                deslogars();
            }
        });

    }*/



    private void deslogars() {
        Conexao.logOut();
        Intent i = new Intent(getApplicationContext(), login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
    private void deslogars2() {
        Toast.makeText(getApplicationContext(), "Esta conta não é de uma empresa", Toast.LENGTH_LONG);
        Conexao.logOut();
        Intent i = new Intent(getApplicationContext(), login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout){
            deslogars();
        }
        return super.onOptionsItemSelected(item);
    }

    private void mconta() {
        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }




    class ViewPageAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPageAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    public void status(String status){
        if(u!=null){
            reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            reference.updateChildren(hashMap);
        }}

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ss = new Intent(this, Main4Activity.class);
        ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ss);
        finish();
    }
}
