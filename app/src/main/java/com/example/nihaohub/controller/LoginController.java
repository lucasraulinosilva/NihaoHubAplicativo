package com.example.nihaohub.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nihaohub.model.Administrador;
import com.example.nihaohub.model.Educador;
import com.example.nihaohub.model.Estudante;
import com.example.nihaohub.view.HomeAdministradorView;
import com.example.nihaohub.view.HomeEducadorView;
import com.example.nihaohub.view.HomeEstudanteView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class LoginController extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private Activity activity;

    public LoginController(FirebaseAuth mAuth, Activity activity) {
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

    public void logar(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Log.e("firebase", "USER " + String.valueOf(user));
                            if(user.isEmailVerified()) {
                                loginAdministrador(user.getEmail());
                                loginEducador(user.getEmail());
                                loginEstudante(user.getEmail());
                            } else {
                                Toast.makeText(activity, "Verifique seu email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            /*user.sendEmailVerification();*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(activity, "Não cadastrado.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loginAdministrador(String email) {
        mDatabase.child("Administrador").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Administrador[] administrador = gson.fromJson(json, Administrador[].class);

                    for(int i = 0; i < administrador.length; i++) {
                        if (administrador[i] != null) {
                            if (administrador[i].getLoginAdministrador().equals(email)) {
                                Intent intent = new Intent(activity, HomeAdministradorView.class);
                                activity.startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }

    public void loginEducador(String email) {
        mDatabase.child("Educador").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Educador[] educador = gson.fromJson(json, Educador[].class);

                    for(int i = 0; i < educador.length; i++) {
                        if (educador[i] != null) {
                            if (educador[i].getLoginEducador().equals(email)) {
                                Intent intent = new Intent(activity, HomeEducadorView.class);
                                activity.startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }

    public void loginEstudante(String email) {
        mDatabase.child("Estudante").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Estudante[] estudante = gson.fromJson(json, Estudante[].class);

                    for(int i = 0; i < estudante.length; i++) {
                        if (estudante[i] != null) {
                            if (estudante[i].getLoginEstudante().equals(email)) {
                                Intent intent = new Intent(activity, HomeEstudanteView.class);
                                activity.startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }
}
