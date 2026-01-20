/*
	Cas : Villes
	Dev : Martin RAVENEL
	Date : 14/101/2026
	Sujet : 6.05 Développement Avancé - Projet d'analyse de données immobilières et démographiques
		Initialisation de la base de données
*/

--Création de la table 
DROP TABLE IF EXISTS Villes CASCADE;

CREATE TABLE Villes (
	code integer PRIMARY KEY,
	nom varchar,
	littoral boolean,
	montagnes boolean,
	population integer,
	densite integer,
	age_moyen integer,
	chomage float,
	prix_m2 integer,
);

--Création des vues
DROP VIEW IF EXISTS Vue_Geographie CASCADE;
DROP VIEW IF EXISTS Vue_Demographie CASCADE;
DROP VIEW IF EXISTS Vue_Immobilier CASCADE;

CREATE VIEW Vue_Geographie AS
SELECT 
    code, 
    nom, 
    littoral, 
    montagnes
FROM Villes;

CREATE VIEW Vue_Demographie AS
SELECT 
    code, 
    nom, 
    population, 
    densite, 
    age_moyen, 
    chomage
FROM Villes;

CREATE VIEW Vue_Immobilier AS
SELECT 
    code, 
    nom, 
    prix_m2
FROM Villes;