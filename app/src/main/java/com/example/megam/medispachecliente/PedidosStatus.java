package com.example.megam.medispachecliente;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.megam.medispachecliente.Adapter.ListaPedidosAdapter;
import com.example.megam.medispachecliente.Common.Common;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PedidosStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseUser IdUsuario = FirebaseAuth.getInstance().getCurrentUser();


    FirebaseRecyclerAdapter<Request, ListaPedidosAdapter> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_status);

        //firebase init
        database = FirebaseDatabase.getInstance();
        //requests = database.getReference("Requests").child(IdUsuario.getUid());


        recyclerView = findViewById(R.id.listaPedidosStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //carregarPedidos();


        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        FirebaseRecyclerOptions<Request> requisicao = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests = FirebaseDatabase.getInstance().getReference("Requests").child(IdUsuario.getUid()), Request.class)
                .build(); // acho que aqui tem que passar o id da empresa e depois o id da pessoa



            super.onPostResume();

            adapter = new FirebaseRecyclerAdapter<Request, ListaPedidosAdapter>(requisicao){
                @NonNull
                @Override
                public ListaPedidosAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.pedidos_layout,parent,false);

                    return new ListaPedidosAdapter(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull ListaPedidosAdapter holder, int position, @NonNull Request model) {
                    holder.txt_nome_pedido.setText(adapter.getRef(position).getKey());
                    //holder.txt_status_pedido.setText(converteCodeTostatus(model.getStatus()));
                    holder.txt_nome_pessoa.setText((model.getEndereco())); // corrigir aqui para passar o nome do produto
                    holder.txt_endereco.setText((model.getEndereco()));
                }
            };

            adapter.startListening();
            recyclerView.setAdapter(adapter);

            onPostResume();

    }
        private String converteCodeTostatus(String status) {
        if(status.equals("0")){return "Pedido postado";}
        else if(status.equals("1")){return "Pedido a caminho";}
        else{return "Pedido entregue";}
    }

    }

