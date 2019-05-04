package com.example.megam.medispachecliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.megam.medispachecliente.Adapter.ProdutosAdapter;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.view.Cadastrar_Produto;
import com.example.megam.medispachecliente.view.Tela_Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ImageButton comprar;
    private ProdutosAdapter produtosAdapter;
    private List<Produtos> mProdutos;
    ImageButton cadastro;
    String userid;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        userid = intent.getStringExtra("userID");
        setContentView(R.layout.activity_main3);
        recyclerView = findViewById(R.id.recycler_view);
        comprar = findViewById(R.id.btn_comprar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cadastro = findViewById(R.id.cadastrar);
        mProdutos = new ArrayList<>();
        readUsers();

    comprar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) { // BOTÃO PARA FAZER O PEDIDO, MAS NAO TA ENVIANDO PARA A EMPRESA, É SÓ TESTE
            //SE TA PASSANDO OS PRODUTOS PARA A OUTRA TELA

            Intent i = new  Intent(getApplicationContext(), Tela_Pedido.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    });

    }






    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProdutos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Produtos produtos = snapshot.getValue(Produtos.class);
                    assert produtos != null;
                    mProdutos.add(produtos);
                }

                produtosAdapter = new ProdutosAdapter(getApplicationContext(), mProdutos, userid);
                recyclerView.setAdapter(produtosAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
