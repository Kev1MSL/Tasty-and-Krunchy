package com.mizix.tastynkrunchy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * Adaptateur pour ajouter les menus
 */
public class AddMenuAdapter extends RecyclerView.Adapter<AddMenuAdapter.AddMenuViewHolder> {
    /**
     * Initialisation des variables globales
     */
    private ArrayList<AddMenuItem> mAddMenuList;
    public static ArrayAdapter<CharSequence> spinnerAdapter;
    private int typeNourritureRestant = 3;


    /**
     * Définir le nombre de type de nourriture restant
     * @param typeNourritureRestant : variable contenant la donnée
     */
    public void setTypeNourritureRestant(int typeNourritureRestant) {
        this.typeNourritureRestant = typeNourritureRestant;
    }

    /**
     * Retourne le nombre de type de nourriture restant
     * @return : la variable contenant la donnée
     */
    public int getTypeNourritureRestant() {
        return typeNourritureRestant;
    }

    /**
     * Création du detenteur de la vue permettant la création des menus
     * @param parent : page de l'activité
     * @param viewType : type de vue
     * @return : detenteur de la vue
     */
    @NonNull
    @Override
    public AddMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmenu_item, parent, false);
        return new AddMenuViewHolder(v);
    }

    /**
     * Relier le détenteur de la vue permettant la création des menus avec les objets le composant
     * @param holder : detenteur
     * @param position : position dans la liste totale
     */
    @Override
    public void onBindViewHolder(@NonNull final AddMenuViewHolder holder, final int position) {
        isSpinnerClicked(false);

        /*
        Afficher la date et l'heure seulement sur la premiere boite
         */
        if (position==1||position==2){
            holder.mDateTimePicker.setVisibility(View.GONE);
            holder.mDateTextView.setVisibility(View.GONE);
        }


        final AddMenuItem currentItem = mAddMenuList.get(position);
        /*
        Recuperer l'heure pour cette boite
         */
        holder.mDateTextView.setText(currentItem.getmDate());
        /*
        Recuperer le max de portion choisi et le mettre en application
         */
        holder.mPortionSeekbar.setMax(loadMaxPortionValue(holder.itemView.getContext())-1);

        /*
        Mettre le selectionneur de nourriture sur une valeur par défaut
         */
        holder.mTypeNourritureSpinner.setSelection(0);


        /*
        Détecter les interactions avec le selectionneur de nourriture
         */
        holder.mTypeNourritureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mAddMenuList.get(position).setmDate(holder.mDateTextView.getText().toString());
                switch (i){
                    case 1:{
                        mAddMenuList.get(position).setmTypeNourriture1(true);
                        mAddMenuList.get(position).setmTypeNourriture2(false);
                        mAddMenuList.get(position).setmTypeNourriture3(false);
                        mAddMenuList.get(position).setmNombrePortions1(holder.mPortionSeekbar.getProgress()+1); //Necessaire car Seekbar.min() fonctionne que sous android API 26
                        Log.d(TAG, "onItemSelected: ");

                        /*
                        Permet que lorsque l'utilisateur clique sur "Veuillez...."  de le rediriger sur "Nourriture1"
                         */
                        isSpinnerClicked(true);
                        break;
                    }
                    case 2:{
                        mAddMenuList.get(position).setmTypeNourriture1(false);
                        mAddMenuList.get(position).setmTypeNourriture2(true);
                        mAddMenuList.get(position).setmTypeNourriture3(false);
                        mAddMenuList.get(position).setmNombrePortions2(holder.mPortionSeekbar.getProgress()+1);
                        Log.d(TAG, "onItemSelected: ");
                        isSpinnerClicked(true);
                        break;
                    }
                    case 3:{
                        mAddMenuList.get(position).setmTypeNourriture1(false);
                        mAddMenuList.get(position).setmTypeNourriture2(false);
                        mAddMenuList.get(position).setmTypeNourriture3(true);
                        mAddMenuList.get(position).setmNombrePortions3(holder.mPortionSeekbar.getProgress()+1);
                        Log.d(TAG, "onItemSelected: ");
                        isSpinnerClicked(true);
                        break;
                    }
                    case 0:{

                        if (holder.isAlertDialogShowed){
                            holder.mTypeNourritureSpinner.setSelection(1);
                        }else {
                            holder.mTypeNourritureSpinner.setSelection(0);
                        }
                        isSpinnerClicked(false);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "onNothingSelected: ");
            }
        });

        /*
        Detecter les modification de portions
         */
        holder.mPortionSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                majPortion(i, holder, position);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }


    /**
     * Recuperer la taille de liste
     * @return : Taille de la liste
     */
    @Override
    public int getItemCount() {
        return mAddMenuList.size();
    }

    private boolean _isClicked;
    public void isSpinnerClicked(boolean isClicked){
        _isClicked = isClicked;
    }
    public boolean getIsSpinnerClicked(){
        return _isClicked;
    }

    /**
     * Classe de la vue interactive
     */
    class AddMenuViewHolder extends RecyclerView.ViewHolder{

        /**
         * Initialisation des variables
         */
        ImageButton mDateTimePicker;
        TextView mDateTextView;
        Spinner mTypeNourritureSpinner;
        SeekBar mPortionSeekbar;
        TextView mNombrePortionsTextView;
        boolean isAlertDialogShowed =false;


        /**
         * Création de l'objet/constructeur
         * @param itemView
         */
        public AddMenuViewHolder(@NonNull final View itemView) {
            super(itemView);

            /*
            Liaison aux objets visuels
             */
            mDateTimePicker = itemView.findViewById(R.id.dateHeureImageButon);
            mDateTextView = itemView.findViewById(R.id.dateheureTextView);
            mTypeNourritureSpinner = itemView.findViewById(R.id.typeNourritureSpinner);
            mPortionSeekbar = itemView.findViewById(R.id.portionSeekBar);
            mNombrePortionsTextView = itemView.findViewById(R.id.portionTextView);
            mPortionSeekbar.setMax(loadMaxPortionValue(itemView.getContext())-1);
            /*
            Mise en place du selectionneur de dates
             */
            mDateTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Initialisation du sélectionneur de date et d'heure
                    final SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                            "Date et heure de distribution",
                            "Unique",
                            "Annuler", "Hebdo");

                    //Assignement des valeurs
                    dateTimeDialogFragment.startAtCalendarView();
                    dateTimeDialogFragment.set24HoursMode(true);
                    dateTimeDialogFragment.setMinimumDateTime(Calendar.getInstance().getTime());
                    dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2030,Calendar.DECEMBER,31).getTime());
                    dateTimeDialogFragment.setDefaultDateTime(Calendar.getInstance().getTime());

                    //Definition du format du jour et du mois
                    try {
                        dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.FRANCE));
                    }catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e){
                        Log.e(AddMenuViewHolder.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
                    }

                    //Mise en place du Listener

                    //Bouton pour un menu avec fréquence hebdomadaire
                    dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {

                        /**
                         * Valider la date pour un menu avec fréquence hebdomadaire
                         * @param date Date sélectionnée
                         */
                        @Override
                        public void onNeutralButtonClick(Date date) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
                            String dateFormatted = simpleDateFormat.format(date);
                            String dateTime = "Date et heure : " + dateFormatted + " Frequence : Hebdomadaire";
                            mDateTextView.setText(dateTime);
                            isAlertDialogShowed = true;
                            mAddMenuList.get(0).setmDate(dateTime);
                        }

                        /**
                         * Valider la date pour un menu unique
                         * @param date Date sélectionnée
                         */
                        @Override
                        public void onPositiveButtonClick(Date date) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
                            String dateFormatted = simpleDateFormat.format(date);
                            String dateTime = "Date et heure : " + dateFormatted + " Frequence : Unique";
                            mDateTextView.setText(dateTime);
                            isAlertDialogShowed = true;
                            mAddMenuList.get(0).setmDate(dateTime);
                        }

                        /**
                         * Annuler la saisie de la date
                         * @param date Date sélectionnée
                         */
                        @Override
                        public void onNegativeButtonClick(Date date) {
                            dateTimeDialogFragment.dismiss();
                            isAlertDialogShowed = false;
                        }
                    });
                    //Afficher
                    FragmentManager manager = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
                    dateTimeDialogFragment.show(manager, "dialog_time");

                }
            });



            /*
            Initialisation du menu déroulant
             */
            spinnerAdapter = new ArrayAdapter<>(itemView.getContext(), R.layout.spinner_layout, loadDataForSpinner(itemView.getContext()));
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
            mTypeNourritureSpinner.setAdapter(spinnerAdapter);
            mTypeNourritureSpinner.setSelection(0);
            /*
            Detecter si l'utilisateur a cliqué sur "Veuillez..." et le rediriger vers "Nourriture1"
             */
            mTypeNourritureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (isAlertDialogShowed){
                        if (i==0){
                            mTypeNourritureSpinner.setSelection(1);
                        }
                    }else {
                        if (i==0){
                            mTypeNourritureSpinner.setSelection(0);
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d(TAG, "onNothingSelected: ");
                }
            });



        }




    }

    /**
     * Mettre à jour les portions
     * @param progress : position de la barre
     * @param holder : detenteur du menu
     * @param position : position du menu
     */
    private void majPortion(int progress, AddMenuViewHolder holder, int position ) {
        String textToShow = "Nombre de portions : " + String.valueOf(progress + 1);
        holder.mNombrePortionsTextView.setText(textToShow);
        //mAddMenuList.get(position).setmNombrePortions1(progress+1);

        switch (holder.mTypeNourritureSpinner.getSelectedItemPosition()){
            case 1:{
                mAddMenuList.get(position).setmNombrePortions1(holder.mPortionSeekbar.getProgress()+1);
                break;
            }
            case 2:{
                mAddMenuList.get(position).setmNombrePortions2(holder.mPortionSeekbar.getProgress()+1);
                break;
            }
            case 3:{
                mAddMenuList.get(position).setmNombrePortions3(holder.mPortionSeekbar.getProgress()+1);
                break;
            }
        }

    }

    /**
     * Constructeur de l'adaptateur
     * @param addMenuListe : liste des menus à ajouter
     */
    AddMenuAdapter(ArrayList<AddMenuItem> addMenuListe){
        mAddMenuList = addMenuListe;
    }

    /**
     * Charger les noms des nourritures/données pour le selectionneur de nourriture (menu déroulant)
     * @param context : contexte du detenteur necessaire pour acceèder à la mémoire de l'application
     * @return : liste des noms de nourriture
     */
    private ArrayList<CharSequence> loadDataForSpinner(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences data for spinner", Context.MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("data for spinner", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<CharSequence> dataForSpinner = new ArrayList<>();
        dataForSpinner = gson.fromJson(json, type);
        return dataForSpinner;
    }

    /**
     * Chargement des portion max de nourriture
     * @param context : contexte du détenteur nécessaire pour accèder à la mémoire de l'application
     * @return : la valeur max à passer à la SeekBar
     */
    private int loadMaxPortionValue(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences max portion", Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getInt("max portion", 8); //Par défaut 8 car étant la valeur max pouvant être contenue dans la gamelle
    }



}
