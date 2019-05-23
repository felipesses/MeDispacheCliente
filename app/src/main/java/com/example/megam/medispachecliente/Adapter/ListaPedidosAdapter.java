package com.example.megam.medispachecliente.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.megam.medispachecliente.Interface.ItemClickListener;
import com.example.megam.medispachecliente.R;



public class ListaPedidosAdapter extends RecyclerView.ViewHolder implements View.OnClickListener  {


    public TextView txt_nome_pedido, txt_status_pedido, txt_endereco, txt_nome_pessoa;
    private ItemClickListener itemClickListener;

    public ListaPedidosAdapter(View itemView) {
        super(itemView);
        txt_nome_pedido = itemView.findViewById(R.id.nome_pedido);
        txt_status_pedido = itemView.findViewById(R.id.pedido_status);
        txt_endereco = itemView.findViewById(R.id.pedido_endereco);
        txt_nome_pessoa = itemView.findViewById(R.id.pedido_pessoa_nome);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

