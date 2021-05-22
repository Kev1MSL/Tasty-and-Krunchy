package com.mizix.tastynkrunchy.ui.menuliste;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mizix.tastynkrunchy.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Adapateur pour afficher la liste de tous les menus présents dans la base de données
 */
public class MenuListeAdapter extends RecyclerView.Adapter<MenuListeAdapter.MenuListeViewHolder> {
    /**
     * Création de variable globales
     */
    private ArrayList<MenuListeItem> mMenuListeItems;
    private OnItemClickListener mListener;

    /**
     * Interface pour gérer les clics
     */
    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onActifClick(int position);
    }

    /**
     * Initialisation du détecteur
     * @param listener : detecteur
     */
    void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    /**
     * Classe de la vue interactive
     */
    public class MenuListeViewHolder extends RecyclerView.ViewHolder {

        /**
         * Création des variables
         */
        CheckBox mCheckBox;
        TextView mDateTextView, mFrequenceTextView, mNourriture1TextView, mNourriture2TextView,mNourriture3TextView;
        ImageView mDeleteImage;

        /**
         * Création de l'objet/constructeur
         * @param itemView
         */
        public MenuListeViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.menuCheckbox);
            mDateTextView = itemView.findViewById(R.id.dateMenuListeTextView);
            mFrequenceTextView = itemView.findViewById(R.id.frequenceMenuListeTextView);
            mNourriture1TextView =  itemView.findViewById(R.id.nourriture1TextView);
            mNourriture2TextView =  itemView.findViewById(R.id.nourriture2TextView);
            mNourriture3TextView =  itemView.findViewById(R.id.nourriture3TextView);
            mDeleteImage = itemView.findViewById(R.id.imageDelete);

            /*
            Detecter le clic pour supprimer le menu
             */
            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MenuListeAdapter.this.mListener !=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            MenuListeAdapter.this.mListener.onDeleteClick(position);
                        }
                    }
                }
            });
            /*
            Detecter le clic pour activer ou désactiver le menu
             */
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MenuListeAdapter.this.mListener !=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION)
                            MenuListeAdapter.this.mListener.onActifClick(position);
                    }
                }
            });
        }

    }
    /**
     * Constructeur de l'adaptateur
     * @param menuListeItems : liste des menus à ajouter
     */
    MenuListeAdapter(ArrayList<MenuListeItem> menuListeItems){
        mMenuListeItems = menuListeItems;
    }

    /**
     * Création du detenteur de la vue permettant l'affichage de tous les menus
     * @param parent : page de l'activité
     * @param viewType : type de vue
     * @return : detenteur de la vue
     */
    @NonNull
    @Override
    public MenuListeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuliste_item,parent,false);
        return new MenuListeViewHolder(v);
    }

    /**
     * Relier le détenteur de la vue permettant la création des menus avec les objets le composant
     * @param holder : detenteur
     * @param position : position de la liste totale
     */
    @Override
    public void onBindViewHolder(@NonNull final MenuListeViewHolder holder, int position) {

        /*
        Mettre en page les données à partir des valeurs retournées par l'URL
         */
        MenuListeItem currentItem = mMenuListeItems.get(position);

        /*
        Mise en place de l'heure
         */
        holder.mDateTextView.setText(currentItem.getmDate());

        if (currentItem.ismEnabled().equals("1"))
            holder.mCheckBox.setChecked(true);
        else
            holder.mCheckBox.setChecked(false);
        if (currentItem.getmFrequence() == 'u'){
            holder.mFrequenceTextView.setText("Frequence : Unique");
        }else {
            holder.mFrequenceTextView.setText("Frequence : Hebdomadaire");
        }

        if (currentItem.ismNourriture1().equals("1")) {
            if (currentItem.getmQuantiteNourriture1().startsWith("0")){
                if (currentItem.getmQuantiteNourriture1().equals("01")){
                    /*
                    Détection du pluriel
                     */
                    if (getNourritureNoms(holder.itemView.getContext()).get(0).endsWith("s")){
                        String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e)s avec " + currentItem.getmQuantiteNourriture1().replace("0","") + " portion";
                        holder.mNourriture1TextView.setText(nourriture1String);
                    }else {
                        String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e) avec " + currentItem.getmQuantiteNourriture1().replace("0","") + " portion";
                        holder.mNourriture1TextView.setText(nourriture1String);
                    }
                }else {
                    if (getNourritureNoms(holder.itemView.getContext()).get(0).endsWith("s")){
                        String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e)s avec " + currentItem.getmQuantiteNourriture1().replace("0","") + " portions";
                        holder.mNourriture1TextView.setText(nourriture1String);
                    }else {
                        String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e) avec " + currentItem.getmQuantiteNourriture1().replace("0","") + " portions";
                        holder.mNourriture1TextView.setText(nourriture1String);
                    }

                }

            }else {
                if (getNourritureNoms(holder.itemView.getContext()).get(0).endsWith("s")){
                    String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e)s avec " + currentItem.getmQuantiteNourriture1() + " portions";
                    holder.mNourriture1TextView.setText(nourriture1String);
                }else {
                    String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " servi(e) avec " + currentItem.getmQuantiteNourriture1() + " portions";
                    holder.mNourriture1TextView.setText(nourriture1String);
                }

            }

        }else {
            if (getNourritureNoms(holder.itemView.getContext()).get(0).endsWith("s")){
                String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " ne sont pas servi(e)s";
                holder.mNourriture1TextView.setText(nourriture1String);
            }else {
                String nourriture1String = getNourritureNoms(holder.itemView.getContext()).get(0) + " n'est pas servi(e)";
                holder.mNourriture1TextView.setText(nourriture1String);
            }

        }
        if (currentItem.ismNourriture2().equals("1")){
            if (currentItem.getmQuantiteNourriture2().startsWith("0")){
                if (currentItem.getmQuantiteNourriture2().equals("01")){
                    if (getNourritureNoms(holder.itemView.getContext()).get(1).endsWith("s")){
                        String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e)s avec " + currentItem.getmQuantiteNourriture2().replace("0","") + " portion";
                        holder.mNourriture2TextView.setText(nourriture2String);
                    }else {
                        String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e) avec " + currentItem.getmQuantiteNourriture2().replace("0","") + " portion";
                        holder.mNourriture2TextView.setText(nourriture2String);
                    }

                }else {
                    if (getNourritureNoms(holder.itemView.getContext()).get(1).endsWith("s")){
                        String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e)s avec " + currentItem.getmQuantiteNourriture2().replace("0","") + " portions";
                        holder.mNourriture2TextView.setText(nourriture2String);
                    }else {
                        String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e) avec " + currentItem.getmQuantiteNourriture2().replace("0","") + " portions";
                        holder.mNourriture2TextView.setText(nourriture2String);
                    }

                }

            }else {
                if (getNourritureNoms(holder.itemView.getContext()).get(1).endsWith("s")){
                    String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e)s avec " + currentItem.getmQuantiteNourriture2() + " portions";
                    holder.mNourriture2TextView.setText(nourriture2String);
                }else {
                    String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " servi(e) avec " + currentItem.getmQuantiteNourriture2() + " portions";
                    holder.mNourriture2TextView.setText(nourriture2String);
                }

            }
        }else {
            if (getNourritureNoms(holder.itemView.getContext()).get(1).endsWith("s")){
                String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " ne sont pas servi(e)s";
                holder.mNourriture2TextView.setText(nourriture2String);
            }else {
                String nourriture2String = getNourritureNoms(holder.itemView.getContext()).get(1) + " n'est pas servi(e)";
                holder.mNourriture2TextView.setText(nourriture2String);
            }

        }
        if (currentItem.ismNourriture3().equals("1")){
            if (currentItem.getmQuantiteNourriture3().startsWith("0")){
                if (currentItem.getmQuantiteNourriture3().equals("01")){
                    if (getNourritureNoms(holder.itemView.getContext()).get(2).endsWith("s")){
                        String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e)s avec " + currentItem.getmQuantiteNourriture3().replace("0","") + " portion";
                        holder.mNourriture3TextView.setText(nourriture3String);
                    }else {
                        String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e) avec " + currentItem.getmQuantiteNourriture3().replace("0","") + " portion";
                        holder.mNourriture3TextView.setText(nourriture3String);
                    }

                }else {
                    if (getNourritureNoms(holder.itemView.getContext()).get(2).endsWith("s")){
                        String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e)s avec " + currentItem.getmQuantiteNourriture3().replace("0","") + " portions";
                        holder.mNourriture3TextView.setText(nourriture3String);
                    }else {
                        String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e) avec " + currentItem.getmQuantiteNourriture3().replace("0","") + " portions";
                        holder.mNourriture3TextView.setText(nourriture3String);
                    }

                }

            }else {
                if (getNourritureNoms(holder.itemView.getContext()).get(2).endsWith("s")){
                    String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e)s avec " + currentItem.getmQuantiteNourriture3() + " portions";
                    holder.mNourriture3TextView.setText(nourriture3String);
                }else {
                    String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " servi(e) avec " + currentItem.getmQuantiteNourriture3() + " portions";
                    holder.mNourriture3TextView.setText(nourriture3String);
                }

            }
        }else {
            if (getNourritureNoms(holder.itemView.getContext()).get(2).endsWith("s")){
                String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " ne sont pas pas servi(e)s";
                holder.mNourriture3TextView.setText(nourriture3String);
            }else {
                String nourriture3String = getNourritureNoms(holder.itemView.getContext()).get(2) + " n'est pas servi(e)";
                holder.mNourriture3TextView.setText(nourriture3String);
            }

        }

    }

    /**
     * Charger les noms des nourritures/données
     * @param context : contexte du detenteur necessaire pour acceèder à la mémoire de l'application
     * @return : liste des noms de nourriture
     */
    private ArrayList<String> getNourritureNoms(Context context){
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
     * Retourne la taille de la liste
     * @return : taille de la liste
     */
    @Override
    public int getItemCount() {
        return mMenuListeItems.size();
    }


}
