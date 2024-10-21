package com.example.nihaohub.controller;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nihaohub.R;
import com.example.nihaohub.model.Avaliacao;
import com.example.nihaohub.model.Comentario;
import com.example.nihaohub.model.Conteudo;
import com.example.nihaohub.model.Educador;
import com.example.nihaohub.model.Estudante;
import com.example.nihaohub.view.DetalhesConteudoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class MostrarConteudoController{
    private FirebaseStorage storage = FirebaseStorage.getInstance();;
    private StorageReference storageRef = storage.getReference();;
    private ListView conteudosEducador;
    private ListView comentariosConteudo;
    private ArrayList<String> nomesConteudos = new ArrayList<>();
    private ArrayList<String> comentariosConteudoValor = new ArrayList<>();
    private Activity activity;
    private TextView autor;
    private TextView descricao;
    private TextView nomeConteudo;
    private TextView tema;
    private String nomeConteudoDetalhado;
    private String emailUsuario;
    private int idUsuario;
    private int idConteudo;
    private String nomeUsuario;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private WebView ws;
    private TextInputEditText comentarioConteudo;
    private TextView qtnLikes;
    private TextView qtnDislikes;
    private int contadorQtnLikes;
    private int contadorQtnDislikes;
    private boolean found = false;
    private String tipoAvaliacao = "";
    private int idAvaliacaoMudar = 0;
    private ImageView imgLike;
    private ImageView imgDislike;

    public MostrarConteudoController(ListView conteudosEducador, Activity activity) {
        this.conteudosEducador = conteudosEducador;
        this.activity = activity;
    }

    public MostrarConteudoController(Activity activity, TextView nomeConteudo, TextView autor, TextView descricao, TextView tema, String nomeConteudoDetalhado, WebView ws, ListView comentariosConteudo, String emailUsuario, TextInputEditText comentarioConteudo, TextView qtnLikes, TextView qtnDislikes, ImageView imgLike,ImageView imgDislike) {
        this.activity = activity;
        this.nomeConteudo = nomeConteudo;
        this.autor = autor;
        this.descricao = descricao;
        this.tema = tema;
        this.nomeConteudoDetalhado = nomeConteudoDetalhado;
        this.ws = ws;
        this.comentariosConteudo = comentariosConteudo;
        this.emailUsuario = emailUsuario;
        this.comentarioConteudo = comentarioConteudo;
        this.qtnLikes = qtnLikes;
        this.qtnDislikes = qtnDislikes;
        this.imgLike = imgLike;
        this.imgDislike = imgDislike;
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
                                idConteudo = conteudos[i].getIdConteudo();
                                pegarComentarios();
                            }
                        }
                    }

                    qtnLikes();
                }
            }
        });
    }

    public void pegarComentarios() {
        mDatabase.child("Comentario").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Comentario[] comentarios = gson.fromJson(json, Comentario[].class);

                    for (int i = 0; i < comentarios.length; i++) {
                        if (comentarios[i] != null) {
                            if (comentarios[i].getIdConteudo() == idConteudo) {
                                comentariosConteudoValor.add(comentarios[i].getNomeAutorComentario() +": " + comentarios[i].getComentario());
                            }
                        }
                    }

                    ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, comentariosConteudoValor);
                    comentariosConteudo.setAdapter(adaptador2);
                    setListViewHeightBasedOnItems(comentariosConteudo);
                }
            }
        });
    }

    public void pegarIdEducador() {
        mDatabase.child("Educador").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Educador[] educadores = gson.fromJson(json, Educador[].class);

                    for (int i = 0; i < educadores.length; i++) {
                        if (educadores[i] != null) {
                            if (educadores[i].getLoginEducador().equals(emailUsuario)) {
                                idUsuario = educadores[i].getIdEducador();
                                nomeUsuario = educadores[i].getNomeEducador();
                            }
                        }
                    }
                }
            }
        });
    }

    public void qtnLikes() {
        mDatabase.child("Avaliacao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Avaliacao[] avaliacoes = gson.fromJson(json, Avaliacao[].class);

                    for (int i = 0; i < avaliacoes.length; i++) {
                        if (avaliacoes[i] != null) {
                            if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getAvaliacao().equals("true")) {
                                contadorQtnLikes++;
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getAvaliacao().equals("false")) {
                                contadorQtnDislikes++;
                            }

                            if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getAvaliacao().equals("true") && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario)) {
                                imgDislike.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                imgLike.setBackgroundColor(Color.parseColor("#ADE8F4"));
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getAvaliacao().equals("false") && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario)) {
                                imgLike.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                imgDislike.setBackgroundColor(Color.parseColor("#E5383B"));
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getAvaliacao().equals("") && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario)) {
                                imgLike.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                imgDislike.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                        }
                    }

                    qtnLikes.setText(String.valueOf(contadorQtnLikes));
                    qtnDislikes.setText(String.valueOf(contadorQtnDislikes));
                    contadorQtnLikes = 0;
                    contadorQtnDislikes = 0;
                }
            }
        });
    }

    public void darLike() {
        mDatabase.child("Avaliacao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Avaliacao[] avaliacoes = gson.fromJson(json, Avaliacao[].class);

                    for (int i = 0; i < avaliacoes.length; i++) {
                        if (avaliacoes[i] != null) {
                            if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && (avaliacoes[i].getAvaliacao().equals("false") || avaliacoes[i].getAvaliacao().equals("")) && !found) {
                                tipoAvaliacao = "mudarTrue";
                                idAvaliacaoMudar = avaliacoes[i].getIdAvaliacao();
                                found = true;
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && avaliacoes[i].getAvaliacao().equals("true") && !found) {
                                tipoAvaliacao = "nenhuma";
                                idAvaliacaoMudar = avaliacoes[i].getIdAvaliacao();
                                found = true;
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && !avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && !found) {
                                tipoAvaliacao = "naoExiste";
                            } else if (avaliacoes[i].getIdConteudo() != idConteudo && !avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && !found) {
                                tipoAvaliacao = "naoExiste";
                            }
                        }
                    }

                    if (tipoAvaliacao.equals("naoExiste")) {
                        Avaliacao avaliacao = new Avaliacao((avaliacoes[avaliacoes.length - 1].getIdAvaliacao()) + 1, idConteudo, nomeUsuario, "true");
                        mDatabase.child("Avaliacao").child(String.valueOf((avaliacoes[avaliacoes.length - 1].getIdAvaliacao()) + 1)).setValue(avaliacao);
                    }

                    if (tipoAvaliacao.equals("mudarTrue")) {
                        Avaliacao avaliacao = new Avaliacao(idAvaliacaoMudar, idConteudo, nomeUsuario, "true");
                        mDatabase.child("Avaliacao").child(String.valueOf(idAvaliacaoMudar)).setValue(avaliacao);
                    }

                    if(tipoAvaliacao.equals("nenhuma")) {
                        Avaliacao avaliacao = new Avaliacao(idAvaliacaoMudar, idConteudo, nomeUsuario, "");
                        mDatabase.child("Avaliacao").child(String.valueOf(idAvaliacaoMudar)).setValue(avaliacao);
                    }

                    qtnLikes();
                    found = false;
                }
            }
        });
    }

    public void darDislike() {
        mDatabase.child("Avaliacao").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Avaliacao[] avaliacoes = gson.fromJson(json, Avaliacao[].class);

                    for (int i = 0; i < avaliacoes.length; i++) {
                        if (avaliacoes[i] != null) {
                            if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && (avaliacoes[i].getAvaliacao().equals("true") || avaliacoes[i].getAvaliacao().equals("")) && !found) {
                                tipoAvaliacao = "mudarFalse";
                                idAvaliacaoMudar = avaliacoes[i].getIdAvaliacao();
                                found = true;
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && avaliacoes[i].getAvaliacao().equals("false") && !found) {
                                tipoAvaliacao = "nenhuma";
                                idAvaliacaoMudar = avaliacoes[i].getIdAvaliacao();
                                found = true;
                            } else if (avaliacoes[i].getIdConteudo() == idConteudo && !avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && !found) {
                                tipoAvaliacao = "naoExiste";
                            } else if (avaliacoes[i].getIdConteudo() != idConteudo && !avaliacoes[i].getNomeAutorComentario().equals(nomeUsuario) && !found) {
                                tipoAvaliacao = "naoExiste";
                            }
                        }
                    }

                    if (tipoAvaliacao.equals("naoExiste")) {
                        Avaliacao avaliacao = new Avaliacao((avaliacoes[avaliacoes.length - 1].getIdAvaliacao()) + 1, idConteudo, nomeUsuario, "false");
                        mDatabase.child("Avaliacao").child(String.valueOf((avaliacoes[avaliacoes.length - 1].getIdAvaliacao()) + 1)).setValue(avaliacao);
                    }

                    if (tipoAvaliacao.equals("mudarFalse")) {
                        Avaliacao avaliacao = new Avaliacao(idAvaliacaoMudar, idConteudo, nomeUsuario, "false");
                        mDatabase.child("Avaliacao").child(String.valueOf(idAvaliacaoMudar)).setValue(avaliacao);
                    }

                    if(tipoAvaliacao.equals("nenhuma")) {
                        Avaliacao avaliacao = new Avaliacao(idAvaliacaoMudar, idConteudo, nomeUsuario, "");
                        mDatabase.child("Avaliacao").child(String.valueOf(idAvaliacaoMudar)).setValue(avaliacao);
                    }

                    qtnLikes();
                    found = false;
                }
            }
        });
    }

    public void pegarIdEstudante() {
        mDatabase.child("Estudante").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Estudante[] estudantes = gson.fromJson(json, Estudante[].class);

                    for (int i = 0; i < estudantes.length; i++) {
                        if (estudantes[i] != null) {
                            if (estudantes[i].getLoginEstudante().equals(emailUsuario)) {
                                idUsuario = estudantes[i].getIdEstudante();
                                nomeUsuario = estudantes[i].getNomeEstudante();
                            }
                        }
                    }
                }
            }
        });
    }

    public void pegarIdAdministrador() {

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

    public void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // Sem adaptador, nada a fazer.
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void inserirComentario() {
        mDatabase.child("Comentario").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(task.getResult().getValue());
                    Comentario[] comentarios = gson.fromJson(json, Comentario[].class);
                    Comentario comentario = new Comentario((comentarios[comentarios.length - 1].getIdComentario()) + 1, idConteudo, String.valueOf(comentarioConteudo.getText()), nomeUsuario);
                    mDatabase.child("Comentario").child(String.valueOf((comentarios[comentarios.length - 1].getIdComentario()) + 1)).setValue(comentario);
                }
            }
        });
    }

}
