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

import com.example.seupostoestabelecimento.activity.CadastroProdutoActivity;
import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.adapter.AdapterProduto;
import com.example.seupostoestabelecimento.helper.RecyclerItemClickListener;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.example.seupostoestabelecimento.model.Produto;
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
public class ProdutoFragment extends Fragment {

    private RecyclerView recyclerViewProduto;
    private Estabelecimento estabelecimento;
    private Produto produtoSelecionado;
    private List<Produto> listaProdutos = new ArrayList<>();

    public ProdutoFragment(Estabelecimento est) {
        this.estabelecimento = est;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produto, container, false);

        //Configurar componentes
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        recyclerViewProduto = view.findViewById(R.id.recyclerViewProduto);

        carregarProdutos();
        carregarLista();

        listaProdutos = estabelecimento.getProduto();

        //Evento Click
        recyclerViewProduto.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerViewProduto,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Produto produto = listaProdutos.get(position);
                        produto.setId(position);
                        Intent intent = new Intent(view.getContext(), CadastroProdutoActivity.class);
                        intent.putExtra("produto", produto);
                        startActivity(intent);
                        //Toast.makeText(view.getContext(), "Clique: "+produto.getDescricao(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        produtoSelecionado = listaProdutos.get(position);
                        produtoSelecionado.setId(position);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

                        //Configurar titulo e mensagem
                        dialog.setTitle("Confirmar exclusão");
                        dialog.setMessage("Deseja excluir o produto " + produtoSelecionado.getDescricao() + "?");
                        dialog.setNegativeButton("Não", null);
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Produto> listaProduto;
                                listaProduto = estabelecimento.getProduto();

                                Produto prod;
                                prod = listaProduto.get(produtoSelecionado.getId());
                                listaProduto.remove(prod);

                                estabelecimento.setProduto(listaProduto);
                                estabelecimento.atualizar();
                                carregarLista();
                                Toast.makeText(getContext(), "Produto " + produtoSelecionado.getDescricao() + " removido", Toast.LENGTH_SHORT).show();
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
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Fab Clicked!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), CadastroProdutoActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        carregarProdutos();
        carregarLista();
        super.onStart();
    }

    private void carregarProdutos() {
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

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

    private void carregarLista() {

        if (listaProdutos != null) {
            //Configurar Adapter
            AdapterProduto adapterProduto = new AdapterProduto(listaProdutos);

            //Configurar RecyclerView
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewProduto.setLayoutManager(layoutManager);
            recyclerViewProduto.setHasFixedSize(true);
            recyclerViewProduto.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
            recyclerViewProduto.setAdapter(adapterProduto);
        }
    }

}
