use zaloapp;

ALTER DATABASE zaloapp CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

ALTER TABLE message_chat CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

ALTER TABLE reaction CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

insert ignore into user values(1,'2002-12-17','2002-12-17',"Võ Giang Nam" ,"DefaultAvatar.jpg","DefaultCoverAvatar.jpg",true,false,'2002-12-17',"$2a$12$D0xTq0HXSEMj7Vb86VMDwO4Q99bPi07ZwQbgmcKBLdoTeLqqmp0Ea","0968322444","Male", "OFFLINE", '2002-12-17');

insert ignore into user values(2,'2002-12-17','2002-12-17',"Từ Thanh Thoại","DefaultAvatar.jpg","DefaultCoverAvatar.jpg",true,false,'2002-12-17',"$2a$12$D0xTq0HXSEMj7Vb86VMDwO4Q99bPi07ZwQbgmcKBLdoTeLqqmp0Ea","0968322555","Female", "OFFLINE", '2002-12-17');

insert ignore into user values(3,'2002-12-17','2002-12-17',"Acc thứ 3","DefaultAvatar.jpg","DefaultCoverAvatar.jpg",true,false,'2002-12-17',"$2a$12$D0xTq0HXSEMj7Vb86VMDwO4Q99bPi07ZwQbgmcKBLdoTeLqqmp0Ea","0968322666","Female", "OFFLINE", '2002-12-17');

insert ignore into roles values(1,"ROLE_USER");

insert ignore into roles values(2,"ROLE_ADMIN");

insert ignore into user_roles values (1,1);

insert ignore into user_roles values(2,1);

insert ignore into user_roles values(3,1);

INSERT ignore INTO `zaloapp`.`category_user` (`id`, `created_at`, `index_category`, `name`, `updated_at`, `phone_number_user`) VALUES ('1', '2024-05-16 12:00:00', '1', 'aaa', '2024-05-16 12:00:00', '1');

INSERT ignore INTO `zaloapp`.`group_chat` (`id`, `group_name`, `is_change_name_and_avatar`, `is_create_new_note`, `is_create_new_poll`, `is_pin_message`, `is_send_message`, `avatar`) VALUES ('1', 'Yasuo', b'0', b'0', b'0', b'0', b'1', 'DefaultAvatar.jpg');
INSERT ignore INTO `zaloapp`.`group_chat` (`id`, `group_name`, `is_change_name_and_avatar`, `is_create_new_note`, `is_create_new_poll`, `is_pin_message`, `is_send_message`, `avatar`) VALUES ('2', 'Solo', b'0', b'0', b'0', b'0', b'1', 'DefaultAvatar.jpg');

INSERT ignore INTO `zaloapp`.`group_chat_user` (`group_id`, `phone_number_user`, `is_admin`, `is_owner`, `is_user_create_group`, `category_id`) VALUES ('1', '0968322444', b'0', b'0', b'0', b'1');
INSERT ignore INTO `zaloapp`.`group_chat_user` (`group_id`, `phone_number_user`, `is_admin`, `is_owner`, `is_user_create_group`, `category_id`) VALUES ('1', '0968322555', b'0', b'0', b'0', b'1');
INSERT ignore INTO `zaloapp`.`group_chat_user` (`group_id`, `phone_number_user`, `is_admin`, `is_owner`, `is_user_create_group`, `category_id`) VALUES ('2', '0968322444', b'0', b'0', b'0', b'1');
INSERT ignore INTO `zaloapp`.`group_chat_user` (`group_id`, `phone_number_user`, `is_admin`, `is_owner`, `is_user_create_group`, `category_id`) VALUES ('2', '0968322666', b'0', b'0', b'0', b'1');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (1, 'Hello', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:00:00', '2024-05-16 12:00:00', 1, 1 , b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (2, 'Hi', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:01:00', '2024-05-16 12:01:00', 1, 2 , b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (3, 'How do you do', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:05:00', '2024-05-16 12:05:00', 1, 1 , b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (4, 'How do you do', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:07:00', '2024-05-16 12:07:00', 1, 2, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (5, 'Hello there', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:08:00', '2024-05-16 12:08:00', 1, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (6, 'Hello Gogo', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:09:00', '2024-05-16 12:09:00', 1, 2, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (7, 'Lets go', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:10:00', '2024-05-16 12:10:00', 1, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (8, 'Yo yo', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:11:00', '2024-05-16 12:11:00', 1, 2, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (9, 'Good to see you', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:12:00', '2024-05-16 12:12:00', 1, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (10, 'Yeah yeah', b'0', b'0', b'0', b'0', b'1', b'0', '2024-05-16 12:13:00', '2024-05-16 12:13:00', 1, 2, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (11, 'Ay yo', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:13:00', '2024-05-16 12:13:00', 2, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (12, 'Hi hi', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:14:00', '2024-05-16 12:14:00', 2, 3, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (13, 'Lets go to cinema', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:15:00', '2024-05-16 12:15:00', 2, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (14, 'Okay lets go', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:16:00', '2024-05-16 12:16:00', 2, 3, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (15, 'Whats time is it', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:17:00', '2024-05-16 12:17:00', 2, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (16, 'I can drive you', b'0', b'0', b'0', b'0', b'1', b'1', '2024-05-16 12:17:30', '2024-05-16 12:17:30', 2, 1, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (17, 'Okay see u at 11 am', b'0', b'0', b'0', b'0', b'1', b'0', '2024-05-16 12:20:00', '2024-05-16 12:20:00', 2, 3, b'1', b'0');

INSERT ignore INTO `zaloapp`.`message_chat`
(`id`, `content`, `deleted`, `disable_actions`, `disable_reactions`, `failure`, `saved`, `seen`, `send_at`, `updated_at`, `group_id`, `user_id`, `distributed`, `is_system`)
VALUES
    (18, 'Okay', b'0', b'0', b'0', b'0', b'1', b'0', '2024-05-16 12:22:00', '2024-05-16 12:22:00', 2, 3, b'1', b'0');

insert ignore into zaloapp.resource(id,resource_type,resource_value) values (1, "Image", "hq720.jpg");
insert ignore into zaloapp.resource(id,resource_type,resource_value) values (2, "Image", "response.jpeg");

INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('1', '😁', '9', '1');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('2', '😁', '9', '2');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('3', '🥰', '9', '1');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('4', '🥰', '9', '2');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('5', '🥰', '16', '3');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('6', '🥰', '16', '1');
INSERT ignore INTO `zaloapp`.`reaction` (`id`, `emoji`, `message_chat_id`, `user_id`) VALUES ('7', '😁', '16', '3');