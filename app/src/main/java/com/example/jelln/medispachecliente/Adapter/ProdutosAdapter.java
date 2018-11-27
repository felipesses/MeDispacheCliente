package com.example.jelln.medispachecliente.Adapter;

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

import com.example.jelln.medispache.R;
import com.example.jelln.medispache.model.Produtos;
import com.example.jelln.medispache.view.Atualizar_Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolder> {

    private Context mContext;
    private List<Produtos> mProdutos;

    public ProdutosAdapter(Context mContext, List<Produtos> mProdutos){

        this.mProdutos = mProdutos;
        this.mContext = mContext;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
                Intent intent = new Intent(mContext, Atualizar_Produto.class);
                Bundle bundle = new Bundle();

                bundle.putString("nome", produtos.getNome() );
                bundle.putString("quantidade",produtos.getQuantidade() );
                bundle.putString("valor", produtos.getValor() );
                bundle.putString("id", produtos.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);


            }
        });


        holder.botao_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProdutosEmpresa").child(user.getUid()).child(produtos.getId());
                databaseReference.removeValue();
                alert("Container: "+produtos.getNome()+" removido.");

            }
        });


    }

    @Override
    public int getItemCount() {
        return mProdutos.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        public  TextView quantidade;
        public TextView tipo;
        public ImageView profile_image;
        public ImageButton botao_mp;
        public ImageButton botao_del;
        public ViewHolder(View itemView){
            super(itemView);
            tipo = itemView.findViewById(R.id.tipo);
            profile_image = itemView.findViewById(R.id.container_img);
            quantidade = itemView.findViewById(R.id.quantidade);
            botao_mp = itemView.findViewById(R.id.atualizar);
            botao_del = itemView.findViewById(R.id.deletar);



        }

    }

    public void   alert(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }
}
