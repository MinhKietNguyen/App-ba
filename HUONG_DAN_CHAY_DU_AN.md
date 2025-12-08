# Hướng Dẫn Chạy Dự Án Backend ToDoList

## 📋 Yêu Cầu Hệ Thống

1. **Java JDK 17** hoặc cao hơn
2. **Maven 3.6+** (hoặc sử dụng Maven Wrapper có sẵn)
3. **PostgreSQL** (phiên bản 12+)
4. **IDE** (IntelliJ IDEA, Eclipse, VS Code) - tùy chọn

---

## 🗄️ Bước 1: Cài Đặt và Cấu Hình PostgreSQL

### 1.1. Cài đặt PostgreSQL
- Tải và cài đặt PostgreSQL từ: https://www.postgresql.org/download/
- Hoặc sử dụng Docker:
  ```bash
  docker run --name postgres-todolist -e POSTGRES_PASSWORD=123456 -p 5433:5432 -d postgres
  ```

### 1.2. Tạo Database
Mở PostgreSQL (pgAdmin hoặc psql) và thực hiện:

```sql
-- Tạo database
CREATE DATABASE todolist_db;
```

### 1.3. Chạy Script SQL
Mở file `postgresDB.txt` và copy toàn bộ nội dung, sau đó chạy trong PostgreSQL để:
- Tạo các ENUM types
- Tạo các bảng (Users, Projects, Tasks, Messages, Events, Labels, ...)
- Insert dữ liệu mẫu
- Tạo indexes

**Lưu ý:** File `postgresDB.txt` chứa script SQL đầy đủ để setup database.

---

## ⚙️ Bước 2: Cấu Hình Application Properties

File `src/main/resources/application.properties` đã được cấu hình sẵn:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/todolist_db
spring.datasource.username=postgres
spring.datasource.password=123456
server.port=8000
```

**Nếu database của bạn khác**, hãy chỉnh sửa các thông tin sau:
- `spring.datasource.url`: URL kết nối database
- `spring.datasource.username`: Username PostgreSQL
- `spring.datasource.password`: Password PostgreSQL
- `server.port`: Port chạy ứng dụng (mặc định 8000)

---

## 🚀 Bước 3: Chạy Dự Án

### Cách 1: Sử dụng Maven Wrapper (Khuyến nghị)

**Trên Windows:**
```bash
cd App-ba
.\mvnw.cmd spring-boot:run
```

**Trên Linux/Mac:**
```bash
cd App-ba
./mvnw spring-boot:run
```

### Cách 2: Sử dụng Maven (nếu đã cài đặt Maven)

```bash
cd App-ba
mvn clean install
mvn spring-boot:run
```

### Cách 3: Chạy từ IDE (IntelliJ IDEA/Eclipse)

1. Mở project trong IDE
2. Tìm file `BaTodoListApplication.java`
3. Click chuột phải → Run `BaTodoListApplication`
4. Hoặc tìm Run Configuration → Spring Boot → BaTodoListApplication

---

## ✅ Bước 4: Kiểm Tra Ứng Dụng Đã Chạy

Sau khi chạy thành công, bạn sẽ thấy log tương tự:
```
Started BaTodoListApplication in X.XXX seconds
```

### Truy cập Swagger UI:
Mở trình duyệt và vào:
```
http://localhost:8000/ba-todolist/api/swagger-ui/index.html
```

---

## 🔐 Bước 5: Test API với Swagger

### 5.1. Đăng nhập để lấy JWT Token

1. Trong Swagger UI, tìm đến **Auth Controller**
2. Mở endpoint `POST /auth/login`
3. Click **Try it out**
4. Sử dụng thông tin đăng nhập mặc định:
   ```json
   {
     "username": "Tester",
     "password": "password123"  // Hoặc password tương ứng trong DB
   }
   ```
   **Lưu ý:** Password trong database đã được hash bằng BCrypt. Nếu không đăng nhập được, bạn có thể:
   - Tạo user mới qua endpoint `/auth/register`
   - Hoặc kiểm tra lại password hash trong database

5. Click **Execute**
6. Copy token từ response (trong field `token` hoặc `accessToken`)

### 5.2. Authorize với JWT Token

1. Ở đầu trang Swagger UI, tìm biểu tượng **🔒 Authorize** (hoặc nút "Authorize")
2. Paste token vừa copy vào ô "Value"
3. Click **Authorize** → **Close**

### 5.3. Test các API khác

Bây giờ bạn có thể test các endpoint khác:
- **Task Controller**: Quản lý tasks
- **Project Controller**: Quản lý projects
- **Event Controller**: Quản lý events
- **Message Controller**: Quản lý messages
- **Label Controller**: Quản lý labels
- **Reminder Controller**: Quản lý reminders

---

## 📝 Các Endpoint Chính

### Authentication
- `POST /auth/login` - Đăng nhập
- `POST /auth/register` - Đăng ký (nếu có)

### Tasks
- `GET /tasks` - Lấy danh sách tasks
- `POST /tasks` - Tạo task mới
- `PUT /tasks/{id}` - Cập nhật task
- `DELETE /tasks/{id}` - Xóa task

### Projects
- `GET /projects` - Lấy danh sách projects
- `POST /projects` - Tạo project mới
- `PUT /projects/{id}` - Cập nhật project
- `DELETE /projects/{id}` - Xóa project

---

## 🐛 Xử Lý Lỗi Thường Gặp

### Lỗi kết nối database:
```
Connection refused hoặc Connection timeout
```
**Giải pháp:**
- Kiểm tra PostgreSQL đã chạy chưa
- Kiểm tra port (5433) có đúng không
- Kiểm tra username/password trong `application.properties`

### Lỗi port đã được sử dụng:
```
Port 8000 is already in use
```
**Giải pháp:**
- Đổi port trong `application.properties`: `server.port=8001`
- Hoặc tắt ứng dụng đang chạy trên port 8000

### Lỗi thiếu dependency:
```
ClassNotFoundException hoặc NoClassDefFoundError
```
**Giải pháp:**
```bash
mvn clean install
```

### Lỗi JWT Token:
```
JWT expired hoặc Invalid token
```
**Giải pháp:**
- Đăng nhập lại để lấy token mới
- Token có thời hạn, cần refresh sau một khoảng thời gian

---

## 📚 Thông Tin Bổ Sung

- **Base URL API**: `http://localhost:8000/ba-todolist/api`
- **Swagger UI**: `http://localhost:8000/ba-todolist/api/swagger-ui/index.html`
- **API Docs (JSON)**: `http://localhost:8000/ba-todolist/api/v3/api-docs`

---

## 🎯 Ghi Chú

- Nếu gặp bug, ghi lại vào file `Bug.txt`
- Database sẽ tự động tạo/update schema nhờ `spring.jpa.hibernate.ddl-auto=update`
- Dữ liệu mẫu đã được insert sẵn trong script SQL

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề, kiểm tra:
1. Logs trong console khi chạy ứng dụng
2. File `Bug.txt` để xem các bug đã được ghi nhận
3. README.md gốc trong project

