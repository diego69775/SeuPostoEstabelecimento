package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EstabelecimentoInfoActivity extends AppCompatActivity {

    private TextView txtNome, txtEndereco, txtTelefone, txtSemDadosPromocao, txtSemdadosAvaliacao;
    private ListView listAvaliacao, listPromocao, listProduto;

    final private List<Estabelecimento> listaEstabelecimento = new ArrayList<>();
    private Estabelecimento estabelecimento;
    private int position;
    private String cnpjAux = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento_info);

        //Configurar Componentes
        txtNome = findViewById(R.id.txtNomePosto);
        txtEndereco = findViewById(R.id.txtEnderecoPosto);
        txtTelefone = findViewById(R.id.txtTelefonePosto);
        listAvaliacao = findViewById(R.id.listAvaliacao);
        listPromocao = findViewById(R.id.listPromocao);
        listProduto = findViewById(R.id.listProduto);
        txtSemDadosPromocao = findViewById(R.id.txtSemDadosPromocao);
        txtSemdadosAvaliacao = findViewById(R.id.txtSemDadosAvaliacao);
        FloatingActionButton floatingActionButtonRota = findViewById(R.id.floatingActionButtonRota);
        FloatingActionButton floatingActionButtonAvaliacao = findViewById(R.id.floatingActionButtonAvaliacao);

        //Recuperar Estabelecimento
        estabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento");
        position = (int) getIntent().getSerializableExtra("position");
        cnpjAux = (String) getIntent().getSerializableExtra("cnpj");

        carregarDados();

        //Configurar Texto
        if (estabelecimento != null) {
            //Ação Floating
            floatingActionButtonAvaliacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Estabelecimento est = estabelecimento;
                    Intent intent = new Intent(view.getContext(), CadastroAvaliacaoActivity.class);
                    intent.putExtra("estabelecimento", est);
                    startActivity(intent);
                }
            });

            //Ação Floating Rota
            floatingActionButtonRota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Estabelecimento est = estabelecimento;
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    intent.putExtra("estabelecimento", est);
                    intent.putExtra("cont", 0);
                    startActivity(intent);
                }
            });
        }
    }

    private void carregarDados() {
        //Configurar Texto
        if (estabelecimento != null) {
            txtNome.setText(estabelecimento.getNomeFantasia());
            String textoEndereco =estabelecimento.getRua() + " " + estabelecimento.getNumero() + " " + estabelecimento.getBairro();
            txtEndereco.setText(textoEndereco);
            txtTelefone.setText(estabelecimento.getTelefone());
            boolean possuiPromocao = false;
            boolean possuiAvaliacao = false;

            List<String> itensProduto = new ArrayList<>();
            if (estabelecimento.getProduto() != null) {
                for (int i = 0; i < estabelecimento.getProduto().size(); i++) {
                    itensProduto.add(estabelecimento.getProduto().get(i).getDescricao() + " R$" + estabelecimento.getProduto().get(i).getValor());
                }

                //Configurar Adapter para lista Produto
                ArrayAdapter<String> adapterProduto = new ArrayAdapter<>(
                        getApplication(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        itensProduto);
                //Adicionar Adapter a lista
                listProduto.setAdapter(adapterProduto);
            }

            List<String> itensPromocao = new ArrayList<>();
            if (estabelecimento.getProduto() != null) {
                for (int i = 0; i < estabelecimento.getProduto().size(); i++) {
                    if (estabelecimento.getProduto().get(i).getPromocao() != null) {
                        possuiPromocao = true;
                        itensPromocao.add(estabelecimento.getProduto().get(i).getPromocao().getNomeProduto() + " R$" + estabelecimento.getProduto().get(i).getPromocao().getValorPromocao());
                    }
                }
                if (possuiPromocao) {
                    txtSemDadosPromocao.setVisibility(View.GONE);
                    listPromocao.setVisibility(View.VISIBLE);
                } else {
                    txtSemDadosPromocao.setVisibility(View.VISIBLE);
                    listPromocao.setVisibility(View.GONE);
                }
                //Configurar Adapter para lista Promocao
                ArrayAdapter<String> adapterPromocao = new ArrayAdapter<>(
                        getApplication(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        itensPromocao);
                //Adicionar Adapter a lista
                listPromocao.setAdapter(adapterPromocao);
            }

            List<String> itensAvaliacao = new ArrayList<>();
            if (estabelecimento.getAvaliacao() != null) {
                for (int i = 0; i < estabelecimento.getAvaliacao().size(); i++) {
                    if (estabelecimento.getAvaliacao() != null) {
                        possuiAvaliacao = true;
                        itensAvaliacao.add("Usuário: " + estabelecimento.getAvaliacao().get(i).getEmailAvaliador() + "\nComentário:" + estabelecimento.getAvaliacao().get(i).getDescricao() + "\nNota:" + estabelecimento.getAvaliacao().get(i).getNota());
                    }
                }
            }
            if (possuiAvaliacao) {
                txtSemdadosAvaliacao.setVisibility(View.GONE);
                listAvaliacao.setVisibility(View.VISIBLE);
            } else {
                txtSemdadosAvaliacao.setVisibility(View.VISIBLE);
                listAvaliacao.setVisibility(View.GONE);
            }
            //Configurar Adapter para lista Avaliacao
            ArrayAdapter<String> adapterAvaliacao = new ArrayAdapter<>(
                    getApplication(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    itensAvaliacao);
            //Adicionar Adapter a lista
            listAvaliacao.setAdapter(adapterAvaliacao);
        }
    }

    private void carregarPostos() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos");

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (cnpjAux.equals("")) {
                    listaEstabelecimento.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        estabelecimento = postSnapshot.getValue(Estabelecimento.class);
                        listaEstabelecimento.add(estabelecimento);
                    }
                    estabelecimento = listaEstabelecimento.get(position);
                }else{
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Estabelecimento dadosEstabelecimento = postSnapshot.getValue(Estabelecimento.class);
                        if(dadosEstabelecimento.getCnpj().equals(cnpjAux)){
                            estabelecimento = dadosEstabelecimento;
                            listaEstabelecimento.add(estabelecimento);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        carregarPostos();
        carregarDados();
        super.onStart();
    }
}
