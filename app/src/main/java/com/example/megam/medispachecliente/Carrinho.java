package com.example.megam.medispachecliente;



import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megam.medispachecliente.Adapter.CarrinhoAdapter;
import com.example.megam.medispachecliente.Common.Common;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.Notifications.Data;
import com.example.megam.medispachecliente.Notifications.MyResponse;
import com.example.megam.medispachecliente.Notifications.Sender;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.control.Conexao;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.model.Request;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.sql.pedido_db;
import com.example.megam.medispachecliente.view.Confirmado_pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Carrinho extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference database_do_cliente;
    DatabaseReference database_do_delivery;
    String idE="";
    FirebaseUser idUsuario;
    Intent intent;
    boolean notify =false;
    APIService apiService;
    Usuarios usuarios = new Usuarios();

    RadioGroup radioGroup;
    RadioButton radioDinheiro, radioCartao;
    TextView txt_total_preco, txt_valor_entrega, txt_taxa_entrega;
    Button btn_fazer_pedido, btn_att_cart;
    String forma ="";
    String nomeDocliente ="";

    List<Pedidos> carrinho = new ArrayList<>();
    CarrinhoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        idE = intent.getStringExtra("EId");

        System.out.println("IIDE: "+idE);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Requests").child(idE); // aqui envia o pedido com o ID da empresa
        //init

        recyclerView = findViewById(R.id.lista_carrinho);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txt_taxa_entrega = findViewById(R.id.taxa_entrega);
        txt_valor_entrega = findViewById(R.id.valor_entrega);
        radioGroup = findViewById(R.id.radio_group);
        txt_total_preco = findViewById(R.id.valor_total);
        btn_fazer_pedido = findViewById(R.id.btn_pedir);
        btn_att_cart = findViewById(R.id.att_cart);
        radioDinheiro = findViewById(R.id.dinheiro_radio);
        radioCartao = findViewById(R.id.cartao_radio);


        CarregarListaPedidos();

       btn_fazer_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

       btn_att_cart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               recreate();
           }
       });
    }

    private void showAlertDialog() {
        if(idUsuario==null){ // botar isso aqui ali embaixo
            idUsuario = Conexao.getFirebaseAuth().getCurrentUser();
            database_do_cliente = firebaseDatabase.getInstance().getReference("User").child(idUsuario.getUid());
            database_do_cliente.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Usuarios u = dataSnapshot.getValue(Usuarios.class);
                        nomeDocliente = u.getName();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
            AlertDialog.Builder alertDialog  = new AlertDialog.Builder(Carrinho.this);
            alertDialog.setTitle("Mais um passo!");
            alertDialog.setMessage("Digite o seu endereço");

            final EditText edtAdress = new EditText(Carrinho.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            edtAdress.setLayoutParams(lp);
            alertDialog.setView(edtAdress); // adiciona um edit txt no dialog
            alertDialog.setIcon(R.drawable.confirme);

            alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (radioCartao.isChecked()){
                        forma = "Cartão";
                    }else if(radioDinheiro.isChecked()){
                        forma = "Dinheiro";
                    }
                    //Criar novo request
                    Request request = new Request(
                            nomeDocliente,
                            edtAdress.getText().toString(),
                            txt_total_preco.getText().toString(),
                            txt_valor_entrega.getText().toString(),
                            txt_taxa_entrega.getText().toString(),
                            forma,
                            carrinho
                    );
                    //Submit to firebase
                    databaseReference.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    System.out.println("NOME DO USUARIO: "+ request.getNome());
                    //Delete carrinho
                    new pedido_db(getBaseContext()).limpar_Carrinho();
                    startActivity(new Intent(getBaseContext(), Confirmado_pedido.class));
                    finish();
                    //ENVIAR NOTIFICAÇÃO DO PEDIDO
                    Produtos produtos = new Produtos();
                    final String userC;
                    final FirebaseUser userE = FirebaseAuth.getInstance().getCurrentUser();
                    userC = userE.getUid();
                    notify = true;

                    if(notify){
                        SendNotify(idE, usuarios.getName(), produtos.getNome()); // CORRIGIR A PASSAGEM DO NOME DO USER + NOME DO PRODUTO
                        System.out.println("ide "+idE+"u.getname"+usuarios.getName()+"produtos.getnome"+produtos.getNome());
                    }
                    notify = false;
                }
            });

            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog.show();
        }

    }

    //MÉTODO QUE ENVIA A NOTIFICACAO PARA O APP DA EMPRESA
    private void SendNotify(String receiver, final String username, final String message){

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        final String userC;
        final FirebaseUser userE = FirebaseAuth.getInstance().getCurrentUser();
        userC = userE.getUid();

        System.out.println("USERC AQUI "+userC);
        ///////////////////////////////////////////////////////////////////////////

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        idE = intent.getStringExtra("EId");

        System.out.println("ID da empresa AQUI "+idE);


        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(userC, R.drawable.ic_motivo, username+": "+message, "Novo pedido",
                            idE);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success!=1){}
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void CarregarListaPedidos() { // aqui carrega o valor total do pedido
        carrinho = new pedido_db(this).getCarrinhos();
        adapter = new CarrinhoAdapter(carrinho, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    if (idE!=null){
     database_do_delivery = firebaseDatabase.getInstance().getReference("UserEmpresa").child(idE);
     database_do_delivery.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Usuarios u = dataSnapshot.getValue(Usuarios.class);
                System.out.println("VALOR DO DELIVERY: "+u.getValor());
                String valorEntregador = u.getValor();

                //calcular o valor total
                double total = 0;
                double armazenaTemp = 0;
                double percent = 0;
                double delivery = Double.parseDouble(valorEntregador);
                for (Pedidos pedidos : carrinho) {
                    armazenaTemp = ((Double.parseDouble(pedidos.getPrecoProduto())) * (Integer.parseInt(pedidos.getQuantidadeProduto())));
                    percent += (armazenaTemp * 0.1);
                    total +=
                    (((Double.parseDouble(pedidos.getPrecoProduto()))*(Integer.parseInt(pedidos.getQuantidadeProduto()))) +
                    (0.1 * (Double.parseDouble(pedidos.getPrecoProduto()))*(Integer.parseInt(pedidos.getQuantidadeProduto()))));
                }
                // TA PASSANDO O VALOR DA ENTREGA VÁRIAS VEZES

                Locale locale = new Locale("pt", "BR");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                txt_total_preco.setText(fmt.format(total+delivery));
                txt_taxa_entrega.setText(fmt.format(percent));
                txt_valor_entrega.setText(valorEntregador);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }


    }
}
