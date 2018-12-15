package com.example.megam.medispachecliente.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.megam.medispachecliente.Myapp;

public class Maindb extends SQLiteOpenHelper {
    private static String NOME_DB = "DB";
    private  static  int  VERSAO_DB=1;
    public static String TABELA_PESSOA = "TABELA_PESSOA";

    private static  Maindb instancia;


    public  static Maindb getInstancia(){
        if(instancia == null) instancia =  new Maindb();
        return instancia;
    }

    public Maindb() {
        super(Myapp.getnContext(), NOME_DB, null, VERSAO_DB);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
