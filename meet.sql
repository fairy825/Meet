# Host: 127.0.0.1  (Version: 5.5.15)
# Date: 2020-02-24 23:38:52
# Generator: MySQL-Front 5.3  (Build 4.269)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "admin"
#

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "admin"
#

INSERT INTO `admin` VALUES (1,'root','admin'),(2,'meethere','meethere');

#
# Structure for table "book"
#

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(50) DEFAULT NULL,
  `douban_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `publishing_house` varchar(50) DEFAULT NULL,
  `total_num` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `tag` varchar(100) DEFAULT NULL,
  `rec_book` varchar(1000) DEFAULT NULL,
  `review_tag` varchar(255) DEFAULT NULL,
  `eigen_vec` varchar(5000) DEFAULT NULL,
  `emotion_vec` varchar(5000) DEFAULT NULL,
  `recommended` tinyint(4) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "book"
#

INSERT INTO `book` VALUES (1,'1234',NULL,'死亡清扫日记','.......','[日]前川誉','cb',10,3,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-19 14:00:00'),(2,'KKK',NULL,'C++开发',NULL,NULL,NULL,NULL,4,5,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-20 14:00:00'),(32,'1',NULL,'JAVA开发',NULL,NULL,NULL,NULL,5,7,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-18 14:00:00'),(34,'123124',NULL,'124235','135135','13513','1251',1,1,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 00:41:13'),(35,'98',NULL,'s','ss','ss','ss',12,12,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 00:42:18'),(36,'s',NULL,'s','s','s','s',1,1,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 00:44:18'),(37,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 20:22:38'),(38,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 20:23:33'),(39,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 20:25:41'),(40,'aaaaa',NULL,'aaaaa','aa','a','aa',1,1,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 20:38:06'),(41,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'2020-02-24 20:51:25');

#
# Structure for table "bookimage"
#

DROP TABLE IF EXISTS `bookimage`;
CREATE TABLE `bookimage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_book_bookimage` (`book_id`) USING BTREE,
  CONSTRAINT `fk_book_bookimage` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "bookimage"
#

INSERT INTO `bookimage` VALUES (1,1),(5,2),(60,1),(61,1),(62,2),(63,2);

#
# Structure for table "category"
#

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "category"
#

INSERT INTO `category` VALUES (1,'悬疑','popular'),(3,'爱情','literature'),(4,'推理','popular'),(5,'aa','literature'),(6,'a','culture'),(7,'aaa','literature');

#
# Structure for table "category_book"
#

DROP TABLE IF EXISTS `category_book`;
CREATE TABLE `category_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `category_book_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE,
  CONSTRAINT `category_book_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

#
# Data for table "category_book"
#

INSERT INTO `category_book` VALUES (1,1,1),(2,32,3),(3,2,1);

#
# Structure for table "news"
#

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "news"
#

INSERT INTO `news` VALUES (1,'test','news','2020-02-19 20:30:00'),(2,'news','news','2020-02-19 20:30:00'),(3,'test1','news','2020-02-19 20:33:00'),(5,'a','aaaa','2020-02-24 19:33:05');

#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `phone` varchar(21) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `face_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "user"
#

INSERT INTO `user` VALUES (1,'13000000000','b7797cce01b4b131b433b6acf4add449','1a2b3c',NULL,NULL,NULL,'/faceImage/1.jpg'),(37,'13000000001','c4ee3528e3889a0c09f7821df3c2e6dc','1a2b3c',NULL,NULL,'dd',NULL),(38,'aa','b7797cce01b4b131b433b6acf4add449','1a2b3c',NULL,NULL,'13000000002',NULL),(39,'a','b7797cce01b4b131b433b6acf4add449','1a2b3c',NULL,NULL,'13000000003',NULL);

#
# Structure for table "comment"
#

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(4000) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `appoint_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_comment_book` (`book_id`) USING BTREE,
  KEY `fk_comment_user` (`user_id`) USING BTREE,
  CONSTRAINT `fk_comment_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "comment"
#

INSERT INTO `comment` VALUES (1,'good',6,1,1,'2020-02-20 15:00:00','refused',2),(2,'bad',8,1,1,'2020-02-20 15:00:00','refused',2);

#
# Structure for table "appoint"
#

DROP TABLE IF EXISTS `appoint`;
CREATE TABLE `appoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `arrive_date` datetime DEFAULT NULL COMMENT '取书的日期',
  `start_date` datetime DEFAULT NULL COMMENT '借书的起始日期',
  `end_date` datetime DEFAULT NULL COMMENT '归还的日期',
  `back_date` datetime DEFAULT NULL COMMENT '归还的截止日期',
  `state` varchar(40) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_appoint_book` (`book_id`) USING BTREE,
  KEY `fk_appoint_user` (`user_id`) USING BTREE,
  CONSTRAINT `fk_appoint_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `fk_appoint_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

#
# Data for table "appoint"
#

INSERT INTO `appoint` VALUES (2,1,32,'2020-02-21 20:51:37','2020-02-24 14:55:51','2020-02-20 00:00:00','2020-03-20 00:00:00','2020-02-24 14:55:59','waitReview','备注！'),(5,37,2,'2020-02-21 21:04:57',NULL,'2020-02-22 00:00:00','2020-04-20 00:00:00',NULL,'cancelled',NULL);
