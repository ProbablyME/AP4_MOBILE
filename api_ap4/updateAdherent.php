<?php
// updateAdherent.php
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__ . '/Classes/connexion.class.php';

$id      = $_POST['id']      ??  0;
$nom     = $_POST['nom']     ?? '';
$prenom  = $_POST['prenom']  ?? '';
$mail    = $_POST['mail']    ?? '';
$adresse = $_POST['adresse'] ?? '';
$age     = $_POST['age']     ?? '';

if($id<=0 || empty($nom) || empty($prenom) || empty($mail)) {
  echo json_encode(['erreur'=>true,'message'=>'Champs manquants']);
  exit;
}

try {
  $pdo = Connexion::connectSqlsrv();
  $stmt= $pdo->prepare("
    UPDATE Adherents
       SET nom=:nom,
           prenom=:prenom,
           mail=:mail,
           adresse=:adresse,
           age=:age
     WHERE idAdherents=:id
  ");
  $stmt->execute([
    'nom'=>$nom,'prenom'=>$prenom,'mail'=>$mail,
    'adresse'=>$adresse,'age'=>(int)$age,'id'=>$id
  ]);
  echo json_encode(['erreur'=>false,'message'=>'Adhérent mis à jour']);
} catch(Exception $e) {
  http_response_code(500);
  echo json_encode(['erreur'=>true,'message'=>$e->getMessage()]);
}
