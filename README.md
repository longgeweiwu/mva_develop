# **开发注意事项**
**Q&A**  
* 1、  ShedLock 部署时需要创建表

  ```
   CREATE TABLE shedlock(
          name VARCHAR(64), 
          lock_until TIMESTAMP(3) NULL, 
          locked_at TIMESTAMP(3) NULL, 
          locked_by  VARCHAR(255), 
          PRIMARY KEY (name)
      ) 
    ```


> [项目ShedLock地址](https://github.com/lukas-krecan/ShedLock)

---