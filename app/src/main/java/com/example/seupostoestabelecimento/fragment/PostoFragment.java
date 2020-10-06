package com.example.seupostoestabelecimento.fragment;


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
import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.activity.EstabelecimentoInfoActivity;
import com.example.seupostoestabelecimento.activity.MapsActivity;
import com.example.seupostoestabelecimento.adapter.AdapterPosto;
import com.example.seupostoestabelecimento.helper.RecyclerItemClickListener;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostoFragment extends Fragment {

    private RecyclerView recyclerViewPosto;
    private Estabelecimento estabelecimento;
    final private List<Estabelecimento> listaEstabelecimento;

    public PostoFragment(List<Estabelecimento> listEst) {
        this.listaEstabelecimento = listEst;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posto, container, false);

        //Configurar componentes
        recyclerViewPosto = view.findViewById(R.id.recyclerViewPosto);
        FloatingActionButton floatingActionButtonMapa = view.findViewById(R.id.floatingActionButtonMapa);

        carregarPostos();
        carregarLista();

        //Evento Click
        recyclerViewPosto.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerViewPosto,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Estabelecimento estabelecimento = listaEstabelecimento.get(position);
                        Intent intent = new Intent(view.getContext(), EstabelecimentoInfoActivity.class);
                        intent.putExtra("estabelecimento", estabelecimento);
                        intent.putExtra("position", position);
                        intent.putExtra("cnpj", "");
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        //Ação Floating
        floatingActionButtonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("cont",listaEstabelecimento.size());
                for (int i=0;i<listaEstabelecimento.size();i++){
                    intent.putExtra("estabelecimento"+i,listaEstabelecimento.get(i));
                }
                startActivityForResult(intent,1);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        carregarPostos();
        carregarLista();
        super.onStart();
    }

    private void carregarPostos(){
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos");

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEstabelecimento.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    estabelecimento = postSnapshot.getValue(Estabelecimento.class);
                    listaEstabelecimento.add(estabelecimento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarLista(){
        if(listaEstabelecimento != null){
            //Configurar Adapter
            AdapterPosto adapterPosto = new AdapterPosto(listaEstabelecimento);

            //Configurar RecyclerView
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerViewPosto.setLayoutManager(layoutManager);
            recyclerViewPosto.setHasFixedSize(true);
            recyclerViewPosto.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
            recyclerViewPosto.setAdapter(adapterPosto);
        }
    }
}
