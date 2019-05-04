package com.example.megam.medispachecliente.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.megam.medispachecliente.Notifications.Client;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.fragments.APIService;
import com.example.megam.medispachecliente.model.Produtos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder>{

        FirebaseUser user;
        private Context mContext;
        private List<Produtos> mProdutos;
        APIService apiService;
        boolean notify =false;
        String userid;

        public PedidosAdapter(Context mContext, List<Produtos> mProdutos, String userid){
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
            holder.tipo.setText(produtos.getNome());
            holder.quantidade.setText("R$: "+produtos.getValor());

            if (produtos.getImageUrl() == null) {
                holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Glide.with(mContext).load(produtos.getImageUrl()).into(holder.profile_image);
            }

        }

    @Override
    public int getItemCount() {
        return mProdutos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView quantidade;
            public TextView tipo;
            public ImageView profile_image;
            //public ImageButton botao_mp;

                public ViewHolder(View itemView){
                        super(itemView);
                        tipo = itemView.findViewById(R.id.tipo);
                        profile_image = itemView.findViewById(R.id.container_img);
                        quantidade = itemView.findViewById(R.id.quantidade);
                        //botao_mp = itemView.findViewById(R.id.Comprar);
                    }
        }


}
