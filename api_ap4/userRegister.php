<?php
// userRegister.php
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__ . '/Classes/connexion.class.php';

// Récupération des champs POST
$nom      = $_POST['nom']     ?? '';
$prenom   = $_POST['prenom']  ?? '';
$mail     = $_POST['mail']    ?? '';
$password = $_POST['password']?? '';
$adresse  = $_POST['adresse'] ?? '';
$age      = $_POST['age']     ?? '';

// Vérification minimaliste
if (empty($nom) || empty($prenom) || empty($mail) || empty($password)) {
    echo json_encode(['erreur' => true, 'message' => 'Tous les champs obligatoires doivent être remplis']);
    exit;
}

try {
    $pdo = Connexion::connectSqlsrv();

    // Vérifie si l'e-mail est déjà utilisé
    $stmt = $pdo->prepare('SELECT COUNT(*) FROM Adherents WHERE mail = :m');
    $stmt->execute(['m' => $mail]);
    if ($stmt->fetchColumn() > 0) {
        echo json_encode(['erreur' => true, 'message' => 'Cette adresse e-mail est déjà utilisée']);
        exit;
    }

    // Insertion SANS hash PHP — le trigger en base s'en charge
    $stmt = $pdo->prepare("
      INSERT INTO Adherents 
        (nom, prenom, mail, adresse, dateAdhesion, age, password)
      VALUES
        (:nom, :prenom, :mail, :adresse, GETDATE(), :age, :pwd)
    ");
    $stmt->execute([
        'nom'     => $nom,
        'prenom'  => $prenom,
        'mail'    => $mail,
        'adresse' => $adresse,
        'age'     => (int)$age,
        'pwd'     => $password // mot de passe en clair -> hashé en base
    ]);

    echo json_encode(['erreur' => false, 'message' => 'Inscription réussie']);
}
catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['erreur' => true, 'message' => 'Erreur interne : '.$e->getMessage()]);
}
