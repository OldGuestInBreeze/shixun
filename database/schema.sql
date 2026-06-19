CREATE DATABASE IF NOT EXISTS stray_animal_rescue
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE stray_animal_rescue;

DROP TABLE IF EXISTS adoption_application;
DROP TABLE IF EXISTS donation;
DROP TABLE IF EXISTS volunteer;
DROP TABLE IF EXISTS rescue_case;
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(64) NOT NULL,
  display_name VARCHAR(50) NOT NULL,
  role VARCHAR(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE animal (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  species VARCHAR(30) NOT NULL,
  breed VARCHAR(80),
  gender VARCHAR(10),
  age INT,
  health_status VARCHAR(120),
  status VARCHAR(30) NOT NULL,
  location VARCHAR(120),
  image_url VARCHAR(255),
  description VARCHAR(1000),
  created_at DATETIME NOT NULL,
  INDEX idx_animal_status(status),
  INDEX idx_animal_species(species)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rescue_case (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(120) NOT NULL,
  animal_name VARCHAR(80),
  location VARCHAR(160) NOT NULL,
  urgency VARCHAR(20),
  status VARCHAR(30),
  contact_name VARCHAR(50),
  contact_phone VARCHAR(30),
  description VARCHAR(1000),
  reported_at DATETIME NOT NULL,
  INDEX idx_case_status(status),
  INDEX idx_case_urgency(urgency)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE adoption_application (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  animal_id BIGINT NOT NULL,
  applicant_name VARCHAR(50) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  address VARCHAR(200) NOT NULL,
  experience VARCHAR(255),
  reason VARCHAR(1000),
  status VARCHAR(30) NOT NULL,
  created_at DATETIME NOT NULL,
  CONSTRAINT fk_application_animal FOREIGN KEY (animal_id) REFERENCES animal(id),
  INDEX idx_application_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE volunteer (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  skill VARCHAR(120),
  available_time VARCHAR(120),
  status VARCHAR(30) NOT NULL,
  created_at DATETIME NOT NULL,
  INDEX idx_volunteer_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE donation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  donor_name VARCHAR(80) NOT NULL,
  amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  donation_type VARCHAR(30) NOT NULL,
  message VARCHAR(500),
  created_at DATETIME NOT NULL,
  INDEX idx_donation_type(donation_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users(username, password, display_name, role) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '救助站管理员', 'ADMIN');

INSERT INTO animal(name, species, breed, gender, age, health_status, status, location, image_url, description, created_at) VALUES
('团团', '猫', '中华田园猫', '雌', 1, '已驱虫，轻微营养不良恢复中', '待领养', '城南社区救助点', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=900&q=80', '亲人、安静，适合有养猫经验的家庭。', NOW()),
('豆豆', '狗', '混血犬', '雄', 3, '疫苗齐全，后腿旧伤已恢复', '待领养', '河滨公园临时安置点', 'https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=900&q=80', '性格活泼，会基础牵引，适合经常运动的家庭。', NOW()),
('小橘', '猫', '橘猫', '雄', 2, '皮肤病治疗中', '康复中', '东门动物医院', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=900&q=80', '需要继续药浴和观察，暂不开放领养。', NOW());

INSERT INTO rescue_case(title, animal_name, location, urgency, status, contact_name, contact_phone, description, reported_at) VALUES
('雨夜受伤幼猫救助', '未命名幼猫', '人民路公交站附近', '高', '处理中', '李同学', '13800000001', '幼猫躲在站牌后，前爪疑似受伤，已联系志愿者前往。', NOW()),
('校园流浪犬绝育回访', '黑背混血犬', '北区操场', '中', '已完成', '王老师', '13800000002', '完成捕捉、绝育、疫苗和原地放归，后续定期回访。', NOW());

INSERT INTO volunteer(name, phone, skill, available_time, status, created_at) VALUES
('赵志愿', '13700000001', '摄影、接送', '周末上午', '已联系', NOW());

INSERT INTO donation(donor_name, amount, donation_type, message, created_at) VALUES
('爱心人士A', 200.00, '资金', '用于购买猫粮', NOW());
