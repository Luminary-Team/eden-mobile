package com.eden.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.services.CEPService;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

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

        Places.initialize(getApplicationContext(), "SUA_CHAVE_DE_API_AQUI");
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
                            // Buscar ecopontos próximos
                            findNearbyEcopoints(userLocation);
                        }
                    });
        }
    }

//    private void findNearbyEcopoints(LatLng userLocation) {
//        // Criar o pedido para encontrar ecopontos
//        String keyword = "ecoponto"; // Define a palavra-chave
//        double radius = 5000; // Raio de 5 km (5000 metros)
//
//        // Usar a API de lugares para encontrar os ecopontos
//        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(
//                Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT));
//
//        placesClient.findCurrentPlace(request).addOnSuccessListener(response -> {
//            List<PlaceLikelihood> ecopoints = new ArrayList<>();
//
//            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                if (placeLikelihood.getPlace().getName().toLowerCase().contains(keyword)) {
//                    ecopoints.add(placeLikelihood);
//                }
//            }
//
//            // Ordenar por distância
//            Collections.sort(ecopoints, (a, b) -> {
//                float[] resultA = new float[1];
//                float[] resultB = new float[1];
//                Location.distanceBetween(userLocation.latitude, userLocation.longitude,
//                        a.getPlace().getLatLng().latitude, a.getPlace().getLatLng().longitude, resultA);
//                Location.distanceBetween(userLocation.latitude, userLocation.longitude,
//                        b.getPlace().getLatLng().latitude, b.getPlace().getLatLng().longitude, resultB);
//                return Float.compare(resultA[0], resultB[0]);
//            });
//
//            // Limitar a 20 ecopontos mais próximos
//            for (int i = 0; i < Math.min(20, ecopoints.size()); i++) {
//                LatLng ecopointLocation = ecopoints.get(i).getPlace().getLatLng();
//                if (ecopointLocation != null) {
//                    mMap.addMarker(new MarkerOptions().position(ecopointLocation).title(ecopoints.get(i).getPlace().getName()));
//                }
//            }
//        }).addOnFailureListener(exception -> {
//            Log.e("EcoPoint", "Erro ao buscar ecopontos: ", exception);
//        })
//        .addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
//                Log.d("EcoPoint", task.getException().toString());
//                Log.d("EcoPoint", task.getResult().toString());
//            }
//        });
//    }

    private void findNearbyEcopoints(LatLng userLocation) {
        // Lista de ecopontos (exemplo)
        List<Ecopoint> ecopoints = Arrays.asList(
                new Ecopoint("Ecoponto 1", new LatLng(-23.5505, -46.6333)),
                new Ecopoint("Ecoponto 2", new LatLng(-23.5510, -46.6340)),
                new Ecopoint("Ecoponto 3", new LatLng(-23.5520, -46.6350))
                // Adicione mais ecopontos conforme necessário
        );

        // Filtrar e ordenar ecopontos por distância
        List<Ecopoint> nearbyEcopoints = new ArrayList<>();
        for (Ecopoint ecopoint : ecopoints) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                    ecopoint.getLocation().latitude, ecopoint.getLocation().longitude, results);
            if (results[0] <= 5000) { // Apenas ecopontos dentro de 5 km
                nearbyEcopoints.add(ecopoint);
            }
        }

        // Ordenar por distância
        Collections.sort(nearbyEcopoints, (a, b) -> {
            float[] resultA = new float[1];
            float[] resultB = new float[1];
            Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                    a.getLocation().latitude, a.getLocation().longitude, resultA);
            Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                    b.getLocation().latitude, b.getLocation().longitude, resultB);
            return Float.compare(resultA[0], resultB[0]);
        });

        // Limitar a 20 ecopontos mais próximos e adicionar marcadores
        for (int i = 0; i < Math.min(20, nearbyEcopoints.size()); i++) {
            LatLng ecopointLocation = nearbyEcopoints.get(i).getLocation();
            mMap.addMarker(new MarkerOptions().position(ecopointLocation).title(nearbyEcopoints.get(i).getName()));
        }
    }

    // Classe para representar um ecoponto
    class Ecopoint {
        private String name;
        private LatLng location;

        public Ecopoint(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public LatLng getLocation() {
            return location;
        }
    }

    private FusedLocationProviderClient fusedLocationClient;

    // Get user permission for location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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