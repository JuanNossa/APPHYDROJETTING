package com.example.apphydrojetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphydrojetting.activity.InicioActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botón para ver visitas agendadas
        findViewById(R.id.button_visit_scheduled).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduledVisitsActivity.class);
            startActivity(intent);
        });

        // Botón para ver visitas no agendadas
        findViewById(R.id.button_visit_not_scheduled).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotScheduledVisitsActivity.class);
            startActivity(intent);
        });

        // Botón para ver visitas cerradas
        findViewById(R.id.button_visit_closed).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClosedVisitsActivity.class);
            startActivity(intent);
        });

        // Botón para cerrar sesión
        Button logoutButton = findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, InicioActivity.class);
            startActivity(intent);
            finish();
        });
    }
}