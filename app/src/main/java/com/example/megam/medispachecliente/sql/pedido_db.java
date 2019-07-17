package com.example.megam.medispachecliente.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.megam.medispachecliente.model.Pedidos;
import com.example.megam.medispachecliente.model.Produtos;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class pedido_db extends SQLiteAssetHelper {

    private static final String DB_NAME ="Bancodispache.db";
    private static final int DB_VER = 1;
    public pedido_db(Context context)
    {
        super(context, DB_NAME, null,  DB_VER);
    }

    public List<Pedidos> getCarrinhos(){ //retorna a lista de pedidos para tela carrinho
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"Nomeproduto","Precoproduto","Quantidadeproduto","Idproduto"};
        String sqlTable = "Detalhespedidos";
        qb.setTables(sqlTable);

        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Pedidos> resultado = new ArrayList<>();
        if(c!= null && c.moveToFirst()){
            do{
                Log.i("nome: ",c.getString(c.getColumnIndex("Nomeproduto")));
                Log.i("preço: ",c.getString(c.getColumnIndex("Precoproduto")));
                Log.i("qtd: ",c.getString(c.getColumnIndex("Quantidadeproduto")));
                Log.i("id: ",c.getString(c.getColumnIndex("Idproduto")));

                resultado.add(new Pedidos(c.getString(c.getColumnIndex("Nomeproduto")),
                        c.getString(c.getColumnIndex("Precoproduto")),
                        c.getString(c.getColumnIndex("Quantidadeproduto")),
                        c.getString(c.getColumnIndex("Idproduto"))
                ));
            }while (c.moveToNext());
        }
        return resultado;
    }


    public void add_Carrinho(Pedidos pedidos){
        SQLiteDatabase db =  getReadableDatabase();
        String query = String.format("INSERT INTO Detalhespedidos (Nomeproduto, Precoproduto, Quantidadeproduto, Idproduto) VALUES ('%s','%s','%s','%s');",
                pedidos.getNomeProduto(),
                pedidos.getPrecoProduto(),
                pedidos.getQuantidadeProduto(),
                pedidos.getIdProduto());

        db.execSQL(query);
        System.out.println(query);
        System.out.println(pedidos.getNomeProduto() + pedidos.getPrecoProduto() + pedidos.getQuantidadeProduto() + pedidos.getIdProduto());
    }

    public void limpar_Carrinho(){
        SQLiteDatabase db =  getReadableDatabase();
        String query = String.format("DELETE FROM Detalhespedidos");
        db.execSQL(query);
        System.out.println(query);
    }

    public void limpar_posicao(String nome){
        SQLiteDatabase db =  getReadableDatabase();
        String query = String.format("DELETE FROM Detalhespedidos where Nomeproduto = '"+nome+"'");
        db.execSQL(query);
        System.out.println(query);
    }
}
