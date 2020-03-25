package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText txtMail, txtPass;

    //Variables utilizadas para Firebase.
    private static final String TAG = "EmailPassword"; //Variable para el LOG de consola.
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Variables creadas para vincular con la interfaz grafica activity_login.xml
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtContraseña);

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

    public void entrar(View view){
        //Valido los datos ingresados en el registro si no estan vacios.
        //Agregar validación de contraseña (length >= 6)
        if (validacion()){
            String mail  = txtMail.getText().toString();
            String password = txtPass.getText().toString();

            mAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Si el resgistro es correcto, obtengo informacion del usuario y muestro mensaje.
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Autentificación exitosa!",
                                        Toast.LENGTH_LONG).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // Si el registro falla, muestra mensaje en pantalla.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Email o Usuario incorrecto!",
                                        Toast.LENGTH_LONG).show();
                                //updateUI(null);
                                limpartCajas();
                            }

                        }
                    });

        }else{
            limpartCajas();
        }
    }

    //Metodo para limpiar el contenido de los EditText.
    private void limpartCajas() {
        txtMail.setText("");
        txtPass.setText("");
    }

    //Metodo para validar los datos ingresados en los EditText si no son vacios.
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

    //Metodo para inicializar la conexion con Firebase.
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void Olvidar(View view) {
        //Crear activity para recuperar la contraseña.
    }

    // Abrir actividad de registro.
    public void Registrar(View view) {
        Intent intento = new Intent (this,RegistroActivity.class);
        startActivity(intento);
    }
}
