package com.example.ap4;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ap4.adapter.PlayerAdapter;
import com.example.ap4.Model.PlayerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Rocket_League extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_league);

        // 1) Récupérer le code du jeu transmis depuis MainActivity
        //    ici ce sera "rocket_league"
        String jeu = getIntent().getStringExtra("jeu");
        if (jeu == null) jeu = "rocket_league";  // valeur par défaut si nécessaire

        // 2) Construire et encoder l’URL de l’API
        String encodedJeu;
        try {
            encodedJeu = URLEncoder.encode(jeu, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // ne devrait jamais arriver pour UTF-8
            encodedJeu = jeu;
        }
        String url = "http://10.0.2.2/api_ap4/listByGame.php?jeu=" + encodedJeu;


        // 3) Préparation du ListView et de l’Adapter
        ListView lv = findViewById(R.id.listview_joueurs);
        List<PlayerModel> players = new ArrayList<>();
        PlayerAdapter adapter = new PlayerAdapter(this, players);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setAdapter(adapter);

        // 4) Envoi de la requête Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray arr = response.getJSONArray("players");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            int    id   = obj.getInt("id");
                            String name = obj.getString("name");
                            players.add(new PlayerModel(id, name, 0));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,
                                "Erreur parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this,
                            "Erreur réseau : " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
        );
        queue.add(req);

        // 5) Gestion du clic sur un joueur
        lv.setOnItemClickListener((parent, view, pos, id2) -> {
            PlayerModel p = players.get(pos);
            Toast.makeText(
                    Rocket_League.this,
                    "Sélectionné : " + p.getName(),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}