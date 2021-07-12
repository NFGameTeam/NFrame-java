# 导出 oauth-center 的数据库结构
CREATE DATABASE IF NOT EXISTS `oauth-center` DEFAULT CHARACTER SET = utf8mb4;
Use `oauth-center`;


#
# Structure for table "oauth_client_details"
#
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(48) NOT NULL COMMENT '应用标识',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '资源限定串(逗号分割)',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '应用密钥(bcyt) 加密',
  `client_secret_str` varchar(256) DEFAULT NULL COMMENT '应用密钥(明文)',
  `scope` varchar(256) DEFAULT NULL COMMENT '范围',
  `authorized_grant_types` varchar(256) DEFAULT NULL COMMENT '5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '回调地址 ',
  `authorities` varchar(256) DEFAULT NULL COMMENT '权限',
  `access_token_validity` int(11) DEFAULT NULL COMMENT 'access_token有效期',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT 'refresh_token有效期',
  `additional_information` varchar(4096) DEFAULT '{}' COMMENT '{}',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '是否自动授权 是-true',
  `status` int(1) DEFAULT NULL,
  `if_limit` int(11) NOT NULL DEFAULT '0' COMMENT '是否应用限流',
  `limit_count` int(11) NOT NULL DEFAULT '10000' COMMENT '限流阈值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

#
# Data for table "oauth_client_details"
#

INSERT INTO `oauth_client_details` VALUES (1,'app',NULL,'$2a$10$i3F515wEDiB4Gvj9ym9Prui0dasRttEUQ9ink4Wpgb4zEDCAlV8zO','app','app','password,refresh_token',NULL,NULL,180000,NULL,'{}','true',1,0,10000),(2,'mobile','mobile,test','$2a$10$ULxRssv/4NWOc388lZFbyus3IFfsbcpG/BZOq4TRxDhsx5HHIR7Jm','mobile','all','password,refresh_token',NULL,NULL,180000,NULL,'{}','true',1,0,10000),(4,'webApp',NULL,'$2a$10$06msMGYRH8nrm4iVnKFNKOoddB8wOwymVhbUzw/d3ZixD7Nq8ot72','webApp','app','authorization_code,password,refresh_token,client_credentials',NULL,NULL,180000,NULL,'{}','true',1,0,10000),(5,'beck','','$2a$10$56LGyH.2wOFNNp3ScUkspOMdyRnenYhnWEnfI0itIFfsXsd5ZhKh.','beck','all','authorization_code,password,refresh_token,client_credentials','http://www.baidu.com','',180000,NULL,'{}','true',1,0,10000),(6,'owen',NULL,'$2a$10$a1ZEXiZQr604LN.wVxet.etPm6RvDs.HIaXP48J2HKRaEnZORTVwe','owen','app','authorization_code,password,refresh_token,client_credentials','http://127.0.0.1:9997/dashboard/login',NULL,180000,NULL,'{}','true',1,0,10000),(8,'testOne','','$2a$10$nI9kx19HHJTkJq0kMRPZ6uu/4uW7J9kPIpZ8YjFmbUlvwJmorc5Qa','testOne','all','authorization_code,password,refresh_token,client_credentials','http://bai.com','',18000,18000,'{}','true',0,0,10000),(9,'gwapi','','$2a$10$l7plpxQk42cuKbB8tbNe8eAA6v2xA6xkPXsjGEezago239102LRL2','gwapi','all','authorization_code,password,refresh_token,client_credentials','https://www.baidu.co','',18000,18000,'{}','true',1,0,10000),(10,'testtwo','','$2a$10$49ESIYmzu1n.cGzwMLRgleQMk0.kBTMOYnW4WUBDKwu9V23qOBovG','testtwo','all','authorization_code,password,refresh_token,client_credentials','locahost:9090/test','',18000,18000,'{}','true',1,0,10000),(11,'uc-app','','$2a$10$8UxEUaT2D2vSTJvTA/7YbODgCbK44bozsNA1kvMFSz8R153Xat7UO','uc-app','all','authorization_code,password,refresh_token,client_credentials','www.baidu.com','',18000,18000,'{}','true',1,0,10000),(12,'testtwo4','','$2a$10$Q8Qg5RQv1t0NFyL8Epfnj.wB/5NQnNJRMv5yIOVyeZ3ACXvzGwloq','testtwo4','all','implicit,password,refresh_token','locahost:9090/test','',18000,18000,'{}','true',1,1,10000);

#
# Structure for table "sys_client_service"
#
DROP TABLE IF EXISTS `sys_client_service`;
CREATE TABLE `sys_client_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` int(11) NOT NULL COMMENT '应用主键ID',
  `service_id` int(11) NOT NULL COMMENT '服务主键ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`,`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_client_service"
#

INSERT INTO `sys_client_service` VALUES (1,4,1),(2,4,2),(3,4,3),(4,4,4),(5,4,5),(6,4,6),(7,4,7),(8,4,8),(9,4,9),(10,4,10),(11,4,11),(12,4,12),(13,4,13),(14,4,14),(15,4,15),(16,4,16),(17,4,17),(18,4,18);

#
# Structure for table "sys_gateway_routes"
#
DROP TABLE IF EXISTS `sys_gateway_routes`;
CREATE TABLE `sys_gateway_routes` (
  `id` char(32) NOT NULL COMMENT 'id',
  `uri` varchar(100) NOT NULL COMMENT 'uri路径',
  `predicates` varchar(1000) DEFAULT NULL COMMENT '判定器',
  `filters` varchar(1000) DEFAULT NULL COMMENT '过滤器',
  `order` int(11) DEFAULT NULL COMMENT '排序',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `delFlag` int(11) DEFAULT '0' COMMENT '删除标志 0 不删除 1 删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务网关路由表';

#
# Data for table "sys_gateway_routes"
#


#
# Structure for table "sys_service"
#
DROP TABLE IF EXISTS `sys_service`;
CREATE TABLE `sys_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `path` varchar(1024) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `is_service` int(11) DEFAULT NULL COMMENT '是否服务 1 是 2 不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_service"
#

INSERT INTO `sys_service` VALUES (1,-1,'认证中心','/api-auth',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(2,-1,'用户中心','/api-user',2,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(3,-1,'文件中心','/api-file',3,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(4,-1,'短信中心','/api/sms',4,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(5,-1,'日志中心','/api-log',5,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(6,-1,'注册中心','/api-eureka',6,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(7,1,'应用管理','/api-user/client**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(8,1,'认证管理','/api-auth/oauth**/**',2,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(9,1,'redis监控','/api-auth/redis**/**',3,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(10,1,'服务管理','/api-auth/services**/**',4,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(11,2,'用户管理','/api-user/users**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(12,2,'角色管理','/api-user/roles**/**',2,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(13,2,'菜单管理','/api-user/menus**/**',3,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(14,2,'权限管理','/api-user/permissions**/**',4,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(15,3,'文件管理','/api-file/files**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(16,4,'短信管理','/api/sms/sms**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(17,5,'日志管理','/api-log/sysLog**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1),(18,6,'服务治理','/api-eureka/eureka**/**',1,'2018-04-09 12:37:57','2018-04-09 12:37:57',1);
