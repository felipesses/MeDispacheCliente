package com.example.megam.medispachecliente.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.megam.medispachecliente.Main4Activity;
import com.example.megam.medispachecliente.R;

public class Confirmado_pedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmado_pedido);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          alert("Seu pedido chegará mais rápido que a peste!");
                                          startActivity(new Intent(getBaseContext(), Main4Activity.class));
                                          finish();
                                      }
                                  }
        ,3000);
        }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    }

