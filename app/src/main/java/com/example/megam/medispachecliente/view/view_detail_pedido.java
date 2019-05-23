package com.example.megam.medispachecliente.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.megam.medispachecliente.Carrinho;
import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.sql.pedido_db;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;

public class view_detail_pedido extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseAuth auth;
    Intent intent;
    TextView  id_Text, nome_produto, precoProduto;
    String id_produto, id_Empresa;
    FloatingActionButton floatingActionButton;

    Button addProduto;
    ElegantNumberButton numberButton;
    Produtos produtos = new Produtos();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pedidos);

        //init firebase
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        //finish init

        intent = getIntent();
        Bundle bundle = intent.getExtras();


        id_produto = bundle.getString("id");
        id_Empresa = intent.getStringExtra("UId");

        inicializarComponentes();
        initProdutosDetail();
        inicializarfirebase();
        CarrinhoButton();
        setButtonListener();
    }


    private void inicializarfirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    private void inicializarComponentes(){
        id_Text = findViewById(R.id.nome_produto);
        nome_produto = findViewById(R.id.text_nome_produto);
        precoProduto = findViewById(R.id.preco_produto_detalhe);
        addProduto = findViewById(R.id.btn_add_carrinho);
        numberButton = findViewById(R.id.number_button);
        floatingActionButton = findViewById(R.id.float_cart_button);
    }

    private void initProdutosDetail(){
        produtos.setNome(intent.getStringExtra("nome"));
        produtos.setValor(intent.getStringExtra("valor"));
        produtos.setId(intent.getStringExtra("id"));

        nome_produto.setText(produtos.getNome());
        precoProduto.setText(produtos.getValor());


        System.out.println("ID PRODUTO:"+id_produto);
        System.out.println("ID EMPRESA:"+id_Empresa);
    }

    private void setButtonListener(){

            addProduto.setOnClickListener(new View.OnClickListener() { // envia o produto para o carrinho
                @Override
                public void onClick(View view) {
                    if (user==null){
                        alert("Realize sou login para concluir o seu pedido!");
                    }else{
                        Enviar_Pedido(); //chama o envio do pedido
                    }
                }
            });

    }

    private void Enviar_Pedido(){ //enviando produto para o carrinho (SQLite)
        new pedido_db(getBaseContext()).add_Carrinho(new Pedidos(
                    produtos.getNome(),
                    produtos.getValor(),
                    numberButton.getNumber(),
                    produtos.getId()
        ));
        Toast.makeText(view_detail_pedido.this,"Adicionado ao Carrinho", Toast.LENGTH_SHORT).show();
    }

    private void CarrinhoButton(){
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(user == null){
                        alert("Você ainda não tem acesso ao carrinho!");
                    }else {
                        Intent CarrinhoIntent = new Intent(view_detail_pedido.this, Carrinho.class);
                        CarrinhoIntent.putExtra("EId", id_Empresa);
                        System.out.println("ID EMPRESA NO FLOATING ACTION BUTTON: " + id_Empresa);
                        startActivity(CarrinhoIntent);
                    }
                }
            });
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
