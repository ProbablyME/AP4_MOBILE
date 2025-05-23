package com.example.ap4;

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

public class Register extends AppCompatActivity {
    private EditText edtNom, edtPrenom, edtMail, edtPwd, edtAdresse, edtAge;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_register);

        edtNom    = findViewById(R.id.edt_nom);
        edtPrenom = findViewById(R.id.edt_prenom);
        edtMail   = findViewById(R.id.edt_mail_reg);
        edtPwd    = findViewById(R.id.edt_pwd_reg);
        edtAdresse= findViewById(R.id.edt_adresse);
        edtAge    = findViewById(R.id.edt_age);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            String nom     = edtNom.getText().toString().trim();
            String prenom  = edtPrenom.getText().toString().trim();
            String mail    = edtMail.getText().toString().trim();
            String pwd     = edtPwd.getText().toString().trim();
            String adresse = edtAdresse.getText().toString().trim();
            String age     = edtAge.getText().toString().trim();

            StringRequest req = new StringRequest(
                    Request.Method.POST,
                    C.REGISTER_URL,
                    response -> {
                        try {
                            JSONObject j = new JSONObject(response);
                            Toast.makeText(this, j.getString("message"), Toast.LENGTH_LONG).show();
                            if(!j.getBoolean("erreur")) {
                                startActivity(new Intent(this, Login.class));
                                finish();
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Erreur réseau", Toast.LENGTH_LONG).show();
                    }
            ){
                @Override
                protected Map<String,String> getParams() {
                    Map<String,String> p = new HashMap<>();
                    p.put("nom", nom);
                    p.put("prenom", prenom);
                    p.put("mail", mail);
                    p.put("password", pwd);
                    p.put("adresse", adresse);
                    p.put("age", age);
                    return p;
                }
            };
            Volley.newRequestQueue(this).add(req);
        });
    }
}
