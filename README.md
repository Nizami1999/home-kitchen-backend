# 🏡 KiHome Kitchen 🍽️

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![React](https://img.shields.io/badge/React-18.2-blue)](https://reactjs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

A modern web application for ordering **home-cooked dishes online**, built with **Java Spring Boot**, **PostgreSQL**, and **React**.

---

## 🌟 Features

### 🥘 Dishes
- CRUD operations for dishes
- Categories with `sortOrder` and `isActive` flags
- Dish properties:
  - ⭐ `isFeatured`, 🥦 `isVegetarian`, 📝 `ingredients`, 🔥 `calories`
  - 🖼️ `galleryImages`, 🎥 `videoUrl`
  - ⭐ `averageRating`, 💰 `discountPrice`
- Dish snapshots stored for **historical price accuracy**

### 🛒 Orders
- Create and manage customer orders
- Tracks:
  - 👤 `customerName`, 🏠 `customerAddress`, 📧 `customerEmail`, 📱 `customerPhone`
  - 🔢 `quantity`, 🥘 `dish`, 💵 `totalPrice`
  - 📦 `status` (`PENDING`, `PREPARING`, `DELIVERED`, `CANCELLED`)
  - 💳 `paymentStatus` (`PENDING`, `PAID`, `FAILED`) and `paymentMethod`
  - 📝 `specialInstructions` and ⏰ `deliveryTime`
- Soft deletes via `isActive` flag
- Stores 🕒 `createdAt` / `updatedAt` timestamps

### 🔧 Other Features
- 📄 Pagination for dishes and orders
- ⚡ Caching for frequently requested data
- 📦 Bulk creation endpoints
- ✅ Validation on all requests

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| 💻 Backend | Java 17, Spring Boot, Spring Data JPA, Hibernate |
| 🗄️ Database | PostgreSQL |
| 🎨 Frontend | React + Tailwind CSS |
| 🖼️ Storage | Supabase / Cloudinary for images & videos |
| ⚡ Caching | Spring Cache (dish count caching) |
| 🚀 Deployment | Railway ($5 Hobby) or Fly.io (Docker-based) |

---
