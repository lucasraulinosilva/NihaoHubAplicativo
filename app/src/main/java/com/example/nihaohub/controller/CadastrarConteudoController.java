package com.example.nihaohub.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nihaohub.model.Conteudo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class CadastrarConteudoController {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private int idConteudo;

    public CadastrarConteudoController() {
        pegarIdConteudo();
    }

    public int getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(int idConteudo) {
        this.idConteudo = idConteudo;
    }

    public void pegarIdConteudo() {
        mDatabase.child("Conteudo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Conteudo[] conteudos = gson.fromJson(json, Conteudo[].class);

                    idConteudo = conteudos[conteudos.length - 1].getIdConteudo() + 1;
                }
            }
        });
    }
}
