<?php
// listByGame.php

header('Content-Type: application/json; charset=UTF-8');

require_once __DIR__ . '/Classes/connexion.class.php';

$jeu = isset($_GET['jeu']) ? $_GET['jeu'] : '';

try {
    $pdo = Connexion::connectSqlsrv();

    $stmt = $pdo->prepare(
      'SELECT idAdherents AS id, post AS name
       FROM Joueur
       WHERE jeu = :jeu'
    );
    $stmt->execute(['jeu' => $jeu]);
    $players = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode(['players' => $players]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}
