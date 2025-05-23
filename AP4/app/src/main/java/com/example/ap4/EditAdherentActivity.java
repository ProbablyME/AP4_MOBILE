package com.example.ap4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAdherentActivity extends AppCompatActivity {
    private EditText edtNom, edtPrenom, edtMail, edtAdresse, edtAge;
    private Button   btnSave;
    private int      id;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_edit_adherent);

        edtNom    = findViewById(R.id.edt_nom_edit);
        edtPrenom = findViewById(R.id.edt_prenom_edit);
        edtMail   = findViewById(R.id.edt_mail_edit);
        edtAdresse= findViewById(R.id.edt_adresse_edit);
        edtAge    = findViewById(R.id.edt_age_edit);
        btnSave   = findViewById(R.id.btn_save);

        // Récupérer les extras
        Intent i = getIntent();
        id       = i.getIntExtra("id", 0);
        edtNom.setText(i.getStringExtra("nom"));
        edtPrenom.setText(i.getStringExtra("prenom"));
        edtMail.setText(i.getStringExtra("mail"));

        // Charger adresse/age via un autre champ ou ignore si non transmis

        btnSave.setOnClickListener(v -> {
            String nom     = edtNom.getText().toString().trim();
            String prenom  = edtPrenom.getText().toString().trim();
            String mail    = edtMail.getText().toString().trim();
            String adresse = edtAdresse.getText().toString().trim();
            String age     = edtAge.getText().toString().trim();

            StringRequest req = new StringRequest(
                    Request.Method.POST,
                    C.UPDATE_ADHERENT_URL,
                    resp -> {
                        try {
                            JSONObject j = new JSONObject(resp);
                            Toast.makeText(this, j.getString("message"), Toast.LENGTH_SHORT).show();
                            if (!j.getBoolean("erreur")) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        } catch(Exception e){ e.printStackTrace(); }
                    },
                    err -> Toast.makeText(this,"Erreur réseau",Toast.LENGTH_LONG).show()
            ) {
                @Override protected Map<String,String> getParams() {
                    Map<String,String> p=new HashMap<>();
                    p.put("id", String.valueOf(id));
                    p.put("nom", nom);
                    p.put("prenom", prenom);
                    p.put("mail", mail);
                    p.put("adresse", adresse);
                    p.put("age", age);
                    return p;
                }
            };
            Volley.newRequestQueue(this).add(req);
        });
    }
}
