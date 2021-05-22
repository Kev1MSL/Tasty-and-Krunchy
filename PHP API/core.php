<?php
if (validerLaCleSecurisee() == true) {
//HEY maboi
    connectionBaseDeDonnees();
    // Tentative de requete de type selection
    $sql = "SELECT * FROM MenuTable";
    if ($result = mysqli_query(connectionBaseDeDonnees(), $sql)) {
        if (isset($_GET['mode'])) {
            getMode($_GET['mode']);
        } else {
            if (mysqli_num_rows($result) > 0) {
                echo "<html>";
                echo "<head>";
                echo "<title>Tasty n' Krunchy</title>";
                echo "<style>";
                echo "table{
                        display:grid;
                        grid-template-columns: repeat(5,1fr);
                        border: 1px solid black;
                    }
                    td{
                        padding: 8px 4px;
                        border-left: 1px, solid black;
                        border-bottom: 1px solid black;
                        border-right: 1px solid black;
                        border: 1px solid black;
                    }
                    th{
                        padding: 8px 4px;
                        border-left: 1px, solid black;
                        border-bottom: 1px solid black;
                        border-right: 1px solid black;
                        border: 1px solid black;
                    }";

                echo "</style>";
                echo "</head>";
                echo "<table>";
                echo "<tr>";
                echo "<th>MenuID</th>";
                echo "<th>Dates</th>";
                echo "<th>Nourriture1</th>";
                echo "<th>Quantite1</th>";
                echo "<th>Nourriture2</th>";
                echo "<th>Quantite2</th>";
                echo "<th>Nourriture3</th>";
                echo "<th>Quantite3</th>";
                echo "<th>Frequence</th>";
                echo "<th>Actif</th>";
                echo "</tr>";
                while ($row = mysqli_fetch_array($result)) {
                    echo "<tr>";
                    echo "<td>" . $row['MenuId'] . "</td>";
                    echo "<td>" . $row['Dates'] . "</td>";
                    echo "<td>" . $row['Nourriture1'] . "</td>";
                    echo "<td>" . $row['Quantite1'] . "</td>";
                    echo "<td>" . $row['Nourriture2'] . "</td>";
                    echo "<td>" . $row['Quantite2'] . "</td>";
                    echo "<td>" . $row['Nourriture3'] . "</td>";
                    echo "<td>" . $row['Quantite3'] . "</td>";
                    echo "<td>" . $row['Frequence'] . "</td>";
                    echo "<td>" . $row['Actif'] . "</td>";
                    echo "</tr>";
                }
                echo "</table>";
                
                
                $sqlEtat = "SELECT * FROM EtatDistributeurTable";
                if ($resultEtat = mysqli_query(connectionBaseDeDonnees(), $sqlEtat)) {
                    if (mysqli_num_rows($resultEtat) > 0) {
                        echo "<table>";
                        echo "<tr>";
                        echo "<th>Batterie Restante</th>";
                        echo "<th>MinNourriture1Atteint</th>";
                        echo "<th>MinNourriture2Atteint</th>";
                        echo "<th>MinNourriture3Atteint</th>";
                        echo "<th>MaxPoubelleAtteint</th>";
                        echo "</tr>";
                        while ($rowEtat = mysqli_fetch_array($resultEtat)) {
                            echo "<tr>";
                            echo "<td>" . $rowEtat['BatterieRestante'] . "</td>";
                            echo "<td>" . $rowEtat['MinNourriture1Atteint'] . "</td>";
                            echo "<td>" . $rowEtat['MinNourriture2Atteint'] . "</td>";
                            echo "<td>" . $rowEtat['MinNourriture3Atteint'] . "</td>";
                            echo "<td>" . $rowEtat['MaxPoubelleAtteint'] . "</td>";
                            echo "</tr>";
                        }
                        echo "</table>";
                    }
                }
                
                
                $sqlHistorique = "SELECT * FROM RapportInteractionsGerbillesAvecDistributeurTable";
                if ($resultHistorique = mysqli_query(connectionBaseDeDonnees(), $sqlHistorique)) {
                    if (mysqli_num_rows($resultHistorique) > 0) {
                        echo "<table>";
                        echo "<tr>";
                        echo "<th>HistoriqueID</th>";
                        echo "<th>Historique</th>";
                        echo "</tr>";
                        while ($rowHistorique = mysqli_fetch_array($resultHistorique)) {
                            echo "<tr>";
                            echo "<td>" . $rowHistorique['HistoriqueID'] . "</td>";
                            echo "<td>" . $rowHistorique['Historique'] . "</td>";
                            echo "</tr>";
                        }
                        echo "</table>";
                    }
                }
                
                echo "</html>";

                // Vider le set de résultats
                mysqli_free_result($result);
            } else {
                echo "Aucun enregistrement correspondant à la requete n'a été trouvé.";
            }
        }
    } else {
        echo "ERREUR: Impossible d'executer $sql. " . mysqli_error(connectionBaseDeDonnees());
    }

    // Fermer la connexion
    mysqli_close(connectionBaseDeDonnees());
    } else {
    die('<p>Echec de la connexion !!' . '</p>' . '<p>Etes-vous un intrus qui veuille faire du mal aux petites gerbilles !?' . '</p>');
}

