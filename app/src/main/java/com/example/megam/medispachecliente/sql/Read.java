package com.example.megam.medispachecliente.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.megam.medispachecliente.model.Usuarios;

import java.util.ArrayList;

public class Read {

    public ArrayList<Usuarios> getUsuarios() {

        SQLiteDatabase db = Maindb.getInstancia().getReadableDatabase();
        String query = "SELECT *FROM " +Maindb.TABELA_PESSOA;
        ArrayList<Usuarios> usuarios = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
Usuarios u = new Usuarios();
u.setId(c.getString(0));
u.setEmail(c.getString(1));
u.setSenha(c.getString(2));
u.setName(c.getString(3));
usuarios.add(u);



            }while(c.moveToNext());
        }


        c.close();
return usuarios;
    }
}
