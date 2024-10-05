package com.eden;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eden.model.Product;
import com.eden.utils.FirebaseProdutoUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class RegisterProduct extends AppCompatActivity {

    EditText name, preco, descricao, tipoEntrega;
    ImageView productImage;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);

        FirebaseProdutoUtil db = new FirebaseProdutoUtil();
        Button btnAvancar = findViewById(R.id.btnCadastroAvancar);
//        name = findViewById(R.id.editText_name_produto);
//        preco = findViewById(R.id.editText_valor_produto);
//        descricao = findViewById(R.id.editText_descricao_produto);
//        tipoEntrega = findViewById(R.id.editText_meio_entrega);
//        productImage = findViewById(R.id.product_image);

        // Selecionando imagem
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            Glide.with(this).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(productImage);
                        }
                    }
                }
        );

        // Salvando os dados no firebase
        btnAvancar.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty() || preco.getText().toString().isEmpty() || descricao.getText().toString().isEmpty() || tipoEntrega.getText().toString().isEmpty()) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            } else if (Double.parseDouble(preco.getText().toString()) <= 0) {
                Toast.makeText(this, "O valor deve ser maior que 0", Toast.LENGTH_SHORT).show();
            } else {
                // Salvando produto
//                db.salvarProduto(this,
//                        new Product(123, 0, 0, name.getText().toString(),
//                                Float.parseFloat(preco.getText().toString()), descricao.getText().toString(),
//                                "", Float.parseFloat(tipoEntrega.getText().toString()), 0));
                finish();
            }
        });

        productImage = findViewById(R.id.register_product_image);
        productImage.setOnClickListener(v -> {
            // Selecionando imagem
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

    }
}