package com.eden;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eden.model.Product;
import com.eden.utils.FirebaseProdutoUtil;

public class RegisterProduct extends AppCompatActivity {

    EditText name, preco, descricao, tipoEntrega;
    ImageView productImage;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        FirebaseProdutoUtil db = new FirebaseProdutoUtil();
        Button btnAvancar = findViewById(R.id.btnCadastroAvancar);
//        name = findViewById(R.id.editText_name_produto);
//        preco = findViewById(R.id.editText_valor_produto);
//        descricao = findViewById(R.id.editText_descricao_produto);
//        tipoEntrega = findViewById(R.id.editText_meio_entrega);
//        productImage = findViewById(R.id.product_image);

        // Salvando os dados no firebase
        btnAvancar.setOnClickListener(v -> {
            if (name.getText().toString().equals("") || preco.getText().toString().equals("") || descricao.getText().toString().equals("") || tipoEntrega.getText().toString().equals("")) {
                Toast.makeText(this, "Os valores não podem estar vazios", Toast.LENGTH_SHORT).show();
            } else if (Double.parseDouble(preco.getText().toString()) <= 0) {
                Toast.makeText(this, "O valor deve ser maior que 0", Toast.LENGTH_SHORT).show();
            } else {
                // Salvando produto
                db.salvarProduto(this,
                        new Product(123, 0, 0, name.getText().toString(),
                                Float.parseFloat(preco.getText().toString()), descricao.getText().toString(),
                                "", Float.parseFloat(tipoEntrega.getText().toString()), 0));
                finish();
            }
        });

//        productImage.setOnClickListener(v -> {
//            // Selecionando imagem
//            imagePickLauncher = registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            selectedImageUri = result.getData().getData();
//                            productImage.setImageURI(selectedImageUri);
//                        }
//                    });
//
//            // Pegando imagem
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            imagePickLauncher.launch(intent);
//
//            // Colocando imagem no ImageView
//            AndroidUtil.setProductImage(this, selectedImageUri, productImage);
//        });

    }
}