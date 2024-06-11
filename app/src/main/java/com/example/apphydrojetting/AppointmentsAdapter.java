package com.example.apphydrojetting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Map<String, Object>> appointments;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AppointmentsAdapter(List<Map<String, Object>> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Map<String, Object> appointment = appointments.get(position);
        String appointmentId = (String) appointment.get("id");
        String statusText = ((Long) appointment.get("status") == 1) ? "Scheduled" : "Not Scheduled";

        holder.appointmentInfo.setText(getAppointmentDetails(appointment, statusText));

        holder.scheduledButton.setOnClickListener(v -> updateAppointmentStatus(appointmentId, 1, holder, appointment));
        holder.notScheduledButton.setOnClickListener(v -> updateAppointmentStatus(appointmentId, 2, holder, appointment));
    }

    private String getAppointmentDetails(Map<String, Object> appointment, String statusText) {
        return "First Name: " + appointment.get("firstName") + "\n" +
                "Last Name: " + appointment.get("lastName") + "\n" +
                "Email: " + appointment.get("email") + "\n" +
                "Street Address: " + appointment.get("streetAddress") + "\n" +
                "City: " + appointment.get("city") + "\n" +
                "Phone: " + appointment.get("phone") + "\n" +
                "Service Type: " + appointment.get("serviceType") + "\n" +
                "What Need: " + appointment.get("whatNeed") + "\n" +
                "Home or Business: " + appointment.get("homeOrBusiness") + "\n" +
                "Description: " + appointment.get("description") + "\n" +
                "Date: " + appointment.get("date") + "\n" +
                "Available Time: " + appointment.get("availableTime") + "\n" +
                "Status: " + statusText;
    }

    private void updateAppointmentStatus(String appointmentId, int status, AppointmentViewHolder holder, Map<String, Object> appointment) {
        db.collection("appointments").document(appointmentId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    String statusText = (status == 1) ? "Scheduled" : "Not Scheduled";
                    appointment.put("status", (long) status);
                    holder.appointmentInfo.setText(getAppointmentDetails(appointment, statusText));
                    Toast.makeText(holder.itemView.getContext(), "Status updated to " + statusText, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Error updating status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentInfo;
        Button scheduledButton, notScheduledButton;

        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentInfo = itemView.findViewById(R.id.appointment_info);
            scheduledButton = itemView.findViewById(R.id.button_scheduled);
            notScheduledButton = itemView.findViewById(R.id.button_not_scheduled);
        }
    }
}