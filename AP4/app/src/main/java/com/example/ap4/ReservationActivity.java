package com.example.ap4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Button ok    = findViewById(R.id.btnConfirmer);
        Button cancel= findViewById(R.id.btnAnnuler);

        ok.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
        cancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });


    }


}
