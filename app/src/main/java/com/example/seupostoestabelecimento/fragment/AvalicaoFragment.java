package com.example.seupostoestabelecimento.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
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
public class AvalicaoFragment extends Fragment {

    private TextView txtNotaEst;
    private ListView listAvaliacaoEst;
    private RatingBar ratingBarNotaEst;

    private Estabelecimento estabelecimento;

    public AvalicaoFragment(Estabelecimento est) {
        this.estabelecimento = est;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avalicao, container, false);

        //Configurar Componentes
        txtNotaEst = view.findViewById(R.id.txtNotaEst);
        listAvaliacaoEst = view.findViewById(R.id.listAvaliacaoEst);
        ratingBarNotaEst = view.findViewById(R.id.ratingBarNotaEst);

        carregarDados();
        carregarLista();

        return view;
    }

    private void carregarDados() {
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

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

    private void carregarLista() {

        if (estabelecimento.getAvaliacao() != null) {
            List<String> itensAvaliacao = new ArrayList<>();
            double nt = 0;


            if (estabelecimento.getAvaliacao() != null) {
                int qtdAvaliacao = estabelecimento.getAvaliacao().size();
                for (int i = 0; i < estabelecimento.getAvaliacao().size(); i++) {
                    if (estabelecimento.getAvaliacao() != null) {
                        itensAvaliacao.add("Usuário: " + estabelecimento.getAvaliacao().get(i).getEmailAvaliador() + "\nComentário:" + estabelecimento.getAvaliacao().get(i).getDescricao() + "\nNota:" + estabelecimento.getAvaliacao().get(i).getNota());
                        nt = nt + estabelecimento.getAvaliacao().get(i).getNota();
                    }
                }
                nt = nt / qtdAvaliacao;
            }

            //Configurar Adapter para lista Avaliacao
            ArrayAdapter<String> adapterAvaliacao = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    itensAvaliacao);
            //Adicionar Adapter a lista
            listAvaliacaoEst.setAdapter(adapterAvaliacao);
            ratingBarNotaEst.setRating((float) nt);
            String textoNotEst = "" + nt;
            txtNotaEst.setText(textoNotEst);
        }
    }


}
