package com.eden.ui.activities;

import static com.eden.utils.AndroidUtil.downloadImageFromFirebase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.dto.ProductRequest;
import com.eden.api.services.ProductService;
import com.eden.model.Product;
import com.eden.utils.AndroidUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {

    EditText title, price, description;
    Spinner condition;
    ImageView productImage;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Button btnAvancar = findViewById(R.id.button_edit_product);
        productImage = findViewById(R.id.edit_product_image);
        title = findViewById(R.id.editText_product_title);
        price = findViewById(R.id.editText_product_price);
        description = findViewById(R.id.editText_product_description);
        condition = findViewById(R.id.spinner_condicao);


        // Preenchendo campos
        Intent intent = getIntent();
        if (intent != null) {
            title.setText(intent.getStringExtra("nome"));
            price.setText(String.valueOf(intent.getFloatExtra("valor", 0.0f)));
            description.setText(intent.getStringExtra("descricao"));
        }

        downloadImageFromFirebase(this, productImage, "product_" + intent.getIntExtra("id", 0) + ".jpg");

        // Selecionando imagem
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Glide.with(this).load(selectedImageUri).centerCrop().into(productImage);

                            // Definir nome e caminho pra imagem
                            String name = "image_" + System.currentTimeMillis() + ".jpg";
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CameraSalaF");

                            // Carregar imagem com as configs
                            ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                                    getContentResolver(),
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                            ).build();
                        }
                    }
                }
        );

        // Saving data on database
        btnAvancar.setOnClickListener(v -> {
            ProductRequest product = new ProductRequest(
                    1,
                    1,
                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    title.getText().toString(),
                    description.getText().toString(),
                    Float.parseFloat(price.getText().toString()),
                    "12345678",
                    5
            );

            // Calling API
            ProductService productService = RetrofitClient.getClient().create(ProductService.class);
            Call<Product> call = productService.updateProduct(product, intent.getIntExtra("id", 0));
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {

                        if (selectedImageUri != null) {
                            AndroidUtil.uploadImageToFirebase(selectedImageUri,
                                    "product_" + intent.getIntExtra("id", 0) + ".jpg");
                        }

                        Toast.makeText(EditProduct.this, "Produto alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Product", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable throwable) {
                    // TODO: handle failure
                    Log.d("Product", throwable.getMessage());
                }
            });

            finish();
        });

        productImage = findViewById(R.id.edit_product_image);
        productImage.setOnClickListener(v -> {
            // Selecionando imagem
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intentImage -> {
                        imagePickLauncher.launch(intentImage);
                        return null;
                    });
        });

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());

    }
}