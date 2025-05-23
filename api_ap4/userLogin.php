<?php
// userLogin.php (utilise VerifLoginPassword côté base de données)
header('Content-Type: application/json; charset=UTF-8');
require_once __DIR__ . '/Classes/connexion.class.php';

$mail     = $_POST['mail']     ?? '';
$password = $_POST['password'] ?? '';

try {
    $pdo = Connexion::connectSqlsrv();

    // Appel de la fonction SQL VerifLoginPassword(mail, password)
    $stmt = $pdo->prepare("
        SELECT dbo.VerifLoginPassword(:mail, :password) AS estValide
    ");
    $stmt->execute([
        'mail'     => $mail,
        'password' => $password
    ]);
    $row = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$row || !$row['estValide']) {
        echo json_encode(['erreur' => true, 'message' => 'Identifiants invalides']);
        exit;
    }

    // Récupérer les infos de l'utilisateur si login valide
    $stmtUser = $pdo->prepare("
        SELECT idAdherents   AS id,
               nom, prenom,
               mail,
               adresse,
               age,
               NbEvenement    AS nbEvents
        FROM Adherents
        WHERE mail = :mail
    ");
    $stmtUser->execute(['mail' => $mail]);
    $user = $stmtUser->fetch(PDO::FETCH_ASSOC);

    if (!$user) {
        echo json_encode(['erreur' => true, 'message' => 'Utilisateur introuvable']);
        exit;
    }

    echo json_encode(array_merge(
        ['erreur' => false, 'message' => 'Connexion réussie'],
        $user
    ));
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['erreur' => true, 'message' => 'Erreur interne']);
}
