package com.example.megam.medispachecliente.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.google.android.gms.maps.MapView;

public class Pedido_local_atual extends AppCompatActivity {

    Button dispachar;
    EditText local_atual;
    TextView local_texto;
    MapView map_visu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_local_atual);

        //eventoBotao();
        inicializarcomponentes();
    }

   /* private void eventoBotao(){
        dispachar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }*/

    @SuppressLint("WrongViewCast")
    private void inicializarcomponentes(){
        local_texto = findViewById(R.id.text_local);
        dispachar = findViewById(R.id.btn_dispache);
        local_atual = findViewById(R.id.endereco_pessoa_agora);
        map_visu = findViewById(R.id.recycler_view_mapa);

    }
}
