# URL Shortener API
URL Shortener API - это приложение на Spring Boot, которое позволяет пользователям создавать, управлять и запрашивать короткие URL. API предоставляет функционал для сокращения URL, перенаправления, статистики и управления.

## Начало работы
### Требования
- Java 17 или выше
- Maven 3.6.0 или выше
- PostgreSQL 12 или выше

## Установка
### 1. Клонируйте репозиторий:
```
git clone https://github.com/yourusername/url-shortener.git
cd url-shortener
```
### 2. Настройте базу данных PostgreSQL:
- Создайте новую базу данных PostgreSQL с именем url_shortener.
- Обновите файл src/main/resources/application.properties с вашим именем пользователя и паролем PostgreSQL.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=your_postgresql_username
spring.datasource.password=your_postgresql_password
spring.jpa.hibernate.ddl-auto=update
```
- Запустите приложение

## API Endpoints
### Публичные эндпоинты
#### Создание короткого URL
- Запрос:
```
POST /api/urls
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "urls",
    "attributes": {
      "longUrl": "http://example.com"
    }
  }
}
```
- Ответ:
```
HTTP/1.1 201 Created
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "urls",
    "id": "1",
    "attributes": {
      "shortUrl": "http://localhost:8080/api/urls/abc123",
      "longUrl": "http://example.com"
    }
  }
}
```
### Перенаправление на оригинальный URL
- Запрос:
```
GET /api/urls/{shortCode}
```
- Ответ:
Перенаправляет на оригинальный URL с HTTP 302 Found.
### Получение всех созданных URL
- Запрос:
```
GET /api/urls
```
- Ответ:
```
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "data": [
    {
      "type": "urls",
      "id": "1",
      "attributes": {
        "shortUrl": "http://localhost:8080/api/urls/abc123",
        "longUrl": "http://example1.com"
      }
    },
    {
      "type": "urls",
      "id": "2",
      "attributes": {
        "shortUrl": "http://localhost:8080/api/urls/AbC123",
        "longUrl": "http://example2.com"
       }
    }
  ]
}
```
### Поиск по длинному URL
- Запрос:
```
GET /api/urls/search?longUrl={longUrl}
```
- Ответ:
```
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "urls",
    "id": "1",
    "attributes": {
      "shortUrl": "http://localhost:8080/api/urls/abc123",
      "longUrl": "http://example.com"
    }
  }
}
```
## Админские эндпоинты
(Для следующих эндпоинтов требуется роль администратора)
### Получение статистики URL
- Запрос:
```
GET /api/urls/admin/stats/{shortCode}
```
- Ответ:
```
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "urls",
    "id": "1",
    "attributes": {
      "createdAt": "2023-06-25T12:34:56",
      "usageCount": 10,
      "lastUsedAt": "2023-06-26T14:23:45"
    }
  }
}
```

### Получение топ-10 наиболее используемых URL

- Запрос:
```
GET /api/urls/admin/top10
```
- Ответ:
```
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "data": [
    {
      "type": "urls",
      "id": "1",
      "attributes": {
        "longUrl": "http://example1.com",
        "usageCount": 15,
        "lastUsedAt": "2023-06-26T14:23:45"
      }
    },
    {
      "type": "urls",
      "id": "2",
      "attributes": {
        "longUrl": "http://example2.com",
        "usageCount": 12,
        "lastUsedAt": "2023-06-26T14:00:00"
       }
    }
  ]
}
```

## Периодические задачи
Приложение имеет периодическую задачу, которая выполняется каждые 10 минут для аннулирования URL, которые не использовались более 10 минут. Аннулированные URL помечаются как недействительные и не будут перенаправляться.

## Безопасность
Базовая аутентификация используется для защиты админских эндпоинтов.
### Роли
- Имя пользователя: admin, Пароль: password (Роль: ADMIN)
- Имя пользователя: user, Пароль: password (Роль: USER)
