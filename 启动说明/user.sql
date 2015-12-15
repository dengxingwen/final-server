CREATE DATABASE studb;

USE studb;

CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `account` varchar(200) NOT NULL,
  `passwrod` varchar(200) NOT NULL,
  `username` varchar(200) NOT NULL,
  `photo` varchar(200) ,
  `phone` varchar(200) ,
  `token` varchar(200) ,
  `signer` varchar(200) ,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `friend` (
  `id` int(11) NOT NULL auto_increment,
  `addId` varchar(200) NOT NULL,
  `addedId` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;