# Инструкция по развертыванию

## Требования к окружению

### Обязательные компоненты
| Компонент | Минимальная версия | Рекомендуемая версия | Назначение |
|-----------|-------------------|----------------------|------------|
| Java | 17 | 17 или выше | Запуск Spring Boot приложения |
| Maven | 3.6 | 3.8+ | Сборка проекта |
| Git | 2.20 | 2.40+ | Клонирование репозитория |
| Docker | 20.10 | 23.0+ | Контейнеризация (опционально) |

### Опциональные компоненты
| Компонент | Назначение |
|-----------|------------|
| PostgreSQL 14+ | Продакшен база данных |
| Redis 6+ | Распределенный кэш |
| Prometheus | Мониторинг метрик |
| Grafana | Визуализация метрик |

## Локальное развертывание (для разработки)

### Шаг 1: Клонирование репозитория
```bash
git clone https://github.com/elliansss/Bank-Service.git
cd Bank-Service
Шаг 2: Сборка проекта
bash
# Сборка JAR-архива
mvn clean package -DskipTests

# Или с тестами
mvn clean package
После сборки JAR файл будет доступен по пути:

text
target/bank-service-1.0.0.jar
Шаг 3: Запуск приложения
bash
# Базовый запуск
java -jar target/bank-service-1.0.0.jar

# С указанием профиля
java -jar target/bank-service-1.0.0.jar --spring.profiles.active=dev

# С дополнительной памятью
java -Xmx512m -Xms256m -jar target/bank-service-1.0.0.jar
Шаг 4: Проверка работоспособности
bash
# Проверка health-check
curl http://localhost:8080/actuator/health

# Проверка информации о сервисе
curl http://localhost:8080/management/info

# Открытие Swagger UI в браузере
open http://localhost:8080/swagger-ui.html
Конфигурация Spring Boot
Основные переменные окружения
Переменная	Описание	Значение по умолчанию	Обязательно
SPRING_PROFILES_ACTIVE	Активный профиль	default	Нет
SERVER_PORT	Порт приложения	8080	Нет
DATASOURCE_TRANSACTION_URL	URL БД транзакций	jdbc:h2:file:./data/transaction	Да
DATASOURCE_TRANSACTION_USERNAME	Пользователь БД транзакций	sa	Да
DATASOURCE_TRANSACTION_PASSWORD	Пароль БД транзакций	password	Да
DATASOURCE_RULES_URL	URL БД правил	jdbc:h2:file:./data/rules	Да
DATASOURCE_RULES_USERNAME	Пользователь БД правил	sa	Да
DATASOURCE_RULES_PASSWORD	Пароль БД правил	password	Да
TELEGRAM_BOT_TOKEN	Токен Telegram бота	-	Для бота
TELEGRAM_BOT_USERNAME	Имя Telegram бота	BankRecommendationBot	Нет
CACHE_TTL_MINUTES	Время жизни кэша	5	Нет
CACHE_MAX_SIZE	Макс. размер кэша	1000	Нет
LOGGING_LEVEL	Уровень логирования	INFO	Нет
Пример файла .env
bash
# Database Configuration
DATASOURCE_TRANSACTION_URL=jdbc:h2:file:./data/transaction
DATASOURCE_TRANSACTION_USERNAME=sa
DATASOURCE_TRANSACTION_PASSWORD=password

DATASOURCE_RULES_URL=jdbc:h2:file:./data/rules
DATASOURCE_RULES_USERNAME=sa
DATASOURCE_RULES_PASSWORD=password

# Telegram Bot
TELEGRAM_BOT_TOKEN=your_telegram_bot_token_here
TELEGRAM_BOT_USERNAME=BankRecommendationBot

# Cache Configuration
CACHE_TTL_MINUTES=5
CACHE_MAX_SIZE=1000

# Application
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
LOGGING_LEVEL=INFO
Запуск с переменными окружения
bash
# Linux/Mac
export TELEGRAM_BOT_TOKEN="your_token"
export SPRING_PROFILES_ACTIVE=prod
java -jar target/bank-service-1.0.0.jar

# Windows (Command Prompt)
set TELEGRAM_BOT_TOKEN=your_token
set SPRING_PROFILES_ACTIVE=prod
java -jar target/bank-service-1.0.0.jar

# Windows (PowerShell)
$env:TELEGRAM_BOT_TOKEN="your_token"
$env:SPRING_PROFILES_ACTIVE="prod"
java -jar target/bank-service-1.0.0.jar
Развертывание с помощью Docker
Шаг 1: Сборка Docker образа
bash
# Сборка образа
docker build -t bank-recommendation-service:1.0.0 .

# Или используя docker-compose
docker-compose build
Шаг 2: Запуск контейнера
bash
# Запуск с переменными окружения
docker run -d \
  --name bank-service \
  -p 8080:8080 \
  -e TELEGRAM_BOT_TOKEN="your_token" \
  -e SPRING_PROFILES_ACTIVE=prod \
  bank-recommendation-service:1.0.0

# Или с docker-compose
docker-compose up -d
Шаг 3: Проверка контейнера
bash
# Просмотр логов
docker logs bank-service

# Проверка статуса
docker ps | grep bank-service

# Остановка контейнера
docker stop bank-service
Продакшен развертывание
Конфигурация для production
1. База данных (PostgreSQL вместо H2)
properties
# application-production.properties
spring.datasource.transaction.url=jdbc:postgresql://localhost:5432/transaction_db
spring.datasource.transaction.username=transaction_user
spring.datasource.transaction.password=strong_password

spring.datasource.rules.url=jdbc:postgresql://localhost:5432/rules_db
spring.datasource.rules.username=rules_user
spring.datasource.rules.password=strong_password

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
2. Redis для распределенного кэша
properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=redis_password
3. Безопасность
properties
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.show-details=when-authorized
Миграция базы данных
Создание таблиц в PostgreSQL
sql
-- База данных транзакций (read-only, миграция данных из H2)
CREATE DATABASE transaction_db;

-- База данных правил
CREATE DATABASE rules_db;

-- Таблица динамических правил
CREATE TABLE recommendation_rule (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    text TEXT NOT NULL,
    sql_query TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recommendation_rule_active ON recommendation_rule(is_active);

-- Таблица статистики
CREATE TABLE rule_statistic (
    id UUID PRIMARY KEY,
    rule_id UUID NOT NULL REFERENCES recommendation_rule(id),
    trigger_count INTEGER DEFAULT 0,
    last_triggered TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rule_statistic_rule_id ON rule_statistic(rule_id);
Запуск в production
bash
# Сборка production версии
mvn clean package -Pproduction -DskipTests

# Запуск с production профилем
java -jar target/bank-service-1.0.0.jar \
  --spring.profiles.active=production \
  --server.port=8080 \
  --spring.datasource.transaction.url=jdbc:postgresql://localhost:5432/transaction_db \
  --spring.datasource.rules.url=jdbc:postgresql://localhost:5432/rules_db
Мониторинг и логирование
Health Checks
bash
# Проверка состояния приложения
curl http://localhost:8080/actuator/health

# Детальная информация
curl http://localhost:8080/actuator/health/details

# Информация о приложении
curl http://localhost:8080/actuator/info
Метрики Prometheus
text
http://localhost:8080/actuator/prometheus
Логирование
properties
# Настройки логирования в application.properties
logging.level.com.bank=INFO
logging.level.org.springframework.web=DEBUG
logging.file.name=logs/application.log
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
Устранение неполадок
Распространенные проблемы
Проблема 1: Не запускается приложение
bash
# Проверка порта
netstat -an | grep 8080

# Проверка Java версии
java -version

# Просмотр логов
tail -f logs/application.log
Проблема 2: Ошибки подключения к БД
bash
# Проверка доступности БД
telnet localhost 5432

# Проверка учетных данных
psql -h localhost -U transaction_user -d transaction_db
Проблема 3: Telegram бот не работает
bash
# Проверка токена
echo $TELEGRAM_BOT_TOKEN

# Тестирование API Telegram
curl https://api.telegram.org/bot{YOUR_TOKEN}/getMe
Проблема 4: Недостаточно памяти
bash
# Увеличение памяти JVM
java -Xmx1024m -Xms512m -jar target/bank-service-1.0.0.jar
Логи для отладки
bash
# Просмотр логов приложения
tail -f logs/application.log

# Поиск ошибок
grep -i error logs/application.log

# Мониторинг запросов
grep "GET /api/recommendations" logs/application.log
Обновление версии
Шаг 1: Остановка текущей версии
bash
# Найти PID процесса
jps -l | grep bank-service

# Остановить приложение
kill -15 <PID>
Шаг 2: Обновление кода
bash
git pull origin main
mvn clean package
Шаг 3: Запуск новой версии
bash
java -jar target/bank-service-1.0.0.jar
Шаг 4: Проверка обновления
bash
curl http://localhost:8080/management/info | jq .version
Резервное копирование
База данных правил
bash
# Экспорт данных
pg_dump -h localhost -U rules_user rules_db > rules_backup_$(date +%Y%m%d).sql

# Импорт данных
psql -h localhost -U rules_user rules_db < rules_backup.sql
Конфигурация
bash
# Копирование конфигурационных файлов
cp -r config/ backup/config_$(date +%Y%m%d)/
Логи
bash
# Ротация логов
logrotate /etc/logrotate.d/bank-service
Дополнительные ресурсы
Полезные команды
bash
# Просмотр использования памяти
jstat -gc <PID>

# Дамп памяти при утечке
jmap -dump:live,format=b,file=heap.bin <PID>

# Профилирование CPU
jstack <PID>
Ссылки
Spring Boot Documentation

H2 Database Console

Swagger UI

Actuator Endpoints

Контакты для поддержки
При возникновении проблем:

Проверьте логи в logs/application.log

Убедитесь в доступности всех зависимостей

Проверьте конфигурацию переменных окружения

Обратитесь к документации или создайте Issue в репозитории