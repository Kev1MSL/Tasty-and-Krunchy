<?php
if($_GET['m']){
    $sqlReadMenu = "SELECT menuRead FROM ReadTable";
    if ($resultReadMenu = mysqli_query(connectionBaseDeDonnees(), $sqlReadMenu)) {
        $row = mysqli_fetch_assoc($resultReadMenu);
        $string = "";
        $string = $row["menuRead"];
        echo $string;
        //echo json_encode($row["menuRead"]);
    }
} else if ($_GET['e']){
    $sqlReadMenu = "SELECT rapportRead FROM ReadTable";
    if ($resultReadMenu = mysqli_query(connectionBaseDeDonnees(), $sqlReadMenu)) {
        $row = mysqli_fetch_assoc($resultReadMenu);
        echo $row['rapportRead'];
    }
}elseif($_GET['i']){
    $sqlReadMenu = "SELECT interactionRead FROM ReadTable";
    if ($resultReadMenu = mysqli_query(connectionBaseDeDonnees(), $sqlReadMenu)) {
        $row = mysqli_fetch_assoc($resultReadMenu);
        echo $row['interactionRead'];
    }
}


function connectionBaseDeDonnees()
{
    $host_name = '_hostname_';
    $database = '_database_';
    $user_name = '_username_';
    $password = '_password_';
    return mysqli_connect($host_name, $user_name, $password, $database);
}
?>