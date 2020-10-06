package com.example.seupostoestabelecimento.fragment;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.activity.DefinirEnderecoEstabelecimentoActivity;
import com.example.seupostoestabelecimento.helper.UsuarioFirebase;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {


    private TextInputEditText campoRazaoSocial, campoCNPJ,
            campoNomefantasia, campoTelefone, campoCEP, campoRua,
            campoBairro, campoNumero, campoCidade, campoEstado;

    private Estabelecimento estabelecimento;
    private LatLng enderecoEstabelecimento;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configurar componentes
        campoRazaoSocial = view.findViewById(R.id.editRazao);
        campoNomefantasia = view.findViewById(R.id.editFantasia);
        campoCNPJ = view.findViewById(R.id.editCnpj);
        campoTelefone = view.findViewById(R.id.editTelefone);
        campoCEP = view.findViewById(R.id.editCep);
        campoRua = view.findViewById(R.id.editRua);
        campoBairro = view.findViewById(R.id.editBairro);
        campoNumero = view.findViewById(R.id.editNumero);
        campoCidade = view.findViewById(R.id.editCidade);
        campoEstado = view.findViewById(R.id.editEstado);
        Button btnAlterar = view.findViewById(R.id.btnAlterar);
        //progressCadastro = findViewById(R.id.progressCadastro);
        ImageButton imageButtonGpsEstabelecimento = view.findViewById(R.id.imageButtonGpsEstabelecimento);

        setEditEstabelecimento();

        imageButtonGpsEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DefinirEnderecoEstabelecimentoActivity.class);
                startActivityForResult(intent,1);
            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        String textoRazaoSocial = campoRazaoSocial.getText().toString();
        String textoNomefantasia = campoNomefantasia.getText().toString();
        String textoCNPJ = campoCNPJ.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        String textoCEP = campoCEP.getText().toString();
        String textoRua = campoRua.getText().toString();
        String textoBairro = campoBairro.getText().toString();
        String textoNumero = campoNumero.getText().toString();
        String textoCidade = campoCidade.getText().toString();
        String textoEstado = campoEstado.getText().toString();


        if (!textoRazaoSocial.isEmpty()) {
            if (!textoNomefantasia.isEmpty()) {
                if (!textoCNPJ.isEmpty()) {
                    if (!textoTelefone.isEmpty()) {
                        if (!textoCEP.isEmpty()) {
                            if (!textoRua.isEmpty()) {
                                if (!textoBairro.isEmpty()) {
                                    if (!textoNumero.isEmpty()) {
                                        if (!textoCidade.isEmpty()) {
                                            if (!textoEstado.isEmpty()) {

                                                estabelecimento.setRazaoSocial(textoRazaoSocial);
                                                estabelecimento.setNomeFantasia(textoNomefantasia);
                                                estabelecimento.setCnpj(textoCNPJ);
                                                estabelecimento.setTelefone(textoTelefone);
                                                estabelecimento.setCep(textoCEP);
                                                estabelecimento.setRua(textoRua);
                                                estabelecimento.setBairro(textoBairro);
                                                estabelecimento.setNumero(textoNumero);
                                                estabelecimento.setCidade(textoCidade);
                                                estabelecimento.setEstado(textoEstado);
                                                estabelecimento.setLatitute(String.valueOf(enderecoEstabelecimento.latitude));
                                                estabelecimento.setLongitude(String.valueOf(enderecoEstabelecimento.longitude));


                                                estabelecimento.atualizar();

                                            } else {
                                                Toast.makeText(getActivity(), "Preencha o estado", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Preencha a cidade", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Preencha o numero", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Preencha o bairro", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Preencha a rua", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Preencha o CEP", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Preencha o telefone", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Preencha o CNPJ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Preencha o nome fantasia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Preencha a razao social", Toast.LENGTH_SHORT).show();
        }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                double lat = data.getDoubleExtra("latitude",0);
                double lon = data.getDoubleExtra("longitude",0);
                enderecoEstabelecimento = new LatLng(lat,lon);
                Log.d("Teste","Endereço: "+enderecoEstabelecimento.toString());
                preencherEndereco();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void preencherEndereco(){
        try {
            //Geocoding -> processo de transformar um endereço ou descriç~çao de um local em LAT/LONG
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            List<Address> listEndereco = geocoder.getFromLocation(enderecoEstabelecimento.latitude, enderecoEstabelecimento.longitude,1);
            if(listEndereco != null && listEndereco.size()>0){
                Address endereco = listEndereco.get(0);
                Log.d("Teste"," "+endereco.toString());

                campoCEP.setText(endereco.getPostalCode());
                campoRua.setText(endereco.getThoroughfare());
                campoBairro.setText(endereco.getSubLocality());
                campoNumero.setText(endereco.getFeatureName());
                campoCidade.setText(endereco.getSubAdminArea());
                campoEstado.setText(endereco.getAdminArea());

            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("Teste", "Erro: "+ e.getMessage());
        }

    }

    private void setEditEstabelecimento() {
        estabelecimento = new Estabelecimento();
        String idUsuarioLogado = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(idUsuarioLogado);
        estabelecimentoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Estabelecimento dadosEstabelecimento = dataSnapshot.getValue(Estabelecimento.class);

                estabelecimento = dadosEstabelecimento;

                campoRazaoSocial.setText(dadosEstabelecimento.getRazaoSocial());
                campoNomefantasia.setText(dadosEstabelecimento.getNomeFantasia());
                campoCNPJ.setText(dadosEstabelecimento.getCnpj());
                campoTelefone.setText(dadosEstabelecimento.getTelefone());
                campoCEP.setText(dadosEstabelecimento.getCep());
                campoRua.setText(dadosEstabelecimento.getRua());
                campoBairro.setText(dadosEstabelecimento.getBairro());
                campoNumero.setText(dadosEstabelecimento.getNumero());
                campoCidade.setText(dadosEstabelecimento.getCidade());
                campoEstado.setText(dadosEstabelecimento.getEstado());
                enderecoEstabelecimento = new LatLng(Double.parseDouble(dadosEstabelecimento.getLatitute()),Double.parseDouble(dadosEstabelecimento.getLongitude()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
