# DiscoverEat

[![Status](https://img.shields.io/badge/Status-Completed-success)](#)
[![Java](https://img.shields.io/badge/Java-11%2B-red)](#)
[![Android](https://img.shields.io/badge/Platform-Android-green)](#)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%7C%20Repository-blueviolet)](#)
[![License](https://img.shields.io/badge/License-Educational--Only-lightgrey)](#)

---

## 📖 Overview
**DiscoverEat** is a Java-based Android application designed to help users explore restaurants around the world.  
By entering a city or location, users can view a list of restaurants with detailed information such as:
- Address  
- Price range  
- Average rating  
- Phone number  

Additionally, users can save restaurants to their **Favorites** list for quick access anytime.

---

## 🏗️ Architecture
The application follows an **MVVM + Repository** pattern:
- **UI** → Handles presentation logic  
- **ViewModel** → Mediates between the Repository and UI  
- **Repository** → Interfaces with external APIs (Yelp Fusion)  

---

## ⚙️ Features
- 🔎 **Search restaurants** by city or location  
- ⭐ **Save favorites** and manage them easily  
- 📋 **View restaurant details** (address, rating, price, phone)  
- 📞 **Direct interaction** → call the restaurant or open maps for directions  
- 🔐 **Authentication** with Firebase (Email/Password & Google Sign-In)  

---

## 📚 Technologies & Libraries
- **Java 11+**  
- **Android SDK**  
- **Firebase Authentication** (login & registration)  
- **Yelp Fusion API** (restaurant data)  
- **Retrofit** → HTTP client for API requests  
- **Picasso** → image loading and caching  
- **GSON** → JSON serialization/deserialization  

---

## 🚀 Future Improvements
- Search by cuisine, type of restaurant, or user reviews  
- Integration of menus, opening hours, and booking features  
- Table reservations directly in-app  
- Notifications & reminders for bookings  

---

## 👨‍💻 Author
**Sandu Stoicov**  
Bachelor in Computer Science – Università degli Studi di Milano-Bicocca  

---

## 📑 License
This project is released for educational purposes only.  
No commercial use intended.
