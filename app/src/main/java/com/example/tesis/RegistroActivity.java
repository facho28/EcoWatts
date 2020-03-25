package com.example.tesis;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesis.Model.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtMail, txtPass, txtNombre, txtApellido;
    private ProgressDialog progressBar;

    //Variables utilizadas para Firebase
    private static final String TAG = "EmailPassword"; //Variable para el LOG de consola.
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Variables creadas para vincular con la interfaz grafica activity_registro.xml
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtContraseña);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        progressBar = new ProgressDialog(this);


        // Inicializar Firebase y metodo de autentificacion por Mail.
        inicializarFirebase();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Compruebe si el usuario ha iniciado sesión (no es nulo) y actualice la IU en consecuencia.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    //Metodo para actualizar los datos del usuario ya registrado.
    //Falta agregar que se debe actualizar y que datos que retorna. Dejo comentado los llamados.
    private void updateUI(FirebaseUser currentUser) {
    }

    public void Registrar(View view){
        //Valido los datos ingresados en el registro si no estan vacios.
        //Agregar validación de contraseña (length >= 6)
        if (validacion()){
            // Guardo en las variables los contenidos de los txt
            final String mail  = txtMail.getText().toString();
            final String password = txtPass.getText().toString();
            final String nombre = txtNombre.getText().toString();
            final String apellido = txtApellido.getText().toString();

            //Comienza el ProgressBar
            progressBar.setMessage("Realizando registro en linea");
            progressBar.show();

            //Metodo estandar de Firebase para registrar usuarios por autentificación de Mail.
            //Se pasa por parametro el mail y la contraseña.
            mAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Si el resgistro es correcto, obtengo informacion del usuario, creo registro en tabla de usuarios y muestro mensaje.

                                //Creo objeto del tipo usuario y le asigno los datos del registro
                                Usuarios P = new Usuarios();
                                P.setCorreo(mail);
                                P.setNombre(nombre);
                                P.setApellido(apellido);
                                P.setEstado(1);

                                //Inserto los datos en la tabla Usuarios de la BD
                                databaseReference.child("Usuarios").child(UUID.randomUUID().toString()).setValue(P);

                                //Mensaje
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(RegistroActivity.this, "Registro exitoso!",
                                        Toast.LENGTH_LONG).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);

                                limpartCajas();
                                finish(); //Vuelvo a pantalla de login.
                            } else { //Error en el registro del usuario.
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){ // Si el usuario ya existe.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistroActivity.this, "Este usuario ya existe!",
                                            Toast.LENGTH_LONG).show();
                                }else {  // Si el registro fallo.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistroActivity.this, "No se pudo registrar el usuario!",
                                            Toast.LENGTH_LONG).show();
                                }


                                //updateUI(null);
                                limpartCajas();
                            }
                            //Finaliza el ProgressBar
                            progressBar.dismiss();
                        }
                    });
        }else {
            //Mensaje cuando falta completar los datos de los txt
            Toast.makeText(RegistroActivity.this, "Completar los datos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Accion del boton Cancelar
    public void Cancelar(View view){
        finish();
    }

    //Metodo para limpiar el contenido de los EditText
    private void limpartCajas() {
        txtMail.setText("");
        txtPass.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
    }

    //Metodo para validar los datos ingresados en los EditText si no son vacios.
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
