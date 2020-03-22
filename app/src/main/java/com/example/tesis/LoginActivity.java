package com.example.tesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tesis.Model.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;


public class LoginActivity extends AppCompatActivity {

    private EditText txtMail, txtPass;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtContraseña);

        inicializarFirebase();
    }

    public void entrar(View view){
        if (validacion()){
            String mail  = txtMail.getText().toString();
            String password = txtPass.getText().toString();

        }else{
            limpartCajas();
        }
    }

    //Metodo para limpiar el contenido de los EditText
    private void limpartCajas() {
        txtMail.setText("");
        txtPass.setText("");
    }

    //Metodo para validar los datos ingresados en los EditText
    private boolean validacion() {
        String mail  = txtMail.getText().toString();
        String password = txtPass.getText().toString();
        boolean ban = true;

        if (mail.equals("")){
            txtMail.setError("Required");
            ban = false;
        } else if (password.equals("")){
            txtPass.setError("Required");
            ban = false;
        }

        return ban;
    }

    //Metodo para inicializar la conexion con Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void Olvidar(View view) {
        //Crear activity para recuperar la contraseña
    }

    public void Registrar(View view) {
        Intent intento = new Intent (this,RegistroActivity.class);
        startActivity(intento);
    }
}
