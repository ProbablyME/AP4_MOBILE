<?php
// listAdherents.php
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__ . '/Classes/connexion.class.php';

try {
    $pdo = Connexion::connectSqlsrv();
    $stmt = $pdo->prepare(
      'SELECT idAdherents AS id, nom, prenom, mail
       FROM Adherents'
    );
    $stmt->execute();
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
      'erreur'     => false,
      'adherents'  => $rows
    ]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
      'erreur'  => true,
      'message' => 'Erreur serveur : ' . $e->getMessage()
    ]);
}
