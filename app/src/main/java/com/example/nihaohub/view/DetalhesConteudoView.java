package com.example.nihaohub.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nihaohub.R;
import com.example.nihaohub.controller.MostrarConteudoController;
import com.google.android.material.textfield.TextInputEditText;
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
    private ListView comentariosConteudo;
    private FirebaseUser currentUser;
    private ScrollView scrollView;
    private TextInputEditText comentarioConteudo;
    private ImageView like;
    private ImageView dislike;
    private TextView qtnLikes;
    private TextView qtnDislikes;

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
        comentariosConteudo = findViewById(R.id.comentariosConteudo);
        scrollView = findViewById(R.id.scrollView);
        comentarioConteudo = findViewById(R.id.comentarioConteudo);
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        qtnLikes = findViewById(R.id.qtnLikes);
        qtnDislikes = findViewById(R.id.qtnDislikes);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            mostrarConteudoController = new MostrarConteudoController(this, nomeConteudo, autor, descricao, tema, nomeConteudoDetalhado, ws, comentariosConteudo, currentUser.getEmail(), comentarioConteudo, qtnLikes, qtnDislikes, like, dislike);
            mostrarConteudoController.mostrarDetalhesConteudo();
            mostrarConteudoController.exibirPdf();
            mostrarConteudoController.pegarIdEducador();
        }
    }

    public void dowloadConteudo(View view) {
        mostrarConteudoController.dowloadConteudo();
    }

    public void inserirComentario(View view) {
        if (!String.valueOf(comentarioConteudo.getText()).isEmpty()) {
            mostrarConteudoController.inserirComentario();
            Toast.makeText(DetalhesConteudoView.this, "Comentário adicionado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetalhesConteudoView.this, "Insira um comentário primeiro", Toast.LENGTH_SHORT).show();
        }
    }

    public void like(View view) {
        dislike.setBackgroundColor(Color.parseColor("#FFFFFF"));
        like.setBackgroundColor(Color.parseColor("#ADE8F4"));
        mostrarConteudoController.darLike();
    }

    public void dislike(View view) {
        like.setBackgroundColor(Color.parseColor("#FFFFFF"));
        dislike.setBackgroundColor(Color.parseColor("#E5383B"));
        mostrarConteudoController.darDislike();
    }
}