package com.mizix.tastynkrunchy.ui.interactions;

/**
 * Objet d'interaction
 */
public class InteractionsItem {

    /**
     * Initialisation de la variable globale
     */
    private String mHistorique;

    /**
     * Objet constructeur permettant de faire passer et organiser les paramètres de l'historique
     * @param historique
     */
    InteractionsItem(String historique){
        mHistorique = historique;
    }

    public String getmHistorique() {
        return mHistorique;
    }

    public void setmHistorique(String mHistorique) {
        this.mHistorique = mHistorique;
    }
}
