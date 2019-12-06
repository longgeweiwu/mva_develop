# 部署步骤

---
Q&A 

---

#### 问题一
多节点定时任务需要创建表
```
CREATE TABLE shedlock(
    NAME VARCHAR(64), 
    lock_until TIMESTAMP(3) NULL, 
    locked_at TIMESTAMP(3) NULL, 
    locked_by  VARCHAR(255), 
    PRIMARY KEY (NAME)
)
```

参照：TimeTaskJob


---