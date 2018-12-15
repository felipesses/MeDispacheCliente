package com.example.megam.medispachecliente.sql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.megam.medispachecliente.model.Usuarios;

public class Update {


    public boolean addUsuario(Usuarios usuarios){

        SQLiteDatabase db = Maindb.getInstancia().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", usuarios.getId());
        cv.put("EMAIL", usuarios.getEmail());
        cv.put("SENHA", usuarios.getSenha());
        cv.put("NAME", usuarios.getName());


return db.insert(Maindb.TABELA_PESSOA,null, cv)!= -1;

    }

    public boolean updateUsuario(Usuarios usuarios){

        SQLiteDatabase db = Maindb.getInstancia().getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", usuarios.getId());
        cv.put("EMAIL", usuarios.getEmail());
        cv.put("SENHA", usuarios.getSenha());
        cv.put("NAME", usuarios.getName());

String where = "ID = '" +usuarios.getId() + "'";
        return db.update(Maindb.TABELA_PESSOA, cv, where, null)!= -1;

    }

}