/*
 * Fonction pour afficher le tableau de la base de données
 */
function showTable()
{
    connectionBaseDeDonnees();
    // Tentative de requete de type selection
    $sql2 = "SELECT * FROM MenuTable";
    if ($result = mysqli_query(connectionBaseDeDonnees(), $sql2)) {
        if (mysqli_num_rows($result) > 0) {
            echo "<html>";
            echo "<head>";
            echo "<title>Tasty n' Krunchy</title>";
            echo "<style>";
            echo "table{
                            display:grid;
                            grid-template-columns: repeat(5,1fr);
                            border: 1px solid black;
                        }
                        td{
                            padding: 8px 4px;
                            border-left: 1px, solid black;
                            border-bottom: 1px solid black;
                            border-right: 1px solid black;
                            border: 1px solid black;
                        }
                        th{
                            padding: 8px 4px;
                            border-left: 1px, solid black;
                            border-bottom: 1px solid black;
                            border-right: 1px solid black;
                            border: 1px solid black;
                        }";
            echo "</style>";
            echo "</head>";
            echo "<table>";
            echo "<tr>";
            echo "<th>MenuID</th>";
            echo "<th>Dates</th>";
            echo "<th>Nourriture1</th>";
            echo "<th>Quantite1</th>";
            echo "<th>Nourriture2</th>";
            echo "<th>Quantite2</th>";
            echo "<th>Nourriture3</th>";
            echo "<th>Quantite3</th>";
            echo "<th>Frequence</th>";
            echo "<th>Actif</th>";
            echo "</tr>";
            while ($row = mysqli_fetch_array($result)) {
                echo "<tr>";
                echo "<td>" . $row['MenuId'] . "</td>";
                echo "<td>" . $row['Dates'] . "</td>";
                echo "<td>" . $row['Nourriture1'] . "</td>";
                echo "<td>" . $row['Quantite1'] . "</td>";
                echo "<td>" . $row['Nourriture2'] . "</td>";
                echo "<td>" . $row['Quantite2'] . "</td>";
                echo "<td>" . $row['Nourriture3'] . "</td>";
                echo "<td>" . $row['Quantite3'] . "</td>";
                echo "<td>" . $row['Frequence'] . "</td>";
                echo "<td>" . $row['Actif'] . "</td>";
                echo "</tr>";
            }
            echo "</table>";
            
            
            $sqlEtat = "SELECT * FROM EtatDistributeurTable";
            if ($resultEtat = mysqli_query(connectionBaseDeDonnees(), $sqlEtat)) {
                if (mysqli_num_rows($resultEtat) > 0) {
                    echo "<table>";
                    echo "<tr>";
                    echo "<th>Batterie Restante</th>";
                    echo "<th>MinNourriture1Atteint</th>";
                    echo "<th>MinNourriture2Atteint</th>";
                    echo "<th>MinNourriture3Atteint</th>";
                    echo "<th>MaxPoubelleAtteint</th>";
                    echo "</tr>";
                    while ($rowEtat = mysqli_fetch_array($resultEtat)) {
                        echo "<tr>";
                        echo "<td>" . $rowEtat['BatterieRestante'] . "</td>";
                        echo "<td>" . $rowEtat['MinNourriture1Atteint'] . "</td>";
                        echo "<td>" . $rowEtat['MinNourriture2Atteint'] . "</td>";
                        echo "<td>" . $rowEtat['MinNourriture3Atteint'] . "</td>";
                        echo "<td>" . $rowEtat['MaxPoubelleAtteint'] . "</td>";
                        echo "</tr>";
                    }
                    echo "</table>";
                }
            }
            
            $sqlHistorique = "SELECT * FROM RapportInteractionsGerbillesAvecDistributeurTable";
            if ($resultHistorique = mysqli_query(connectionBaseDeDonnees(), $sqlHistorique)) {
                if (mysqli_num_rows($resultHistorique) > 0) {
                    echo "<table>";
                    echo "<tr>";
                    echo "<th>HistoriqueID</th>";
                    echo "<th>Historique</th>";
                    echo "</tr>";
                    while ($rowHistorique = mysqli_fetch_array($resultHistorique)) {
                        echo "<tr>";
                        echo "<td>" . $rowHistorique['HistoriqueID'] . "</td>";
                        echo "<td>" . $rowHistorique['Historique'] . "</td>";
                        echo "</tr>";
                    }
                    echo "</table>";
                }
            }
            
            echo "</html>";
            // Free result set
            mysqli_free_result($result);
        } else {
            echo "No records matching your query were found.";
        }
    } else {
        echo "ERROR: Could not able to execute $sql2. " . mysqli_error(connectionBaseDeDonnees());
    }
    // Fermeture de la connexion SQL après avoir terminer l'affichage
    mysqli_close(connectionBaseDeDonnees());
}

