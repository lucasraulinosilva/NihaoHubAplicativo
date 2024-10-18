package com.example.nihaohub.controller;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nihaohub.R;
import com.example.nihaohub.model.Area;
import com.example.nihaohub.model.Educador;
import com.example.nihaohub.model.Estudante;
import com.example.nihaohub.model.Formacao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class CadastroController extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private Activity activity;

    public CadastroController(FirebaseAuth mAuth, Activity activity) {
        this.mAuth = mAuth;
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void cadastrarEstudante(String nome, String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(activity, "Verifique seu email.",
                                    Toast.LENGTH_SHORT).show();
                            cadastrarEstudanteStorage(nome, email);
                        } else {
                            Toast.makeText(activity, "Algo deu errado.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cadastrarEducador(String nome, String email, String senha,  String area, String formacao) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(activity, "Verifique seu email.",
                                    Toast.LENGTH_SHORT).show();
                            cadastrarEducadorStorage(nome, email, area, formacao);
                        } else {
                            Toast.makeText(activity, "Algo deu errado.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cadastrarEstudanteStorage(String nome, String email) {
        mDatabase.child("Estudante").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Estudante[] estudantes = gson.fromJson(json, Estudante[].class);
                    Estudante estudante = new Estudante((estudantes[estudantes.length - 1].getIdEstudante()) + 1, nome, email);
                    mDatabase.child("Estudante").child(String.valueOf((estudantes[estudantes.length - 1].getIdEstudante()) + 1)).setValue(estudante);
                }
            }
        });
    }

    public void cadastrarEducadorStorage(String nome, String email, String area, String formacao) {
        mDatabase.child("Educador").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Educador[] educadores = gson.fromJson(json, Educador[].class);
                    Educador educador = new Educador((educadores[educadores.length - 1].getIdEducador()) + 1, nome, email);
                    mDatabase.child("Educador").child(String.valueOf((educadores[educadores.length - 1].getIdEducador()) + 1)).setValue(educador);
                    cadastrarAreaStorage(area, (educadores[educadores.length - 1].getIdEducador()) + 1);
                    cadastrarFormacaoStorage(formacao, (educadores[educadores.length - 1].getIdEducador()) + 1);
                }
            }
        });
    }

    public void cadastrarAreaStorage(String nome, Integer idEducador) {
        mDatabase.child("Area").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Area[] areas = gson.fromJson(json, Area[].class);
                    Area area = new Area((areas[areas.length - 1].getIdArea()) + 1, idEducador, nome);
                    mDatabase.child("Area").child(String.valueOf((areas[areas.length - 1].getIdArea()) + 1)).setValue(area);
                }
            }
        });
    }

    public void cadastrarFormacaoStorage(String nome, Integer idEducador) {
        mDatabase.child("Formacao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Formacao[] formacoes = gson.fromJson(json, Formacao[].class);
                    Formacao formacao = new Formacao((formacoes[formacoes.length - 1].getIdFormacao()) + 1, idEducador, nome);
                    mDatabase.child("Formacao").child(String.valueOf((formacoes[formacoes.length - 1].getIdFormacao()) + 1)).setValue(formacao);
                }
            }
        });
    }
}