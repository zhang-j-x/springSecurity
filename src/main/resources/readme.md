菜单递归查询函数

CREATE FUNCTION querySystemMenu(itemId INT)
RETURNS VARCHAR(4000)
BEGIN
DECLARE sTemp VARCHAR(4000) ;
DECLARE sTempChd VARCHAR(4000) ;

SET sTemp='$';
SET sTempChd = CAST(itemId AS CHAR);

WHILE sTempChd IS NOT NULL DO
SET sTemp= CONCAT(sTemp,',',sTempChd);
SELECT GROUP_CONCAT(id) INTO sTempChd FROM menu  WHERE FIND_IN_SET(parentId,sTempChd)>0;
END WHILE;
RETURN sTemp;
END
;

mysql查询语句

 select
    A.id,
    A.url,
    A.path,
    A.name,
    A.code,
    A.icon,
    A.status,
    A.require_auth AS requireAuth,
    A.parentId,
    A.level,
    A.seq
    from
            (SELECT * from menu,(select querySystemMenu(0) cids) t where FIND_IN_SET(parentId,cids)) A JOIN
     (select distinct  menu_id from menu_role where role_id in (1) ) B
     on B.menu_id = A.id
