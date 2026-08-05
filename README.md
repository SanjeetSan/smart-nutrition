# Smart Nutrition – AI-Powered Student Nutrition Monitoring

## Quick Start (Backend)

```bash
# 1️⃣ Clone the repo
git clone https://github.com/SanjeetSan/smart-nutrition.git
cd smart-nutrition

# 2️⃣ Create the MySQL database & import seed data
mysql -u root -p -e "CREATE DATABASE smart_nutrition_db;"
mysql -u root -p smart_nutrition_db < db_dump.sql

# 3️⃣ Create a local .env file (copy from .env.example)
cp .env.example .env   # edit .env with your MySQL credentials & a JWT secret (256‑bit base64)

# 4️⃣ Build & run
./mvnw clean package
./mvnw spring-boot:run   # runs on http://localhost:8081
```

## Swagger UI

Open <http://localhost:8081/swagger-ui.html> and click **Authorize**. Paste the **Bearer token** you receive from `POST /api/auth/login`.

## Invite Teammates

1. In GitHub → Settings → Manage access → Invite collaborator.
2. They clone the repo, add their own `.env`, and run the steps above.

## Database

* `db_dump.sql` – full schema + sample data (school, class, two students).
* To apply changes later run `mysqldump … > db_dump.sql` and commit.

## Development notes

* **Java 21** – compiled with Maven wrapper (`./mvnw`).
* **JWT secret** must be a 256‑bit base64 string (e.g., generated via `openssl rand -base64 32`).
* **Port** – currently set to `8081` to avoid conflicts on Windows.
