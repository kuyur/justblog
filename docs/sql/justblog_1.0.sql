CREATE TABLE `justblog_users` (
  `ID` bigint(20) unsigned NOT NULL auto_increment,
  `user_account` varchar(40) NOT NULL default '',
  `user_hashedkey` varchar(64) NOT NULL default '',
  `user_nicename` varchar(80) NOT NULL default '',
  `user_email` varchar(100) NOT NULL default '',
  `user_url` varchar(100) NOT NULL default '',
  `user_registered` datetime NOT NULL default '0000-00-00 00:00:00',
  `user_role` varchar(10) NOT NULL default 'reader',
  PRIMARY KEY  (`ID`),
  KEY `user_account` (`user_account`),
  KEY `user_nicename` (`user_nicename`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ;