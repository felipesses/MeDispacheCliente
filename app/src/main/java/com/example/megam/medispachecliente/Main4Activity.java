package com.example.megam.medispachecliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.*;
import android.support.v7.widget.CardView;
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


public class Main4Activity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth auth;
    TextView username;
    GridLayout maingrid;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        maingrid = (GridLayout)findViewById(R.id.maingrid);
        username = findViewById(R.id.nomeuser);
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        Usuarios u = new Usuarios();



    /*    u.getName();
        username.setText(u.getName());*/


      setSingleEvent(maingrid);





        if(user == null){
            username.setVisibility(View.GONE);

        }



    }

    private void setSingleEvent(GridLayout maingrid) {
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
                }


            }
        });
    }
    }


}
