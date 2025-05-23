package com.example.ap4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.appbar.MaterialToolbar;

import android.content.Intent;



public class MainActivity extends AppCompatActivity {

    // Launcher pour la réservation KCX
    private ActivityResultLauncher<Intent> reservationLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(this,
                                    getString(R.string.reservation_confirmed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this,
                                    getString(R.string.reservation_canceled),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    // Launcher pour la recherche
    private ActivityResultLauncher<Intent> searchLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {
                            String q = result.getData().getStringExtra("search_query");
                            Toast.makeText(this,
                                    "Vous avez recherché : " + q,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ─── NOUVEAU : récupère et stocke l'utilisateur connecté ───
        Intent launchIntent = getIntent();
        int    userId   = launchIntent.getIntExtra("id", 0);
        String nom      = launchIntent.getStringExtra("nom");
        String prenom   = launchIntent.getStringExtra("prenom");
        String mail     = launchIntent.getStringExtra("mail");
        String adresse  = launchIntent.getStringExtra("adresse");
        int    age      = launchIntent.getIntExtra("age", 0);
        int    nbEvents = launchIntent.getIntExtra("nbEvents", 0);

// On stocke tout ça en SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit()
                .putInt("id",       userId)
                .putString("nom",   nom)
                .putString("prenom",prenom)
                .putString("mail",  mail)
                .putString("adresse", adresse)
                .putInt("age",      age)
                .putInt("nbEvents", nbEvents)
                .apply();

// Puis on peut afficher le toast
        if (mail != null) {
            Toast.makeText(this,
                    "Connecté en tant de " + prenom + " " + nom,
                    Toast.LENGTH_LONG
            ).show();
        }
        // ──────────────────────────────────────────────────────────────

        // 1) Trouver la toolbar et l'activer
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Appliquer les marges du système
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }    // ← Fermeture de la lambda ici
        );

        // ← Tout ce qui suit est désormais en-dehors de la lambda d’insets

        // 1) Récupération des boutons
        Button btnRL      = findViewById(R.id.btnRocketLeague);
        Button btnValorant= findViewById(R.id.btnValorant);
        Button btnLOL     = findViewById(R.id.btnLeagueOfLegendsLEC);
        Button btnSSBU    = findViewById(R.id.btnSuperSmashBrosUltimate);

        // 2) Listener générique avec AlertDialog (après la partie insets)
        View.OnClickListener confirmListener = clickedView -> {
            int id = clickedView.getId();
            String nomJeu, codeJeu;
            Intent intent;

            if (id == R.id.btnRocketLeague) {
                nomJeu  = "Rocket League";
                codeJeu = "rl";
                intent  = new Intent(this, Rocket_League.class);
            }
            else if (id == R.id.btnValorant) {
                nomJeu  = "Valorant";
                codeJeu = "valo";
                intent  = new Intent(this, Valorant.class);
            }
            else if (id == R.id.btnLeagueOfLegendsLEC) {
                nomJeu  = "League of Legends (LEC)  ";
                codeJeu = "lol";
                intent  = new Intent(this, League_of_Legends_LEC.class);
            }
            else if (id == R.id.btnSuperSmashBrosUltimate) {
                nomJeu  = "Super Smash Bros. Ultimate";
                codeJeu = "ssbu";
                intent  = new Intent(this, Super_Smash_Bros_Ultimate.class);
            }
            else {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment choisir « " + nomJeu + " » ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // ici on injecte le code « jeu » exact attendu par l'API
                        intent.putExtra("jeu", codeJeu);
                        Toast.makeText(this,
                                nomJeu + " sélectionné !",
                                Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    })
                    .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                    .show();
        };

// 3) Assignation du listener
        btnRL.setOnClickListener(confirmListener);
        btnValorant.setOnClickListener(confirmListener);
        btnLOL.setOnClickListener(confirmListener);
        btnSSBU.setOnClickListener(confirmListener);


        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menugeneral, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuReserver) {
            reservationLauncher.launch(
                    new Intent(this, ReservationActivity.class)
            );
            return true;
        }
        else if (id == R.id.menuRechercher) {
            searchLauncher.launch(
                    new Intent(this, RechercheActivity.class)
            );
            return true;
        }
        else if (id == R.id.menuLogin) {
            // Lance l'écran de connexion
            startActivity(new Intent(this, Login.class));
            return true;
        }
        else if (id == R.id.menuRegister) {
            // Lance l'écran d'inscription
            startActivity(new Intent(this, Register.class));
            return true;
        }
        else if (id == R.id.menuViewAdherents) {
            // Nouveau : lance l'activité qui affiche la liste des adhérents
            startActivity(new Intent(this, AdherentsListActivity.class));
            return true;
        }
        else if (id == R.id.menuProfile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }


        else {
            return super.onOptionsItemSelected(item);
        }
    }



//        // Gestion du bouton BtnRocketLeague
//        Button btnRocketLeauge = findViewById(R.id.btnRocketLeague);
//        btnRocketLeauge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                //Ferme la poge ouverte et retour à la page précédente
//                Intent intent = new Intent(MainActivity.this, Rocket_League.class);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        Button btnValorant = findViewById(R.id.btnValorant);
//        btnValorant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                //Ferme la poge ouverte et retour à la page précédente
//                Intent intent = new Intent(MainActivity.this, Valorant.class);
//                startActivity(intent);
//
//
//            }
//        });
//
//        Button btnLOL = findViewById(R.id.btnLeagueOfLegendsLEC);
//        btnLOL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                //Ferme la poge ouverte et retour à la page précédente
//                Intent intent = new Intent(MainActivity.this, League_of_Legends_LEC.class);
//                startActivity(intent);
//
//
//            }
//        });
//
//        Button btnSSBU = findViewById(R.id.btnSuperSmashBrosUltimate);
//        btnSSBU.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                //Ferme la poge ouverte et retour à la page précédente
//                Intent intent = new Intent(MainActivity.this, Super_Smash_Bros_Ultimate.class);
//                startActivity(intent);
//
//
//            }
//        });


    }

