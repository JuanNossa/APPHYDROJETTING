package com.example.apphydrojetting.activity.ui.contact;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.apphydrojetting.databinding.FragmentContactBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContactFragment extends Fragment {

    private FragmentContactBinding binding;
    //Declaración de variable para la conexión a Firebase
    private FirebaseFirestore db;

    @Override //Inicialización de ContactViewModel usando ViewModelProvider
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.titleContactUs;
        contactViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Inicialización de Firestore
        db = FirebaseFirestore.getInstance();
        //Asignar OnClickListener para mostrar el DatePickerDialog
        binding.etDate.setOnClickListener(v -> showDatePickerDialog());
        //Asignar OnClickListener para enviar el formulario
        binding.btnRequestAppointment.setOnClickListener(v -> submitForm());

        return root;
    }

    //Datapicker para selección de la fecha con el limitante de que debe ser el mismo día o superior
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //Crear y mostrar el DatePickerDialog con la fecha mínima de hoy
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> binding.etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //Método para enviar el formulario
    private void submitForm() {
        // Captura de datos del formulario
        String serviceType = ((RadioButton) getView().findViewById(binding.serviceTypeGroup.getCheckedRadioButtonId())).getText().toString();
        String whatNeed = "";
        if (binding.cbPlumbing.isChecked()) whatNeed += "Plumbing ";
        if (binding.cbDuctCleaning.isChecked()) whatNeed += "Duct cleaning/sealing ";
        String homeOrBusiness = ((RadioButton) getView().findViewById(binding.homeBusinessGroup.getCheckedRadioButtonId())).getText().toString();
        String description = binding.etDescription.getText().toString();
        String dateStr = binding.etDate.getText().toString();
        String availableTime = ((RadioButton) getView().findViewById(binding.timeGroup.getCheckedRadioButtonId())).getText().toString();
        String firstName = binding.etFirstName.getText().toString();
        String lastName = binding.etLastName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String streetAddress = binding.etStreetAddress.getText().toString();
        String city = binding.etCity.getText().toString();
        String phone = binding.etPhone.getText().toString();
        int status = 2;
        int serviceTypeOrder = 0;
        //Validación de campos obligatorios
        if (serviceType.isEmpty() || whatNeed.isEmpty() || homeOrBusiness.isEmpty() || description.isEmpty() ||
                dateStr.isEmpty() || availableTime.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                email.isEmpty() || streetAddress.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        //Conversión de la fecha a timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timestamp = date != null ? date.getTime() : 0;

        //asignar tipo de servicios
        if (serviceType.equalsIgnoreCase("Repair")){
            serviceTypeOrder = 1;
        }
        if (serviceType.equalsIgnoreCase("Maintenance Visit")){
            serviceTypeOrder = 2;
        }
        if (serviceType.equalsIgnoreCase("New Install/replacement")){
            serviceTypeOrder = 3;
        }
        //Creación del mapa de datos y envío a Firestore
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("serviceType", serviceType);
        appointment.put("whatNeed", whatNeed);
        appointment.put("homeOrBusiness", homeOrBusiness);
        appointment.put("description", description);
        appointment.put("date", dateStr);
        appointment.put("timestamp", timestamp);
        appointment.put("availableTime", availableTime);
        appointment.put("firstName", firstName);
        appointment.put("lastName", lastName);
        appointment.put("email", email);
        appointment.put("streetAddress", streetAddress);
        appointment.put("city", city);
        appointment.put("phone", phone);
        appointment.put("status", status);
        appointment.put("serviceTypeOrder", serviceTypeOrder);

        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Appointment requested successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to request appointment", Toast.LENGTH_SHORT).show());
    }

    //Método para limpiar el formulario
    private void clearForm() {
        binding.serviceTypeGroup.clearCheck();
        binding.cbPlumbing.setChecked(false);
        binding.cbDuctCleaning.setChecked(false);
        binding.homeBusinessGroup.clearCheck();
        binding.etDescription.setText("");
        binding.etDate.setText("");
        binding.timeGroup.clearCheck();
        binding.etFirstName.setText("");
        binding.etLastName.setText("");
        binding.etEmail.setText("");
        binding.etStreetAddress.setText("");
        binding.etCity.setText("");
        binding.etPhone.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

