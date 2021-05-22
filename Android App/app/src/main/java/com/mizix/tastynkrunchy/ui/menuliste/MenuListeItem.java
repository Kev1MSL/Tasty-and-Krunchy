package com.mizix.tastynkrunchy.ui.menuliste;

/**
 * Objet composant les menus
 */
public class MenuListeItem {

    /**
     * Initialisation des variables globales
     */
    private String mMenuID;
    private String mDate;
    private String mEnabled;
    private String mNourriture1;
    private String mNourriture2;
    private String mNourriture3;
    private String mQuantiteNourriture1;
    private String mQuantiteNourriture2;
    private String mQuantiteNourriture3;
    private char mFrequence;

    /**
     * Objet constructeur permettant de faire passer et organiser les paramètres des menus
     * @param menuID : identificateur du menu
     * @param date : date et heure de service
     * @param enabled : menu activé ou désactivé
     * @param nourriture1 : nourriture 1 servie ?
     * @param nourriture2 : nourriture 2 servie ?
     * @param nourriture3 : nourriture 3 servie ?
     * @param quantiteNourriture1 : quantité de nourriture 1
     * @param quantiteNourriture2 : quantité de nourriture 2
     * @param quantiteNourriture3 : quantité de nourriture 3
     * @param frequence : frequence de distribution
     */
    MenuListeItem(String menuID, String date, String enabled, String nourriture1,String nourriture2,String nourriture3, String quantiteNourriture1,String quantiteNourriture2,String quantiteNourriture3,char frequence){
        mMenuID = menuID;
        mDate = date;
        mEnabled =enabled;
        mNourriture1 = nourriture1;
        mNourriture2 = nourriture2;
        mNourriture3 = nourriture3;
        mQuantiteNourriture1 = quantiteNourriture1;
        mQuantiteNourriture2 = quantiteNourriture2;
        mQuantiteNourriture3 = quantiteNourriture3;
        mFrequence = frequence;
    }

    public void setmMenuID(String mMenuID) {
        this.mMenuID = mMenuID;
    }

    public String getmMenuID() {
        return mMenuID;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmDate() {
        return mDate;
    }

    public String ismEnabled() {
        return mEnabled;
    }

    public void setmEnabled(String mEnabled) {
        this.mEnabled = mEnabled;
    }

    public void setmNourriture1(String mNourriture1) {
        this.mNourriture1 = mNourriture1;
    }

    public String ismNourriture1() {
        return mNourriture1;
    }

    public void setmNourriture2(String mNourriture2) {
        this.mNourriture2 = mNourriture2;
    }

    public String ismNourriture2() {
        return mNourriture2;
    }

    public void setmNourriture3(String mNourriture3) {
        this.mNourriture3 = mNourriture3;
    }

    public String ismNourriture3() {
        return mNourriture3;
    }

    public void setmQuantiteNourriture1(String mQuantiteNourriture1) {
        this.mQuantiteNourriture1 = mQuantiteNourriture1;
    }

    public String getmQuantiteNourriture1() {
        return mQuantiteNourriture1;
    }

    public void setmQuantiteNourriture2(String mQuantiteNourriture2) {
        this.mQuantiteNourriture2 = mQuantiteNourriture2;
    }

    public String getmQuantiteNourriture2() {
        return mQuantiteNourriture2;
    }

    public void setmQuantiteNourriture3(String mQuantiteNourriture3) {
        this.mQuantiteNourriture3 = mQuantiteNourriture3;
    }

    public String getmQuantiteNourriture3() {
        return mQuantiteNourriture3;
    }

    public void setmFrequence(char mFrequence) {
        this.mFrequence = mFrequence;
    }

    public char getmFrequence() {
        return mFrequence;
    }
}
