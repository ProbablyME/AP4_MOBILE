<?php
// Classes/connexion.class.php

class Connexion {
    public static function connectSqlsrv() {
        $server   = 'sql-lp.database.windows.net';       
        $database = 'AP4';
        $username = 'lp';               
        $password = 'zWyu99.o';                 

        try {
            $pdo = new PDO(
                "sqlsrv:Server=$server;Database=$database",
                $username,
                $password,
                [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
            );
            return $pdo;
        } catch (PDOException $e) {
            // En prod, on loggerait au lieu d’afficher
            die(json_encode(['error' => $e->getMessage()]));
        }
    }
}
