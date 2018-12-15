package com.example.megam.medispachecliente.sql;

import android.database.sqlite.SQLiteDatabase;

public class Create {

    public void  createTable(){
        SQLiteDatabase db = Maindb.getInstancia().getWritableDatabase();
        String colunas = "(ID TEXT, EMAIL TEXT, SENHA TEXT, NAME TEXT)";
        String query = "CREATE TABLE IF NOT EXISTS " +Maindb.TABELA_PESSOA + colunas;
        db.execSQL(query);
    }
}
