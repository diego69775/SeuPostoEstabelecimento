package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;

    private ProgressBar progressCadastro;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        iniciarComponentes();

        progressCadastro.setVisibility(View.GONE);
    }

    public void btnCadastrarUsuario(View view) {
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                cadastrarUsuario(usuario);

            } else {
                Toast.makeText(CadastroUsuarioActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroUsuarioActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
        }

    }

    private void cadastrarUsuario(final Usuario usuario) {

        progressCadastro.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = autenticacao.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("usuario").build();
                            user.updateProfile(profileUpdates);

                            progressCadastro.setVisibility(View.GONE);
                            Toast.makeText(CadastroUsuarioActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                            finish();

                        } else {

                            progressCadastro.setVisibility(View.GONE);

                            String erroExcecao;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "Digite um e-mail valido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Este e-mail ja foi cadastrado";
                            } catch (Exception e) {
                                erroExcecao = "Erro ao cadastrar usuario: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
    }

    private void iniciarComponentes() {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        progressCadastro = findViewById(R.id.progressCadastro);
    }

}
