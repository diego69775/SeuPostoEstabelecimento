package com.example.seupostoestabelecimento.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seupostoestabelecimento.R;
import com.example.seupostoestabelecimento.helper.Permissoes;
import com.example.seupostoestabelecimento.model.Estabelecimento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    int aux = 0;
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
                            if(j==listaEstabelecimento.get(i).getProduto().size()-1){
                                texto.append(listaEstabelecimento.get(i).getProduto().get(j).getDescricao()).append(" ").append(listaEstabelecimento.get(i).getProduto().get(j).getValor());
                            }else{
                                texto.append(listaEstabelecimento.get(i).getProduto().get(j).getDescricao()).append(" ").append(listaEstabelecimento.get(i).getProduto().get(j).getValor()).append("\n");
                            }

                        }
                        mMap.addMarker(new MarkerOptions().position(enderecoEstabelecimento).title(listaEstabelecimento.get(i).getNomeFantasia()).snippet(texto.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
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

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter(){
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents,null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            if(aux == 0){
                return null;
            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if(aux == 1) {
                return null;
            }
            render(marker,mContents);
            return mContents;
        }

        private void render(Marker marker, View view){
            int badge;

            //if(marker.equals(mPosto)){
                //badge = R.drawable.badge_qld;
            //}else{
                badge=0;
            //}

            //((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if(title != null){
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED),0,titleText.length(),0);
                titleUi.setText(titleText);
            }else{
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if(snippet != null && snippet.length() > 12) {
                SpannableString snippetText  = new SpannableString(snippet);
                //snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA),0,10,0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK),0, snippet.length(),0);
                snippetUi.setText(snippetText);
            }else{
                snippetUi.setText("");
            }
        }
    }

}

