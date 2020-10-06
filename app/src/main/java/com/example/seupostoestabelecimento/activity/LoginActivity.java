package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.example.seupostoestabelecimento.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private ProgressBar progressLogin;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();
        inicializarComponentes();
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            if(autenticacao.getCurrentUser().getDisplayName().equals("usuario")){
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            }else if(autenticacao.getCurrentUser().getDisplayName().equals("estabelecimento")){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        }
    }

    public void btnEntrar(View view){
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoEmail.isEmpty()){
            if(!textoSenha.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                validarLogin(usuario);

            }else{
                Toast.makeText(LoginActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
        }

    }

    private void validarLogin(Usuario usuario){
        progressLogin.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("Teste","DisplayName: "+autenticacao.getCurrentUser().getDisplayName()+" UID: "+autenticacao.getCurrentUser().getUid());
                    progressLogin.setVisibility(View.GONE);

                    if(autenticacao.getCurrentUser().getDisplayName().equals("usuario")){
                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                    }else if(autenticacao.getCurrentUser().getDisplayName().equals("estabelecimento")){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                    progressLogin.setVisibility(View.GONE);
                }
            }
        });
    }

    public void abrirCadastro(View view){
        Intent i = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(i);
    }

    public void abrirCadastroEstabelecimento(View view){
        Intent i = new Intent(LoginActivity.this, CadastroUsuarioEstabelecimentoActivity.class);
        startActivity(i);
    }

    private void inicializarComponentes(){
        progressLogin = findViewById(R.id.progressLogin);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
    }
}
