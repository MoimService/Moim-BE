# 🧑🏻‍💻 Let's Code Together! - Deving

## 📜 Table of Contents
1. [🚀 Project Overview](#-project-overview)
2. [📌 Introduction](#-introduction)
3. [👥 Team Members](#-team-members)
4. [🗣️ Team Communication](#-team-communication)
5. [🖥️ Development Environment](#-development-environment)
   - [⚙️ Tools & Technologies](#-tools--technologies)
   - [🛠 Backend Stack](#-backend-stack)
   - [🚀 Deployment & CI/CD](#-deployment--cicd)
   - [🗄️ Database](#-database)
6. [📌 Project Requirements](#-project-requirements)
7. [🖼️ Wireframe & UI/UX Design](#-wireframe--uiux-design)
8. [🗺️ Database ERD](#-database-erd)
9. [🏗️ Key Features](#-key-features)
10. [📹 Demo Video](#-demo-video)
11. [🏆 Key Achievements](#-key-achievements)


## 🚀 Project Overview
- **Duration**: January 31st - March 19th, 2025
- **Live URL**: [Deving Together](https://deving-together.netlify.app/)
- **Design Figma** | [DEVING - Design](https://www.figma.com/design/whAR7r8MPfWo9Cl93zhKlL/DEVING-%EC%8B%9C%EC%95%88?node-id=0-1&p=f&t=hQ9Sr369FbrTyGqY-0) |
- **API Documentation** | [DEVING - Swagger](https://deving.shop/swagger-ui/index.html#/auth-controller) |

![Project Screenshot](https://github.com/user-attachments/assets/df7822d2-9c03-48e8-9bf6-6cb4d1a6983b)

## 📌 Introduction
Deving is an online community for programmers to collaborate, network, and share knowledge. It provides services such as:
- Team projects
- Coding clubs
- Study groups
- Developer hobbies

> ⭐️ **Freelance Project**: This project was developed as a **contract freelance assignment** for **Code-It Company**, where I worked as the **sole backend developer** for two months.



## 👥 Team Members
- **Frontend Developers**: 4
- **Backend Developer**: 1 (myself)
- **Role**: As the only backend developer, I was responsible for setting up the **CI/CD pipeline, backend architecture, and core development**.



## 🗣️ Team Communication
- **[Daily Scrum](https://project-movie-reservation.notion.site/1bd5998d726681f3bb0ae0ee430ab350?v=1bd5998d726681efae7e000c579709f6)**
- **[Backend Sprint Board](https://project-movie-reservation.notion.site/Backend-Deving-1bd5998d726681bc869dde67b7a20664)**
- **[Team Notion Docs](https://project-movie-reservation.notion.site/Deving-1bd5998d726680edae74f9d1b9d28f27)**

---

## 🖥️ Development Environment
### ⚙️ Tools & Technologies
- **IDE**: IntelliJ IDEA Ultimate
- **Language**: Java 17
- **Build Tool**: Gradle

### 🛠 Backend Stack
- **Spring Framework** (Spring Boot 3.4.2, Spring Data JPA)
- **AWS S3** for storage
- **Swagger** for API documentation

### 🚀 Deployment & CI/CD
- **AWS EC2** (Hosting)
- **Route 53** (DNS Management)
- **Nginx + Let's Encrypt** (Reverse Proxy & SSL)
- **CI/CD**: GitHub Actions

### 🗄️ Database
- **MySQL**
- **ERD**: [ERDCloud](https://project-movie-reservation.notion.site/ERD-1bd5998d72668036927dd1bd4056b71a)

---

## 📌 Project Requirements
> Detailed project requirements **[here](https://project-movie-reservation.notion.site/1bd5998d726681acbfcddb49080a37a1?v=1bd5998d726681e3978c000cb7416921)**

| Page  | API |
|  :---:    |  :---:    |
| `Login/Signup`  | Login  |
| `Login/Signup`  |  Signup |
| `Login/Signup`  | Nickname duplication check  |
| `Login/Signup`  | Email duplication check  |
| `Login/Signup`  | Access/Refresh token |
| `Meeting`  |  Create meeting |
| `Meeting`  | Meeting search, filter  |
| `Meeting`  | Get top 4 meeting with most likes count  |
| `Meeting`  | Get meeting detail  |
| `Meeting`  | Get meeting manager detail  |
| `Meeting`  |  Upload meeting image |
| `Meeting`  |  Get meeting reviews |
| `Likes`  | Add likes  |
| `Likes`  | Delete likes  |
|  `Comments` |  Create comment |
|  `Comments` | Delete comment  |
|  `Comments` | Update comment  |
|  `Comments` |  Get comment average |
|  `Comments` | Get comment distribution  |
| `Member`  | Apply to meeting  |
| `Member`  | Cancel meeting apply  |
| `Member`  | Quit meeting  |
| `My Page`  |  Get my reviews |
| `My Page` |  Change user profile pic |
| `My Page`  | Get user info for header  |
| `My Page` | Update user contact |
| `My Page` | Update user skill |
| `My Page` | Update user info |
| `My Page` |  Update user password|
| `My Meetings`  | Approve or reject pending member  |
| `My Meetings`  |  Expel existing member |
| `My Meetings`  | Get all my meetings  |
| `My Meetings`  | Get my managing meetings  |
| `My Meetings`  | Get members of one meeting  |
| `My Meetings`  | Change meeting isPublic = false  |
| `My Meetings`  | Get liked meeting  |
| `My Meetings`  | Get user info when applied to meeting(pending user info)  |
| `My Meetings`  |Update meeting info   |
| `My Meetings`  |  Update meeting skill |

## 🖼️ Wireframe & UI/UX Design
> All wireframes are available **[here](https://www.figma.com/design/qTJwDi2MBNwZ7OD1O1ozbx/%ED%8C%801?node-id=2-2&p=f&t=MXrg4nloPskaIf3U-0)**

![Wireframe Screenshot](https://github.com/user-attachments/assets/9fdc1f78-cdbd-459e-b113-34bd8251ea2c)

---

## 🗺️ Database ERD
> **Detailed ERD Progress**: [Check Here](https://project-movie-reservation.notion.site/ERD-1bd5998d72668036927dd1bd4056b71a)

![ERD Screenshot](https://github.com/user-attachments/assets/6b963e39-0fed-4dd6-9747-862c288f124d)

---

## ✨ Key Features
| Meeting search | Meeting Detail Page|
|:---:|:---:|
| Able to user filter and keywords for searching meetings | Get meeting details |
| <img src="https://github.com/user-attachments/assets/52d9255d-dea1-4ea8-8171-04473944407a" alt="meetingSearch" width="450"> | <img src="https://github.com/user-attachments/assets/6253c052-29f3-41f2-98eb-5bbfe8bc50ca" alt="meetingDetail" width="450"> |


| Apply to meetings | Manage meetings and members |
|:---:|:---:|
| Apply to meetings with a short introduction about the user | Manage my meetings and approve, reject, expel members |
| <img src="https://github.com/user-attachments/assets/cd994e45-8db0-423f-a394-043052a2e68d" alt="applyMeeting" width="450"> | <img src="https://github.com/user-attachments/assets/69d4e0f3-d4fd-4bfb-8f84-5c3658f0c641" alt="manageMeeting" width="450"> |

| Update my info | Top 4 Meetings |
|:---:|:---:|
| Change my profile information | Get top 4 meetings with most likes |
| <img src="https://github.com/user-attachments/assets/b9988728-96a3-4de9-9057-4e48b4cb3648" alt="myinfo" width="450"> | <img src="https://github.com/user-attachments/assets/68b2402b-7de7-446f-b106-8537ed686c57" alt="getTop" width="450" />
 |
</br>


## 📹 Demo Video
Watch a sample project video **[on YouTube](https://youtu.be/1YsrKuZVYJI)**

---

## 🏆 Key Achievements
- Successfully delivered this **freelance contract project** for **Code-It Company**.
- Worked **independently** as the **only backend developer**, handling **CI/CD, backend architecture, and development**.
- Built and deployed a scalable backend infrastructure using **Spring Boot, AWS, and MySQL**.

