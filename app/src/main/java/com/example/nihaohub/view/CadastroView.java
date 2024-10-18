package com.example.nihaohub.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nihaohub.R;
import com.example.nihaohub.controller.CadastroController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CadastroController cadastroController;
    private TextInputEditText nome;
    private TextInputEditText email;
    private TextInputEditText senha;
    private TextInputEditText area;
    private TextInputEditText formacao;
    private CheckBox termos;
    private TextInputLayout layoutArea;
    private TextInputLayout layoutFormacao;
    private CheckBox educadorCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_view);

        mAuth = FirebaseAuth.getInstance();

        nome = findViewById(R.id.nomeCadastrar);
        email = findViewById(R.id.emailCadastrar);
        senha = findViewById(R.id.senhaCadastrar);
        area = findViewById(R.id.areaCadastrar);
        formacao = findViewById(R.id.formacaoCadastrar);
        termos = findViewById(R.id.checkBoxTermos);
        layoutArea = findViewById(R.id.layoutArea);
        layoutFormacao = findViewById(R.id.layoutFormacao);

        area.setVisibility(View.INVISIBLE);
        layoutArea.setVisibility(View.INVISIBLE);
        formacao.setVisibility(View.INVISIBLE);
        layoutFormacao.setVisibility(View.INVISIBLE);

        educadorCheckBox = findViewById(R.id.educadorCheckBox);
        educadorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    area.setVisibility(View.VISIBLE);
                    layoutArea.setVisibility(View.VISIBLE);
                    formacao.setVisibility(View.VISIBLE);
                    layoutFormacao.setVisibility(View.VISIBLE);
                } else {
                    area.setVisibility(View.INVISIBLE);
                    layoutArea.setVisibility(View.INVISIBLE);
                    formacao.setVisibility(View.INVISIBLE);
                    layoutFormacao.setVisibility(View.INVISIBLE);
                }
            }
        });

        cadastroController = new CadastroController(mAuth, this);
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

    public void cadastrarUsuario(View view) {
        if (!String.valueOf(nome.getText()).isEmpty() && !String.valueOf(email.getText()).isEmpty() && !String.valueOf(senha.getText()).isEmpty() && termos.isChecked() && !educadorCheckBox.isChecked()) {
            cadastroController.cadastrarEstudante(String.valueOf(nome.getText()), String.valueOf(email.getText()), String.valueOf(senha.getText()));
        } else if (!String.valueOf(nome.getText()).isEmpty() && !String.valueOf(email.getText()).isEmpty() && !String.valueOf(senha.getText()).isEmpty() && !String.valueOf(area.getText()).isEmpty() && !String.valueOf(formacao.getText()).isEmpty() && termos.isChecked() && educadorCheckBox.isChecked()) {
            cadastroController.cadastrarEducador(String.valueOf(nome.getText()), String.valueOf(email.getText()), String.valueOf(senha.getText()), String.valueOf(area.getText()), String.valueOf(formacao.getText()));
        } else {
            Toast.makeText(CadastroView.this, "Preencha todos os campos.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
