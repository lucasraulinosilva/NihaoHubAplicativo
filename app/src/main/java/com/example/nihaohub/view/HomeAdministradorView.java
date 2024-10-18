package com.example.nihaohub.view;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nihaohub.R;
import com.example.nihaohub.controller.MostrarConteudoController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdministradorView extends AppCompatActivity {
    private MostrarConteudoController mostrarConteudoController;
    private FirebaseAuth mAuth;
    private ListView conteudosEducador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_administrador_view);
        mAuth = FirebaseAuth.getInstance();

        conteudosEducador = findViewById(R.id.conteudosAdministrador);

        mostrarConteudoController = new MostrarConteudoController(conteudosEducador, this);
        mostrarConteudoController.mostrarConteudos();
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
}