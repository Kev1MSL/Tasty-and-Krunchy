package com.mizix.tastynkrunchy;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Activité principale de Tasty n' Krunchy. C'est celle qui se lance en première lorsqu'on ouvre l'application.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Création des variables globales
     * AppBarConfiguration : correspond au menu (navigateur) qui apparait à gauche lorsqu'on clique sur les 3 traits en haut à gauche.
     * Snackbar : correspond à une bande de texte sur fond noir qui apparait en bas de l'écran. Elle permet de confirmer à l'utilisateur l'insertion du menu dans la base de données.
     * FloatingActionButton : correspond à un bouton (flottant) en bas à droite de l'écran. Il permet d'ouvrir la page d'insertion de menus.
     */
    private AppBarConfiguration mAppBarConfiguration;
    Snackbar snackbar;
    FloatingActionButton fab;

    /**
     * Fontion appelée lors de la création de l'activité
     * @param savedInstanceState : sert à enregistrer les dernières actions/données de l'activité. Nous n'utilisons pas ce paramètre car nous ne stockons pas ces paramètres.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        Initialisation de la barre d'outils de l'application. C'est la barre verte tout en haut de l'application
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        Initialisation de l'outil permettant la mise en page du navigateur
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        /*
        Initialisation du bouton flottant
         */
        fab = findViewById(R.id.fab);

        /*
        Fonction permettant de détecter le clic sur un bouton
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createMenu = new Intent(MainActivity.this, AddMenuActivity.class);
                initDataForSpinner();
                startActivity(createMenu);
                finish();
            }
        });

        /*
        Initialisation de la vue de navigation
         */
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menuliste, R.id.nav_etats, R.id.nav_interaction, R.id.nav_devtools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*
        Fonction permettant d'afficher le le bouton pour ajouter des menus seulement sur la page des menus
         */
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                assert destination.getLabel() != null;
                if (destination.getLabel().toString().equals("Menus")){
                    fab.setVisibility(View.VISIBLE);
                }else {
                    fab.setVisibility(View.GONE);
                }
            }
        });


        /*
        Afficher un message de confirmation lorsque l'utilisateur à ajouter un menu
         */
        try {
            if (Objects.requireNonNull(getIntent().getExtras()).getBoolean("menu added")){
                snackbar = Snackbar.make(findViewById(R.id.drawer_layout), "Menu ajouté avec succès ! Veuillez patientez pendant que nous l'inserons dans la base de données", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }catch (Exception ignored){

        }


    }

    /**
     * Fonction permettant de charger le nom des nourrtures.
     * Les noms de nourriture sont les seules données que l'application sauvegarde dans sa mémoire interne.
     * @return : Renvoie les noms de nourriture sous forme de liste
     */
    private ArrayList<String> loadNourritureNoms(){
        /*
        Liste locale contenant les noms de nourriture
         */
        ArrayList<String> nomsNourritures;

        /*
        Outil permettant d'accèder aux données en mémoire interne
         */
        SharedPreferences sharedPreferencesNourritures = getSharedPreferences("shared preferences nourritures noms", MODE_MULTI_PROCESS);

        /*
        Outil développé par Google pour mettre des données sous forme de liste en mémoire
         */
        Gson gson = new Gson();

        /*
        Transformation en JSON de ces listes pour mieux sérialiser les données
         */
        String json = sharedPreferencesNourritures.getString("noms nourritures", null);

        /*
        Désignation du type de données
         */
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        /*
        Réception des données
         */
        nomsNourritures = gson.fromJson(json, type);

        /*
        Si les données sont vides, il faut leur donner des valeurs par défaut
         */
        if (nomsNourritures == null){
            nomsNourritures = new ArrayList<>();
            nomsNourritures.add("Nourriture 1");
            nomsNourritures.add("Nourriture 2");
            nomsNourritures.add("Nourriture 3");
        }

        /*
        Renvoyer la liste construite
         */
        return nomsNourritures;
    }

    /**
     * Fonction pour initialiser les données pour la liste de nourriture
     */
    private void initDataForSpinner() {
        /*
        Création d'une liste pour stocker les 3 types de nourriture et la phrase d'explication
         */
        ArrayList<String> dataForSpinner = new ArrayList<>();

        dataForSpinner.add("Veuillez selectionner un type de nourriture"); //Phrase d'explication
        dataForSpinner.add(loadNourritureNoms().get(0)); //Nourrture 1
        dataForSpinner.add(loadNourritureNoms().get(1)); //Nourriture 2
        dataForSpinner.add(loadNourritureNoms().get(2)); //Nourriture 3

        /*
        Sauvegarde de données
         */
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences data for spinner", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataForSpinner);
        editor.putString("data for spinner", json);
        /*
        Enregistrement de la sauvegarde
         */
        editor.apply();
    }

    /**
     * Initialisation de la barre d'outil.
     * @param menu : permet la disposition des boutons dans la barre d'outil
     * @return : renvoie OK pour l'initialisation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Ajoute le bouton parramètre dans la barre d'outil
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Fonction pour détecter le clic sur le bouton paramètre de la barre d'outil
     * @param item : objet sur lequel l'utilisateur a interagi
     * @return : renvoie OK pour le clic
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*
        Vérification si le clic est bien sur le bouton paramètre
         */
        if (item.getItemId() == R.id.action_settings){

            /*
            Ouvrir la page pour accèder aux paramètres
             */
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

            /*
            Fermer MainActivity
             */
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fonction obsolète dans la version android utilisée par notre smartphone, mais nécessaire en vue d'un quelconque développement à plus grande échelle
     * Elle permet de remonter dans les activités de l'application.
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}
