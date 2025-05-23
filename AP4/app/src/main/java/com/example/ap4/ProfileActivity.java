package com.example.ap4;

import android.content.SharedPreferences;
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

public class ProfileActivity extends AppCompatActivity {
    private EditText edtId, edtNom, edtPrenom, edtMail, edtAdresse, edtAge, edtNbEvents;
    private Button   btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Récupération des vues
        edtId        = findViewById(R.id.edt_id);
        edtNom       = findViewById(R.id.edt_nom);
        edtPrenom    = findViewById(R.id.edt_prenom);
        edtMail      = findViewById(R.id.edt_mail);
        edtAdresse   = findViewById(R.id.edt_adresse);
        edtAge       = findViewById(R.id.edt_age);
        edtNbEvents  = findViewById(R.id.edt_nb_events);
        btnSave      = findViewById(R.id.btn_save_profile);

        // Lecture depuis SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        int id        = prefs.getInt("id", 0);
        String nom    = prefs.getString("nom", "");
        String prenom = prefs.getString("prenom", "");
        String mail   = prefs.getString("mail", "");
        String adresse= prefs.getString("adresse", "");
        int age       = prefs.getInt("age", 0);
        int nbEvents  = prefs.getInt("nbEvents", 0);

        // Pré-remplissage des champs
        edtId.setText(String.valueOf(id));
        edtNom.setText(nom);
        edtPrenom.setText(prenom);
        edtMail.setText(mail);
        edtAdresse.setText(adresse);
        edtAge.setText(String.valueOf(age));
        edtNbEvents.setText(String.valueOf(nbEvents));

        // Enregistrement des modifications
        btnSave.setOnClickListener(v -> {
            String newNom     = edtNom.getText().toString().trim();
            String newPrenom  = edtPrenom.getText().toString().trim();
            String newAdresse = edtAdresse.getText().toString().trim();
            String newAge     = edtAge.getText().toString().trim();

            if (newNom.isEmpty() || newPrenom.isEmpty()) {
                Toast.makeText(this, "Nom et prénom sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest req = new StringRequest(
                    Request.Method.POST,
                    C.UPDATE_USER_URL,
                    response -> {
                        try {
                            JSONObject j = new JSONObject(response);
                            String msg = j.getString("message");
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                            if (!j.getBoolean("erreur")) {
                                // Met à jour SharedPreferences
                                prefs.edit()
                                        .putString("nom", newNom)
                                        .putString("prenom", newPrenom)
                                        .putString("adresse", newAdresse)
                                        .putInt("age", Integer.parseInt(newAge))
                                        .apply();
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(this, "Erreur traitement serveur", Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> Toast.makeText(this, "Erreur réseau", Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String,String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    params.put("nom", newNom);
                    params.put("prenom", newPrenom);
                    params.put("adresse", newAdresse);
                    params.put("age", newAge);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(req);
        });
    }
}
