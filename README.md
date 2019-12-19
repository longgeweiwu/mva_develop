# **主要架构选型**
Springboot(2.2.2)+Mybatis-Plus(3.0-RC3)+shedLock(4.0.1)

# **开发注意事项**
**Q&A**  
 *  1、 ShedLock 部署时需要创建表 （特别注意不同库创建的sql）

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
  @SchedulerLock(name = "任务名称 不要重复", lockAtMostFor = "见下", lockAtLeastFor ="见下")
  ```
    /**
     *You can also set lockAtMostFor attribute which specifies how long the lock should be kept in case the executing node dies.
     *  This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes.
     *  You have to set lockAtMostFor to a value which is much longer than normal execution time.
     *  If the task takes longer than lockAtMostFor the resulting behavior may be unpredictable
     *  (more then one process will effectively hold the lock).
     *  lockAtMostFor：锁的最大时间单位为毫秒
     *
     *Lastly, you can set lockAtLeastFor attribute which specifies minimum amount of time for which the lock should be kept.
     *  Its main purpose is to prevent execution from multiple nodes in case of really short tasks and clock difference between the nodes.
     *  lockAtLeastFor：锁的最小时间单位为毫秒
     */
```

> [项目ShedLock地址](https://github.com/lukas-krecan/ShedLock)


 * 2、 本项目需要引入本地jar包   
   - 两种方式：  
      - 本地安装：   
       ``` mvn install:install-file -DgroupId=com.inspur.est -DartifactId=signsec -Dversion=1.1.6 -Dpackaging=jar -Dfile=D:\excLib\sign-security-1.1.6.jar  ```  
      - 手动安装：  
      ``` 登陆nexus --> Browse --> itcc-release --> upload component --> Group ID:com.inspur.est Artifact ID:signsec Version:1.1.6 -->勾选 Generate a POM file with these coordinates -->Upload``` 

---

* 3、 oracle 数据创建表语句

```
 捷通解析表（基表）
 待填写  

 讯飞解析表
DROP TABLE ICC_UNION.MVA_IFLY_INTELLIGENT_ASR CASCADE CONSTRAINTS;

CREATE TABLE ICC_UNION.MVA_IFLY_INTELLIGENT_ASR
	(
	  PID                VARCHAR2 (32) NOT NULL
	, LOG_ID             VARCHAR2 (40) NOT NULL
	, LEAV_WORD_TIME     VARCHAR2 (20) NOT NULL
	, ANI                VARCHAR2 (40) NOT NULL
	, LEAVEWORD_PATH     VARCHAR2 (128)
	, VOICE_FILE_NAME    VARCHAR2 (128) NOT NULL
	, CALLID             VARCHAR2 (32) NOT NULL
	, FULL_PATH          VARCHAR2 (128) NOT NULL
	, RMAVOICE_FILE_NAME VARCHAR2 (131)
	, RMAFLAG           NUMBER
	, AID                VARCHAR2 (128)
	, IFLY_RESULT        CLOB
	, INSERT_TIME        DATE
	, IFLYPARSE_STATUS   NUMBER
	, ISSUBMIT           NUMBER,
	CONSTRAINT PK_MVA_IFLY_INTELLIGENT_ASR PRIMARY KEY (PID)
	)
	TABLESPACE ICC_UNION_DATA
	STORAGE (BUFFER_POOL DEFAULT);
```

* 4、身份证号码sql
```
DROP TABLE ICC_UNION.T_ID_CARD CASCADE CONSTRAINTS;

CREATE TABLE ICC_UNION.T_ID_CARD
	(
	  F_CODE     NUMBER NOT NULL
	, F_PROVINCE VARCHAR2 (100)
	, F_CITY     VARCHAR2 (100)
	, F_AREA     VARCHAR2 (100),
	PRIMARY KEY (F_CODE)
	)
	TABLESPACE USERS
	STORAGE (BUFFER_POOL DEFAULT);
```
数据请参照数据库