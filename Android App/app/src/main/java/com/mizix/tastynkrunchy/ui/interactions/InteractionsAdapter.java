package com.mizix.tastynkrunchy.ui.interactions;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mizix.tastynkrunchy.R;

import java.util.ArrayList;

/**
 * Adaptateur pour connaitre les interactions des gerbilles avec la cage
 */
public class InteractionsAdapter extends RecyclerView.Adapter<InteractionsAdapter.InteractionsViewHolder> {

    /**
     * Initialisation des variables globales
     */
    private ArrayList<InteractionsItem> mInteractionItem;

    /**
     * Classe de la vue interactive
     */
    public class InteractionsViewHolder extends RecyclerView.ViewHolder{
        /**
         * Initialisation des variables
         */
        TextView historiqueTexview;
        public InteractionsViewHolder(@NonNull View itemView){
            super(itemView);
            historiqueTexview = itemView.findViewById(R.id.historiqueTextView);
        }
    }
    /**
     * Constructeur de l'adaptateur
     * @param interactionsItems : liste des menus à ajouter
     */
    InteractionsAdapter(ArrayList<InteractionsItem> interactionsItems){
        mInteractionItem = interactionsItems;
    }

    /**
     * Création du detenteur de la vue permettant l'affichage des interactions
     * @param parent : page de l'activité
     * @param viewType : type de vue
     * @return : detenteur de la vue
     */
    @NonNull
    @Override
    public InteractionsAdapter.InteractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interaction_item, parent, false);
        return new InteractionsViewHolder(v);
    }

    /**
     * Relier le détenteur de la vue permettant l'affichage des interactions avec les objets le composant
     * @param holder : detenteur
     * @param position : position dans la liste totale
     */
    @Override
    public void onBindViewHolder(@NonNull InteractionsAdapter.InteractionsViewHolder holder, int position) {
        InteractionsItem currentItem = mInteractionItem.get(position);
        holder.historiqueTexview.setText(currentItem.getmHistorique());
    }

    /**
     * Retourner la taille de la liste
     * @return : taille de la liste
     */
    @Override
    public int getItemCount() {
        return mInteractionItem.size();
    }
}
