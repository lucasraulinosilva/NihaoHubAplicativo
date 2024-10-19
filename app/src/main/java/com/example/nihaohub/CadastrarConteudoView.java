package com.example.nihaohub;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nihaohub.controller.CadastrarConteudoController;
import com.example.nihaohub.model.Conteudo;
import com.example.nihaohub.model.Educador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CadastrarConteudoView extends AppCompatActivity {
    private TextInputEditText autor;
    private TextInputEditText descricao;
    private TextInputEditText tema;
    private FirebaseAuth mAuth;
    private UploadTask uploadTask;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://nihao-hub-default-rtdb.firebaseio.com/").getReference();
    private CadastrarConteudoController cadastrarConteudoController = new CadastrarConteudoController();
    private String nomeConteudo;
    private int idEducador;
    private FirebaseUser currentUser;
    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_conteudo_view);

        mAuth = FirebaseAuth.getInstance();

        autor = findViewById(R.id.autorCadastrarConteudo);
        descricao = findViewById(R.id.descricaoCadastrarConteudo);
        tema = findViewById(R.id.temaCadastrarConteudo);

        pegarIdEducador();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void escolherConteudo(View view) {
        openFile();
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        startActivityForResult(intent, PICK_PDF_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            // O arquivo foi selecionado com sucesso
            if (data != null) {
                // Pegue o URI do arquivo selecionado
                Uri pdfUri = data.getData();

                // Agora você pode usar o URI para abrir ou manipular o PDF
                // Exemplo: mostrando o URI em um log
                Log.d("Selected PDF", "PDF Uri: " + pdfUri.toString());
                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(pdfUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                nomeConteudo = getFileName(pdfUri);
                storageRef = storage.getReference().child(nomeConteudo);
                uploadTask = storageRef.putStream(stream);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    // Obtém o índice da coluna do nome do arquivo
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    result = cursor.getString(nameIndex);
                }
            } finally {
                if (cursor != null) {
                    cursor.close(); // Fecha o cursor após o uso
                }
            }
        }
        // Se não for um URI de conteúdo, retorna o nome do arquivo do URI
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public void cadastrarConteudo(View view) {
        if (!String.valueOf(autor.getText()).isEmpty() && !String.valueOf(tema.getText()).isEmpty() && !String.valueOf(descricao.getText()).isEmpty()) {
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CadastrarConteudoView.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Conteudo user = new Conteudo(cadastrarConteudoController.getIdConteudo(), idEducador, nomeConteudo, String.valueOf(autor.getText()), String.valueOf(descricao.getText()), String.valueOf(tema.getText()));

                    mDatabase.child("Conteudo").child(String.valueOf(cadastrarConteudoController.getIdConteudo())).setValue(user);
                    Toast.makeText(CadastrarConteudoView.this, "Conteúdo cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CadastrarConteudoView.this, "Preencha todos os campos", Toast.LENGTH_SHORT);
        }
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
                            if (educadores[i].getLoginEducador().equals(currentUser.getEmail())) {
                                idEducador = educadores[i].getIdEducador();
                            }
                        }
                    }
                }
            }
        });
    }

}