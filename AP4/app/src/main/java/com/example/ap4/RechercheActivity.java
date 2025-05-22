package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RechercheActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        EditText edt = findViewById(R.id.edtRecherche);
        Button btn  = findViewById(R.id.btnRecherche);

        btn.setOnClickListener(v -> {
            String query = edt.getText().toString();
            Intent data  = new Intent();
            data.putExtra("search_query", query);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
