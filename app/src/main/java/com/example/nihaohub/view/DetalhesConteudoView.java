package com.example.nihaohub.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nihaohub.R;
import com.example.nihaohub.controller.MostrarConteudoController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetalhesConteudoView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MostrarConteudoController mostrarConteudoController;
    private TextView autor;
    private TextView descricao;
    private TextView nomeConteudo;
    private TextView tema;
    private String nomeConteudoDetalhado;
    private WebView ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes_conteudo_view);

        mAuth = FirebaseAuth.getInstance();
        autor = findViewById(R.id.autorConteudoDetalhe);
        descricao = findViewById(R.id.descricaoConteudoDetalhe);
        nomeConteudo = findViewById(R.id.nomeConteudoDetalhe);
        tema = findViewById(R.id.temaConteudoDetalhe);
        nomeConteudoDetalhado = getIntent().getStringExtra("nomeConteudoDetalhado");
        ws = (WebView) findViewById(R.id.webView);

        mostrarConteudoController = new MostrarConteudoController(this, nomeConteudo, autor, descricao, tema, nomeConteudoDetalhado, ws);
        mostrarConteudoController.mostrarDetalhesConteudo();
        mostrarConteudoController.exibirPdf();
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

    public void dowloadConteudo(View view) {
        mostrarConteudoController.dowloadConteudo();
    }
}