package com.mizix.tastynkrunchy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {

    /**
     * Création des variables globales
     */
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("800001101-0000-1000-8000-00805f9b34fb"); //Identificateur du module Bluetooth

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice; // ESP 32
    private UUID deviceUUID;
    //ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread; //Thread de connexion (asynchrone)

    /**
     * Création du constructeur de la classe
     * @param context : contexte de l'activité
     */
    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }


    /**
     * Ce fil s'exécute pendant l'écoute des connexions entrantes.
     * Il se comporte comme un client côté serveur.
     * Il fonctionne jusqu'à ce qu'une connexion soit acceptée (ou jusqu'à ce qu'elle soit annulée).
     */
    private class AcceptThread extends Thread {

        /*
        Le socket de serveur local
         */
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            /*
            Créer un nouveau socket de serveur d'écoute
             */
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Paramètrage du Server en utilisant: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread En cours d'exécution.");

            BluetoothSocket socket = null;

            try{
                /*
                Il s'agit d'un appel bloquant et ne renverra que sur un
                une connexion réussie ou une exception
                 */
                Log.d(TAG, "run: RFCOM server socket demarrage.....");

                /*
                Accepter la connexion
                 */
                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket a accepté la connexion.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            /*
            Etablir la connexion
             */
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "Fin mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Annulation AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Echec de la fermeture de AcceptThread ServerSocket. " + e.getMessage() );
            }
        }

    }

    /**
     * Ce thread fonctionne en essayant d'établir une connexion sortante
     * avec un appareil. Il passe tout droit ; la connexion soit
     * réussit ou échoue.
     * Nous utiliserons surtout et principalement ce thread lors de nos connexions avec l'ESP 32
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        /**
         * Création du constructeur
         * @param device : ESP 32
         * @param uuid : identificateur de l'ESP 32
         */
        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: Commencé.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "En cours mConnectThread ");

            /*
            Créer un socket avec l'appareil bluetooth sélectionné
             */
            try {
                Log.d(TAG, "ConnectThread: Essaye de créer InsecureRfcommSocket en utilisant ce UUID: "
                        +MY_UUID_INSECURE );
                /*
                Création d'une communication avec l'ESP 32 via un socket temporaire
                 */
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Echec de la création de InsecureRfcommSocket " + e.getMessage());
            }

            /*
            Bascullement sur le socket principal
             */
            mmSocket = tmp;

            /*
            Suppression de la recherche car cela ralentit la connexion
             */
            mBluetoothAdapter.cancelDiscovery();

            /*
            Intent pour renvoyer l'état de la connexion et actualiser le statut de la connexion Bluetooth
            dans Settings Activity
             */
            Intent intent = new Intent("Refresh Bluetooth status");

            /*
            Connexion à l'ESP 32
             */
            try {
                /*
                Il s'agit d'un appel bloqué et ne renvoie que sur un
                une connexion réussie ou une exception
                 */
                mmSocket.connect();


                /*
                Connexion réussie
                 */
                intent.putExtra("bluetooth status", true);

                /*
                Passe les données à SettingsActivity
                 */
                mContext.sendBroadcast(intent);
                Log.d(TAG, "run: ConnectThread connecté.");
            } catch (IOException e) {
                /*
                 Fermer le socket
                 */
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Socket fermé.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Echec de la fermeture de la connexion avec le socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Echec de la connexion à ce UUID: " + MY_UUID_INSECURE );


                /*
                Echec de la connexion, l'appareil est soit :
                - Pas allumé
                - Pas activé en mode Bluetooth
                 */
                intent.putExtra("bluetooth status", false);

                /*
                Passe les données à SettingsActivity
                 */
                mContext.sendBroadcast(intent);
            }

            /*
            Création d'un thread pour gérer les transmissions et les interactions
             */
            connected(mmSocket,mmDevice);
        }

        /**
         * Quitter/Arreter la communication
         */
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Fermeture du Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Echec de close() de mmSocket dans Connectthread. " + e.getMessage());
            }
        }
    }



    /**
     * Lancement du service. Démarrer AcceptThread pour commencer une session en mode d'écoute ( en mode serveur).
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Fermer tous les threads essayant de se connecter
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     * AcceptThread démarre et reste en attente d'une connexion.
     * ConnectThread démarre ensuite et tente d'établir une connexion avec les autres périphériques AcceptThread.
     * @param device : ESP 32
     * @param uuid : identificateur du module
     */

    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(TAG, "startClient: Démarré.");

        /*
        Démarrage du Thread
         */
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     Le ConnectedThread qui est responsable de la maintenance de la connexion de BTConnection, l'envoi de données, et
     la reception de données entrantes par des flux d'entrée/sortie.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Demarrage.");

            mmSocket = socket;
            /*
            Création de variables temporaires
             */
            InputStream tmpIn = null;
            OutputStream tmpOut = null;




            try {
                /*
                Liaison des variables temporaires au Socket
                 */
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            Bascullement sur les variables principales
             */
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /**
         * Réception de données
         */
        public void run(){
            byte[] buffer = new byte[1024];  // Matrice pour enregistrer le flux

            int bytes; // bytes retournés par read()

            // Boucle lisant les données à l'infini tant qu'il n'y a pas d'erreur
            while (true) {
                try {
                    /*
                    Lecture des données
                     */
                    bytes = mmInStream.read(buffer);

                    /*
                    Conversion en chaine de caractères pour être lisible et affichage par l'application
                     */
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.e(TAG, "write: Erreur en lisant le flux entrant. " + e.getMessage() );
                    break;
                }
            }
        }

        /**
         * Ecriture des données
         * @param bytes : données à envoyer
         */
        public void write(byte[] bytes) {

            /*
            Affiche les données à envoyer dans la console
             */
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                /*
                Envoie les données
                 */
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Erreur en écrivant sur le flux de sortie. " + e.getMessage() );
            }
        }

        /*
        Fermer la connexion
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Début.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write dans le ConnectedThread de manière asynchrone
     *
     * @param out : les bytes à écrire
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        /*
        Créer un objet temporaire pour vérifier le bon fonctionnement de ConnectedThread
         */
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Ecriture demandée.");
        //perform the write
        mConnectedThread.write(out);
    }

    public void cancel(){
        /*
        Créer un objet temporaire pour vérifier le bon fonctionnement de ConnectedThread
         */
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "cancel: Arret demandé.");
        //perform the cancel
        mConnectedThread.cancel();
    }

}