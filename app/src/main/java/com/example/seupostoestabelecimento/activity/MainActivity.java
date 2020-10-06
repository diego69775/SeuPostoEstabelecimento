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
import com.example.seupostoestabelecimento.fragment.AvalicaoFragment;
import com.example.seupostoestabelecimento.fragment.PerfilFragment;
import com.example.seupostoestabelecimento.fragment.ProdutoFragment;
import com.example.seupostoestabelecimento.fragment.PromocaoFragment;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CarregarDados
        carregarEstabelecimento();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Seu Posto Estabelecimento");
        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configurar bottom navigation view
        configuraBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
    }

    private void configuraBottomNavigationView() {

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //Configuracoes iniciais Bottom Navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegacao
        habilitarNavegacao(bottomNavigationViewEx);

    }

    //Metodo responsavel por tratar eventos de click no BottomNavigation
    private void habilitarNavegacao(BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.ic_avaliacao:
                        fragmentTransaction.replace(R.id.viewPager, new AvalicaoFragment(estabelecimento)).commit();
                        return true;
                    case R.id.ic_produto:
                        fragmentTransaction.replace(R.id.viewPager, new ProdutoFragment(estabelecimento)).commit();
                        return true;
                    case R.id.ic_promocao:
                        fragmentTransaction.replace(R.id.viewPager, new PromocaoFragment(estabelecimento)).commit();
                        return true;
                    case R.id.ic_perfil:
                        fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }

    private void carregarEstabelecimento() {
        //estabelecimento = new Estabelecimento();
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);

        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                if (estabelecimento != null) {
                    Log.i("Teste", "Main: " + estabelecimento.getNomeFantasia());
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