/*
 * Fontion pour selectionner le mode (ecriture ou lecture)
 */
function getMode($modeSelectionne)
{
    if ($modeSelectionne == "e") { // Mode ectriture
        if (isset($_GET['d'],$_GET['n'], $_GET['q'])) {
            ecritureMenu($_GET['d'], $_GET['n'], $_GET['q'], $_GET['f'],FALSE, FALSE, FALSE); // d : date (jour, mois, annee, heure, minute); n : nourriture (nourriture1(0 ou 1), nourriture2(0 ou 1), nourriture3)(0 ou 1); q: nombre portion nourriture1,nombre portion nourriture2,nombre portion nourriture3.
        }elseif (isset($_GET['batt'], $_GET['etats'])){
            ecritureMenu(FALSE,FALSE, FALSE,FALSE,$_GET['batt'], $_GET['etats'], FALSE); // batt : pourcentage de batterie restant; etats : (minNourriture1 0 ou 1), (minNourriture2 0 ou 1), (minNourriture3 0 ou 1), (maxPoubelle 0 ou 1),
        }elseif (isset($_GET['h'])){
            ecritureMenu(FALSE, FALSE, FALSE,FALSE, FALSE, FALSE, $_GET['h']);
        }
        
    } else if ($modeSelectionne == "l") { // Mode lecture
        if(isset($_GET['m'])){
            lectureDonnees($_GET['m'], FALSE, FALSE, FALSE, FALSE); // m : menu
        }elseif (isset($_GET['etats'])) {
            lectureDonnees(FALSE,$_GET['etats'], FALSE, FALSE, FALSE); // e : etat
        }elseif (isset($_GET['i'])) {
            lectureDonnees(FALSE, FALSE, $_GET['i'], FALSE, FALSE); // e : etat
        }elseif (isset($_GET['tm'])){
            lectureDonnees(FALSE, FALSE, FALSE, $_GET['tm'], FALSE); // tm : taille menu (taille de la liste des menus)
        }elseif (isset($_GET['th'])){
            lectureDonnees(FALSE, FALSE, FALSE, FALSE, $_GET['th']);
        }
    } else if($modeSelectionne=="c"){
        if (isset($_GET['m'], $_GET['a'])){
            changeMenu($_GET['m'], $_GET['a']);
        }
    }else if($modeSelectionne == "sup"){
        if (isset($_GET['m']))
            deleteMenu($_GET['m'], FALSE);
        elseif (isset($_GET['h']))
            deleteMenu(FALSE, $_GET['h']);
    }
    else { // Erreur de mode
        die('<p>Erreur de paramètre pour le mode !' . '</p>' . '<p>Mode lecture : l et Mode ecriture : e' . '</p>');
    }
    // Afficher le tableau après l'insertion
    showTable();

}

