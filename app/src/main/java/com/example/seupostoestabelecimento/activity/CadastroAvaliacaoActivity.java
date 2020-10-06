package com.example.seupostoestabelecimento.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.example.seupostoestabelecimento.model.Avaliacao;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CadastroAvaliacaoActivity extends AppCompatActivity {

    private RatingBar ratingBarAvaliacao;
    private EditText editDescricao;

    private Estabelecimento estabelecimento;
    private Avaliacao avaliacao;
    private int idAvaliacao;
    private String idAvaliador;
    private String emailAvaliador;
    private boolean mesmoAvaliador = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_avaliacao);

        TextView txtNomeEstabelecimento;
        Button btnSalvarAvaliacao;

        //Configurar compenentes
        txtNomeEstabelecimento = findViewById(R.id.txtNomeEstabelecimento);
        ratingBarAvaliacao = findViewById(R.id.ratingBarAvaliacao);
        editDescricao = findViewById(R.id.editDescricao);
        btnSalvarAvaliacao = findViewById(R.id.btnSalvarAvaliacao);

        //Recuperar Estabelecimento
        estabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento");
        if(estabelecimento != null) {
            txtNomeEstabelecimento.setText(estabelecimento.getNomeFantasia());
        }

        //Verificar Avaliador
        verificarUsuario();

        //Configurar Texto
        if (estabelecimento != null) {

            //Verifica se ja possui avaliacao
            if(estabelecimento.getAvaliacao() != null) {
                for (int i = 0; i < estabelecimento.getAvaliacao().size(); i++) {
                    if (estabelecimento.getAvaliacao().get(i).getIdAvaliador().equals(idAvaliador)) {
                        editDescricao.setText(estabelecimento.getAvaliacao().get(i).getDescricao());
                        float nt = (float) estabelecimento.getAvaliacao().get(i).getNota();
                        ratingBarAvaliacao.setRating(nt);
                        idAvaliacao = i;
                        mesmoAvaliador = true;
                    }
                }
            }

        }

        btnSalvarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoDescricao = editDescricao.getText().toString();
                double nota = ratingBarAvaliacao.getRating();
                if (nota > 0) {
                    if (!textoDescricao.equals("")) {

                        if (mesmoAvaliador) {
                            List<Avaliacao> listaAvaliacao;
                            listaAvaliacao = estabelecimento.getAvaliacao();

                            avaliacao = new Avaliacao();
                            avaliacao = listaAvaliacao.get(idAvaliacao);
                            avaliacao.setDescricao(textoDescricao);
                            avaliacao.setNota(nota);

                            estabelecimento.setAvaliacao(listaAvaliacao);
                            estabelecimento.atualizar();
                            Toast.makeText(CadastroAvaliacaoActivity.this, "Avaliação alterada com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            List<Avaliacao> listaAvaliacao = new ArrayList<>();
                            if(estabelecimento.getAvaliacao() != null){
                                listaAvaliacao = estabelecimento.getAvaliacao();
                            }

                            avaliacao = new Avaliacao();
                            avaliacao.setDescricao(textoDescricao);
                            avaliacao.setNota(nota);
                            avaliacao.setEmailAvaliador(emailAvaliador);
                            avaliacao.setIdAvaliador(idAvaliador);

                            listaAvaliacao.add(avaliacao);
                            estabelecimento.setAvaliacao(listaAvaliacao);
                            estabelecimento.atualizar();
                            Toast.makeText(CadastroAvaliacaoActivity.this, "Avaliação feita com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(CadastroAvaliacaoActivity.this, "Escreva um comentário", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroAvaliacaoActivity.this, "Avaliacão não pode ser 0 estrelas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarUsuario() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            idAvaliador = autenticacao.getCurrentUser().getUid();
            emailAvaliador = autenticacao.getCurrentUser().getEmail();
        }
    }

}
