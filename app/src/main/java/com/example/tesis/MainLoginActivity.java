package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Metodo para llamar al Activity del login
    public void ingresar(View view){
        Intent intento = new Intent (this,LoginActivity.class);
        startActivity(intento);
    }

    //Metodo para llamar al Activity del registro
    public void registrar(View view) {
        Intent intento = new Intent (this,RegistroActivity.class);
        startActivity(intento);
    }
}