/*
 * Fonction supprimer
 */

function deleteMenu($MenuID, $Historique){
    if ($MenuID !=FALSE){
        //Requete SQL
        $sqlDelete = "DELETE FROM MenuTable WHERE MenuId = $MenuID";

        $sqlResetID1 = "ALTER TABLE MenuTable DROP MenuId";
        $sqlResetID2 = "ALTER TABLE MenuTable AUTO_INCREMENT = 1";
        $sqlResetID3 = "ALTER TABLE MenuTable ADD MenuId INT(2) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST";


        // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
        static $calls = 0;
        error_log('Called ' . (++ $calls) . ' time(s).');

        //Requete pour ecrire dans la base de données le statut
        if (mysqli_query(connectionBaseDeDonnees(), $sqlDelete)) {
            header("Location: _link_to_the_API/core.php?key=secure_key");
            usleep(50*1000); //Pause de 50ms entre les 2 requêtes

            //Requetes pour remettre les ID dans l'ordre
            if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID1)) {
                usleep(50*1000);
                if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID2)) {
                    usleep(50*1000);
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID3)) {
                        mysqli_close(connectionBaseDeDonnees()); //Fermeture de la connexion sql
                    }
                }

            }

        } else {
            echo "Error: " . $sqlDelete . "<br>" . mysqli_error(connectionBaseDeDonnees());
        }

    }
    elseif ($Historique !=FALSE){
        //Requete SQL
        $sqlCleanTable = "TRUNCATE TABLE RapportInteractionsGerbillesAvecDistributeurTable";
        $sqlResetID1 = "ALTER TABLE RapportInteractionsGerbillesAvecDistributeurTable DROP HistoriqueID";
        $sqlResetID2 = "ALTER TABLE RapportInteractionsGerbillesAvecDistributeurTable AUTO_INCREMENT = 1";
        $sqlResetID3 = "ALTER TABLE RapportInteractionsGerbillesAvecDistributeurTable ADD HistoriqueID INT(4) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST";

        // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
        static $calls = 0;
        error_log('Called ' . (++ $calls) . ' time(s).');

        //Requete pour ecrire dans la base de données le statut
        if (mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)) {
            header("Location: _link_to_the_API/core.php?key=secure_key");
            usleep(50*1000); //Pause de 50ms entre les 2 requêtes

            //Requetes pour remettre les ID dans l'ordre
            if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID1)) {
                usleep(50*1000);
                if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID2)) {
                    usleep(50*1000);
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlResetID3)) {
                        mysqli_close(connectionBaseDeDonnees()); //Fermeture de la connexion sql
                    }
                }

            }

        } else {
            echo "Error: " . $sqlCleanTable . "<br>" . mysqli_error(connectionBaseDeDonnees());
        }
    }

}

/*
 * Fonction pour ecrire le menu
 */
