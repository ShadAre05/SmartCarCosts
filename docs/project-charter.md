# CarSmartCosts

## Information about project

| Role | Name | GitHub | Email |
|------|------|--------|-------|
| Team Lead + Frontend + Backend(PR) + PM  | Arseni Simanchuk | @ShadAre05| arseni.simanchuk@stud.esdc.lt |
| Backend | Matsvei Nikalayeu | @Matvey-N | matsvei.nikalayeu@stud.esdc.lt |
| Backend | Vladyslav Kosheliuk | @Vladkosheliuk | vladyslav.kosheliuk@stud.esdc.lt |

**Repository:** https://github.com/ShadAre05/SmartCarCosts/

**Team сhannel:** https://teams.microsoft.com/l/chat/19:5c4fd3fa3a624c39a0160d124f310ac8@thread.v2/conversations?context=%7B%22contextType%22%3A%22chat%22%7D

**Figma:** https://www.figma.com/design/mj1LEKgipZpJiga6GVekkg/SmartCarCosts?node-id=0-1&p=f&t=nTddjnYjrXMGq9fj-0

---

## Description of project

### Problem
Car owners often do not track their expenses systematically and forget about scheduled maintenance (oil changes, insurance, inspections). This leads to unexpected costs and a poor understanding of the real cost of car ownership.

At the same time, service stations often do not have a simple digital tool to record performed work and maintain a structured service history for vehicles.

---

### Solution
The application provides a centralized system for managing vehicle-related data for both car owners and service stations.

- **For users (car owners):**
  - Track all car-related expenses  
  - Analyze spending by categories and time periods  
  - Receive reminders about important events (insurance, maintenance, etc.)  

- **For service stations:**
  - Register vehicles manually with VIN for unique information about cars in service
  - Maintain service history (repairs, parts, labor costs)  
  - Record detailed descriptions of performed work  

This dual-role system allows better transparency, structured data management, and improved control over vehicle maintenance and costs.

---

### Target Audience

- **Primary audience:**  
  - Car owners (18–45 years old) who want to control and optimize their expenses  

- **Secondary audience:**  
  - Service stations and mechanics who need a simple system to track performed work  
  - Students and young professionals interested in personal finance management  

---

## Project Scope

### MVP (for final delivery)

#### Authentication & Roles
- [ ] User registration and authentication (JWT)
- [ ] Role selection during registration (USER / SERVICE)

---

#### USER functionality
- [ ] Add up to 3 vehicles (brand, model, year, engine, fuel, license plate)
- [ ] Add and delete expenses  
- [ ] View expense list with filtering (by date, category)  
- [ ] Basic analytics (expenses by category and period)  
- [ ] Set reminders (insurance, maintenance, etc.)

---

#### SERVICE functionality
- [ ] Add unlimited vehicles  
- [ ] Register vehicles by VIN manually
- [ ] Create service records:
  - description of work  
  - parts cost  
  - labor cost  
  - total cost  

---

#### Notifications & Profile
- [ ] Notification system (maintenance, insurance reminders)
- [ ] Profile management (email, password, full name, country)

---

### Out of Scope (post-course)

- [ ] Map integration (finding nearby service stations)
- [ ] Expense optimization recommendations
- [ ] Export to CSV/Excel
- [ ] Mobile version / PWA
- [ ] Automatic currency detection by country
- [ ] Integration with banking APIs (automatic expense tracking)
- [ ] Linking users with service stations (shared service history)

---

## Tech Stack

| Component | Technology |
|-----------|------------|
| Backend | Java |
| Frontend | Java Thymeleaf |
| Data Base | PostgreSQL |
| Хостинг | AWS |
| CI/CD | GitHub Actions |

---

## Risks

| Risks | Possibility | Impact | Mitigation |
|------|-------------|---------|-----------|
| Lack of time | High | High | Strict MVP prioritization |
| Frontend complexity | Very high | Very high | We dont have frontend developer |
| Backend/frontend integration issues | Medium | Medium | Define clear API contract (Swagger) |
| Limited experience with Spring Security | Medium | Medium | Use guides and simplify initial setup |
| Low team engagement | Low | High | Regular meetings and task tracking |

---

## Communication

- **Daily standup:** 1 time per week, Monday 11.00 a.m.
- **Main Channel:** MS Teams
- **Code review:** GitHub Pull Requests
- **Task-Tracker:** GitHub Projects

---

## Definition of Done (optional)

- [ ] Code is written and follows project standards  
- [ ] Pull Request is created and approved  
- [ ] Functionality is manually tested  
- [ ] API is tested (Postman / Swagger)  
- [ ] No critical bugs  
- [ ] Documentation is updated (if needed)  

---

## Signatures

| Participant | Date | Signature |
|----------|------|---------|
| Arseni Simanchuk | 2026.04.12 | Agreed |
| Matsvei Nikalayeu | 2026.04.13 | Agreed |
| Vladyslav Kosheliuk | 2026.04.14 | Agreed |
