# Документация REST API

## Общая информация

Данный документ содержит спецификацию REST API банковской рекомендательной системы в формате OpenAPI 3.0.

### Базовый URL
http://localhost:8080

text

### Формат данных
- **Content-Type:** `application/json`
- **Ответы:** Все ответы в формате JSON
- **Кодировка:** UTF-8

### Коды ответов
| Код | Описание |
|-----|----------|
| 200 | Успешный запрос |
| 201 | Ресурс создан |
| 400 | Неверный запрос |
| 404 | Ресурс не найден |
| 500 | Внутренняя ошибка сервера |

## Эндпоинты

### 1. Получение рекомендаций (ТЗ-1)

#### GET /api/recommendations/{userId}
Возвращает список рекомендаций для указанного пользователя.

**Параметры:**
| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| userId | string (UUID) | Да | Идентификатор пользователя |

**Пример запроса:**
```bash
curl -X GET "http://localhost:8080/api/recommendations/cd515076-5d8a-44be-930e-8d4fcb79f42d"
Пример успешного ответа (200 OK):

json
{
  "userId": "cd515076-5d8a-44be-930e-8d4fcb79f42d",
  "recommendations": [
    {
      "id": "147f6a0f-3b91-413b-ab99-87f081d60d5a",
      "name": "Invest 500",
      "text": "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом..."
    },
    {
      "id": "59efc529-2fff-41af-baff-90ccd7402925",
      "name": "Top Saving",
      "text": "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели..."
    }
  ],
  "timestamp": "2024-01-15T10:30:00Z"
}
Пример ответа без рекомендаций:

json
{
  "userId": "d4a4d619-9a0c-4fc5-b0cb-76c49409546b",
  "recommendations": [],
  "timestamp": "2024-01-15T10:30:00Z"
}
2. Управление динамическими правилами (ТЗ-2)
GET /api/manager/v1/recommendation-rules
Возвращает список всех динамических правил.

Пример запроса:

bash
curl -X GET "http://localhost:8080/api/manager/v1/recommendation-rules"
Пример ответа (200 OK):

json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Инвестиционный продукт для опытных",
    "description": "Продукт для опытных инвесторов",
    "text": "Текст, который будет отображаться пользователю в рекомендациях",
    "sqlQuery": "SELECT user_id FROM transactions t JOIN products p ON t.product_id = p.id WHERE p.type = 'INVEST' GROUP BY user_id HAVING SUM(t.amount) > 50000",
    "isActive": true,
    "createdAt": "2024-01-10T14:30:00Z",
    "updatedAt": "2024-01-12T09:15:00Z"
  },
  {
    "id": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
    "name": "Кредит для надежных клиентов",
    "description": "Продукт для клиентов с хорошей историей",
    "text": "Текст, который будет отображаться пользователю в рекомендациях",
    "sqlQuery": "SELECT user_id FROM transactions t JOIN products p ON t.product_id = p.id WHERE p.type = 'DEBIT' AND t.type = 'DEPOSIT' GROUP BY user_id HAVING SUM(t.amount) > 100000",
    "isActive": false,
    "createdAt": "2024-01-11T10:20:00Z",
    "updatedAt": "2024-01-11T10:20:00Z"
  }
]
GET /api/manager/v1/recommendation-rules/{ruleId}
Возвращает правило по его идентификатору.

Параметры:

Параметр	Тип	Обязательный	Описание
ruleId	string (UUID)	Да	Идентификатор правила
Пример запроса:

bash
curl -X GET "http://localhost:8080/api/manager/v1/recommendation-rules/550e8400-e29b-41d4-a716-446655440000"
Пример ответа (200 OK):

json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Инвестиционный продукт для опытных",
  "description": "Продукт для опытных инвесторов",
  "text": "Текст, который будет отображаться пользователю в рекомендациях",
  "sqlQuery": "SELECT user_id FROM transactions t JOIN products p ON t.product_id = p.id WHERE p.type = 'INVEST' GROUP BY user_id HAVING SUM(t.amount) > 50000",
  "isActive": true,
  "createdAt": "2024-01-10T14:30:00Z",
  "updatedAt": "2024-01-12T09:15:00Z"
}
POST /api/manager/v1/recommendation-rules
Создает новое динамическое правило.

Тело запроса:

json
{
  "name": "Новое правило",
  "description": "Описание нового правила",
  "text": "Текст, который будет отображаться пользователю в рекомендациях",
  "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 1000"
}
Пример запроса:

bash
curl -X POST "http://localhost:8080/api/manager/v1/recommendation-rules" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Новое правило",
    "description": "Описание нового правила",
    "text": "Текст рекомендации",
    "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 1000"
  }'
Пример ответа (201 Created):

json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Новое правило",
  "description": "Описание нового правила",
  "text": "Текст, который будет отображаться пользователю в рекомендациях",
  "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 1000",
  "isActive": true,
  "createdAt": "2024-01-15T11:20:00Z",
  "updatedAt": "2024-01-15T11:20:00Z"
}
PUT /api/manager/v1/recommendation-rules/{ruleId}
Обновляет существующее правило.

Параметры:

Параметр	Тип	Обязательный	Описание
ruleId	string (UUID)	Да	Идентификатор правила
Тело запроса:

json
{
  "name": "Обновленное название",
  "description": "Обновленное описание",
  "text": "Обновленный текст рекомендации",
  "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 5000",
  "isActive": false
}
Пример запроса:

bash
curl -X PUT "http://localhost:8080/api/manager/v1/recommendation-rules/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Обновленное название",
    "description": "Обновленное описание",
    "text": "Обновленный текст рекомендации",
    "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 5000",
    "isActive": false
  }'
Пример ответа (200 OK):

json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Обновленное название",
  "description": "Обновленное описание",
  "text": "Обновленный текст рекомендации",
  "sqlQuery": "SELECT user_id FROM transactions WHERE amount > 5000",
  "isActive": false,
  "createdAt": "2024-01-15T11:20:00Z",
  "updatedAt": "2024-01-15T11:25:00Z"
}
DELETE /api/manager/v1/recommendation-rules/{ruleId}
Удаляет правило.

Пример запроса:

bash
curl -X DELETE "http://localhost:8080/api/manager/v1/recommendation-rules/123e4567-e89b-12d3-a456-426614174000"
Ответ: 204 No Content (без тела)

3. Статистика срабатываний (ТЗ-3)
GET /rule/stats
Возвращает статистику срабатываний динамических правил.

Пример запроса:

bash
curl -X GET "http://localhost:8080/rule/stats"
Пример ответа (200 OK):

json
{
  "stats": [
    {
      "ruleId": "550e8400-e29b-41d4-a716-446655440000",
      "count": 42
    },
    {
      "ruleId": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
      "count": 15
    },
    {
      "ruleId": "123e4567-e89b-12d3-a456-426614174000",
      "count": 0
    }
  ],
  "totalRecommendations": 57,
  "generatedAt": "2024-01-15T10:30:00Z"
}
4. Управление кэшем (ТЗ-3)
POST /management/clear-caches
Сбрасывает все закешированные результаты.

Пример запроса:

bash
curl -X POST "http://localhost:8080/management/clear-caches"
Ответ: 200 OK (без тела)

5. Информация о сервисе (ТЗ-3)
GET /management/info
Возвращает информацию о сервисе.

Пример запроса:

bash
curl -X GET "http://localhost:8080/management/info"
Пример ответа (200 OK):

json
{
  "name": "bank-recommendation-service",
  "version": "1.0.0",
  "buildTime": "2024-01-15T10:00:00Z",
  "environment": "development",
  "status": "UP"
}
Модели данных
Recommendation (Рекомендация)
json
{
  "type": "object",
  "properties": {
    "id": {
      "type": "string",
      "format": "uuid",
      "description": "Уникальный идентификатор рекомендации"
    },
    "name": {
      "type": "string",
      "description": "Название продукта"
    },
    "text": {
      "type": "string",
      "description": "Текст рекомендации"
    }
  },
  "required": ["id", "name", "text"]
}
RecommendationResponse (Ответ с рекомендациями)
json
{
  "type": "object",
  "properties": {
    "userId": {
      "type": "string",
      "format": "uuid"
    },
    "recommendations": {
      "type": "array",
      "items": {
        "$ref": "#/components/schemas/Recommendation"
      }
    },
    "timestamp": {
      "type": "string",
      "format": "date-time"
    }
  },
  "required": ["userId", "recommendations", "timestamp"]
}
Rule (Правило)
json
{
  "type": "object",
  "properties": {
    "id": {
      "type": "string",
      "format": "uuid"
    },
    "name": {
      "type": "string"
    },
    "description": {
      "type": "string"
    },
    "text": {
      "type": "string"
    },
    "sqlQuery": {
      "type": "string",
      "description": "SQL запрос, возвращающий user_id"
    },
    "isActive": {
      "type": "boolean"
    },
    "createdAt": {
      "type": "string",
      "format": "date-time"
    },
    "updatedAt": {
      "type": "string",
      "format": "date-time"
    }
  },
  "required": ["name", "text", "sqlQuery"]
}
RuleCreateRequest (Запрос на создание правила)
json
{
  "type": "object",
  "properties": {
    "name": {
      "type": "string"
    },
    "description": {
      "type": "string"
    },
    "text": {
      "type": "string"
    },
    "sqlQuery": {
      "type": "string"
    }
  },
  "required": ["name", "text", "sqlQuery"]
}
StatisticsResponse (Статистика)
json
{
  "type": "object",
  "properties": {
    "stats": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "ruleId": {
            "type": "string",
            "format": "uuid"
          },
          "count": {
            "type": "integer",
            "minimum": 0
          }
        }
      }
    },
    "totalRecommendations": {
      "type": "integer"
    },
    "generatedAt": {
      "type": "string",
      "format": "date-time"
    }
  }
}
ServiceInfo (Информация о сервисе)
json
{
  "type": "object",
  "properties": {
    "name": {
      "type": "string"
    },
    "version": {
      "type": "string"
    },
    "buildTime": {
      "type": "string",
      "format": "date-time"
    },
    "environment": {
      "type": "string"
    },
    "status": {
      "type": "string",
      "enum": ["UP", "DOWN", "MAINTENANCE"]
    }
  }
}
Ошибки
CommonError
json
{
  "type": "object",
  "properties": {
    "timestamp": {
      "type": "string",
      "format": "date-time"
    },
    "status": {
      "type": "integer"
    },
    "error": {
      "type": "string"
    },
    "message": {
      "type": "string"
    },
    "path": {
      "type": "string"
    }
  }
}
Пример ошибки (400 Bad Request)
json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid UUID format",
  "path": "/api/recommendations/invalid-uuid"
}
Пример ошибки (404 Not Found)
json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Rule not found with id: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/manager/v1/recommendation-rules/550e8400-e29b-41d4-a716-426614174000"
}
Swagger UI
Интерактивная документация доступна по адресу:

text
http://localhost:8080/swagger-ui.html
После запуска приложения можно:

Просматривать все эндпоинты

Тестировать API напрямую из браузера

Скачивать OpenAPI спецификацию

Примечания
SQL инъекции: Все SQL запросы в динамических правилах выполняются через PreparedStatement

Валидация: Все входные данные валидируются

Логирование: Все запросы логируются для отладки

Кэширование: Результаты кэшируются для повышения производительности

Миграция с предыдущих версий
Изменения в API:
ТЗ-1: Добавлен базовый эндпоинт рекомендаций

ТЗ-2: Добавлены эндпоинты управления правилами

ТЗ-3: Добавлены эндпоинты статистики, управления кэшем и информации

Все эндпоинты обратно совместимы.