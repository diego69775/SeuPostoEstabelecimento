package com.example.seupostoestabelecimento.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.activity.MapsActivity;
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
public class GpsFragment extends Fragment {

    final private List<Estabelecimento> listaEstabelecimento = new ArrayList<>();
    private Estabelecimento estabelecimento;

    public GpsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gps, container, false);

        //Componentes
        Button btnAbrirMapa = view.findViewById(R.id.btnAbrirMapa);

        //Carregar Lat e Long Postos
        carregarPostos();

        btnAbrirMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("cont", listaEstabelecimento.size());
                for (int i = 0; i < listaEstabelecimento.size(); i++) {
                    intent.putExtra("estabelecimento" + i, listaEstabelecimento.get(i));
                }
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    private void carregarPostos() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos");

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEstabelecimento.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    estabelecimento = postSnapshot.getValue(Estabelecimento.class);
                    listaEstabelecimento.add(estabelecimento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
