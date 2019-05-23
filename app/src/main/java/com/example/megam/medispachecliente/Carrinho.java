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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megam.medispachecliente.Adapter.CarrinhoAdapter;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.Notifications.Data;
import com.example.megam.medispachecliente.Notifications.MyResponse;
import com.example.megam.medispachecliente.Notifications.Sender;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.model.Request;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.sql.pedido_db;
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
    String idE;
    String PRECO_ENTREGA;
    Intent intent;
    boolean notify =false;
    APIService apiService;
    Usuarios usuarios = new Usuarios();

    RadioGroup radioGroup;
    TextView txt_total_preco, txt_valor_entrega, txt_taxa_entrega;
    Button btn_fazer_pedido;

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
        PRECO_ENTREGA = intent.getStringExtra("precoDELIVERI");

        System.out.println("IIDE: "+idE);
        System.out.println("USUARIOS EM STRING: "+PRECO_ENTREGA);
        //firebase

        //FirebaseUser userE = FirebaseAuth.getInstance().getCurrentUser();
        //userE.getUid();
        //System.out.println("USERE.getuid: AQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"+userE.getUid());


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Requests").child(idE); // aqui envia o pedido com o ID da empresa|Passar o do usuario também :) TA FUNCIONANDO ESSA LEBARA

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


        CarregarListaPedidos();

        btn_fazer_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {
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
                    //Criar novo request
                    Request request = new Request(
                            //Common.currentUser.getName(),
                            edtAdress.getText().toString(),
                            txt_total_preco.getText().toString(),
                            txt_valor_entrega.getText().toString(),
                            txt_taxa_entrega.getText().toString(),
                            carrinho
                    );
                    //Submit to firebase
                    databaseReference.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    //Delete carrinho
                    new pedido_db(getBaseContext()).limpar_Carrinho();
                    Toast.makeText(Carrinho.this, "Obrigado pela escolha, seu pedido chegará em breve", Toast.LENGTH_SHORT).show();
                    finish();
                    //ENVIAR NOTIFICAÇÃO DO PEDIDO
                    Usuarios u = new Usuarios();
                    Produtos produtos = new Produtos();
                    final String userC;
                    final FirebaseUser userE = FirebaseAuth.getInstance().getCurrentUser();
                    userC = userE.getUid();
                    notify = true;

                    if(notify){
                        SendNotify(idE, u.getName(), produtos.getNome()); // CORRIGIR A PASSAGEM DO NOME DO USER + NOME DO PRODUTO
                        System.out.println("ide "+idE+"u.getname"+u.getName()+"produtos.getnome"+produtos.getNome());
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

        System.out.println("ID da empresa q AQUI "+idE);


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
        recyclerView.setAdapter(adapter);

        //calcular o valor total
        double total = 0;
        double armazenaTemp = 0;
        double percent = 0;
        double delivery = 0;
        for (Pedidos pedidos : carrinho) {
            armazenaTemp = ((Double.parseDouble(pedidos.getPrecoProduto())) * (Integer.parseInt(pedidos.getQuantidadeProduto())));
            percent += (armazenaTemp * 0.1);
            total += (((Double.parseDouble(pedidos.getPrecoProduto())) * (Integer.parseInt(pedidos.getQuantidadeProduto()))) + (0.1 * (Double.parseDouble(pedidos.getPrecoProduto())) * (Integer.parseInt(pedidos.getQuantidadeProduto()))) + delivery);
        }

        Locale locale = new Locale("pt", "BR");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txt_total_preco.setText(fmt.format(total));
        txt_taxa_entrega.setText(fmt.format(percent));
        //txt_valor_entrega.setText(usuarios.getValor());
    }
}
