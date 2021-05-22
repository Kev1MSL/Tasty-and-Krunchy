package com.mizix.tastynkrunchy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Activité pour ajouter des menus
 */
public class AddMenuActivity extends AppCompatActivity {
    /**
     * Initialisation des variables globales
     */
    private ArrayList<AddMenuItem> addMenuItemsList;
    private RecyclerView mAddMenuRecyclerView;
    private AddMenuAdapter addMenuAdapter;
    private RecyclerView.LayoutManager mAddMenuLayoutManager;
    private FloatingActionButton addMenu;
    private TextView typeNourritureRestant;
    FloatingActionButton removeMenu;
    /**
     * Detecter si le bouton valider et envoyer le menu est cliqué
     * @param item : objet sur lequel l'utilisateur a interagi
     * @return : renvoie OK pour le clic
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.valider){
            /*
            Envoie les données au serveur
             */
            sendDataToServer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fonction pour envoyer les données du menu au serveur
     */
    private void sendDataToServer(){
        try {
            /*
            En fonction de la taille du menu les paramètres changent
             */
            switch (addMenuItemsList.size()){
                case 1:{
                    if (!addMenuItemsList.get(0).getmTypeNourriture1() && !addMenuItemsList.get(0).getmTypeNourriture2() &&!addMenuItemsList.get(0).getmTypeNourriture3()){
                        Toast.makeText(AddMenuActivity.this, "Veuillez renseigner le type de nourriture souhaité !", Toast.LENGTH_LONG).show();
                    }else {
                        /*
                        Création d'une URL pour envoyer les données au serveur
                         */
                        StringBuilder builder = new StringBuilder();
                        builder
                                .append("_link_to_the_API") //Clé d'activation
                                .append(Objects.requireNonNull(setUpData(1)).get(0)) //Jour
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(1)) //Mois
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(2)) //Année
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(3)) //Heure
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(4)) //Minute
                                .append("&n=")
                                .append(Objects.requireNonNull(setUpData(1)).get(5)) //Nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(6)) //Nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(7)) //Nourriture 3
                                .append("&q=")
                                .append(Objects.requireNonNull(setUpData(1)).get(8)) //Quantité nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(9)) //Quantité nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(1)).get(10)) //Quantité nourriture 3
                                .append("&f=")
                                .append(Objects.requireNonNull(setUpData(1)).get(11)); //Frequence
                        faireLaRequeteHTTP(builder.toString());
                        Log.d(AddMenuActivity.class.getSimpleName(), "sendDataToServer: " + builder.toString());
                    }
                    break;
                }
                case 2:{
                    if ((!addMenuItemsList.get(0).getmTypeNourriture1() && !addMenuItemsList.get(0).getmTypeNourriture2() &&!addMenuItemsList.get(0).getmTypeNourriture3()) ||
                            (!addMenuItemsList.get(1).getmTypeNourriture1() && !addMenuItemsList.get(1).getmTypeNourriture2() &&!addMenuItemsList.get(1).getmTypeNourriture3() )){
                        Toast.makeText(AddMenuActivity.this, "Veuillez renseigner le type de nourriture souhaité !", Toast.LENGTH_LONG).show();
                    }else {
                        StringBuilder builder = new StringBuilder();
                        builder
                                .append("_link_to_the_API")
                                .append(Objects.requireNonNull(setUpData(2)).get(0)) //Jour
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(1)) //Mois
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(2)) //Année
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(3)) //Heure
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(4)) //Minute
                                .append("&n=")
                                .append(Objects.requireNonNull(setUpData(2)).get(5)) //Nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(6)) //Nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(7)) //Nourriture 3
                                .append("&q=")
                                .append(Objects.requireNonNull(setUpData(2)).get(8)) //Quantité nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(9)) //Quantité nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(2)).get(10)) //Quantité nourriture 3
                                .append("&f=")
                                .append(Objects.requireNonNull(setUpData(2)).get(11)); //Frequence
                        faireLaRequeteHTTP(builder.toString());
                        Log.d(AddMenuActivity.class.getSimpleName(), "sendDataToServer: " + builder.toString());
                    }
                    break;
                }
                case 3:{
                    if ((!addMenuItemsList.get(0).getmTypeNourriture1() && !addMenuItemsList.get(0).getmTypeNourriture2() &&!addMenuItemsList.get(0).getmTypeNourriture3()) ||
                            (!addMenuItemsList.get(1).getmTypeNourriture1() && !addMenuItemsList.get(1).getmTypeNourriture2() &&!addMenuItemsList.get(1).getmTypeNourriture3() )){
                        Toast.makeText(AddMenuActivity.this, "Veuillez renseigner le type de nourriture souhaité !", Toast.LENGTH_LONG).show();
                    }else {
                        StringBuilder builder = new StringBuilder();
                        builder
                                .append("_link_to_the_API")
                                .append(Objects.requireNonNull(setUpData(3)).get(0)) //Jour
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(1)) //Mois
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(2)) //Année
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(3)) //Heure
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(4)) //Minute
                                .append("&n=")
                                .append(Objects.requireNonNull(setUpData(3)).get(5)) //Nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(6)) //Nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(7)) //Nourriture 3
                                .append("&q=")
                                .append(Objects.requireNonNull(setUpData(3)).get(8)) //Quantité nourriture 1
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(9)) //Quantité nourriture 2
                                .append(",")
                                .append(Objects.requireNonNull(setUpData(3)).get(10)) //Quantité nourriture 3
                                .append("&f=")
                                .append(Objects.requireNonNull(setUpData(3)).get(11)); //Frequence
                        Log.d(AddMenuActivity.class.getSimpleName(), "sendDataToServer: " + builder.toString());
                        faireLaRequeteHTTP(builder.toString());
                    }
                    break;
                }
            }
        }
        catch (Exception e){
            /*
            Verifier et gerer l'erreur
             */
            View view = findViewById(R.id.activityAddLayout);
            Snackbar.make(view, "Erreur ! Vous avez sans doute renseigné deux fois le même type de nourriture.", Snackbar.LENGTH_INDEFINITE).show();
            final Snackbar snackbar = Snackbar.make(view, "Erreur ! Vous avez sans doute renseigné deux fois le même type de nourriture.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            //Toast.makeText(this, "Erreur ! Vous avez sans doute renseigné deux fois le même type de nourriture", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Faire la requete HTTP de manière asynchrone
     * @param url : le lien à envoyer (le code en PHP prend le relai après)
     */
    private void faireLaRequeteHTTP(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Toast.makeText(AddMenuActivity.this, "Erreur de connexion avec le serveur !", Toast.LENGTH_SHORT).show();
                Log.d(AddMenuActivity.class.getSimpleName(), "onResponse: Erreur de connexion avec le serveur !");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Erreur : " +response);
                }else {
                    //  Toast.makeText(AddMenuActivity.this, "Ajout du menu dans la base de données avec succès !", Toast.LENGTH_SHORT).show();
                    Intent mainActivity = new Intent(AddMenuActivity.this, MainActivity.class);
                    mainActivity.putExtra("menu added", true);
                    startActivity(mainActivity);
                    finish();
                    Log.d(AddMenuActivity.class.getSimpleName(), "onResponse: Ajout du menu dans la base de données avec succès !");
                }
            }
        });
    }

    /**
     * "Mise en page" des données entrée par l'utilisateur pour les rentrer dans une URL normalisée
     * @param tailleListe
     * @return
     */
    private ArrayList<String> setUpData(int tailleListe){
        switch (tailleListe){
            case 1:{

                ArrayList<String> arrayList = new ArrayList<>();                    //index :
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(16,18)); //Jour : 0
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(19,21)); //Mois : 1
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(22,26)); //Année : 2
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(27,29)); //Heure : 3
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(30,32)); //Minute : 4


                //Differents agencements suivant les types de nourriture selectionnés
                if (addMenuItemsList.get(0).getmTypeNourriture1()){ //Type nourriture :
                    arrayList.add("1"); //Nourriture 1 : 5
                    arrayList.add("0"); //Nourriture 2 : 6
                    arrayList.add("0"); //Nourriture 3 : 7

                    //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                    if (addMenuItemsList.get(0).getmNombrePortions1()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8
                    arrayList.add("00"); // Quantité nourriture 2 : 9
                    arrayList.add("00"); // Quantité nourriture 3 : 10
                }

                if (addMenuItemsList.get(0).getmTypeNourriture2()){
                    arrayList.add("0"); //Nourriture 1 : 5
                    arrayList.add("1"); //Nourriture 2 : 6
                    arrayList.add("0"); //Nourriture 3 : 7

                    arrayList.add("00"); // Quantité nourriture 1 : 8

                    if (addMenuItemsList.get(0).getmNombrePortions2()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                    arrayList.add("00"); // Quantité nourriture 3 : 10

                }
                if (addMenuItemsList.get(0).getmTypeNourriture3()){
                    arrayList.add("0"); //Nourriture 1 : 5
                    arrayList.add("0"); //Nourriture 2 : 6
                    arrayList.add("1"); //Nourriture 3 : 7
                    arrayList.add("00"); // Quantité nourriture 1 : 8
                    arrayList.add("00");  // Quantité nourriture 2 : 9

                    if (addMenuItemsList.get(0).getmNombrePortions3()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10
                }

                //Verification de la fréquence
                if (addMenuItemsList.get(0).getmDate().contains("U"))
                    arrayList.add("u"); // Frequence unique : 11
                else
                    arrayList.add("h"); // Frequence hebdomadaire : 11
                return arrayList;
            }
            case 2:{
                ArrayList<String> arrayList = new ArrayList<>();                    //index :
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(16,18)); //Jour : 0
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(19,21)); //Mois : 1
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(22,26)); //Année : 2
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(27,29)); //Heure : 3
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(30,32)); //Minute : 4


                //Differents agencements suivant les types de nourriture selectionnés
                if (addMenuItemsList.get(0).getmTypeNourriture1()){ //Type nourriture :
                    arrayList.add("1"); //Nourriture 1 : 5

                    /**
                     * On part du principe que l'utilisateur ne sélectionne pas deux fois le meme type de nourriture
                     */


                    if (addMenuItemsList.get(1).getmTypeNourriture2()){
                        arrayList.add("1"); //Nourriture 2 : 6
                        arrayList.add("0"); //Nourriture 3 : 7

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(0).getmNombrePortions1()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions2())); // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions2())); // Quantité nourriture 2 : 9

                        arrayList.add("00"); // Quantité nourriture 3 : 10


                    }else {
                        arrayList.add("0"); //Nourriture 2 : 6
                        if (addMenuItemsList.get(1).getmTypeNourriture3()) {
                            arrayList.add("1"); //Nourriture 3 : 7

                            //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                            if (addMenuItemsList.get(0).getmNombrePortions1()<10)
                                arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8
                            else
                                arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8

                            arrayList.add("00"); // Quantité nourriture 2 : 9

                            //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                            if (addMenuItemsList.get(1).getmNombrePortions3()<10)
                                arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 2 : 9
                            else
                                arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 2 : 9

                        }

                    }



                }
                //Differents agencements suivant les types de nourriture selectionnés
                if (addMenuItemsList.get(0).getmTypeNourriture2()){ //Type nourriture :

                    if (addMenuItemsList.get(1).getmTypeNourriture1()){
                        arrayList.add("1"); //Nourriture 1 : 5
                        arrayList.add("1"); //Nourriture 2 : 6
                        arrayList.add("0"); //Nourriture 3 : 7

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions1()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions1())); // Quantité nourriture 1 : 8
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(0).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions2())); // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions2())); // Quantité nourriture 2 : 9

                        arrayList.add("00"); // Quantité nourriture 3 : 10


                    }else {
                        arrayList.add("0"); //Nourriture 1 : 5
                        arrayList.add("1"); //Nourriture 2 : 6
                        arrayList.add("1"); //Nourriture 3 : 7

                        arrayList.add("00"); // Quantité nourriture 1 : 8
                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(0).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions2())); // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions2())); // Quantité nourriture 2 : 9

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10

                    }

                }
                //Differents agencements suivant les types de nourriture selectionnés
                if (addMenuItemsList.get(0).getmTypeNourriture3()){ //Type nourriture :

                    if (addMenuItemsList.get(1).getmTypeNourriture1()){
                        arrayList.add("1"); //Nourriture 1 : 5
                        arrayList.add("0"); //Nourriture 2 : 6
                        arrayList.add("1"); //Nourriture 3 : 7

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions1()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions1())); // Quantité nourriture 1 : 8
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8

                        arrayList.add("00"); // Quantité nourriture 2 : 9

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(0).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 2 : 9


                    }else {
                        arrayList.add("0"); //Nourriture 1 : 5
                        arrayList.add("1"); //Nourriture 2 : 6
                        arrayList.add("1"); //Nourriture 3 : 7

                        arrayList.add("00"); // Quantité nourriture 1 : 8
                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions2())); // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions2())); // Quantité nourriture 2 : 9

                        //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                        if (addMenuItemsList.get(1).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10

                    }
                }
                //Verification de la fréquence
                if (addMenuItemsList.get(0).getmDate().contains("U"))
                    arrayList.add("u"); // Frequence unique : 11
                else
                    arrayList.add("h"); // Frequence hebdomadaire : 11
                return arrayList;
            }
            case 3:{

                ArrayList<String> arrayList = new ArrayList<>();                    //index :
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(16,18)); //Jour : 0
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(19,21)); //Mois : 1
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(22,26)); //Année : 2
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(27,29)); //Heure : 3
                arrayList.add(addMenuItemsList.get(0).getmDate().substring(30,32)); //Minute : 4

                arrayList.add("1"); //Nourriture 1 : 5
                arrayList.add("1"); //Nourriture 2 : 6
                arrayList.add("1"); //Nourriture 3 : 7

                if (addMenuItemsList.get(0).getmTypeNourriture1()){

                    //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                    if (addMenuItemsList.get(0).getmNombrePortions1()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions1())); // Quantité nourriture 1 : 8



                    if (addMenuItemsList.get(1).getmTypeNourriture2()){

                        if (addMenuItemsList.get(1).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions2()));  // Quantité nourriture 2 : 9



                        if (addMenuItemsList.get(2).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(2).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(2).getmNombrePortions3())); // Quantité nourriture 3 : 10


                    }else if (addMenuItemsList.get(2).getmTypeNourriture2()){

                        if (addMenuItemsList.get(2).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(2).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(2).getmNombrePortions2()));  // Quantité nourriture 2 : 9


                        if (addMenuItemsList.get(1).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10

                    }
                } else if (addMenuItemsList.get(1).getmTypeNourriture1()){
                    //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                    if (addMenuItemsList.get(1).getmNombrePortions1()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions1())); // Quantité nourriture 1 : 8
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions1())); // Quantité nourriture 1 : 8





                    if (addMenuItemsList.get(0).getmTypeNourriture2()){

                        if (addMenuItemsList.get(0).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9



                        if (addMenuItemsList.get(2).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(2).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(2).getmNombrePortions3())); // Quantité nourriture 3 : 10


                    }else if (addMenuItemsList.get(2).getmTypeNourriture2()){

                        if (addMenuItemsList.get(2).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(2).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(2).getmNombrePortions2()));  // Quantité nourriture 2 : 9


                        if (addMenuItemsList.get(0).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10

                    }


                }else if (addMenuItemsList.get(2).getmTypeNourriture1()){
                    //Ajout du chiffre 0 devant la quantité de nourriture (par souci de compatibilité)
                    if (addMenuItemsList.get(2).getmNombrePortions1()<10)
                        arrayList.add("0" + String.valueOf(addMenuItemsList.get(2).getmNombrePortions1())); // Quantité nourriture 1 : 8
                    else
                        arrayList.add(String.valueOf(addMenuItemsList.get(2).getmNombrePortions1())); // Quantité nourriture 1 : 8



                    if (addMenuItemsList.get(0).getmTypeNourriture2()){

                        if (addMenuItemsList.get(0).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions2()));  // Quantité nourriture 2 : 9



                        if (addMenuItemsList.get(1).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions3())); // Quantité nourriture 3 : 10


                    }else if (addMenuItemsList.get(1).getmTypeNourriture2()){

                        if (addMenuItemsList.get(1).getmNombrePortions2()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(1).getmNombrePortions2()));  // Quantité nourriture 2 : 9
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(1).getmNombrePortions2()));  // Quantité nourriture 2 : 9


                        if (addMenuItemsList.get(0).getmNombrePortions3()<10)
                            arrayList.add("0" + String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10
                        else
                            arrayList.add(String.valueOf(addMenuItemsList.get(0).getmNombrePortions3())); // Quantité nourriture 3 : 10

                    }



                }

                //Verification de la fréquence
                if (addMenuItemsList.get(0).getmDate().contains("U"))
                    arrayList.add("u"); // Frequence unique : 11
                else
                    arrayList.add("h"); // Frequence hebdomadaire : 11
                return arrayList;
            }
        }
        return null;
    }


    /**
     * Initialisation de la barre d'outil.
     * @param menu : permet la disposition des boutons dans la barre d'outil
     * @return : renvoie OK pour l'initialisation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu_toolbar_menu, menu);
        return true;
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        /*
        Mise en place de la barre d'outil
         */
        Toolbar toolbar = findViewById(R.id.addmenueToolbar);
        setSupportActionBar(toolbar);

        typeNourritureRestant = findViewById(R.id.nbnourriturerestante);

        /*
        Création de la liste dans une vue interactive
         */
        buildRecyclerView();
        addOnClick(addMenuAdapter.getItemCount());

        addMenu = findViewById(R.id.addFoodButton);
        /*
        Detecter l'ajour d'un nouvel ingrédient dans le menu
         */
        addMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if (addMenuItemsList.size()<3) {
                    if (addMenuAdapter.getIsSpinnerClicked()){
                        addOnClick(addMenuAdapter.getItemCount());
                        removeMenu.setVisibility(View.VISIBLE);
                    }else
                        Toast.makeText(AddMenuActivity.this, "Veuillez renseigner le type de nourriture souhaité !", Toast.LENGTH_LONG).show();

                }
                else
                    addMenu.setVisibility(View.INVISIBLE);
            }
        });

        removeMenu = findViewById(R.id.removeFoodButton);
        removeMenu.setVisibility(View.INVISIBLE);
        /*
        Detecter la suppression d'un ingrédient dans le menu
         */
        removeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addMenuItemsList.size()>1){
                    removeOnClick(addMenuAdapter.getItemCount()-1);
                    addMenu.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    /**
     * Fonction gérant la suppression d'ingrédient dans le menu
     * @param position
     */
    @SuppressLint("RestrictedApi")
    private void removeOnClick(int position) {
        /*
        Remettre les valeurs par défaut
         */
        addMenuItemsList.get(position).setmTypeNourriture1(false);
        addMenuItemsList.get(position).setmTypeNourriture2(false);
        addMenuItemsList.get(position).setmTypeNourriture3(false);
        addMenuItemsList.remove(position);
        addMenuAdapter.notifyItemRemoved(position);
        AddMenuAdapter.spinnerAdapter.notifyDataSetChanged();
        mAddMenuLayoutManager.scrollToPosition(position);

        /*
        Cacher lorsqu'on a atteint le nombre min d'ingrédient
         */
        if (addMenuItemsList.size()==1){
            removeMenu.setVisibility(View.INVISIBLE);
        }
        addMenuAdapter.setTypeNourritureRestant(addMenuAdapter.getTypeNourritureRestant()+1);
        String text = "Type de nourrituƒre restant : " + addMenuAdapter.getTypeNourritureRestant();
        typeNourritureRestant.setText(text);
        addMenuAdapter.isSpinnerClicked(true);


    }

    /**
     * Construction de la vue interactive
     */
    private void buildRecyclerView(){
        addMenuItemsList = new ArrayList<>();
        mAddMenuRecyclerView = findViewById(R.id.recyclerviewAddmenu);
        mAddMenuRecyclerView.setHasFixedSize(true);
        mAddMenuLayoutManager = new LinearLayoutManager(this);
        addMenuAdapter = new AddMenuAdapter(addMenuItemsList);
        mAddMenuRecyclerView.setLayoutManager(mAddMenuLayoutManager);
        mAddMenuRecyclerView.setAdapter(addMenuAdapter);
        addMenuAdapter.setTypeNourritureRestant(3);
        String text = "Type de nourriture restant : " + addMenuAdapter.getTypeNourritureRestant();
        typeNourritureRestant.setText(text);
    }

    /**
     * Fonction pour gérer l'insertion d'un nouvel ingrédient dans le menu
     * @param position
     */
    @SuppressLint("RestrictedApi")
    private void addOnClick(final int position){

        addMenuAdapter.setTypeNourritureRestant(addMenuAdapter.getTypeNourritureRestant()-1);
        String text = "Type de nourriture restant : " + addMenuAdapter.getTypeNourritureRestant();
        typeNourritureRestant.setText(text);

        mAddMenuRecyclerView.clearFocus();
        mAddMenuLayoutManager.scrollToPosition(position);
        addMenuItemsList.add(new AddMenuItem("",false, 0,false,0,false,0));
        addMenuAdapter.notifyItemInserted(position);
        /*
        Ouvir automatiquement la boite de dialogue pour sélectionner la date et l'heure
         */
        if (position == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Objects.requireNonNull(mAddMenuRecyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.dateHeureImageButon).performClick();
                }
            },1);
        }

        /*
        Cacher le bouton lorsqu'on a atteint le nombre max d'ingrédient
         */
        if (addMenuItemsList.size()==3){
            addMenu.setVisibility(View.INVISIBLE);
        }





    }


}
