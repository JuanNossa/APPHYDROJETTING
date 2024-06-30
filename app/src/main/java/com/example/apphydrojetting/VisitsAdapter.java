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

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.VisitViewHolder> {

    private List<Map<String, Object>> visits;
    private int currentStatus;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public VisitsAdapter(List<Map<String, Object>> visits, int currentStatus) {
        this.visits = visits;
        this.currentStatus = currentStatus;
    }

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit, parent, false);
        return new VisitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
        Map<String, Object> visit = visits.get(position);
        String visitId = (String) visit.get("id");
        String statusText = getStatusText((Long) visit.get("status"));

        holder.visitInfo.setText(getVisitDetails(visit, statusText));

        holder.scheduledButton.setVisibility((currentStatus == 1) ? View.GONE : View.VISIBLE);
        holder.notScheduledButton.setVisibility((currentStatus == 2) ? View.GONE : View.VISIBLE);
        holder.closedButton.setVisibility((currentStatus == 3) ? View.GONE : View.VISIBLE);

        holder.scheduledButton.setOnClickListener(v -> updateVisitStatus(visitId, 1, position, holder));
        holder.notScheduledButton.setOnClickListener(v -> updateVisitStatus(visitId, 2, position, holder));
        holder.closedButton.setOnClickListener(v -> updateVisitStatus(visitId, 3, position, holder));
    }

    private String getVisitDetails(Map<String, Object> visit, String statusText) {
        return "First Name: " + visit.get("firstName") + "\n" +
                "Last Name: " + visit.get("lastName") + "\n" +
                "Email: " + visit.get("email") + "\n" +
                "Street Address: " + visit.get("streetAddress") + "\n" +
                "City: " + visit.get("city") + "\n" +
                "Phone: " + visit.get("phone") + "\n" +
                "Service Type: " + visit.get("serviceType") + "\n" +
                "What Need: " + visit.get("whatNeed") + "\n" +
                "Home or Business: " + visit.get("homeOrBusiness") + "\n" +
                "Description: " + visit.get("description") + "\n" +
                "Date: " + visit.get("date") + "\n" +
                "Available Time: " + visit.get("availableTime") + "\n" +
                "Status: " + statusText;
    }

    private void updateVisitStatus(String visitId, int status, int position, VisitViewHolder holder) {
        db.collection("appointments").document(visitId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    visits.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, visits.size());
                    Toast.makeText(holder.itemView.getContext(), "Status updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Error updating status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getStatusText(long status) {
        return (status == 1) ? "Scheduled" : (status == 2) ? "Not Scheduled" : "Closed";
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public static class VisitViewHolder extends RecyclerView.ViewHolder {
        TextView visitInfo;
        Button scheduledButton, notScheduledButton, closedButton;

        public VisitViewHolder(@NonNull View itemView) {
            super(itemView);
            visitInfo = itemView.findViewById(R.id.visit_info);
            scheduledButton = itemView.findViewById(R.id.button_scheduled);
            notScheduledButton = itemView.findViewById(R.id.button_not_scheduled);
            closedButton = itemView.findViewById(R.id.button_closed);
        }
    }
}