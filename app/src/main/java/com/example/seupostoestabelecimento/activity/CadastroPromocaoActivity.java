package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.example.seupostoestabelecimento.model.Produto;
import com.example.seupostoestabelecimento.model.Promocao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastroPromocaoActivity extends AppCompatActivity {

    private TextView txtValorNormal;
    private TextInputEditText campoValorPromocao, campoDataInicial, campoDataFinal;

    private Estabelecimento estabelecimento;
    private List<Produto> listaProdutos = new ArrayList<>();
    private Produto produto;
    private String produtoSelecionado;
    private Promocao promocaoAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_promocao);

        //configurar componentes
        Spinner spinnerProduto = findViewById(R.id.spinnerProduto);
        campoValorPromocao = findViewById(R.id.editValorPromocao);
        campoDataInicial = findViewById(R.id.editDataInicial);
        campoDataFinal = findViewById(R.id.editDataFinal);
        Button btnSalvar = findViewById(R.id.btnSalvarPromocao);
        txtValorNormal = findViewById(R.id.txtValorAtual);

        estabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento");
        promocaoAtual = (Promocao) getIntent().getSerializableExtra("promocao");

        carregarProdutos();
        listaProdutos = estabelecimento.getProduto();


        List<String> listaProdutoString = new ArrayList<>();


        //Configurar promoção na caixa de texto
        if (promocaoAtual != null) {
            listaProdutoString.add(promocaoAtual.getNomeProduto());
            campoValorPromocao.setText(String.valueOf(promocaoAtual.getValorPromocao()));
            campoDataInicial.setText(promocaoAtual.getDataInicial());
            campoDataFinal.setText(promocaoAtual.getDataFinal());
            txtValorNormal.setText(String.valueOf(promocaoAtual.getValorNormal()));
        } else {
            if (listaProdutos != null) {
                for (int i = 0; i < listaProdutos.size(); i++) {
                    listaProdutoString.add(listaProdutos.get(i).getDescricao());
                }
            } else {
                listaProdutoString.add("");
            }
        }

        // Criando adapter para o spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaProdutoString);
        spinnerProduto.setAdapter(adapter);
        spinnerProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produtoSelecionado = (String) parent.getItemAtPosition(position);

                for (int i = 0; i < listaProdutos.size(); i++) {
                    if (listaProdutos.get(i).getDescricao().equals(produtoSelecionado)) {
                        String textoValorNormal = "Valor Atual: " + listaProdutos.get(i).getValor();
                        txtValorNormal.setText(textoValorNormal);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoValorPromocao = campoValorPromocao.getText().toString();
                String textoDataInicial = campoDataInicial.getText().toString();
                String textoDataFinal = campoDataFinal.getText().toString();
                double valorPromocao = 0;
                if (!textoValorPromocao.equals("")) {
                    valorPromocao = Double.parseDouble(textoValorPromocao);
                }


                if (!produtoSelecionado.equals("")) {
                    if (!textoValorPromocao.isEmpty()) {
                        if (!textoDataInicial.isEmpty()) {
                            if (!textoDataFinal.isEmpty()) {


                                if (promocaoAtual != null) {
                                    //Alterar
                                    List<Produto> listaProduto = new ArrayList<>();
                                    if (estabelecimento.getProduto() != null) {
                                        listaProduto = estabelecimento.getProduto();
                                    }

                                    for (int i = 0; i < listaProduto.size(); i++) {
                                        if (listaProduto.get(i).getDescricao().equals(produtoSelecionado)) {

                                            Promocao promocao = new Promocao();
                                            promocao.setId(0);
                                            promocao.setDataInicial(textoDataInicial);
                                            promocao.setDataFinal(textoDataFinal);
                                            promocao.setValorPromocao(valorPromocao);
                                            promocao.setNomeProduto(listaProduto.get(i).getDescricao());
                                            promocao.setValorNormal(listaProduto.get(i).getValor());

                                            produto = new Produto();
                                            produto = listaProduto.get(i);
                                            produto.setPromocao(promocao);

                                            estabelecimento.setProduto(listaProduto);
                                            estabelecimento.atualizar();

                                            Toast.makeText(CadastroPromocaoActivity.this, "Promoção " + produto.getDescricao() + " alterada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                } else {
                                    //Novo
                                    List<Produto> listaProduto = new ArrayList<>();
                                    if (estabelecimento.getProduto() != null) {
                                        listaProduto = estabelecimento.getProduto();
                                    }

                                    for (int i = 0; i < listaProduto.size(); i++) {
                                        if (listaProduto.get(i).getDescricao().equals(produtoSelecionado)) {

                                            Promocao promocao = new Promocao();
                                            promocao.setId(0);
                                            promocao.setDataInicial(textoDataInicial);
                                            promocao.setDataFinal(textoDataFinal);
                                            promocao.setValorPromocao(valorPromocao);
                                            promocao.setNomeProduto(listaProduto.get(i).getDescricao());
                                            promocao.setValorNormal(listaProduto.get(i).getValor());

                                            produto = new Produto();
                                            produto = listaProduto.get(i);
                                            produto.setPromocao(promocao);

                                            estabelecimento.setProduto(listaProduto);
                                            estabelecimento.atualizar();

                                            Toast.makeText(CadastroPromocaoActivity.this, "Promoção " + produto.getDescricao() + " cadastrada", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(CadastroPromocaoActivity.this, "Preencha a data final", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastroPromocaoActivity.this, "Preencha a data inicial", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroPromocaoActivity.this, "Preencha o valor da promoção", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroPromocaoActivity.this, "Nenhum produto cadastrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void carregarProdutos() {
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                if(estabelecimento != null) {
                    listaProdutos = estabelecimento.getProduto();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
