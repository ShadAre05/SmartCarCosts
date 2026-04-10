## There is link for databse (dbdiagram.io)

https://dbdiagram.io/d/SmartCarCosts-69cd82e3fb2db18e3b5d5ddc

# Data Base webapp "Smart Car Costs"

## Tables and their description

---

### 1. `users`
Stores information abour applications users

- `id` — unique user identifier
- `email` — email (unique)  
- `first_name` / `last_name` — first and last name
- `password_hash` — password hash
- `country` — select a county where are you located
- `created_at` — user creation date

**Connections:**  
- `user_cars` (1:N) — one user can have only 3 cars
- `user_company_roles` (1:N) — roles in company 
- `notifications_log` (1:N) — notification story  

---

### 2. `car_brands`
Store car brands (upload from opensource datasets).

- `id` — unique identifier of brand
- `name` — name of brand (BMW, Audi, Toyota, etc.)  

**Connections:**  
- `car_models` (1:N) — One mark can have many models

---

### 3. `car_models`
Stores specific car models (upload from opensource datasets).

- `id` — unique model identifier
- `brand_id` — brand reference (FK)
- `model_name` — model name (540i, A6, Camry, etc.)
- `generation` — model generation (F10, F11, etc.)

**Connections:**
- `user_cars` (1:N) — a specific user car is linked to a model

---

### 4. `user_cars` 
Stores specific user vehicles.

- `id` — unique vehicle identifier
- `user_id` — vehicle owner (FK)
- `model_id` — vehicle model (FK)
- `year` — year of manufacture
- `license_plate` — license plate
- `created_at` — date added

**Features:**
- Limit: up to 3 vehicles per user (implemented in the backend)
- Each vehicle can have its own expenses and reminders

---

### 5. `expense_categories`
Stores car expense categories.

- `id` — unique category identifier
- `name` — category name (fuel, insurance, repairs, etc.)

**Connections:**
- `expenses` (1:N) — user expenses linked to a category

---

### 6. `expenses`
Stores user expenses for a specific car.

- `id` — Unique expense identifier
- `user_car_id` — FK for the car
- `category_id` — FK for the category
- `amount` — Amount
- `description` — Expense description
- `expense_date` — Expense date (current by default)
- `created_at` — Record creation date

**Connections:**
- `user_cars` — The car the money was spent on
- `expense_categories` — Expense category

---

### 7. `reminder_types`
Stores reminder types.

- `id` — unique type identifier
- `name` — name (insurance, maintenance, etc.)

**Connections:**
- `reminders` (1:N) — each reminder belongs to a specific type

---

### 8. `reminders`
Stores reminders for users' cars.

- `id` — unique identifier
- `user_car_id` — FK for the car
- `reminder_type_id` — FK for the reminder type
- `remind_at` — date when the reminder will trigger
- `notify_month` / `notify_week` / `notify_day` — flags for notifications

**Connections:**
- `user_cars` — the reminder is specific to the car
- `reminder_types` — reminder type

---

### 9. `notifications_log`
Stores the history of notifications sent to users.

- `id` — unique identifier
- `user_id` — FK for the user
- `message` — notification text
- `sent_at` — sending time

---

### 10. `companies`
Stores companies to which users may belong.

- `id` — unique company identifier
- `name` — company name
- `created_at` — creation date

**Connections:**
- `user_company_roles` — users with company roles

---

### 11. `roles`
Stores user roles in companies.

- `id` — unique identifier
- `name` — role name (ADMIN, USER)

**Connections:**
- `user_company_roles` — role associations with users and companies

---

### 12. `user_company_roles`
A link table for users and companies with roles.

- `id` — unique identifier
- `user_id` — FK for the user
- `company_id` — FK for the company
- `role_id` — FK for the role
- `joined_at` — joining date

**Features:**
- Allows a single user to be a member of multiple companies with different roles

---

## Connections between tables (ER-schema by text)

users (1) ── (N) user_cars ── (N) car_models ── (1) car_brands
user_cars (1) ── (N) expenses ── (1) expense_categories
user_cars (1) ── (N) reminders ── (1) reminder_types
users (1) ── (N) notifications_log
users (1) ── (N) user_company_roles ── (1) companies
user_company_roles ── (1) roles
