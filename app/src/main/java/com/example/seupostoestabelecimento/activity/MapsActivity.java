package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.Permissoes;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng localUsuario;
    private LatLng enderecoEstabelecimento;

    final private List<Estabelecimento> listaEstabelecimento = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Validar permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        //Recuperar Lista ou Item Estabelecimento
        int cont = (int) getIntent().getSerializableExtra("cont");
        Estabelecimento estabelecimento;
        if (cont == 0) {
            estabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento");
            listaEstabelecimento.add(estabelecimento);
        } else {
            for (int i = 0; i < cont; i++) {
                estabelecimento = (Estabelecimento) getIntent().getSerializableExtra("estabelecimento" + i);
                listaEstabelecimento.add(estabelecimento);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Mudar exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Objeto responsavel por gerenciar a localizacao do usuario
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Log.d("Localizacao", "onLocationChanged: " + location.toString());

                mMap.clear();

                localUsuario = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(localUsuario).title("Meu local").icon(BitmapDescriptorFactory.fromResource(R.drawable.dom2)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 16));


                if (listaEstabelecimento != null) {
                    for (int i = 0; i < listaEstabelecimento.size(); i++) {
                        double lat = Double.valueOf(listaEstabelecimento.get(i).getLatitute());
                        double lon = Double.valueOf(listaEstabelecimento.get(i).getLongitude());
                        enderecoEstabelecimento = new LatLng(lat, lon);
                        StringBuilder texto = new StringBuilder();
                        for (int j = 0; j < listaEstabelecimento.get(i).getProduto().size(); j++) {
                            texto.append(listaEstabelecimento.get(i).getProduto().get(j).getDescricao()).append(" ").append(listaEstabelecimento.get(i).getProduto().get(j).getValor()).append("\n");
                        }
                        mMap.addMarker(new MarkerOptions().position(enderecoEstabelecimento).title(listaEstabelecimento.get(i).getNomeFantasia()).snippet(texto.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                    }
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10,
                    locationListener
            );
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar localizacao do usuario

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000,
                            10,
                            locationListener
                    );
                }
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

