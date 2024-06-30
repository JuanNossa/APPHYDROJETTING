package com.example.apphydrojetting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotScheduledVisitsActivity extends AppCompatActivity {
    //Declaración de las variables para Firestore, RecyclerView y el Adaptador
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private VisitsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        //Inicialización de RecyclerView y Spinner
        recyclerView = findViewById(R.id.recycler_view_visits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner filterSpinner = findViewById(R.id.filter_spinner);
        //Inicialización de Firestore
        db = FirebaseFirestore.getInstance();
        //Cargar visitas con estado 2 (Scheduled)
        loadVisits(2);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//Método para manejar la selección del filtro
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filterOption = parent.getItemAtPosition(position).toString();
                if (filterOption.equals("Date")) { //Filtrar por fecha
                    loadVisitsByDate(2);
                } else if (filterOption.equals("Service Type")) { //Filtrar por tipo de servicio
                    loadVisitsByServiceType(2);
                } else { //Cargar todas las visitas
                    loadVisits(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadVisits(int status) { //Método para cargar visitas por estado
        //Obtener documentos de Firestore y actualizar el adaptador
        db.collection("appointments")
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> visits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> visit = document.getData();
                        visit.put("id", document.getId());
                        visits.add(visit);
                    }
                    adapter = new VisitsAdapter(visits, status);
                    recyclerView.setAdapter(adapter);
                });
    }

    private void loadVisitsByDate(int status) {//Método para cargar visitas por fecha
        //Obtener documentos ordenados por timestamp y actualizar el adaptador
        db.collection("appointments")
                .whereEqualTo("status", status)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> visits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> visit = document.getData();
                        visit.put("id", document.getId());
                        visits.add(visit);
                        // Log para depuración
                        Log.d("Firestore", "Visit: " + visit.get("date") + " Timestamp: " + visit.get("timestamp"));
                    }
                    adapter = new VisitsAdapter(visits, status);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(NotScheduledVisitsActivity.this, "Error loading visits: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadVisitsByServiceType(int status) {//Método para cargar visitas por tipo de servicio
        db.collection("appointments")
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> visits = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> visit = document.getData();
                        visit.put("id", document.getId());
                        int serviceTypeOrder = getServiceTypeOrder((String) visit.get("serviceType"));
                        visit.put("serviceTypeOrder", serviceTypeOrder);
                        visits.add(visit);
                    }
                    // Log para depuración
                    for (Map<String, Object> visit : visits) {
                        Log.d("ServiceType", "Visit: " + visit.get("serviceType") + " Order: " + visit.get("serviceTypeOrder"));
                    }
                    visits.sort((v1, v2) -> Integer.compare((int) v1.get("serviceTypeOrder"), (int) v2.get("serviceTypeOrder")));
                    runOnUiThread(() -> {
                        adapter = new VisitsAdapter(visits, status);
                        recyclerView.setAdapter(adapter);
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(NotScheduledVisitsActivity.this, "Error loading visits: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private int getServiceTypeOrder(String serviceType) { //Obtener documentos y ordenarlos por tipo de servicio, luego actualizar el adaptador
        switch (serviceType) {
            case "Repair":
                return 1;
            case "Maintenance Visit":
                return 2;
            case "New Install/replacement":
                return 3;
            default:
                return Integer.MAX_VALUE;
        }
    }
}