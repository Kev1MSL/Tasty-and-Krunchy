package com.mizix.tastynkrunchy;


/**
 * Objet pour ajouter les menus
 */
public class AddMenuItem {

    //Initialisation des variables locales
    private String mDate;
    private boolean mTypeNourriture1;
    private int mNombrePortions1;
    private boolean mTypeNourriture2;
    private int mNombrePortions2;
    private boolean mTypeNourriture3;
    private int mNombrePortions3;


    /**
     * Objet constructeur permettant de faire passer et organiser les paramètres des menus
     * @param date : Date convertie automatiquement en chaine de caractères
     * @param typeNourriture1 : Paramètre booleen pour savoir si la nourriture 1 est séléctionnée
     * @param nombrePortions1 : Nombre de portions pour la nourriture 1 (écrite avec 2 chiffres)
     * @param typeNourriture2 : Paramètre booleen pour savoir si la nourriture 2 est séléctionnée
     * @param nombrePortions2 : Nombre de portions pour la nourriture 2 (écrite avec 2 chiffres)
     * @param typeNourriture3 : Paramètre booleen pour savoir si la nourriture 3 est séléctionnée
     * @param nombrePortions3 : Nombre de portions pour la nourriture 3 (écrite avec 2 chiffres)
     */
    AddMenuItem(String date, boolean typeNourriture1, int nombrePortions1, boolean typeNourriture2, int nombrePortions2,boolean typeNourriture3, int nombrePortions3){
        mDate = date;
        mTypeNourriture1 = typeNourriture1;
        mNombrePortions1 = nombrePortions1;
        mTypeNourriture2 = typeNourriture2;
        mNombrePortions2 = nombrePortions2;
        mTypeNourriture3 = typeNourriture3;
        mNombrePortions3 = nombrePortions3;
    }

    public void setmDate(String date){
        mDate = date;
    }
    public void setmTypeNourriture1(boolean typeNourriture1){
        mTypeNourriture1 = typeNourriture1;
    }
    public void setmNombrePortions1(int nombrePortions1){
        mNombrePortions1 = nombrePortions1;
    }
    public void setmTypeNourriture2(boolean typeNourriture2){
        mTypeNourriture2 = typeNourriture2;
    }
    public void setmNombrePortions2(int nombrePortions2){
        mNombrePortions2 = nombrePortions2;
    }
    public void setmTypeNourriture3(boolean typeNourriture3){
        mTypeNourriture3 = typeNourriture3;
    }
    public void setmNombrePortions3(int nombrePortions3){
        mNombrePortions3 = nombrePortions3;
    }

    String getmDate(){return mDate;}

    boolean getmTypeNourriture1(){return mTypeNourriture1;}

    public int getmNombrePortions1() {
        return mNombrePortions1;
    }

    boolean getmTypeNourriture2(){return mTypeNourriture2;}

    public int getmNombrePortions2() {
        return mNombrePortions2;
    }
    boolean getmTypeNourriture3(){return mTypeNourriture3;}

    public int getmNombrePortions3() {
        return mNombrePortions3;
    }
}
