package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.example.seupostoestabelecimento.model.Usuario;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CadastroUsuarioEstabelecimentoActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha, campoRazaoSocial, campoCNPJ,
            campoNomefantasia, campoTelefone, campoCEP, campoRua,
            campoBairro, campoNumero, campoCidade, campoEstado;
    private ProgressBar progressCadastro;

    private LatLng enderecoEstabelecimento;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_estabelecimento);

        iniciarComponentes();

        progressCadastro.setVisibility(View.GONE);
    }

    public void btnDefinirEndereco(View view){
        Intent intent = new Intent(view.getContext(), DefinirEnderecoEstabelecimentoActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                double lat = data.getDoubleExtra("latitude",0);
                double lon = data.getDoubleExtra("longitude",0);
                enderecoEstabelecimento = new LatLng(lat,lon);
                Log.d("Teste","Endereço: "+enderecoEstabelecimento.toString());
                preencherEndereco();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void preencherEndereco(){
        try {
            //Geocoding -> processo de transformar um endereço ou descriç~çao de um local em LAT/LONG
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            List<Address> listEndereco = geocoder.getFromLocation(enderecoEstabelecimento.latitude, enderecoEstabelecimento.longitude,1);
            if(listEndereco != null && listEndereco.size()>0){
                Address endereco = listEndereco.get(0);
                Log.d("Teste"," "+endereco.toString());

                campoCEP.setText(endereco.getPostalCode());
                campoRua.setText(endereco.getThoroughfare());
                campoBairro.setText(endereco.getSubLocality());
                campoNumero.setText(endereco.getFeatureName());
                campoCidade.setText(endereco.getSubAdminArea());
                campoEstado.setText(endereco.getAdminArea());

            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("Teste", "Erro: "+ e.getMessage());
        }

    }

    public void btnCadastrarEstabelecimento(View view) {
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        String textoRazaoSocial = campoRazaoSocial.getText().toString();
        String textoNomefantasia = campoNomefantasia.getText().toString();
        String textoCNPJ = campoCNPJ.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        String textoCEP = campoCEP.getText().toString();
        String textoRua = campoRua.getText().toString();
        String textoBairro = campoBairro.getText().toString();
        String textoNumero = campoNumero.getText().toString();
        String textoCidade = campoCidade.getText().toString();
        String textoEstado = campoEstado.getText().toString();

        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {
                if (!textoRazaoSocial.isEmpty()) {
                    if (!textoNomefantasia.isEmpty()) {
                        if (!textoCNPJ.isEmpty()) {
                            if (!textoTelefone.isEmpty()) {
                                if (!textoCEP.isEmpty()) {
                                    if (!textoRua.isEmpty()) {
                                        if (!textoBairro.isEmpty()) {
                                            if (!textoNumero.isEmpty()) {
                                                if (!textoCidade.isEmpty()) {
                                                    if (!textoEstado.isEmpty()) {

                                                        Usuario usuario = new Usuario();
                                                        usuario.setEmail(textoEmail);
                                                        usuario.setSenha(textoSenha);

                                                        Estabelecimento estabelecimento = new Estabelecimento();

                                                        estabelecimento.setRazaoSocial(textoRazaoSocial);
                                                        estabelecimento.setNomeFantasia(textoNomefantasia);
                                                        estabelecimento.setCnpj(textoCNPJ);
                                                        estabelecimento.setTelefone(textoTelefone);
                                                        estabelecimento.setCep(textoCEP);
                                                        estabelecimento.setRua(textoRua);
                                                        estabelecimento.setBairro(textoBairro);
                                                        estabelecimento.setNumero(textoNumero);
                                                        estabelecimento.setCidade(textoCidade);
                                                        estabelecimento.setEstado(textoEstado);
                                                        estabelecimento.setLatitute(String.valueOf(enderecoEstabelecimento.latitude));
                                                        estabelecimento.setLongitude(String.valueOf(enderecoEstabelecimento.longitude));
                                                        estabelecimento.setUsuario(usuario);

                                                        cadastrarEstabelecimento(estabelecimento);

                                                    } else {
                                                        Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o estado", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha a cidade", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o numero", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o bairro", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha a rua", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o CEP", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o telefone", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o CNPJ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o nome fantasia", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha a razao social", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
        }

    }

    private void cadastrarEstabelecimento(final Estabelecimento estabelecimento) {

        progressCadastro.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                estabelecimento.getUsuario().getEmail(),
                estabelecimento.getUsuario().getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = autenticacao.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("estabelecimento").build();
                            user.updateProfile(profileUpdates);

                            estabelecimento.getUsuario().setId(autenticacao.getCurrentUser().getUid());
                            estabelecimento.criar();

                            progressCadastro.setVisibility(View.GONE);
                            Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

                            Toast.makeText(CadastroUsuarioEstabelecimentoActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
    }

    private void iniciarComponentes() {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        campoRazaoSocial = findViewById(R.id.editCadastroRazaoSocial);
        campoNomefantasia = findViewById(R.id.editCadastroNomeFantasia);
        campoCNPJ = findViewById(R.id.editCadastroCNPJ);
        campoTelefone = findViewById(R.id.editCadastroTelefone);
        campoCEP = findViewById(R.id.editCadastroCEP);
        campoRua = findViewById(R.id.editCadastroRua);
        campoBairro = findViewById(R.id.editCadastroBairro);
        campoNumero = findViewById(R.id.editCadastroNumero);
        campoCidade = findViewById(R.id.editCadastroCidade);
        campoEstado = findViewById(R.id.editCadastroEstado);
        progressCadastro = findViewById(R.id.progressCadastro);
        ImageButton imageButtonGpsEstabelecimento = findViewById(R.id.imageButtonGpsEstabelecimento);
    }
}
