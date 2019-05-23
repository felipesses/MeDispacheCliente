package com.example.megam.medispachecliente.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.megam.medispachecliente.Interface.ItemClickListener;
import com.example.megam.medispachecliente.R;
import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.sql.pedido_db;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CarrinhoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_nome_produto, txt_preco_produto;
    public ImageView img_quantidade, del_pedidos;
    private ItemClickListener itemClickListener;

    public void setTxt_nome_produto(TextView txt_nome_produto) {
        this.txt_nome_produto = txt_nome_produto;
    }

    public CarrinhoViewHolder(View itemView) {
        super(itemView);
        txt_nome_produto = itemView.findViewById(R.id.nome_item_carrinho);
        txt_preco_produto = itemView.findViewById(R.id.preco_item_carrinho);
        img_quantidade = itemView.findViewById(R.id.quantidade_item_carrinho);
        del_pedidos = itemView.findViewById(R.id.trash_order);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }
}


public class CarrinhoAdapter extends RecyclerView.Adapter <CarrinhoViewHolder> {

   private List<Pedidos> listaDados = new ArrayList<>();
    private Context context;

    public CarrinhoAdapter(List<Pedidos> listaDados, Context context) {
        this.listaDados = listaDados;
        this.context = context;
    }

    @NonNull
    @Override
    public CarrinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.carrinho_layout,parent,false);
        return new CarrinhoViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listaDados.get(position).getQuantidadeProduto(), Color.RED);
        holder.img_quantidade.setImageDrawable(drawable); // Seta a quantidade de produtos dentro de um círculo vermelho

        Locale locale = new Locale("pt","BR");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        double preco = (Double.parseDouble(listaDados.get(position).getPrecoProduto()))*(Integer.parseInt(listaDados.get(position).getQuantidadeProduto()));
        holder.txt_preco_produto.setText(fmt.format(preco));

        holder.txt_nome_produto.setText(listaDados.get(position).getNomeProduto()); // seta o nome do  produto

        holder.del_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("DELETADO LINDO");
                new pedido_db(context).limpar_Carrinho();
            }
            //É UMA SOLUCAO PROVISÓRIA PARA DELETAR OS PEDIDOS
        });
    }

    @Override
    public int getItemCount() {
        return listaDados.size();
    }

}

