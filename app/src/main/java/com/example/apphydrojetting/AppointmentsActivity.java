package com.example.apphydrojetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphydrojetting.activity.InicioActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private Button logoutButton;
    private AppointmentsAdapter adapter;
    private List<Map<String, Object>> appointmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view_appointments);
        logoutButton = findViewById(R.id.button_logout);
        appointmentsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentsAdapter(appointmentsList);
        recyclerView.setAdapter(adapter);

        loadAppointmentData();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AppointmentsActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadAppointmentData() {
        db.collection("appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        appointmentsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            appointment.put("id", document.getId());
                            appointmentsList.add(appointment);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AppointmentsActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}