function ecritureMenu($date, $typeNourriture, $quantite,$frequence,$prctageBatterie, $etats, $historique)
{
    
    if ($date != FALSE && $typeNourriture != FALSE && $quantite !=FALSE && $frequence!=FALSE) {
        // Transformation de la date, du type et de la quantité de nourriture en matrice
        $dateArray = array(
            substr($date, 0, 2),
            substr($date, 3, 2),
            substr($date, 6, 4),
            substr($date, 11, 2),
            substr($date, 14, 2)
        );
        $typeNourritureArray = array(
            substr($typeNourriture, 0, 1),
            substr($typeNourriture, 2, 1),
            substr($typeNourriture, 4, 1)
        );
        $quantiteArray = array(
            substr($quantite, 0, 2),
            substr($quantite,3,2),
            substr($quantite, 6,2)
        );


        // Requete SQL pour la base de données
        $sqlMenu = "INSERT INTO MenuTable (Dates, Nourriture1, Quantite1,Nourriture2, Quantite2,Nourriture3, Quantite3, Frequence, Actif) VALUES (DATE_FORMAT('$dateArray[2]-$dateArray[1]-$dateArray[0] $dateArray[3]:$dateArray[4]',GET_FORMAT(DATETIME,'EUR')),$typeNourritureArray[0],$quantiteArray[0],$typeNourritureArray[1],$quantiteArray[1],$typeNourritureArray[2], $quantiteArray[2],'$frequence',true)";
        
        // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
        static $calls = 0;
        error_log('Called ' . (++ $calls) . ' time(s).');

        //Requete pour ecrire dans la base de données le menu
        if (mysqli_query(connectionBaseDeDonnees(), $sqlMenu)) {
            header("Location: _link_to_the_API/core.php?key=secure_key");
            mysqli_close(connectionBaseDeDonnees());
        } else {
            echo "Error: " . $sqlMenu . "<br>" . mysqli_error(connectionBaseDeDonnees());
        }
    } elseif ($prctageBatterie !=FALSE && $etats !=FALSE){
        // Transformation des etats du distributeur en matrice
        $etatsArray = array(
            substr($etats, 0,1),
            substr($etats, 2,1),
            substr($etats, 4,1),
            substr($etats, 6,1)
        );
        
        // Requete SQL pour la base de données
        $sqlStatut = "INSERT INTO EtatDistributeurTable (BatterieRestante, MinNourriture1Atteint, MinNourriture2Atteint, MinNourriture3Atteint, MaxPoubelleAtteint) VALUES ($prctageBatterie, $etatsArray[0],$etatsArray[1],$etatsArray[2],$etatsArray[3])";
        // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
        static $calls = 0;
        error_log('Called ' . (++ $calls) . ' time(s).');

        //Requete pour ecrire dans la base de données le statut
        if (mysqli_query(connectionBaseDeDonnees(), $sqlStatut)) {
            header("Location: _link_to_the_API/core.php?key=secure_key");
            mysqli_close(connectionBaseDeDonnees());
        } else {
            echo "Error: " . $sqlStatut . "<br>" . mysqli_error(connectionBaseDeDonnees());
        }
    }elseif ($historique !=FALSE) {
        
        // Transformation de l'historique d'interaction des gerbilles en matrice
        $historiqueArray = array(
            substr($historique, 0, 2),
            substr($historique, 3, 2),
            substr($historique, 6, 4),
            substr($historique, 11, 2),
            substr($historique, 14, 2)
        );
        // Requete SQL pour la base de données
        $sqlHistorique = "INSERT INTO RapportInteractionsGerbillesAvecDistributeurTable (Historique) VALUES (DATE_FORMAT('$historiqueArray[2]-$historiqueArray[1]-$historiqueArray[0] $historiqueArray[3]:$historiqueArray[4]',GET_FORMAT(DATETIME,'EUR')))";
        // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
        static $calls = 0;
        error_log('Called ' . (++ $calls) . ' time(s).');

        //Requete pour ecrire dans la base de données l'historique des interactions
        if (mysqli_query(connectionBaseDeDonnees(), $sqlHistorique)) {
            header("Location: _link_to_the_API/core.php?key=secure_key");
            mysqli_close(connectionBaseDeDonnees());
        } else {
            echo "Error: " . $sqlHistorique . "<br>" . mysqli_error(connectionBaseDeDonnees());
        }
    }


    
}

/*
 * Fonction pour lire les données de la base de données
 */
