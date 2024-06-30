package com.example.apphydrojetting.activity.ui.contact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactViewModel extends ViewModel {
    //Declaración de MutableLiveData para el texto
    private final MutableLiveData<String> mText;
    //Constructor que inicializa el MutableLiveData con "Contact Us"
    public ContactViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Contact Us");
    }
    //Método para obtener el texto como LiveData
    public LiveData<String> getText() {
        return mText;
    }
}