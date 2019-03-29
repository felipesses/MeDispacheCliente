package com.example.megam.medispachecliente;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Main4Activity extends AppCompatActivity {

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
    }

}


    /*private void setSingleEvent(GridLayout maingrid) { // a ideia aqui é abrir os estabelecimentos por tipo
    for(int i=0; i<maingrid.getChildCount();i++){
        RelativeLayout relativeLayout = (RelativeLayout) maingrid.getChildAt(i);
        final int finalI = i;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Main4Activity.this, "Clicked at index "+ finalI, Toast.LENGTH_SHORT).show();
                if(finalI==0){
                    Intent i = new  Intent(getApplicationContext(), MapsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==1){
                    Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==2){
                    Intent i = new  Intent(getApplicationContext(), Splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==3){
                    Intent i = new  Intent(getApplicationContext(), MapsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });
    }
    }*/



