package com.example.megam.medispachecliente;

import android.app.Application;
import android.content.Context;

public class Myapp extends Application {

    private static Context nContext;


    @Override
    public void onCreate() {

        nContext = getApplicationContext();

        super.onCreate();
    }
    public  static Context getnContext(){
        return nContext;
    }
}
