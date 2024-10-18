package com.example.nihaohub.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nihaohub.R;
import com.example.nihaohub.model.Conteudo;
import com.example.nihaohub.view.DetalhesConteudoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MostrarConteudoController{
    private FirebaseStorage storage = FirebaseStorage.getInstance();;
    private StorageReference storageRef = storage.getReference();;
    private ListView conteudosEducador;
    private ArrayList<String> nomesConteudos = new ArrayList<>();
    private Activity activity;
    private TextView autor;
    private TextView descricao;
    private TextView nomeConteudo;
    private TextView tema;
    private String nomeConteudoDetalhado;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private WebView ws;


    public MostrarConteudoController(ListView conteudosEducador, Activity activity) {
        this.conteudosEducador = conteudosEducador;
        this.activity = activity;
    }

    public MostrarConteudoController(Activity activity, TextView nomeConteudo, TextView autor, TextView descricao, TextView tema, String nomeConteudoDetalhado, WebView ws) {
        this.activity = activity;
        this.nomeConteudo = nomeConteudo;
        this.autor = autor;
        this.descricao = descricao;
        this.tema = tema;
        this.nomeConteudoDetalhado = nomeConteudoDetalhado;
        this.ws = ws;
    }

    public void mostrarConteudos() {
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            StorageReference gsReference = storage.getReferenceFromUrl(String.valueOf(item));
                            String nome = gsReference.getName().replaceAll("%20", " ");
                            nome = nome.replaceAll("%C3%A7", "ç");
                            nome = nome.replaceAll("%C3%A3", "ã");
                            nomesConteudos.add(nome);
                        }
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, nomesConteudos );
                        conteudosEducador.setAdapter(adaptador);

                        conteudosEducador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String nome = String.valueOf(parent.getAdapter().getItem(position));
                                Intent intent = new Intent(activity, DetalhesConteudoView.class);
                                intent.putExtra("nomeConteudoDetalhado", nome);
                                activity.startActivity(intent);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    public void mostrarDetalhesConteudo() {
        mDatabase.child("Conteudo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Conteudo[] conteudos = gson.fromJson(json, Conteudo[].class);

                    for (int i = 0; i < conteudos.length; i++) {
                        if (conteudos[i] != null) {
                            if (conteudos[i].getNomeConteudo().equals(nomeConteudoDetalhado)) {
                                nomeConteudo.setText(conteudos[i].getNomeConteudo());
                                autor.setText(conteudos[i].getAutor());
                                tema.setText(conteudos[i].getTema());
                                descricao.setText(conteudos[i].getDescricao());
                            }
                        }
                    }
                }
            }
        });
    }

    public void dowloadConteudo() {
        storageRef.child(nomeConteudoDetalhado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void exibirPdf() {
        storageRef.child(nomeConteudoDetalhado).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ws.getSettings().setJavaScriptEnabled(true);
                ws.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
