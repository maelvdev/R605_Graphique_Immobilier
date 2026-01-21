DROP TABLE IF EXISTS villes;

CREATE TABLE villes (
    code integer PRIMARY KEY,
    nom varchar,
    littoral boolean,
    montagnes boolean,
    population integer,
    densite integer,
    age_moyen integer,
    chomage float,
    prix_m2 integer
);

--Création des vues
DROP VIEW IF EXISTS Vue_Geographie;
DROP VIEW IF EXISTS Vue_Demographie;
DROP VIEW IF EXISTS Vue_Immobilier;


CREATE VIEW Vue_Geographie AS
SELECT
    code,
    nom,
    littoral,
    montagnes
FROM villes;