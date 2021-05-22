package com.mizix.tastynkrunchy.ui.menuliste;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mizix.tastynkrunchy.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * Fragment contenant la liste de tous les menus présents dans la base de données
 */
public class MenuListeFragment extends Fragment {

    /**
     * Création des variables globales
     */
    private static ArrayList<MenuListeItem> menuListeItemsList;
    private static RecyclerView menuListeRecyclerView;
    private static MenuListeAdapter menuListeAdapter;
    private static RecyclerView.LayoutManager mMenuListeLayoutManager;
    private static int tailleListeMenu;
    private static ArrayList<String> listeMenu;
    private static ArrayList<String> dataPourRecyclerView;
    private static TextView noMenu;
    public static FragmentActivity activity;
    private static boolean isCatched;
    private static RelativeLayout layout;
    private static ProgressBar progressBar;
    private ImageButton button;


    /**
     * Création de la vue du fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menuliste, container, false);
    }


    /**
     * Récuperation de la taille totale de la liste des menus de manière asynchrone
     */
    static class RetrieveTailleMenuListeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                if (urls[1].equals("true")) //Reception de la donnée
                    while ((inputLine = in.readLine()) != null)
                        tailleListeMenu = Integer.parseInt(inputLine);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Mise en page des données pour les rendres exploitables par l'application
     * @param menus : menu sous forme de chaine de caractère normalisée
     * @param position : position du menu dans la liste de l'application
     */
    private static void setUpData(String menus, int position) {
        menuListeRecyclerView.clearFocus();
        mMenuListeLayoutManager.scrollToPosition(0);
        String date = menus.substring(11, 13) + "/" + menus.substring(8, 10) + "/" + menus.substring(3, 7) +
                " " + menus.substring(14, 16) + ":" + menus.substring(17, 19);

        menuListeItemsList.add(new MenuListeItem(menus.substring(0, 2), date, menus.substring(40, 41), menus.substring(23, 24), menus.substring(28, 29), menus.substring(33, 34),
                menus.substring(25, 27), menus.substring(30, 32), menus.substring(35, 37), menus.substring(38, 39).charAt(0)));

        menuListeAdapter.notifyItemInserted(position);


        if (menuListeItemsList.size() == 0) {
            noMenu.setVisibility(View.VISIBLE);
        } else
            noMenu.setVisibility(View.GONE);


        //Qt1 = 25,27
        //Qt2 = 30,32
        //Qt3 = 35,37
        /*dataPourRecyclerView.add( menus.substring(0,2) +" " +menus.substring(11,13) + "/" +menus.substring(8,10)+"/"+menus.substring(3,7) +
                " " + menus.substring(14,16) + ":" +menus.substring(17,19) + " " + menus.substring(40,41) + " " + menus.substring(23,24) +
                " " +menus.substring(28,29) + " " + menus.substring(33,34) + " " + menus.substring(25,27) + " " + menus.substring(30,32) +
                " " + menus.substring(35,37) + " " + menus.substring(38,39));*/
    }

