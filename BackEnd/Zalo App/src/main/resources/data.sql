use zaloapp;

insert ignore into user values(1,'2002-12-17','2002-12-17',"Võ Giang Nam" ,"","",true,false,'2002-12-17',"$2a$12$D0xTq0HXSEMj7Vb86VMDwO4Q99bPi07ZwQbgmcKBLdoTeLqqmp0Ea","0968322444","Male",'2002-12-17');

insert ignore into user values(2,'2002-12-17','2002-12-17',"Từ Thanh Thoại","","",true,false,'2002-12-17',"$2a$12$D0xTq0HXSEMj7Vb86VMDwO4Q99bPi07ZwQbgmcKBLdoTeLqqmp0Ea","0968322555","Female",'2002-12-17');

insert ignore into roles values(1,"ROLE_USER");

insert ignore into roles values(2,"ROLE_ADMIN");

insert ignore into user_roles values (1,1);



