/*数据表结构*/

-- 1.user（用户表）
CREATE TABLE IF NOT EXISTS user (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(500) NOT NULL,
  email VARCHAR(50) NOT NULL,
  github VARCHAR(50),
  blog VARCHAR(50),
  head_portrait VARCHAR(100),
  mood VARCHAR(100),
  submit_count INT NOT NULL DEFAULT 0,
  ac_num INT NOT NULL DEFAULT 0,
  rating_num INT NOT NULL DEFAULT 0,
  rating_score INT  NOT NULL DEFAULT 0,
  `rank` INT NOT NULL,
  view_language VARCHAR(10),
  role_id INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uk_username UNIQUE KEY(username)
)

-- 2.user_certification（实名认证表）
CREATE TABLE IF NOT EXISTS user_certification (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  certification_id INT NOT NULL,
  real_name VARCHAR(10),
  sex VARCHAR(10),
  stu_id VARCHAR(50),
  school VARCHAR(20),
  faculty VARCHAR(20),
  major VARCHAR(20),
  class VARCHAR(20),
  phone VARCHAR(20),
  graduation_time VARCHAR(30),
  status TINYINT NOT NULL DEFAULT 0, -- 认证状态：0表示尚未审核、1表示审核通过、-1表示审核不通过或者申请的认证类型失效，需要重新申请
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 3.certification（认证类型）
CREATE TABLE IF NOT EXISTS certification (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 4.solved_problem（用户解决题目表）
CREATE TABLE IF NOT EXISTS solved_problem (
  id INT NOT NULL AUTO_INCREMENT,
  problem_id INT NOT NULL,
  user_id INT NOT NULL,
  submit_status VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 5.admin（管理员表）
CREATE TABLE IF NOT EXISTS admin (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(500) NOT NULL,
  description VARCHAR(50),
  role_id INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uk_username UNIQUE KEY(username)
)

-- 6.role（角色表）
CREATE TABLE IF NOT EXISTS role (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  permission_codes VARCHAR(255) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uk_username UNIQUE KEY(name)
)

-- 7.submitMessage（消息表）
CREATE TABLE IF NOT EXISTS submitMessage (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(20) NOT NULL,
  content VARCHAR(500) NOT NULL,
  creator VARCHAR(50) NOT NULL,
  user_id INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 0, -- 消息状态：0为未读，1为已读
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 8.announcement（公告表）
CREATE TABLE IF NOT EXISTS announcement (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  content TEXT NOT NULL,
  creator VARCHAR(50) NOT NULL,
  source_id INT NOT NULL, -- 公告类型（来源）：-1表示主页公告、0表示FAQ、大于0表示竞赛的公告，数字代表竞赛ID
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 9.problem（题目信息表）
CREATE TABLE IF NOT EXISTS problem (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  description TEXT NOT NULL,
  input_description TEXT NOT NULL,
  output_description TEXT NOT NULL,
  input_samples VARCHAR(300) NOT NULL,
  output_samples VARCHAR(300) NOT NULL,
  hint TEXT,
  source_id INT NOT NULL, -- 比赛界面创建的题目这部分自动填充，题目界面创建的题目这部分为0。
  source_name VARCHAR(50) NOT NULL, -- 比赛界面创建的题目这部分自动填充，题目界面创建的题目这部分必须以@开头。
  time_limit INT NOT NULL,  -- 单位固定ms
  memory_limit INT NOT NULL, -- 单位固定mb
  io_mode VARCHAR(20) NOT NULL, -- io模型，目前只支持standard io
  level VARCHAR(10) NOT NULL, -- easy medium hard
  language VARCHAR(100) NOT NULL, -- 题目支持语言(多种语言之间用逗号间隔)
  submit_count INT NOT NULL DEFAULT 0,
  ac_count INT NOT NULL DEFAULT 0,
  submit_num INT NOT NULL DEFAULT 0,
  ac_num INT NOT NULL DEFAULT 0,
  creator VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1, -- 状态：1表示正常并可视，0表示正常但不可视（这部分是比赛题目才有的），-1表示题目禁用
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(title)
)

-- 10.problem_judge_type（题目评测方式表）
CREATE TABLE IF NOT EXISTS problem_judge_type (
  id INT NOT NULL AUTO_INCREMENT,
  problem_id INT NOT NULL,
  judge_type_id INT NOT NULL,
  problem_type INT, -- 题目类型：0表示ACM/ICPC题型、1表示函数型题型
  code_template TEXT, -- 代码模板（针对题目为函数型题型）
  spj INT, -- 是否特判（针对judge_type_id为评测机），1表示特判，0表示不是
  spider_problem_id INT, -- 表示目标OJ的ID（针对judge_type_id为爬虫）
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(problem_id)
)


-- 11.judge_type（评测方式标准表）
CREATE TABLE IF NOT EXISTS judge_type (
  id INT NOT NULL AUTO_INCREMENT,
  -- 评测方式公用信息
  type TINYINT NOT NULL, -- 0代表爬虫、1代表评测机
  name VARCHAR(50) NOT NULL, -- 爬虫名称或评测机名称
  url VARCHAR(300) NOT NULL, -- 爬虫目标url或评测机服务所在url
  status TINYINT NOT NULL DEFAULT 0, -- 爬虫和评测机刚创建初始状态，0表示被停用，1表示正常，-1表示异常
  -- 爬虫专有信息（无）
  -- 评测机专有信息，初始化可都不填，由心跳进行补充
  hostname VARCHAR(50),
  cpu_core TINYINT,
  cpu_usage VARCHAR(20),
  memory_usage VARCHAR(20), 
  last_heart_beat TIMESTAMP,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(name)
)



-- 12.tag（标签表）
CREATE TABLE IF NOT EXISTS tag (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  creator VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(name)
)

-- 13.problem_tag（题目标签表）
CREATE TABLE IF NOT EXISTS problem_tag (
  id INT NOT NULL AUTO_INCREMENT,
  problem_id INT NOT NULL,
  tag_id INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 14.judge（提交评测表）
CREATE TABLE IF NOT EXISTS submit (
  id INT NOT NULL AUTO_INCREMENT,
  problem_id INT NOT NULL,
  code TEXT NOT NULL,
  time VARCHAR(20),
  memory VARCHAR(20),
  language VARCHAR(10) NOT NULL,
  submit_status VARCHAR(20) NOT NULL,
  source_id INT NOT NULL,  -- 来源ID，0表示无，大于0表示比赛ID
  creator_id INT NOT NULL,
  creator VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 15.contest（竞赛信息表）
CREATE TABLE IF NOT EXISTS contest (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  description TEXT NOT NULL,
  start_time VARCHAR(50) NOT NULL,
  end_time VARCHAR(50) NOT NULL,
  sign_up_rule VARCHAR(10) NOT NULL, -- 报名规则：(1)公开：不用认证与审核(2)认证：认证+审核(3)密码：成功输入密码即可成功
  password VARCHAR(500), -- 排名规则为密码时的密码
  rank_model VARCHAR(10) NOT NULL, -- 排名模式：(1)练习：ac数目降序，wa的次数升序(2)积分：ac数目降序，花费时间升序（罚时错一次罚时20分钟）(3)ACM/ICPC：ac数目降序，花费时间升序（罚时错一次罚时20分钟）
  realtime_rank INT NOT NULL DEFAULT 1, -- 是否实时排名，默认开启
  creator VARCHAR(20) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1, -- 竞赛状态：-1表示下线，0表示已经结束封榜不能进行任何更改，1表示正常
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(title)
)

-- 16.contest_user（竞赛用户报名表）
CREATE TABLE IF NOT EXISTS contest_user (
  id INT NOT NULL AUTO_INCREMENT,
  contest_id INT NOT NULL,
  user_id INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 0, -- 报名状态：0表示尚未审核、1表示审核通过、-1表示审核不通过
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id)
)

-- 17.竞赛排名：实时计算(redis)+落地成表(封榜持久化)。

CREATE TABLE IF NOT EXISTS contest_rank (
  id INT NOT NULL AUTO_INCREMENT,
  contest_id INT NOT NULL,
  rank_json TEXT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  crt_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lm_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_id PRIMARY KEY(id),
  CONSTRAINT uq_id UNIQUE KEY(contest_id)
)