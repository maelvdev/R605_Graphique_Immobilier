/*
	Cas : Villes
	Dev : Martin RAVENEL
	Date : 14/101/2026
	Sujet : 6.05 Développement Avancé - Projet d'analyse de données immobilières et démographiques
		Créatio d'un jeu de test
*/

DELETE FROM Villes;

INSERT INTO Villes (code, nom, littoral, montagnes, population, densite, age_moyen, chomage, prix_m2) VALUES
(1, 'Paris', false, false, 2145906, 20433, 39, 6.2, 10500),
(2, 'Marseille', true, false, 870731, 3618, 38, 11.5, 3800),
(3, 'Lyon', false, false, 522250, 10909, 36, 7.1, 5100),
(4, 'Toulouse', false, false, 493465, 4171, 34, 8.5, 3700),
(5, 'Nice', true, true, 342669, 4764, 42, 8.2, 5000),
(6, 'Nantes', false, false, 318808, 4891, 35, 6.8, 3900),
(7, 'Montpellier', true, false, 295542, 5196, 35, 10.2, 3600),
(8, 'Strasbourg', false, false, 287228, 3669, 35, 8.1, 4000),
(9, 'Bordeaux', false, false, 260958, 5287, 37, 7.5, 4800),
(10, 'Lille', false, false, 234475, 6736, 33, 10.8, 3500),
(11, 'Rennes', false, false, 220488, 4376, 33, 6.1, 4100),
(12, 'Reims', false, false, 181180, 3853, 37, 9.5, 2700),
(13, 'Toulon', true, false, 178745, 4172, 43, 9.1, 3200),
(14, 'Saint-Étienne', false, true, 173821, 2174, 40, 10.5, 1400),
(15, 'Le Havre', true, false, 166058, 3537, 40, 11.2, 2300),
(16, 'Grenoble', false, true, 158198, 8726, 36, 7.2, 3000),
(17, 'Dijon', false, false, 158002, 3910, 38, 6.5, 2900),
(18, 'Angers', false, false, 155876, 3650, 36, 7.3, 3300),
(19, 'Villeurbanne', false, false, 152212, 10483, 34, 8.4, 4000),
(20, 'Nîmes', false, false, 148104, 917, 40, 12.1, 2600),
(21, 'Clermont-Ferrand', false, true, 147865, 3465, 37, 8.0, 2500),
(22, 'Aix-en-Provence', false, false, 145133, 779, 40, 7.5, 5500),
(23, 'Le Mans', false, false, 143847, 2724, 41, 9.2, 2100),
(24, 'Brest', true, false, 139163, 2810, 39, 7.8, 2400),
(25, 'Tours', false, false, 137087, 3954, 38, 8.3, 3100),
(26, 'Amiens', false, false, 133891, 2707, 36, 11.0, 2400),
(27, 'Limoges', false, true, 130876, 1673, 41, 8.7, 1800),
(28, 'Annecy', false, true, 130721, 1952, 39, 5.5, 5600),
(29, 'Boulogne-Billancourt', false, false, 121583, 19706, 40, 6.5, 9200),
(30, 'Perpignan', true, false, 119344, 1753, 41, 14.5, 1900),
(31, 'Besançon', false, true, 117912, 1812, 37, 8.2, 2400),
(32, 'Orléans', false, false, 116238, 4230, 36, 8.4, 2800),
(33, 'Rouen', false, false, 112321, 5253, 35, 10.1, 2800),
(34, 'Mulhouse', false, false, 108312, 4905, 36, 12.8, 1800),
(35, 'Caen', false, false, 106230, 4133, 37, 8.6, 3000),
(36, 'Nancy', false, false, 104000, 6933, 34, 7.9, 2700),
(37, 'Biarritz', true, false, 25787, 2212, 53, 7.2, 8500),
(38, 'Chamonix-Mont-Blanc', false, true, 8642, 35, 44, 4.5, 9800),
(39, 'La Rochelle', true, false, 77205, 2716, 45, 9.3, 5000),
(40, 'Ajaccio', true, true, 71361, 870, 41, 8.1, 3900),
(41, 'Pau', false, true, 75627, 2400, 42, 8.8, 2200),
(42, 'Dunkerque', true, false, 86279, 1966, 41, 11.8, 1700),
(43, 'Saint-Malo', true, false, 46803, 1280, 49, 6.7, 4800),
(44, 'Chambéry', false, true, 59172, 2819, 39, 6.9, 3200),
(45, 'Lorient', true, false, 57246, 3275, 42, 9.4, 2800),
(46, 'Gap', false, true, 40631, 368, 42, 7.4, 2600),
(47, 'Tarbes', false, true, 42758, 2800, 44, 11.2, 1500),
(48, 'Brive-la-Gaillarde', false, true, 46330, 953, 44, 8.5, 1600),
(49, 'Arcachon', true, false, 11630, 1538, 62, 7.0, 9500),
(50, 'Thonon-les-Bains', true, true, 35826, 2210, 40, 6.5, 4200);