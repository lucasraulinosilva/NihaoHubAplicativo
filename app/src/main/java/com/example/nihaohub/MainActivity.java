package com.example.nihaohub;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nihaohub.controller.LoginController;
import com.example.nihaohub.model.Administrador;
import com.example.nihaohub.view.CadastroView;
import com.example.nihaohub.view.HomeAdministradorView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private TextInputEditText email;
    private TextInputEditText password;
    private LoginController login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        login = new LoginController(mAuth, this);
        email = findViewById(R.id.emailLogar);
        password = findViewById(R.id.senhaLogar);

        //FirebaseAuth.getInstance().signOut();
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {

        }
    }

    public void login(View view) {
        if (!String.valueOf(email.getText()).isEmpty() && !String.valueOf(password.getText()).isEmpty()) {
            login.logar(String.valueOf(email.getText()), String.valueOf(password.getText()));
        }
    }

    public void cadastrar(View view) {
        Intent intent = new Intent(MainActivity.this, CadastroView.class);
        startActivity(intent);
    }
}