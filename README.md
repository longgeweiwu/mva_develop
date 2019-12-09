# **开发注意事项**
**Q&A**  
* 1、  ShedLock 部署时需要创建表 （特别注意不同库创建的sql）

  ```
  Mysql:
   CREATE TABLE shedlock(
          name VARCHAR(64), 
          lock_until TIMESTAMP(3) NULL, 
          locked_at TIMESTAMP(3) NULL, 
          locked_by  VARCHAR(255), 
          PRIMARY KEY (name)
      ) 
  
   Oracel:
   DROP TABLE ICC_UNION.SHEDLOCK CASCADE CONSTRAINTS;
   CREATE TABLE SHEDLOCK (
     name VARCHAR2(64 CHAR),
     lock_until TIMESTAMP,
     locked_at TIMESTAMP,
     locked_by  VARCHAR2(255 CHAR),
     PRIMARY KEY (name)
   );
    ```


> [项目ShedLock地址](https://github.com/lukas-krecan/ShedLock)

---