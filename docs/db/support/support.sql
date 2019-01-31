-- MySQL

-- FTP
CREATE TABLE `fileupload` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `file_old_name` varchar(50) DEFAULT NULL COMMENT '文件原始名称',
  `file_new_name` varchar(50) DEFAULT NULL COMMENT '文件新名称',
  `contextpath` varchar(50) DEFAULT NULL COMMENT '所属项目上下文',
  `contenttype` varchar(50) DEFAULT NULL COMMENT '内容类型',
  `fileType` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `location` varchar(200) DEFAULT NULL COMMENT '文件位置',
  `servertype` int(11) DEFAULT NULL COMMENT '1:ftp 2:local',
  `userid` varchar(20) DEFAULT NULL COMMENT '上传人id',
  `userip` varchar(20) DEFAULT NULL COMMENT '上传人ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件表'
