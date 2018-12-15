package com.example.megam.medispachecliente.sql;

import android.database.sqlite.SQLiteDatabase;

import com.example.megam.medispachecliente.model.Usuarios;

public class Delete {


public  void removerTabela(){
    SQLiteDatabase db = Maindb.getInstancia().getWritableDatabase();
    String query="DROP TABLE IF EXISTS " +Maindb.TABELA_PESSOA;
    db.execSQL(query);


}


public  boolean removerUsuario(Usuarios usuarios){

    SQLiteDatabase db = Maindb.getInstancia().getWritableDatabase();
    String query = "EMAIL= '" +usuarios.getId() +"'";
    return  db.delete(Maindb.TABELA_PESSOA, query, null) >0;



}


}
