package com.example.ap4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ap4.adapter.AdherentAdapter;
import com.example.ap4.Model.AdherentModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdherentsListActivity extends AppCompatActivity {
    private static final int REQ_EDIT = 100;
    private List<AdherentModel> list;
    private AdherentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adherents_list);

        ListView lv = findViewById(R.id.listview_adherents);
        list = new ArrayList<>();
        adapter = new AdherentAdapter(this, list);
        lv.setAdapter(adapter);

        // 1) Charger la liste depuis l'API
        loadAdherents();

        // 2) Long click pour Modifier / Supprimer
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            AdherentModel a = list.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(a.getPrenom() + " " + a.getNom())
                    .setItems(new String[]{"Modifier", "Supprimer"}, (dlg, which) -> {
                        if (which == 0) {
                            // Lancer l'édition
                            Intent i = new Intent(this, EditAdherentActivity.class);
                            i.putExtra("id",     a.getId());
                            i.putExtra("nom",    a.getNom());
                            i.putExtra("prenom", a.getPrenom());
                            i.putExtra("mail",   a.getMail());
                            // si tu veux, passe adresse/age aussi en extra
                            startActivityForResult(i, REQ_EDIT);
                        } else {
                            // Supprimer
                            deleteAdherent(a.getId());
                        }
                    })
                    .show();
            return true;
        });
    }

    private void loadAdherents() {
        list.clear();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                C.LIST_ADHERENTS_URL,
                null,
                resp -> {
                    try {
                        if (!resp.getBoolean("erreur")) {
                            JSONArray arr = resp.getJSONArray("adherents");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                list.add(new AdherentModel(
                                        o.getInt("id"),
                                        o.getString("nom"),
                                        o.getString("prenom"),
                                        o.getString("mail")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this,
                                    resp.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this,
                                "Erreur parsing JSON",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this,
                        "Erreur réseau",
                        Toast.LENGTH_LONG).show()
        );
        Volley.newRequestQueue(this).add(req);
    }

    private void deleteAdherent(int id) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                C.DELETE_ADHERENT_URL,
                resp -> {
                    try {
                        JSONObject j = new JSONObject(resp);
                        Toast.makeText(this, j.getString("message"), Toast.LENGTH_SHORT).show();
                        if (!j.getBoolean("erreur")) {
                            loadAdherents();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(this,
                        "Erreur réseau",
                        Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> p = new HashMap<>();
                p.put("id", String.valueOf(id));
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT && resultCode == Activity.RESULT_OK) {
            // Recharger la liste après édition
            loadAdherents();
        }
    }
}
