<?php
// deleteAdherent.php
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__ . '/Classes/connexion.class.php';

$id = $_POST['id'] ?? 0;
if ($id <= 0) {
    echo json_encode(['erreur'=>true,'message'=>'ID invalide']);
    exit;
}

try {
    $pdo = Connexion::connectSqlsrv();
    $stmt = $pdo->prepare('DELETE FROM Adherents WHERE idAdherents = :id');
    $stmt->execute(['id'=>$id]);
    echo json_encode(['erreur'=>false,'message'=>'Adhérent supprimé']);
} catch(Exception $e) {
    http_response_code(500);
    echo json_encode(['erreur'=>true,'message'=>$e->getMessage()]);
}
