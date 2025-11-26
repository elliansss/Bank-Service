-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    telegram_id VARCHAR(100)
);

-- Таблица статистики правил
CREATE TABLE IF NOT EXISTS rule_statistics (
    rule_id VARCHAR(100) PRIMARY KEY,
    count BIGINT DEFAULT 0 NOT NULL
);

-- Индекс для быстрого поиска пользователей по имени
CREATE INDEX IF NOT EXISTS idx_users_name ON users(first_name, last_name);