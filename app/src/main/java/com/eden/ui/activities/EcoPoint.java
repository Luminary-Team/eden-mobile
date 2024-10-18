package com.eden.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.eden.R;
import com.eden.api.RetrofitClient;
import com.eden.api.services.CEPService;

public class EcoPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_point);

        EditText cep = findViewById(R.id.ecopoint_cep);

        formatCep(cep);

        (findViewById(R.id.back_btn)).setOnClickListener(v -> finish());
    }

    private void formatCep(EditText cep) {

        // Format CEP (00000-000)
        cep.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não faz nada antes da mudança
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não faz nada durante a mudança
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                    StringBuilder formatted = new StringBuilder();

                    for (int i = 0; i < clean.length(); i++) {
                        if (i == 5) {
                            formatted.append("-"); // Adiciona o hífen após os cinco primeiros dígitos
                        }
                        formatted.append(clean.charAt(i));
                    }
                    current = formatted.toString();
                    cep.setText(current);
                    cep.setSelection(current.length()); // Move o cursor para o final
                }
            }
        });
    }
}