package com.example.apphydrojetting;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphydrojetting.R;
import com.example.apphydrojetting.activity.InicioActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //Declaración variable de Autenticación para Firebase
    private FirebaseAuth mAuth;
    //Declaración de variables para los elementos de la interfaz
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Inicialización de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //Inicialización de los elementos de la interfaz
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        //Asignar OnClickListener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(email, password);
            }
        });
    }

    //LogIn con correo y contraseña, metodo de Firebase para la autenticación.
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success, navigate to main activity
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
