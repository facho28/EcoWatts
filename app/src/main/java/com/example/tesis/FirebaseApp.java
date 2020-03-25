package com.example.tesis;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApp extends android.app.Application{

    //Metodo para crear la persistencia y no perder los datos. Se llama desde el manifiest.
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
