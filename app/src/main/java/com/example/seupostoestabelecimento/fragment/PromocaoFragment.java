package com.example.seupostoestabelecimento.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.activity.CadastroPromocaoActivity;
import com.example.seupostoestabelecimento.adapter.AdapterPromocao;
import com.example.seupostoestabelecimento.helper.RecyclerItemClickListener;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.example.seupostoestabelecimento.model.Produto;
import com.example.seupostoestabelecimento.model.Promocao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromocaoFragment extends Fragment {

    private RecyclerView recyclerViewPromocao;
    private Estabelecimento estabelecimento;
    private Promocao promocaoSelecionado;
    private List<Produto> listaProdutos = new ArrayList<>();
    private List<Promocao> listaPromocao = new ArrayList<>();

    public PromocaoFragment(Estabelecimento est) {
        this.estabelecimento = est;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promocao, container, false);

        //Configurar componentes
        FloatingActionButton fabAddPromocao = view.findViewById(R.id.fabAddPromocao);
        recyclerViewPromocao = view.findViewById(R.id.recyclerViewPromocao);

        carregarProdutos();
        listaProdutos = estabelecimento.getProduto();
        carregarPromocao();
        carregarLista();

        //Evento Click Lista
        recyclerViewPromocao.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerViewPromocao,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Promocao promocao = listaPromocao.get(position);
                        Intent intent = new Intent(view.getContext(), CadastroPromocaoActivity.class);
                        intent.putExtra("estabelecimento", estabelecimento);
                        intent.putExtra("promocao", promocao);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        promocaoSelecionado = listaPromocao.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        //Configurar titulo e mensagem
                        dialog.setTitle("Confirmar exclusão");
                        dialog.setMessage("Deseja excluir a promoção " + promocaoSelecionado.getNomeProduto() + "?");
                        dialog.setNegativeButton("Não", null);
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Produto> listaProduto;
                                listaProduto = estabelecimento.getProduto();
                                for (int j = 0; j < listaProduto.size(); j++) {
                                    if (listaProduto.get(j).getDescricao().equals(promocaoSelecionado.getNomeProduto())) {

                                        Produto prod = listaProduto.get(j);
                                        prod.setPromocao(null);

                                        estabelecimento.setProduto(listaProduto);
                                        estabelecimento.atualizar();
                                        carregarPromocao();
                                        carregarLista();
                                        Toast.makeText(getContext(), "Promoção " + promocaoSelecionado.getNomeProduto() + " removida", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        //Ação Floating
        fabAddPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Estabelecimento est = estabelecimento;
                Intent intent = new Intent(view.getContext(), CadastroPromocaoActivity.class);
                intent.putExtra("estabelecimento", est);
                startActivity(intent);
            }
        });

        return view;
    }

    private void carregarProdutos() {
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                if (estabelecimento != null) {
                    listaProdutos = estabelecimento.getProduto();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarPromocao() {
        //listaProdutos
        if (listaProdutos != null) {
            listaPromocao = new ArrayList<>();
            for (int i = 0; i < listaProdutos.size(); i++) {
                if (listaProdutos.get(i).getPromocao() != null) {

                    Promocao promocao = listaProdutos.get(i).getPromocao();
                    promocao.setId(i);
                    promocao.setValorNormal(listaProdutos.get(i).getValor());
                    promocao.setNomeProduto(listaProdutos.get(i).getDescricao());
                    listaPromocao.add(listaProdutos.get(i).getPromocao());
                }
            }

            /*for (int i = 0; i < listaPromocao.size(); i++) {
                Log.i("Teste", "Teste: " + listaPromocao.get(i).getNomeProduto());
                Log.i("Teste", "Teste: " + listaPromocao.get(i).getId());
            }*/
        }
    }

    private void carregarLista() {
        if (listaPromocao != null) {
            //Configurar Adapter
            AdapterPromocao adapterPromocao = new AdapterPromocao(listaPromocao);

            //Configurar RecyclerView
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewPromocao.setLayoutManager(layoutManager);
            recyclerViewPromocao.setHasFixedSize(true);
            recyclerViewPromocao.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
            recyclerViewPromocao.setAdapter(adapterPromocao);
        }
    }

    @Override
    public void onStart() {
        carregarProdutos();
        carregarPromocao();
        carregarLista();
        super.onStart();
    }
}
