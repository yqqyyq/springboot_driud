SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `chat_file_log`;
CREATE TABLE `chat_file_log` (
  `filename` varchar(255) CHARACTER SET utf8 NOT NULL,
  `filesize` varchar(255) CHARACTER SET utf8,
  `fileuser` varchar(255) CHARACTER SET utf8,
  `filetime` varchar(255) DEFAULT NULL,
  `filetype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `chat_user`;
CREATE TABLE `chat_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `profilehead` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `profile` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `firsttime` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `lasttime` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

