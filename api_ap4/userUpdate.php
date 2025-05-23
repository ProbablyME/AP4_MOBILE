<?php
// userUpdate.php
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__.'/Classes/connexion.class.php';

$id       = intval($_POST['id']      ?? 0);
$nom      = $_POST['nom']     ?? '';
$prenom   = $_POST['prenom']  ?? '';
$adresse  = $_POST['adresse'] ?? '';
$age      = intval($_POST['age']    ?? 0);
// NbEvenement on ne le modifie pas ici
// mail ne change pas pour simplifier l’exemple

if ($id<=0 || empty($nom)||empty($prenom)) {
    echo json_encode(['erreur'=>true,'message'=>'Champs invalides']);
    exit;
}

try {
    $pdo = Connexion::connectSqlsrv();
    $stmt = $pdo->prepare("
      UPDATE Adherents
         SET nom    = :nom,
             prenom = :prenom,
             adresse= :adresse,
             age    = :age
       WHERE idAdherents = :id
    ");
    $stmt->execute([
      'nom'=>$nom,'prenom'=>$prenom,
      'adresse'=>$adresse,'age'=>$age,
      'id'=>$id
    ]);
    echo json_encode(['erreur'=>false,'message'=>'Profil mis à jour']);
} catch(Exception $e) {
    http_response_code(500);
    echo json_encode(['erreur'=>true,'message'=>'Erreur interne']);
}
