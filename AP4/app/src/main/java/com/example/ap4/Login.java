package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText edtMail, edtPwd;
    private Button btnLogin;
    private TextView txtGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMail       = findViewById(R.id.edt_mail);
        edtPwd        = findViewById(R.id.edt_pwd);
        btnLogin      = findViewById(R.id.btn_login);
        txtGoRegister = findViewById(R.id.txt_go_register);

        btnLogin.setOnClickListener(v -> {
            final String mail = edtMail.getText().toString().trim();
            final String pwd  = edtPwd.getText().toString().trim();

            if (mail.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest req = new StringRequest(
                    Request.Method.POST,
                    C.LOGIN_URL,
                    response -> {
                        try {
                            JSONObject j = new JSONObject(response);
                            boolean erreur = j.getBoolean("erreur");
                            String message = j.getString("message");

                            if (!erreur) {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                // On transmet nom, prenom et mail à MainActivity
                                intent.putExtra("id",      j.getInt("id"));
                                intent.putExtra("nom",     j.getString("nom"));
                                intent.putExtra("prenom",  j.getString("prenom"));
                                intent.putExtra("mail",    j.getString("mail"));
                                intent.putExtra("adresse", j.getString("adresse"));
                                intent.putExtra("age",     j.getInt("age"));
                                intent.putExtra("nbEvents", j.getInt("nbEvents"));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Erreur lors du traitement de la réponse", Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Erreur réseau", Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("mail",     mail);
                    params.put("password", pwd);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(req);
        });

        txtGoRegister.setOnClickListener(v ->
                startActivity(new Intent(Login.this, Register.class))
        );
    }
}
