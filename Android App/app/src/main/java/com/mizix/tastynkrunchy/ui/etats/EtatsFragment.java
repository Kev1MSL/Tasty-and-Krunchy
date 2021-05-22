package com.mizix.tastynkrunchy.ui.etats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mizix.tastynkrunchy.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment affichant le dernier etat du distributeur
 */
public class EtatsFragment extends Fragment {


    /**
     * Initialisation des variables globales
     */
    private static RelativeLayout batterieConteneur, qtNourriture1Conteneur, qtNourriture2Conteneur,qtNourriture3Conteneur, dechetConteneur;
    private static TextView batterieTexView, qtNourriture1TextView, qtNourriture2TextView, qtNourriture3TextView, dechetTextView;
    private static RelativeLayout layout;
    private static ProgressBar progressBar;
    private static String resultString;
    private static ArrayList<String> resultArray;
    private static FragmentActivity activity;
    private ImageButton refreshButton;


    /**
     * Création de la vue du fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_etats, container, false);
    }
    /**
     * Fontion appelée après la création du fragment
     * @param savedInstanceState : sert à enregistrer les dernières actions/données du fragment. Nous n'utilisons pas ce paramètre car nous ne stockons pas ces paramètres.
     * @param view : vue du fragment sur laquelle nous pouvons interagir
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        setUpObjets(view);
        refreshButton = view.findViewById(R.id.refreshButtonEtat);
        /*
        Actualiser l'etat du distributeur
         */
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetViewData();
                progressBar.setVisibility(View.VISIBLE);
                new GetLastStateTask().execute("_link_to_the_API");
            }
        });
    }


    /**
     * Actualisation automatique lorsque l'utilisateur rejoint cette page
     */
    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshButton.performClick();
            }
        }, 1000);
    }


    /**
     * Tache asynchrone pour demander le dernier etat du distributeur
     */
    static class GetLastStateTask extends AsyncTask<String, Void, Void> {



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new ReturnLastStateTask().execute("_link_to_the_API");
                }
            },10);
        }



        @Override
        protected Void doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null){

                }


                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    /**
     * Tache asynchrone pour recevoir le dernier etat du distributeur
     */
    static class ReturnLastStateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setReceivedData();
            changeViewData();
        }





        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    Log.d(EtatsFragment.class.getSimpleName(), "doInBackground: " + inputLine);
                    resultString = inputLine;
                }


                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }

    /**
     * Initialiser les objets et les relier aux variables du fragment
     * @param view
     */
    private void setUpObjets(View view) {
        batterieConteneur = view.findViewById(R.id.batterieConteneur);
        qtNourriture1Conteneur = view.findViewById(R.id.QuantiteNourriture1Conteneur);
        qtNourriture2Conteneur = view.findViewById(R.id.QuantiteNourriture2Conteneur);
        qtNourriture3Conteneur = view.findViewById(R.id.QuantiteNourriture3Conteneur);
        dechetConteneur = view.findViewById(R.id.MaxPoubelleConteneur);
        batterieTexView = view.findViewById(R.id.batterieRestante);
        qtNourriture1TextView = view.findViewById(R.id.minNourriture1);
        qtNourriture2TextView = view.findViewById(R.id.minNourriture2);
        qtNourriture3TextView = view.findViewById(R.id.minNourriture3);
        dechetTextView = view.findViewById(R.id.maxPoubelle);
        layout = view.findViewById(R.id.fragmentetat);
        progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1440, 100);
        progressBar.setIndeterminate(true);
        params.topMargin = 260;
        layout.addView(progressBar, params);
        //progressDialog = getDialogProgessBar(view).create();
        progressBar.setVisibility(View.GONE);
        resetViewData();
    }

    /**
     * Mise en page des données recues
     */
    private static void setReceivedData(){
        progressBar.setVisibility(View.GONE);
        resultArray = new ArrayList<>();
        resultArray.add(resultString.substring(0,2)); //Batterie : 0
        resultArray.add(resultString.substring(3,4)); // Quantité nourriture 1 : 1
        resultArray.add(resultString.substring(5,6)); // Quantité nourriture 2 : 2
        resultArray.add(resultString.substring(7,8)); // Quantité nourriture 3 : 2
        resultArray.add(resultString.substring(9,10)); // Poubelle atteinte : 3



    }


    /**
     * Charger les noms de nourritures
     * @param context : contexte de la vue du fragment
     * @return : les noms des nourritures sous forme de liste
     */
    private static ArrayList<String> getNourritureNoms(Context context){
        ArrayList<String> nomsNourritures;

        SharedPreferences sharedPreferencesNourritures = context.getSharedPreferences("shared preferences nourritures noms", Context.MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = sharedPreferencesNourritures.getString("noms nourritures", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        nomsNourritures = gson.fromJson(json, type);
        if (nomsNourritures == null){
            nomsNourritures = new ArrayList<>();
            nomsNourritures.add("Nourriture 1");
            nomsNourritures.add("Nourriture 2");
            nomsNourritures.add("Nourriture 3");
        }

        return nomsNourritures;
    }

    /**
     * Remettre tous les paramètres à 0, par défaut pendant le chargement
     */
    private void resetViewData(){
        Drawable drawableReset = activity.getResources().getDrawable(R.drawable.backgroundetatunknown);
        String batterie = "Batterie restante :\t\t       ";
        batterieTexView.setText(batterie);
        batterieConteneur.setBackground(drawableReset);

        String qNourriture1 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(0) + ":\t\t         ";
        qtNourriture1TextView.setText(qNourriture1);
        qtNourriture1Conteneur.setBackground(drawableReset);

        String qNourriture2 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(1) + ":\t\t         ";
        qtNourriture2TextView.setText(qNourriture2);
        qtNourriture2Conteneur.setBackground(drawableReset);

        String qNourriture3 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(2) + ":\t\t         ";
        qtNourriture3TextView.setText(qNourriture3);
        qtNourriture3Conteneur.setBackground(drawableReset);

        String dechet = "Quantité de déchets :\t\t       ";
        dechetTextView.setText(dechet);
        dechetConteneur.setBackground(drawableReset);


        batterieConteneur.setPadding(80,80,80,80);
        qtNourriture1Conteneur.setPadding(80,80,80,80);
        qtNourriture2Conteneur.setPadding(80,80,80,80);
        qtNourriture3Conteneur.setPadding(80,80,80,80);
        dechetConteneur.setPadding(80,80,80,80);
    }


    /**
     * Charger les données du dernier etat du distributeur sur la page
     */
    private static void changeViewData(){
        Drawable drawableGood = activity.getResources().getDrawable(R.drawable.backgroundetatgood);
        Drawable drawableMedium = activity.getResources().getDrawable(R.drawable.backgroundetatmedium);
        Drawable drawableCritical = activity.getResources().getDrawable(R.drawable.backgroundetatcritic);
        String batterie = "Batterie restante : " + resultArray.get(0) + "%";
        batterieTexView.setText(batterie);


        /*
        Superieur ou egal a 67% --> Bon niveau de batterie
         */
        if (Integer.parseInt(resultArray.get(0)) >= 67 )
            batterieConteneur.setBackground(drawableGood);
        /*
        Superieur ou egal a 33% et strictement inferieur à 67% --> Niveau de batterie moyen
         */
        else if (Integer.parseInt(resultArray.get(0))<67 && Integer.parseInt(resultArray.get(0))>=33)
            batterieConteneur.setBackground(drawableMedium);
        /*
        Strictement inferieur à 33% --> Niveau de batterie faible
         */
        else
            batterieConteneur.setBackground(drawableCritical);



        if (resultArray.get(1).equals("1")){
            String qNourriture1 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(0) + " : suffisante";
            qtNourriture1TextView.setText(qNourriture1);
            qtNourriture1Conteneur.setBackground(drawableGood);
        }else {
            String qNourriture1 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(0) + " : insuffisante";
            qtNourriture1TextView.setText(qNourriture1);
            qtNourriture1Conteneur.setBackground(drawableCritical);
        }

        if (resultArray.get(2).equals("1")){
            String qNourriture2 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(1) + " : suffisante";
            qtNourriture2TextView.setText(qNourriture2);
            qtNourriture2Conteneur.setBackground(drawableGood);
        }else {
            String qNourriture2 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(1) + " : insuffisante";
            qtNourriture2TextView.setText(qNourriture2);
            qtNourriture2Conteneur.setBackground(drawableCritical);
        }

        if (resultArray.get(3).equals("1")){
            String qNourriture3 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(2) + " : suffisante";
            qtNourriture3TextView.setText(qNourriture3);
            qtNourriture3Conteneur.setBackground(drawableGood);
        }else {
            String qNourriture3 = "Quantité de " + getNourritureNoms(activity.getApplicationContext()).get(2) + " : insuffisante";
            qtNourriture3TextView.setText(qNourriture3);
            qtNourriture3Conteneur.setBackground(drawableCritical);
        }

        if (resultArray.get(4).equals("1")){
            String dechet = "Quantité de déchets : critique";
            dechetTextView.setText(dechet);
            dechetConteneur.setBackground(drawableCritical);
        }else {
            String dechet = "Quantité de déchets : acceptable";
            dechetTextView.setText(dechet);
            dechetConteneur.setBackground(drawableGood);
        }

        /*
        Pour une mise en page correcte, rajouter des marges interieures est nécessaire.
         */
        batterieConteneur.setPadding(70,70,70,70);
        qtNourriture1Conteneur.setPadding(80,80,80,80);
        qtNourriture2Conteneur.setPadding(80,80,80,80);
        qtNourriture3Conteneur.setPadding(80,80,80,80);
        dechetConteneur.setPadding(80,80,80,80);
    }
}