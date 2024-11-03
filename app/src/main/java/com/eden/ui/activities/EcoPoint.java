package com.eden.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchByTextRequest;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EcoPoint extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText cep;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_point);

        // Define a variable to hold the Places API key.
        String apiKey = "AIzaSyCdIld446qODgw5hVFivil3EGPJAP2-Jmk";

        // Initialize the Places SDK
        Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);


        // Configurar o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        getUserLocation();
    }

    private void getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            // Moves the camera to the user's location

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                            // Buscar ecopontos próximos
                            findNearbyEcopoints(userLocation);
                        }
                    });
        }
    }

    private void findNearbyEcopoints(LatLng userLocation) {
//        double radiusInDegrees = 0.01;
//
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        final SearchByTextRequest searchByTextRequest = SearchByTextRequest
                .builder("ecopontos | ecoponto | ponto de coleta | ecopoints | descarte de lixo eletrônico", placeFields)
//                .setMaxResultCount()
//                .setLocationRestriction(bounds) // Adiciona a restrição de localização retangular
                .build();

        placesClient.searchByText(searchByTextRequest)
                .addOnSuccessListener(response -> {
                    List<Place> places = response.getPlaces();
                    if (places != null && !places.isEmpty()) {
                        for (Place place : places) {
                            LatLng latLng = place.getLatLng();
                            if (latLng != null) {
                                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                            }
                        }
                    } else {
                        Log.d("EcoPoint", "Nenhum ecoponto encontrado.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("EcoPoint", "Erro ao buscar ecopontos: " + e.getMessage());
                })
                .addOnCanceledListener(() -> {
                    // O chamado da API foi cancelado, por exemplo, ao pressionar o botão de voltar.
                    Log.e("Error", "O chamado da API foi cancelado.");
                });
    }
    private FusedLocationProviderClient fusedLocationClient;

    // Get user permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng userLocation = new LatLng(location.getLatitude (), location.getLongitude());
                                // Adicionar marcadores para os ecopontos próximos ao usuário
                                addEcopoints(userLocation);
                            }
                        }
                    });
        }
    }

    private void addEcopoints(LatLng userLocation) {
        // Exemplo de coordenadas de ecopontos
        LatLng ecopoint1 = new LatLng(-23.5505, -46.6333); // Exemplo de coordenadas
        LatLng ecopoint2 = new LatLng(-23.5515, -46.6343);

        mMap.addMarker(new MarkerOptions().position(ecopoint1).title("Ecoponto 1"));
        mMap.addMarker(new MarkerOptions().position(ecopoint2).title("Ecoponto 2"));

        // Move a câmera para o usuário
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

}