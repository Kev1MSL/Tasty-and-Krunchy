package com.mizix.tastynkrunchy.ui.interactions;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mizix.tastynkrunchy.R;
import com.mizix.tastynkrunchy.ui.menuliste.MenuListeFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


/**
 * Fragment dans lequel l'utilisateur peut connaitre les dates et heures d'interactions des gerbilles avec la cage
 */
public class InteractionsFragment extends Fragment {

    /**
     * Initialisation des variables globales
     */
    private static ArrayList<InteractionsItem> interactionsItemsHistoriqueListe;
    private static String dataBruteHistorique;
    private static RecyclerView historiqueRecyclerView;
    private static InteractionsAdapter historiqueAdapter;
    private static RecyclerView.LayoutManager historiqueLayoutManager;
    private static int tailleListeHistorique;
    private static FragmentActivity activity;
    private static RelativeLayout layout;
    private static ProgressBar progressBar;
    private static TextView noMenu;
    private static String TAG = InteractionsFragment.class.getSimpleName();
    private ImageButton refreshButton;

    /**
     * Creation du fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interaction, container, false);
    }

    /**
     * Actualiser la liste lorsque l'utilisateur revient sur cette page
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
     * Fontion appelée lors de la création du fragment
     * @param savedInstanceState : sert à enregistrer les dernières actions/données du fragment. Nous n'utilisons pas ce paramètre car nous ne stockons pas ces paramètres.
     * @param view : vue du fragment sur laquelle nous pouvons interagir
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        Création de la liste intéractive
         */
        buildRecyclerView(view);



        activity = getActivity();
        layout = view.findViewById(R.id.interactionFragment);


        /*
        Création d'une barre de chargement rotative
         */
        progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.leftMargin = 590;
        params.topMargin = 950;
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.GONE);


        /*
        Si la liste des interactions est vides, l'application doit le faire savoir à l'utilisateur
         */
        noMenu = view.findViewById(R.id.noHistoriqueTxtView);
        noMenu.setVisibility(View.VISIBLE);
        noMenu.setText("Aucun historique trouvé, veuillez cliquer sur le bouton actualiser.");
        refreshButton = view.findViewById(R.id.refreshButtonInteraction);
        ImageButton deleteAllHistoriqueButton = view.findViewById(R.id.deleteAllInteractionButton);
        /*
        Bouton pour actualiser la liste des interactions
         */
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sizeHistoriqueBefore = interactionsItemsHistoriqueListe.size();
                interactionsItemsHistoriqueListe.clear();
                historiqueAdapter.notifyItemRangeRemoved(0, sizeHistoriqueBefore);

                progressBar.setVisibility(View.VISIBLE);
                noMenu.setVisibility(View.VISIBLE);
                noMenu.setText("Chargement de l'historique veuillez patienter...");

                faireLaRequeteHTTPTailleListe("_link_to_the_API", false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        faireLaRequeteHTTPTailleListe("_link_to_the_API", true);
                    }
                }, 50);
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        faireLaRequeteHTTPHistorique("_link_to_the_API");
                    }
                }, 50);
            }
        });
        /*
        Detecter si l'utilisateur veut supprimer tout sont historique
         */
        deleteAllHistoriqueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastpostion = interactionsItemsHistoriqueListe.size();
                interactionsItemsHistoriqueListe.clear();
                historiqueAdapter.notifyItemRangeRemoved(0,lastpostion);
                progressBar.setVisibility(View.VISIBLE);
                noMenu.setVisibility(View.VISIBLE);
                noMenu.setText("Suppression de l'historique en cours veuillez patienter...");
                new InteractionsFragment.DeleteHistoriqueTask().execute("_link_to_the_API");
            }
        });


    }

    /**
     * Suppression de l'historique de manière asynchrone
     */
    static class DeleteHistoriqueTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            noMenu.setVisibility(View.VISIBLE);
            noMenu.setText("Aucun historique trouvé, veuillez cliquer sur le bouton actualiser.");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
