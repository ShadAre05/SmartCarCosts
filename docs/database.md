## There is link for databse (dbdiagram.io)

https://dbdiagram.io/d/SmartCarCosts-69cd82e3fb2db18e3b5d5ddc

# Data Base webapp "Smart Car Costs"

## Tables and their description

---

### 1. `users`
Stores information about application users.

- `id` — unique user identifier  
- `email` — email (unique)  
- `full_name` — full name  
- `avatar` — profile image  
- `country` — user location  
- `password_hash` — password hash  
- `role_id` — reference to user role (USER or SERVICE)  
- `created_at` — user creation date  

**Connections:**  
- `user_cars` (1:N) — one user can have cars (max 3 for USER role)  
- `service_records` (1:N) — service users create service records  
- `notifications_log` (1:N) — notification history  
- `role` (N:1) — each user has one role  

---

### 2. `role`
Stores roles available in the system.

- `id` — unique role identifier  
- `role` — role name (USER, SERVICE)  

**Connections:**  
- `users` (1:N) — one role can be assigned to many users  

---

### 3. `country`
Stores a list of counties in table.
- `id` — unique country identifier
- `country_name` - county anme (ALL countries)

**Connections:**  
- `users` (1:N) — one country can be selected to many users

---

### 4. `car_brands`
Stores car brands (can be preloaded from datasets).

- `id` — unique brand identifier  
- `name` — brand name (BMW, Audi, Toyota, etc.)  

**Connections:**  
- `car_models` (1:N) — one brand can have multiple models  

---

### 5. `car_models`
Stores specific car models.

- `id` — unique model identifier  
- `brand_id` — reference to brand (FK)  
- `model_name` — model name (e.g., 540i, A6)  
- `generation` — model generation (e.g., F10)  

**Connections:**  
- `cars` (1:N) — one model can be used by many cars  

---

### 6. `cars`
Stores general information about cars.

- `id` — unique car identifier  
- `vin` — VIN number (unique, optional for USER)  
- `model_id` — reference to model (FK)  
- `year` — production year  
- `engine_capacity` — engine volume  
- `fuel_type` — fuel type (petrol, diesel, etc.)  
- `license_plate` — license plate number  
- `created_at` — creation date  

**Features:**  
- Shared entity for both USER and SERVICE roles  
- VIN allows global identification of vehicles  

**Connections:**  
- `user_cars` — ownership relation  
- `service_records` — service history  

---

### 7. `user_cars`
Links users with their cars.

- `id` — unique identifier  
- `user_id` — reference to user (FK)  
- `car_id` — reference to car (FK)  
- `created_at` — date of linking  

**Features:**  
- USER role: maximum 3 cars (controlled in backend)  
- SERVICE role: no restrictions  

**Connections:**  
- `users` — car owner  
- `cars` — linked car  
- `expenses` — expenses per car  
- `reminders` — reminders per car  

---

### 8. `expense_categories`
Stores categories of expenses.

- `id` — unique identifier  
- `name` — category name (fuel, insurance, repair, etc.)  

**Connections:**  
- `expenses` (1:N) — category assigned to expenses  

---

### 9. `expenses`
Stores expenses made by users on their cars.

- `id` — unique expense identifier  
- `user_car_id` — reference to user’s car  
- `category_id` — reference to category  
- `amount` — expense amount  
- `description` — optional description  
- `expense_date` — date of expense (default: current date)  
- `created_at` — record creation date  

**Connections:**  
- `user_cars` — related car  
- `expense_categories` — expense category  

---

### 10. `service_records`
Stores service operations performed by service stations.

- `id` — unique identifier  
- `car_id` — reference to car  
- `service_user_id` — reference to SERVICE user  
- `description` — description of performed work  
- `parts_cost` — cost of parts  
- `labor_cost` — cost of work  
- `total_cost` — total cost (parts + labor)  
- `service_date` — date of service  
- `created_at` — record creation date  

**Connections:**  
- `cars` — serviced car  
- `users` — service provider  

---

### 11. `reminder_types`
Stores types of reminders.

- `id` — unique identifier  
- `name` — type name (insurance, inspection, etc.)  

**Connections:**  
- `reminders` (1:N) — each reminder has a type  

---

### 12. `reminders`
Stores reminders for user cars.

- `id` — unique identifier  
- `user_car_id` — reference to user’s car  
- `reminder_type_id` — reference to reminder type  
- `remind_at` — reminder date  
- `notify_month` / `notify_week` / `notify_day` — notification flags  

**Connections:**  
- `user_cars` — related car  
- `reminder_types` — reminder type  

---

### 13. `notifications_log`
Stores history of notifications sent to users.

- `id` — unique identifier  
- `user_id` — reference to user  
- `message` — notification text  
- `sent_at` — sending timestamp  

---

## Connections between tables (ER-schema by text)

role (1) ── (N) users
users (1) ── (N) user_cars ── (N) cars ── (1) car_models ── (1) car_brands
user_cars (1) ── (N) expenses ── (1) expense_categories
cars (1) ── (N) service_records ── (1) users (SERVICE)
user_cars (1) ── (N) reminders ── (1) reminder_types
users (1) ── (N) notifications_log
