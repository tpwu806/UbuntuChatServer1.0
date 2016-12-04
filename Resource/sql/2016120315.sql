/*添加好友*/
INSERT  INTO `userinfo`(`UC`,`PWD`,`SIGN`,`PHOTOID`,`NICKNAME`,`SEX`,`BIRTHDAY`,`CONSTELLATION`,`BLOODTYPE`,`DIPLOMA`,`TELEPHONE`,`EMAIL`,`ADDRESS`,`STATUS`) VALUES 
(11111,'123','想睡觉','Image/Head/184_100.gif','user1','男','2016-10-16','狮子','B','博士','1212121212112','vito@163.com','香港','1'),
(22222,'123','无聊','Image/Head/184_100.gif','user2','男','2016-10-16','狮子','B','本科','186172638127','vito@qq.com','北京','0'),
(33333,'123','just for fun ！','Image/Head/184_100.gif','user3','男','2016-10-16','狮子','B','本科','18366121000','vito@live.com','济南','1');

DELETE FROM friends WHERE FUC = '77777' AND UC = '99999';

