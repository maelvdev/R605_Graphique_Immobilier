/*
	Cas : Villes
	Dev : Martin RAVENEL
	Date : 14/101/2026
	Sujet : 6.05 Développement Avancé - Projet d'analyse de données immobilières et démographiques
*/

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