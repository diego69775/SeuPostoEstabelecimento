package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.fragment.Perfil2Fragment;
import com.example.seupostoestabelecimento.fragment.PostoFragment;
import com.example.seupostoestabelecimento.fragment.Promocao2Fragment;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Estabelecimento estabelecimento;
    final private List<Estabelecimento> listaEstabelecimento = new ArrayList<>();
    final private List<Estabelecimento> listaEstabelecimentoPromocao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //CarregarDados
        carregarEstabelecimento();
        carregarEstabelecimentoPromocao();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Seu Posto");
        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configurar bottom navigation view
        configuraBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new Perfil2Fragment()).commit();
    }

    private void configuraBottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation2);

        //Configuracoes iniciais Bottom Navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegacao
        habilitarNavegacao(bottomNavigationViewEx);
    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    /*case R.id.ic_gps:
                        fragmentTransaction.replace(R.id.viewPager, new GpsFragment()).commit();
                        return true;*/
                    case R.id.ic_posto:
                        fragmentTransaction.replace(R.id.viewPager, new PostoFragment(listaEstabelecimento)).commit();
                        return true;
                    case R.id.ic_promocao2:
                        fragmentTransaction.replace(R.id.viewPager, new Promocao2Fragment(listaEstabelecimentoPromocao)).commit();
                        return true;
                    case R.id.ic_perfil2:
                        fragmentTransaction.replace(R.id.viewPager, new Perfil2Fragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }

    private void carregarEstabelecimento() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos");

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    estabelecimento = postSnapshot.getValue(Estabelecimento.class);
                    listaEstabelecimento.add(estabelecimento);
                    Log.i("Teste", "Main2 " + estabelecimento.getNomeFantasia());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarEstabelecimentoPromocao() {
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos");

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    estabelecimento = postSnapshot.getValue(Estabelecimento.class);

                    if (estabelecimento != null) {
                        for (int i = 0; i < estabelecimento.getProduto().size(); i++) {
                            if (estabelecimento.getProduto().get(i).getPromocao() != null) {

                                listaEstabelecimentoPromocao.add(estabelecimento);
                                i = estabelecimento.getProduto().size();
                            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.menu_email:
                startActivity(new Intent(getApplicationContext(), EmailActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
