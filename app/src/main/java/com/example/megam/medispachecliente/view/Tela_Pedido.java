package com.example.megam.medispachecliente.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
//import com.example.megam.medispachecliente.Adapter.ConfirmaAdapter;
//import com.example.megam.medispachecliente.Adapter.PedidosAdapter;
import com.example.megam.medispachecliente.Main4Activity;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Chatlist;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Tela_Pedido extends AppCompatActivity {

    EditText valor_troco, extras_pedido;
    RadioButton dinheiro, cartao;
    TextView formas_pagamento, valor_total, troco_para, valor_total_compras;
    Button end_cadastrado, end_atual;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacoes_pedido);

        inicializarcomponentes();
        eventoBotao();
    }

    @SuppressLint("WrongViewCast")
    private void inicializarcomponentes(){

       valor_troco = findViewById(R.id.edit_troco);
       extras_pedido = findViewById(R.id.ExtrasPedido);
        end_atual = findViewById(R.id.endereco_atual) ;
        end_cadastrado = findViewById(R.id.endereco_padrao);
        dinheiro = findViewById(R.id.R_dinheiro);
        cartao = findViewById(R.id.R_cartao);
        formas_pagamento = findViewById(R.id.formadepagamento);
        valor_total = findViewById(R.id.preco_total);
        troco_para = findViewById(R.id.troco);
        valor_total_compras = findViewById(R.id.opreco);
    }


    private void eventoBotao(){
        end_atual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    Intent i = new  Intent(getApplicationContext(), Pedido_local_atual.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
            }
        });

        end_cadastrado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }
}