package com.example.megam.medispachecliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.*;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

/*        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");*/

        maingrid = (GridLayout)findViewById(R.id.maingrid);
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
        setSingleEvent(maingrid);
    }



    private void setSingleEvent(GridLayout maingrid) { // a ideia aqui é abrir os estabelecimentos por tipo
    for(int i=0; i<maingrid.getChildCount();i++){
        LinearLayout linearLayout = (LinearLayout)maingrid.getChildAt(i);
        final int finalI = i;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Main4Activity.this, "Clicked at index "+ finalI, Toast.LENGTH_SHORT).show();
                if(finalI==0){
                    Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==1){
                    Intent i = new  Intent(getApplicationContext(), Splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==2){
                    Intent i = new  Intent(getApplicationContext(), Splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else if(finalI==3){
                    Intent i = new  Intent(getApplicationContext(), Splash.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });
    }
    }


}
