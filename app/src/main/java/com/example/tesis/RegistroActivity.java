package com.example.tesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tesis.Model.Usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegistroActivity extends AppCompatActivity {

    private EditText txtMail, txtPass, txtNombre, txtApellido;
    private List<Usuarios> listPerson = new ArrayList<Usuarios>();
    ArrayAdapter<Usuarios> arrayAdapterPersona;
    ListView listV_personas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtContraseña);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        listV_personas = findViewById(R.id.lstView);

        inicializarFirebase();
        listarDatos();
    }

    public void Registrar(View view){
        //Valido los datos ingresados en el registro
        if (validacion()){
            String mail  = txtMail.getText().toString();
            String password = txtPass.getText().toString();
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();

            //Creo objeto del tipo usuario y le asigno los datos del registro
            Usuarios P = new Usuarios();
            P.setIdUsuario(UUID.randomUUID().toString());
            P.setCorreo(mail);
            P.setContraseña(password);
            P.setNombre(nombre);
            P.setApellido(apellido);

            //Inserto los datos en la tabla Usuarios de la BD
            databaseReference.child("Usuarios").child(P.getIdUsuario()).setValue(P);

            Toast.makeText(this, "Registro exitoso!", Toast.LENGTH_LONG).show();
            limpartCajas();
        }else {
            limpartCajas();
        }
    }

    public void Cancelar(View view){
        finish();
    }

    private void listarDatos() {
        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPerson.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Usuarios p = objSnaptshot.getValue(Usuarios.class);
                    listPerson.add(p);

                    arrayAdapterPersona = new ArrayAdapter<Usuarios>(RegistroActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listV_personas.setAdapter(arrayAdapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Metodo para limpiar el contenido de los EditText
    private void limpartCajas() {
        txtMail.setText("");
        txtPass.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
    }

    //Metodo para validar los datos ingresados en los EditText
    private boolean validacion() {
        String mail     = txtMail.getText().toString();
        String password = txtPass.getText().toString();
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();

        boolean ban = true;

        if (mail.equals("")){
            txtMail.setError("Required");
            ban = false;
        } else if (password.equals("")){
            txtPass.setError("Required");
            ban = false;
        } else if (nombre.equals("")){
            txtNombre.setError("Required");
            ban = false;
        } else if (apellido.equals("")){
            txtApellido.setError("Required");
            ban = false;
        }

        return ban;
    }

    //Metodo que inicializa Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
}
