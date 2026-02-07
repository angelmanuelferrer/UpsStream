-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);

INSERT INTO profile(id,location,occasional_player) VALUES (1,'Madrid, España',1), (2,'Barcelona, España',0), (3,'Valencia, España',1), (4,'Bilbao, España',1), (5,'Sevilla, España',0), (6,'Zaragoza, España',0), (7,'Malaga, España',1), (8,'Murcia, España',0), (9,'Palma de Mallorca, España',1), (10,'Granada, España',0);
-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (4,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (5,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,2);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (6,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,3);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (7,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,4);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (8,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,5);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (9,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,6);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (10,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,7);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (11,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,9);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (12,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,10);
INSERT INTO appusers(id,username,password,authority,profile_id) VALUES (13,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,8);
INSERT INTO appusers(id,username,password,authority) VALUES (14,'YVT0703','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (15,'FBG8620','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (16,'GDT3477','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (17,'CHI1762','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (18,'MHN4567','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);

INSERT INTO match(id,name,code,start,finish, user_id, number_of_players, status) VALUES (1,'Fiesta para todos!!!! UNETE!',null,null,null, 1, 2, null),
                                                             (2,'Partida privada ya comenzada','super-secret','2023-04-11 15:20',null, 1, 4, 'PLAYING'),
                                                             (3,'Partida abierta ya terminada',null,'2023-04-11 18:20','2023-04-11 19:20', 1, 5, 'FINISHED');

INSERT INTO achievement(id,threshold, user_id, name, badge_image, description, metric) VALUES (1, 10, null, 'Experiencia basica', 'https://cdn-icons-png.flaticon.com/512/5243/5243423.png', 'Si juegas 10 partidas o mas', 'GAMES_PLAYED'),
                                                                                              (2, 25, null, 'Explorador', 'https://cdn-icons-png.flaticon.com/512/603/603855.png', 'Si juegas 25 partidas o mas', 'GAMES_PLAYED'),
                                                                                              (3, 20, null, 'Experto', 'https://cdn-icons-png.flaticon.com/512/4737/4737471.png', 'Si ganas 20 partidas o mas', 'VICTORIES');
INSERT INTO gender(id, name) VALUES (1, 'HOMBRE'), (2, 'MUJER'), (3, 'OTRO');
INSERT INTO platform(id, name) VALUES (1, 'PC'), (2, 'ANDROID'), (3, 'IOS');
INSERT INTO saga(id, name) VALUES (1, 'SAGA 1'), (2, 'SAGA 2'), (3, 'SAGA 3');

