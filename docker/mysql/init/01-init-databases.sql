-- Initialize databases for all microservices
CREATE DATABASE IF NOT EXISTS user_service_db;
CREATE DATABASE IF NOT EXISTS costume_service_db;
CREATE DATABASE IF NOT EXISTS bill_costume_service_db;
CREATE DATABASE IF NOT EXISTS supplier_service_db;
CREATE DATABASE IF NOT EXISTS import_bill_service_db;

-- Grant privileges
GRANT ALL PRIVILEGES ON user_service_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON costume_service_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bill_costume_service_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON supplier_service_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON import_bill_service_db.* TO 'root'@'%';

FLUSH PRIVILEGES;
