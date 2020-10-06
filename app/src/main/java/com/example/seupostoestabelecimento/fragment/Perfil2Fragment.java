package com.example.seupostoestabelecimento.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.activity.LoginActivity;
import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil2Fragment extends Fragment {

    private FirebaseAuth autenticacao;

    public Perfil2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil2, container, false);

        Button btnDesativarConta = view.findViewById(R.id.btnDesativarConta);
        TextView txtEmailUsuario = view.findViewById(R.id.txtEmailUsuario);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        txtEmailUsuario.setText(autenticacao.getCurrentUser().getEmail());

        btnDesativarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Deseja  excluir sua conta?");
                builder.setCancelable(false);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        autenticacao.getCurrentUser().delete();
                        deslogarUsuario();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                });
                builder.setNegativeButton("Cancelar",null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return view;
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
