/*插入头像*/
UPDATE  USERINFO  SET PHOTOID  = 'Image/Head/184_100.gif';
/*添加好友备注列*/
ALTER TABLE friends ADD COLUMN REMARKS VARCHAR(30) DEFAULT '' COMMENT '好友备注'; 
/*删除好友序号列*/
ALTER TABLE friends DROP COLUMN FNO; 
/*添加好友*/
INSERT INTO friends VALUES('77777','2','2016-10-29 00:13:51','99999','老11');