/*                String inputLine;
                if (urls[1].equals("true"))
                    while ((inputLine = in.readLine()) != null)
                        tailleListeHistorique = Integer.parseInt(inputLine);
                Log.d(TAG, "doInBackground: taille historique : " + tailleListeHistorique);*/
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    /**
     * Requete asynchrone pour récuperer la taille totale de la liste des interactions
     * @param url : lien demandé
     * @param showString : Paramètre pour recevoir/stocker les données lorsqu'il est actif
     */
    private void faireLaRequeteHTTPTailleListe(String url, final boolean showString) {
        if (showString)
            new InteractionsFragment.RetrieveTailleHistoriqueTask().execute(url, "true");//Reception et lecture de la valeur
        else
            new InteractionsFragment.RetrieveTailleHistoriqueTask().execute(url, "false"); //Demande de la valeur

    }


    /**
     * Requete asynchrone pour recuperer les données de toutes les interactions
     * @param url : requete https
     */
    private void faireLaRequeteHTTPHistorique(String url) {
        new InteractionsFragment.RetrieveTousLHistoriqueTaskTastyMainPHP().execute(url);
    }


    /**
     * Tache asynchrone pour recuperer la taille de l'historique
     */
    static class RetrieveTailleHistoriqueTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                if (urls[1].equals("true"))
                    while ((inputLine = in.readLine()) != null)
                        tailleListeHistorique = Integer.parseInt(inputLine);
                Log.d(TAG, "doInBackground: taille historique : " + tailleListeHistorique);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Fonction pour mettre en page les données recues afin de les rendre exploitables par l'application
     * @param historique
     * @param position
     */
    private static void setUpData(String historique, int position) {

        //Vos gerbilles ont mangé à 10h00 le 01/01/2020
        String heure = historique.substring(11,13);
        String minute = historique.substring(14,16);
        String jour = historique.substring(8,10);
        String mois = historique.substring(5,7);
        String annee = historique.substring(0,4);

        interactionsItemsHistoriqueListe.add(new InteractionsItem("Vos gerbilles ont mangé à " + heure + "h" + minute + " le " + jour + "/"+ mois + "/" +annee));
        historiqueAdapter.notifyItemInserted(position);
    }

    /**
     * Tache asynchrone pour recuperer tout l'historique
     */
    static class RetrieveTousLHistoriqueTaskTastyMainPHP extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //2002-06-10 06:19:00 taille 19

            /*
            3 --> 22
            25 --> 44
            47 --> 66

            ecart de 22 --> (2,4,6) *11
             */

            int j = 0;
            int position = 0;
            for (int i = 3; i <= tailleListeHistorique * 22; i += 22) {
                j += 2;
                setUpData(dataBruteHistorique.substring(i, j * 11), position);
                position++;
            }


            progressBar.setVisibility(View.GONE);
            if (tailleListeHistorique == 0) {
                noMenu.setVisibility(View.VISIBLE);
                noMenu.setText("Aucun historique trouvé, veuillez cliquer sur le bouton actualiser.");
            } else {
                noMenu.setVisibility(View.GONE);
                noMenu.setText("Chargement de l'historique veuillez patienter...");
            }

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                //while ((inputLine = in.readLine()) != null)
                //Log.d(TAG, "doInBackground: " + inputLine);

                in.close();
                Thread.sleep(10);
                try {
                    URL getData2 = new URL("_link_to_the_API");
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(getData2.openStream()));
                    String inputLine2;
                    while ((inputLine2 = in2.readLine()) != null)
                        dataBruteHistorique = inputLine2; //Reception des données sous forme d'une chaine de caractère normalisée
                    Log.d(TAG, "doInBackground: " + dataBruteHistorique);
                        /*if (listeMenu.size()==2)
                            Log.d(TAG, "doInBackground: " + listeMenu.get(1));*/
                    in2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread.sleep(10);


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    /**
     * Création de la vue interactive
     * @param view
     */
    private void buildRecyclerView(View view) {
        interactionsItemsHistoriqueListe = new ArrayList<>();
        historiqueRecyclerView = view.findViewById(R.id.interactionRecyclerView);
        historiqueRecyclerView.setHasFixedSize(true);
        historiqueLayoutManager = new LinearLayoutManager(view.getContext());
        historiqueAdapter = new InteractionsAdapter(interactionsItemsHistoriqueListe);
        historiqueRecyclerView.setLayoutManager(historiqueLayoutManager);
        historiqueRecyclerView.setAdapter(historiqueAdapter);

    }

}