    static class RetrieveTousLesMenusTaskTastyMainPHP extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for (int i = 0; i < tailleListeMenu; i++) {
                //Toast.makeText(activity.getApplicationContext(), String.valueOf(tailleListeMenu), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Menus :" + listeMenu.get(i));
                try {
                    setUpData(listeMenu.get(i), i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            progressBar.setVisibility(View.GONE);
            if (tailleListeMenu == 0) {
                noMenu.setVisibility(View.VISIBLE);
                noMenu.setText("Aucun menu trouvé, veuillez cliquer sur + \npour en ajouter.");
            } else {
                noMenu.setVisibility(View.GONE);
                noMenu.setText("Chargement des menus veuillez patienter...");
            }

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //_link_to_the_API&mode=l&m=3
                String urlAChanger = urls[0];
                for (int i = 1; i <= tailleListeMenu; i++) {
                    urlAChanger = urlAChanger.substring(0, urlAChanger.length() - 1) + i; //Parcourir les menus à l'aide de requete URL multiples
                    URL getData = new URL(urlAChanger);
                    BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                    String inputLine;
                    //while ((inputLine = in.readLine()) != null)
                    //Log.d(TAG, "doInBackground: " + inputLine);

                    in.close();
                    /*
                    Petite pause pour eviter la surcharge
                     */
                    Thread.sleep(10);
                    try {
                        URL getData2 = new URL("_link_to_the_API/returnValue.php?m=search"); //Receptionner les chaines de caractères et les stockées
                        BufferedReader in2 = new BufferedReader(new InputStreamReader(getData2.openStream()));
                        String inputLine2;
                        while ((inputLine2 = in2.readLine()) != null)
                            listeMenu.add(inputLine2); //Ajout dans la liste
                        /*if (listeMenu.size()==2)
                            Log.d(TAG, "doInBackground: " + listeMenu.get(1));*/
                        in2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*
                    Petite pause pour eviter la surcharge
                     */
                    Thread.sleep(10);
                }


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    /**
     * Fontion appelée après la création du fragment
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


        layout = view.findViewById(R.id.menulistefragment);
        isCatched = false;
        listeMenu = new ArrayList<>();
        dataPourRecyclerView = new ArrayList<>();
        activity = getActivity();

        /*
        Création d'une barre de chargement rotative
         */
        progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 300);
        params.leftMargin = 590;
        params.topMargin = 950;
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.GONE);
        noMenu = view.findViewById(R.id.nomenuTxtView);
        noMenu.setVisibility(View.VISIBLE);
        noMenu.setText("Aucun menu trouvé, veuillez cliquer sur + \npour en ajouter.");
		button = view.findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Sauvegarde de la position du dernier menu pour faire une suppression de chaine
                 */
                int sizeMenuListBefore = menuListeItemsList.size();

                menuListeItemsList.clear();
                menuListeAdapter.notifyItemRangeRemoved(0, sizeMenuListBefore);

                /*
                Apparition de la barre de chargement
                 */
                progressBar.setVisibility(View.VISIBLE);
                noMenu.setVisibility(View.VISIBLE);
                noMenu.setText("Chargement des menus veuillez patienter...");


                //mettreLaTailleDeAlertBox1000Hauteur();

                /*
                Requetes HTTPS aynchrones
                 */
                faireLaRequeteHTTPTailleListe("_link_to_the_API&mode=l&tm=search", false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        faireLaRequeteHTTPTailleListe("_link_to_the_API/returnValue.php?m=search", true);
                    }
                }, 50);
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        faireLaRequeteHTTPMenus("_link_to_the_API&mode=l&m=1");
                    }
                }, 50);
            }
        });

    }

    /**
     * Actualisation automatique lorsque l'utilisateur ouvre l'application
     */
	    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        }, 1000); //Delai d'1 seconde
    }


    /**
     * Requete asynchrone pour récuperer la taille totale de la liste des menus
     * @param url : lien demandé, requete https
     * @param showString : Paramètre pour recevoir/stocker les données lorsqu'il est actif
     */
    private void faireLaRequeteHTTPTailleListe(String url, final boolean showString) {
        if (showString)
            new RetrieveTailleMenuListeTask().execute(url, "true"); //Reception et lecture de la valeur
        else
            new RetrieveTailleMenuListeTask().execute(url, "false"); //Demande de la valeur

    }

    /**
     * Requete asynchrone pour recuperer les données de tous les menus
     * @param url : requete https
     */
    private void faireLaRequeteHTTPMenus(String url) {
        new RetrieveTousLesMenusTaskTastyMainPHP().execute(url);
    }

    /**
     * Création de la vue interactive
     * @param view
     */
    private void buildRecyclerView(View view) {
        menuListeItemsList = new ArrayList<>();
        menuListeRecyclerView = view.findViewById(R.id.menulistRecyclerView);
        menuListeRecyclerView.setHasFixedSize(true);
        mMenuListeLayoutManager = new LinearLayoutManager(view.getContext());
        menuListeAdapter = new MenuListeAdapter(menuListeItemsList);
        menuListeRecyclerView.setLayoutManager(mMenuListeLayoutManager);
        menuListeRecyclerView.setAdapter(menuListeAdapter);

        /*
        Initialisation des 2 détecteurs
         */
        menuListeAdapter.setOnItemClickListener(new MenuListeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }

            @Override
            public void onActifClick(int position) {
                disableItem(position);
            }
        });
    }

    /**
     * Fonction pour désactiver un menu
     * @param position : position du menu dans la liste
     */
    private void disableItem(int position) {

        //_link_to_the_API&mode=c&m=1&a=1
        StringBuilder url = new StringBuilder();
        url.append("_link_to_the_API&mode=c&m=");
        url.append(menuListeItemsList.get(position).getmMenuID());
        url.append("&a=");
        if (menuListeItemsList.get(position).ismEnabled().equals("1")) {
            menuListeItemsList.get(position).setmEnabled("0");
            url.append("0");
            progressBar.setVisibility(View.VISIBLE);
            noMenu.setVisibility(View.VISIBLE);
            noMenu.setText("Desactivation en cours...");
        } else {
            menuListeItemsList.get(position).setmEnabled("1");
            url.append("1");
            progressBar.setVisibility(View.VISIBLE);
            noMenu.setVisibility(View.VISIBLE);
            noMenu.setText("Activation en cours...");
        }

        new ChangeMenuActiviteTask().execute(url.toString());
    }

    /**
     * Tache asynchrone pour désactiver le menu
     */
    static class ChangeMenuActiviteTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            noMenu.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                }


                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Tache asynchrone pour supprimer le menu
     */
    static class RemoveMenuTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            noMenu.setVisibility(View.GONE);
            //progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL getData = new URL(urls[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(getData.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                }


                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    /**
     * Fonction pour supprimer le menu
     * @param position : position du menu dans la liste
     */
    private void removeItem(int position) {
        progressBar.setVisibility(View.VISIBLE);
        noMenu.setVisibility(View.VISIBLE);
        noMenu.setText("Suppression en cours...");

        //progressDialog.show();
        new RemoveMenuTask().execute("_link_to_the_API&mode=sup&m=" + menuListeItemsList.get(position).getmMenuID());
        menuListeItemsList.remove(position);
        menuListeAdapter.notifyItemRemoved(position);
    }
}