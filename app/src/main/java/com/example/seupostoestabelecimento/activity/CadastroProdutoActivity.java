package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.example.seupostoestabelecimento.model.Produto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastroProdutoActivity extends AppCompatActivity {

    private TextInputEditText campoDescricao, campoValor;

    private Estabelecimento estabelecimento;
    private Produto produto;
    private Produto produtoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        carregarEstabelecimento();

        //configurar componentes
        campoDescricao = findViewById(R.id.editDecricaoProduto);
        campoValor = findViewById(R.id.editValorProduto);
        Button btnSalvar = findViewById(R.id.btnSalvarProduto);

        //Recuperar produto
        produtoAtual = (Produto) getIntent().getSerializableExtra("produto");

        //Configurar produto na caixa de texto
        if (produtoAtual != null) {
            campoDescricao.setText(produtoAtual.getDescricao());
            campoValor.setText(String.valueOf(produtoAtual.getValor()));
        }


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoDescricao = campoDescricao.getText().toString();
                String textoValor = campoValor.getText().toString();
                double valor = 0;
                if(!textoValor.equals("")){
                    valor = Double.parseDouble(textoValor);
                }
                if (!textoDescricao.isEmpty()) {
                    if (!textoValor.isEmpty()) {

                        if (produtoAtual != null) {

                            List<Produto> listaProduto;
                            listaProduto = estabelecimento.getProduto();

                            produto = new Produto();
                            produto = listaProduto.get(produtoAtual.getId());
                            produto.setDescricao(textoDescricao);
                            produto.setValor(valor);

                            estabelecimento.setProduto(listaProduto);
                            estabelecimento.atualizar();

                            Toast.makeText(CadastroProdutoActivity.this, "Produto " + produto.getDescricao() + " alterado", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            List<Produto> listaProduto = new ArrayList<>();
                            if(estabelecimento.getProduto() != null){
                              listaProduto = estabelecimento.getProduto();
                            }

                            produto = new Produto();
                            produto.setDescricao(textoDescricao);
                            produto.setValor(valor);
                            //produto.setPromocao(promocao);

                            listaProduto.add(produto);

                            estabelecimento.setProduto(listaProduto);
                            estabelecimento.atualizar();

                            Toast.makeText(CadastroProdutoActivity.this, "Produto " + produto.getDescricao() + " cadastrado", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } else {
                        Toast.makeText(CadastroProdutoActivity.this, "Preencha o valor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroProdutoActivity.this, "Preencha a descrição", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void carregarEstabelecimento() {
        estabelecimento = new Estabelecimento();
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