function lectureDonnees($menuID, $dernierEtat, $ListeInteraction, $tailleMenu, $tailleHistorique)
{
    //echo "Lecture";
    if ($menuID != false){
        $_sqlMenu = "SELECT * FROM MenuTable WHERE MenuId=$menuID";

        //Requete pour recevoir le menu correspondant à l'identifiant demandé
        if ($resultMenuSelected = mysqli_query(connectionBaseDeDonnees(), $_sqlMenu)) {
            if ($resultMenuSelected) {
                $row = mysqli_fetch_assoc($resultMenuSelected);
                //echo "<p id=\"menu\">"."Menu selectionné : ". $row['MenuID']. " " .$row['Dates'].  " " .$row['Nourriture1'].  " " .$row['Nourriture2'].  " " .$row['Nourriture3']."</p>";
                $stockeur =$row['MenuId']. " " .$row['Dates'].  " " .$row['Nourriture1']. " " .$row['Quantite1'].  " " .$row['Nourriture2']." " .$row['Quantite2'].  " " .$row['Nourriture3']." " .$row['Quantite3']." " .$row['Frequence']." ". $row['Actif'];
                echo $stockeur; //Afficer le menu selectionné

                //Ligne pour les requetes SQL
                $sqlCleanTable = "TRUNCATE TABLE ReadTable";
                $sqlRead = "INSERT INTO ReadTable (menuRead) VALUES ('$stockeur')";


                //Requete pour vider la table
                if(mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)){

                    usleep(50*1000); //Pause de 50ms entre les 2 requêtes

                    //Requete pour ecrire le menu sélectionné dans une autre table
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlRead)) {
                        mysqli_close(connectionBaseDeDonnees()); //Fermeture de la connexion sql
                    }

                }

            }
        }
    } elseif ($dernierEtat!= false){ //Requete pour recevoir le dernier etat du distributeur
        $_sqlLastEtat = "SELECT * FROM EtatDistributeurTable ORDER BY EtatID DESC LIMIT 1";

        //Requete pour recevoir le dernier état de la cage
        if ($resultLastEtatSelected = mysqli_query(connectionBaseDeDonnees(), $_sqlLastEtat)){
            if ($resultLastEtatSelected){
                $row = mysqli_fetch_assoc($resultLastEtatSelected);
                $stockeur = $row['BatterieRestante'] . " " . $row['MinNourriture1Atteint'] . " " . $row['MinNourriture2Atteint'] . " " . $row['MinNourriture3Atteint'] . " " . $row['MaxPoubelleAtteint'];
                echo $stockeur; //Afficer le dernier état

                //Ligne pour les requetes SQL
                $sqlRead = "INSERT INTO ReadTable (rapportRead) VALUES ('$stockeur')";
                $sqlCleanTable = "TRUNCATE TABLE ReadTable";

                //Requete pour vider la table
                if(mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)){


                    usleep(50*1000); //Pause de 50ms entre les 2 requêtes

                    //Requete pour ecrire le menu sélectionné dans une autre table
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlRead)) {
                        mysqli_close(connectionBaseDeDonnees());
                    }

                }
            }
        }
    }elseif ($ListeInteraction !=false){  //Requete pour recevoir tout l'historique des interactions des gerbilles

        $_sqlListeHistorique = "SELECT * FROM RapportInteractionsGerbillesAvecDistributeurTable";
        $historiqueArray = "";

        //Requete pour recevoir le dernier état de la cage
        if ($resultHistoriqueInteraction = mysqli_query(connectionBaseDeDonnees(), $_sqlListeHistorique)){
            if (mysqli_num_rows($resultHistoriqueInteraction) > 0) {

                //Boucle pour afficher tout l'historique dans une seule ligne
                while ($rowHistorique = mysqli_fetch_array($resultHistoriqueInteraction)) {
                    $historiqueArray .= "---" . $rowHistorique['Historique'];
                }
                echo $historiqueArray; //Afficher le dernier état

                //Ligne pour les requetes SQL
                $sqlRead = "INSERT INTO ReadTable (interactionRead) VALUES ('$historiqueArray')";
                $sqlCleanTable = "TRUNCATE TABLE ReadTable";

                //Requete pour vider la table
                if(mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)){

                    usleep(50*1000); //Pause de 50ms entre les 2 requêtes

                    //Requete pour ecrire le menu sélectionné dans une autre table
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlRead)) {
                        mysqli_close(connectionBaseDeDonnees());
                    }

                }
            }
        }
    }else if ($tailleMenu!=false){ // Requete pour connaitre la taille de la liste des menus
        $_sqlTaille = "SELECT COUNT(*) FROM MenuTable";

        //Requete pour recevoir la taille de la cage
        if ($resultTaille = mysqli_query(connectionBaseDeDonnees(), $_sqlTaille)){
            if ($resultTaille){
                $row = mysqli_fetch_assoc($resultTaille);
                $stockeur = $row["COUNT(*)"];
                echo $stockeur; //Afficer le dernier état

                //Ligne pour les requetes SQL
                $sqlRead = "INSERT INTO ReadTable (menuRead) VALUES ('$stockeur')";
                $sqlCleanTable = "TRUNCATE TABLE ReadTable";

                //Requete pour vider la table
                if(mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)){


                    usleep(50*1000); //Pause de 50ms entre les 2 requêtes

                    //Requete pour ecrire le menu sélectionné dans une autre table
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlRead)) {
                        mysqli_close(connectionBaseDeDonnees());
                    }

                }
            }
        }

    }elseif($tailleHistorique!=null){ //Requete pour connaitre la taille de la taille historique
        $_sqlTaille = "SELECT COUNT(*) FROM RapportInteractionsGerbillesAvecDistributeurTable";
        if ($resultTaille = mysqli_query(connectionBaseDeDonnees(), $_sqlTaille)){
            if ($resultTaille){
                $row = mysqli_fetch_assoc($resultTaille);
                $stockeur = $row["COUNT(*)"];
                echo $stockeur;

                //Ligne pour les requetes SQL
                $sqlRead = "INSERT INTO ReadTable (interactionRead) VALUES ('$stockeur')";
                $sqlCleanTable = "TRUNCATE TABLE ReadTable";

                //Requete pour vider la table
                if(mysqli_query(connectionBaseDeDonnees(), $sqlCleanTable)){


                    usleep(50*1000); //Pause de 50ms entre les 2 requêtes

                    //Requete pour ecrire le menu sélectionné dans une autre table
                    if (mysqli_query(connectionBaseDeDonnees(), $sqlRead)) {
                        mysqli_close(connectionBaseDeDonnees());
                    }

                }
            }
        }
    }

}

