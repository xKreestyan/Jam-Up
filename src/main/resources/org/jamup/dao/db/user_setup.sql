-- execute as root
CREATE USER 'jamup_user'@'localhost' IDENTIFIED BY 'jamup_password';
GRANT SELECT, INSERT, UPDATE ON jamup.* TO 'jamup_user'@'localhost';
GRANT EXECUTE ON jamup.* TO 'jamup_user'@'localhost';
FLUSH PRIVILEGES;