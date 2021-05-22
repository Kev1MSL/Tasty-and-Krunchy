package com.mizix.tastynkrunchy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static android.Manifest.permission_group.LOCATION;

/**
 * Activité dans laquelle l'utilisateur peut changer le nom des nourritures, le nombre maximum de portion, et d'initialiser le WiFi du distributeur
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Création de variables globales
     */
    private static final int REQUEST_ENABLE_BT = 6;
    private static final String TAG = "Settings";
    private EditText nomNourriture1, nomNourriture2,nomNourriture3, SSID, Password;
    private TextInputLayout SSIDTxtInputLayout, PasswordTxtInputLayout;
    private Button sendDataButton; //Bouton pour envoyer les données WiFi au module ESP32
    private TextView wifiStatus,bluetoothStatus; //Les messages affichant les status du WiFi et Bluetooth
    private int maxPortionValue; //Variable contenant le maximum de portion
    BluetoothDevice mBluetoothDevice; //Le module Bluetooth ESP-32
    private ScrollableNumberPicker maxPortion;
    private ImageButton setWifi;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("800001101-0000-1000-8000-00805f9b34fb"); //Identificateur du module Bluetooth ESP-32
    BluetoothConnectionService mBluetoothConnection; //Service permettant la connexion Bluetooth asynchrone


    /**
     * Fontion appelée lors de la création de l'activité
     * @param savedInstanceState : sert à enregistrer les dernières actions/données de l'activité. Nous n'utilisons pas ce paramètre car nous ne stockons pas ces paramètres.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*
        Pour afficher une fleche retour dans la barre d'outil
         */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*
        Demande de permission de localisation, nécessaire à l'initialisation et fonctionnement Bluetooth
         */
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request permission from user
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        /*
        Initialisation des variables
         */
        initDataVariables();

        /*
        Démarrer le Broadcast receiver pour savoir s'il on est connecté en Bluetooth
         */
        setBluetoothConnectionReceiver();
    }

    /**
     * Fonction pour demarrer ce Broadcast receiver
     */
    private void setBluetoothConnectionReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("Refresh Bluetooth status");
        this.registerReceiver(mReceiver, filter);
    }

    /**
     * Fonction appelée lorsqu'on quitte l'activité
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        Suppression du broadcast receiver
         */
        unregisterReceiver(mReceiver);
    }

    /**
     * Broadcast receiver pour detecter les connexions et déconnexions avec le module ESP32
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Handler handler = new Handler();
            Runnable runnable;

            /*
            Boucle permettant de tenter de se connecter à l'ESP 32
             */
            runnable = new Runnable() {
                @Override
                public void run() {
                    startConnection(mBluetoothDevice);
                }
            };
            /*
            Connexion réussie
             */
            if (Objects.requireNonNull(intent.getExtras()).getBoolean("bluetooth status")){
                handler.removeCallbacksAndMessages(runnable);
                bluetoothStatus.setText("Statut connexion Bluetooth : OK");
                setWifi.setEnabled(true);
            }
            /*
            Echec de la connexion
             */
            else {
                /*
                Essaye de se connecter toute les 5 secondes à l'ESP32
                 */
                handler.postDelayed(runnable, 5000);
                bluetoothStatus.setText("Statut connexion Bluetooth : Aucune");
                setWifi.setEnabled(false);
            }

        }
    };

    /**
     * Initialisation des variables et les connecter aux objets de l'applications
     */
    private void initDataVariables(){
        /*
        Variables contenant le nom des nourritures
         */
        nomNourriture1 = findViewById(R.id.nomNourriture1);
        nomNourriture2 = findViewById(R.id.nomNourriture2);
        nomNourriture3 = findViewById(R.id.nomNourriture3);

        /*
        Variables contenant la valeur max de portion
         */
        maxPortion = findViewById(R.id.maxPortionPicker);

        /*
        Paramètrage de la valeur max de portion
         */
        maxPortion.setMinValue(1);
        maxPortion.setMaxValue(30); //30 est la valeur max de nourriture présente dans un conteneur

        /*
        Charger la valeur précèdemment choisie par l'utilisateur
         */
        loadMaxPortionValue();

        /*
        Détecter la valeur sélectionnée
         */
        maxPortion.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                maxPortionValue = value;
            }
        });

        /*
        Charger les noms de nourriture
         */
        loadNourritureNoms();


        bluetoothStatus = findViewById(R.id.bluetoothStatus);
        wifiStatus = findViewById(R.id.wifiStatus);


        SSIDTxtInputLayout = findViewById(R.id.SSIDTxtLayout);
        PasswordTxtInputLayout = findViewById(R.id.PasswordTxtLayout);
        sendDataButton = findViewById(R.id.sendWifiCreditentials);



        SSID = findViewById(R.id.SSID);
        Password = findViewById(R.id.Password);
        SSIDTxtInputLayout.setVisibility(View.GONE);
        PasswordTxtInputLayout.setVisibility(View.GONE);
        sendDataButton.setVisibility(View.GONE);
        setWifi = findViewById(R.id.connectionButton);
        setWifi.setEnabled(false);
        /*
        Vérifier s'il y a une connexion WiFI
         */
        checkWifiStatus();

        /*
        Vérifier s'il y a une connexion Bluetooth
         */
        checkBluetoothStatus();

        /*
        Permet de faire apparaître le bouton envoyer sur le clavier de l'utilisateur
         */
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendDataButton.requestFocus();
                sendDataButton.performClick();
                return false;
            }
        });

        /*
        Détecter le clic sur le bouton d'initialisation du WiFi
         */
        setWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWifiStatus();
                /*
                Récuperer les informations WiFi du réseau connecté
                 */
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
                WifiInfo info = Objects.requireNonNull(wifiManager).getConnectionInfo ();

                /*
                Récuperer le nom du réseau WiFi
                 */
                String ssid  = info.getSSID();

                /*
                Si le téléphone n'est pas en liaison avec un réseau, proposer à l'utlisateur de se connecter
                 */
                if (ssid.equals("<unknown ssid>")){
                   Snackbar snackbar = Snackbar.make(findViewById(R.id.senttingsLayout), "Aucun réseau WiFi détecté", Snackbar.LENGTH_INDEFINITE);
                   snackbar.setAction("Se connecter", new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           /*
                           Lance la page de paramètre WiFi du téléphone de l'utilisateur pour qu'il puisse se connecter
                            */
                           WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                           wifi.setWifiEnabled(true); // Activer le Wifi
                           startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                       }
                   });
                   //Affiche une mini boite de dialogue avec un bouton "Se connecter"
                   snackbar.show();
                }
                /*
                Le téléphone est déja connecté à un réseau WiFi, l'initialisation peut continuer
                 */
                else {
                    SSIDTxtInputLayout.setVisibility(View.VISIBLE); //Affiche la zone de texte : nom Wifi
                    PasswordTxtInputLayout.setVisibility(View.VISIBLE); //Affiche la zone de texte : mot de passe
                    sendDataButton.setVisibility(View.VISIBLE); //Affiche le bouton envoyer
                    SSID.setText(ssid.substring(1, ssid.length()-1)); //Enlève les guillemets du nom du réseau Wifi
                    Password.requestFocus(); //Demande le focus sur la zone de texte : mot de passe
                    InputMethodManager imm = (InputMethodManager) Password.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(Password, InputMethodManager.SHOW_IMPLICIT); //Ouvre le clavier
                }

            }
        });
        /*
        Detecter le clic sur le bouton envoyer les données WiFi
         */
        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Ajout d'un préfixe pour que l'ESP 32 comprenne ce qui va être recu
                Les virgules servent de séparateurs et sont très pratiques pour lire les données sans aucun bug
                 */
                String msg = "WifiInit";
                msg += ",";
                msg += SSID.getText().toString();
                msg += ",";
                msg += Password.getText().toString();
                msg+=",";
                mBluetoothConnection.write(msg.getBytes()); //Envoie les données

            }
        });
    }



    /**
     * Enregistre la valeur max de nourriture choisie par l'utilisateur
     */
    private void saveMaxPortionValue(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences max portion", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("max portion", maxPortionValue);
        editor.apply();
    }



    /**
     * Charge la valeur max de nourriture choisie précèdemment par l'utilisateur
     */
    private void loadMaxPortionValue(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences max portion", MODE_MULTI_PROCESS);
        maxPortionValue = sharedPreferences.getInt("max portion", 8);
        maxPortion.setValue(maxPortionValue);
    }




    /**
     * Detecter le statut de connectivité WiFi
     */
    private void checkWifiStatus(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected())
            wifiStatus.setText("Statut connexion WiFi : OK");
        else
            wifiStatus.setText("Statut connexion WiFi : Aucune");
    }



    /**
     * Detecter le statut de connectivité Bluetooth avec l'ESP 32
     */
    private void checkBluetoothStatus() {
        /*
        Démarrage de l'adaptateur
         */
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        /*
        Si le Bluetooth n'est pas allumé, faire une demande d'activation
         */
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                /*
                Demande d'activation du Bluetooth
                 */
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{
                /*
                Reception de tous les appareils pouvant être connecté au smartphone
                 */
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if(pairedDevices.size() > 0)
                {
                    /*
                    Boucle permettant de naviguer parmi tous les appareils et détecter l'ESP 32
                     */
                    for(BluetoothDevice device : pairedDevices)
                    {
                        /*
                        L'ESP 32 a été détécté
                         */
                        if(device.getName().equals("ESP32"))
                        {
                            setWifi.setEnabled(true);
                            mBluetoothDevice = device;

                            /*
                            S'apparairer à l'ESp 32
                             */
                            mBluetoothDevice.createBond();

                            /*
                            Initialiser le service de connexion asynchrone
                             */
                            mBluetoothConnection = new BluetoothConnectionService(SettingsActivity.this);

                            /*
                            Démarrer la connexion
                             */
                            startConnection(mBluetoothDevice);

                            break; //Casser la boucle puisque nous avons détécter le bon appareil
                        }
                        /*
                        Si rien n'a été détécté : afficher un statut négatif
                         */
                        else {
                            Log.d(TAG, "checkBluetoothStatus: not connected");
                            setWifi.setEnabled(false);
                            bluetoothStatus.setText("Statut connexion Bluetooth : Aucune");
                        }
                    }
                }

            }


        }
    }



    /**
     * Méthode pour commencer la connexion
     * @param mBTDevice : ESP 32
     */
    public void startConnection(BluetoothDevice mBTDevice){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }



    /**
     * Initialisation de la connexion
     * @param device : ESP 32
     * @param uuid : Identificateur du module
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initialisation RFCOM Bluetooth Connection.");
        mBluetoothConnection.startClient(device,uuid);
    }



    /**
     * Enregistrement des noms de nourriture
     */
    private void saveNourritureNoms(){

        ArrayList<String> nomsNourritures = new ArrayList<>();
        nomsNourritures.add(nomNourriture1.getText().toString());
        nomsNourritures.add(nomNourriture2.getText().toString());
        nomsNourritures.add(nomNourriture3.getText().toString());


        SharedPreferences sharedPreferencesNourritures = getSharedPreferences("shared preferences nourritures noms", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferencesNourritures.edit();
        Gson gson = new Gson();
        String json = gson.toJson(nomsNourritures);
        editor.putString("noms nourritures",json);
        editor.apply();
    }



    /**
     * Chargement des noms de nourrriture
     */
    private void loadNourritureNoms(){
        ArrayList<String> nomsNourritures;

        SharedPreferences sharedPreferencesNourritures = getSharedPreferences("shared preferences nourritures noms", MODE_MULTI_PROCESS);
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

        nomNourriture1.setText(nomsNourritures.get(0));
        nomNourriture2.setText(nomsNourritures.get(1));
        nomNourriture3.setText(nomsNourritures.get(2));
    }



    /**
     * Detecter si l'utilisateur appuie sur le bouton retour de son téléphone
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        Mettre fin à la connexion Bluetooth
         */
        mBluetoothConnection.cancel();

        /*
        Faire les enregistrements
         */
        saveNourritureNoms();
        saveMaxPortionValue();

        /*
        Retourner à l'activité principale)
         */
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Fonction pour détecter le clic sur le bouton paramètre de la barre d'outil
     * @param item : objet sur lequel l'utilisateur a interagi
     * @return : renvoie OK pour le clic
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    





}
