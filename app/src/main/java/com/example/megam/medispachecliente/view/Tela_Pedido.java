package com.example.megam.medispachecliente.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


//import com.example.megam.medispachecliente.Adapter.ConfirmaAdapter;
//import com.example.megam.medispachecliente.Adapter.PedidosAdapter;
import com.example.megam.medispachecliente.Adapter.PedidosAdapter;
import com.example.megam.medispachecliente.Adapter.ProdutosAdapter;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.model.Chatlist;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.model.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Tela_Pedido extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;
    EditText valor_troco, extras_pedido;
    RadioButton dinheiro, cartao;
    TextView formas_pagamento, valor_total, troco_para, valor_total_compras, mostrar_endereco;
    Button end_cadastrado, end_atual;
    RecyclerView recyclerView;
    private List<Produtos> mProdutos;
    private PedidosAdapter pedidosAdapter;
    Intent intent;
    String userid;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrinho);
        intent = getIntent();
        userid = intent.getStringExtra("userID");
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        mProdutos = new ArrayList<>();
        inicializarcomponentes();
        eventoBotao();
        atual_button();
        readPedidos();

        reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener( new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Usuarios u = dataSnapshot.getValue(Usuarios.class);
                    mostrar_endereco.setText("Endereço: "+u.getEndereco()+", "+u.getCidade());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }



        // metodo para enviar cada produto para o recycle view


    private void readPedidos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProdutos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Produtos produtos = snapshot.getValue(Produtos.class);
                    assert produtos != null;
                    mProdutos.add(produtos);
                }

                pedidosAdapter = new PedidosAdapter(getApplicationContext(), mProdutos, userid);
                recyclerView.setAdapter(pedidosAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        mostrar_endereco = findViewById(R.id.text_endereco);
    }


    private void eventoBotao(){
        end_cadastrado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                alert("Seu pedido chegará em breve");
                Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void atual_button(){
        end_atual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new  Intent(getApplicationContext(), Pedido_local_atual.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}