/*
 * Fonction pour changer l'activité d'un menu
 */
function changeMenu($menuID, $isActif){

    //Requete SQL pour la base de données
    $sqlChange = "UPDATE MenuTable SET Actif = $isActif WHERE MenuId = $menuID ";

    // Pour empecher le bug d'écriture multiple ces 2 lignes de codes sont nécessaires
    static $calls = 0;
    error_log('Called ' . (++ $calls) . ' time(s).');

    //Requete pour ecrire dans la base de données le statut
    if (mysqli_query(connectionBaseDeDonnees(), $sqlChange)) {
        header("Location: _link_to_the_API/core.php?key=secure_key");
        mysqli_close(connectionBaseDeDonnees());
    } else {
        echo "Error: " . $sqlChange . "<br>" . mysqli_error(connectionBaseDeDonnees());
    }
}

/*
 * Fonction pour sécuriser l'interaction avec la base de données en utilisant une clé
 */
function validerLaCleSecurisee()
{
    echo "<html>";
    echo "<head>";
    echo "<title>Tasty n' Krunchy</title>";
    echo "</head>";
    echo "</html>";
    if (isset($_GET['key']) && ($_GET['key'] == "secure_key")) // Verification de la clé sécurisée pour l'interaction
        return true; // La clé est correcte
    else
        return false; // La clé est incorrecte
}


/*
 * Fonction pour se connecter à la base de données.
 * Cette fonction renvoie la chaine mysqli_connect nécessaire pour les requetes SQL.
 */
function connectionBaseDeDonnees()
{
    $host_name = '_hostname_';
    $database = '_database_';
    $user_name = '_username_';
    $password = '_password_';
    return mysqli_connect($host_name, $user_name, $password, $database);
}

?>
