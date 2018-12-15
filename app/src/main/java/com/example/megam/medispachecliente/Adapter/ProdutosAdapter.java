package com.example.megam.medispachecliente.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megam.medispachecliente.MainActivity;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.Notifications.Data;
import com.example.megam.medispachecliente.Notifications.MyResponse;
import com.example.megam.medispachecliente.Notifications.Sender;
import com.example.megam.medispachecliente.Notifications.Token;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Produtos;
import com.example.megam.medispachecliente.model.Usuarios;
import com.example.megam.medispachecliente.view.Atualizar_Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolder> {
    FirebaseUser user;
    private Context mContext;
    private List<Produtos> mProdutos;
    APIService apiService;
    boolean notify =false;
    String userid;
    public ProdutosAdapter(Context mContext, List<Produtos> mProdutos, String userid){

        this.mProdutos = mProdutos;
        this.mContext = mContext;
        this.userid = userid;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        user = FirebaseAuth.getInstance().getCurrentUser();
        View view = LayoutInflater.from(mContext).inflate(R.layout.produtos_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Produtos produtos = mProdutos.get(position);
        holder.tipo.setText(produtos.getNome().toLowerCase());
        holder.quantidade.setText("R$: "+produtos.getValor().toLowerCase());
        holder.profile_image.setImageResource(R.drawable.ic_launcher_background);


        holder.botao_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(mContext, Atualizar_Produto.class);
                Bundle bundle = new Bundle();

                bundle.putString("nome", produtos.getNome() );
                bundle.putString("quantidade",produtos.getQuantidade() );
                bundle.putString("valor", produtos.getValor() );
                bundle.putString("id", produtos.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);*/
                notify = true;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuarios u = dataSnapshot.getValue(Usuarios.class);
                        if(notify) {
                            sendNotification(userid, u.getName(), produtos.getNome());
                            alert("Pedido concluído");
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);

                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });





    }

    @Override
    public int getItemCount() {
        return mProdutos.size();
    }
    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(),  R.drawable.ic_motivo, username+": "+message, "Novo pedido",
                            userid);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success!=1){

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        public  TextView quantidade;
        public TextView tipo;
        public ImageView profile_image;
        public ImageButton botao_mp;
        public ViewHolder(View itemView){
            super(itemView);
            tipo = itemView.findViewById(R.id.tipo);
            profile_image = itemView.findViewById(R.id.container_img);
            quantidade = itemView.findViewById(R.id.quantidade);
            botao_mp = itemView.findViewById(R.id.comprar);



        }

    }

    public void   alert(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }
}
