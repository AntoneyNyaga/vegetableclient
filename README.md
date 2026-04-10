
- **app/** – Contains Android app code and resources  
- **gradle/** – Gradle configuration files  
- **build.gradle.kts** – Main Gradle build file

---

## 🚀 Features (Example)

✔ Displays a list of vegetables  
✔ UI screens with basic navigation  
✔ Built with Android best practices  
✔ Modular Gradle configuration  

> You can expand it with backend integration, local database, or additional screens.

---

## 🛠 Getting Started

### Prerequisites

Make sure you have the following installed on your system:

- Android Studio (latest stable)
- Java JDK 11+
- Android SDK

---

**VegetableClient** 
Is the Android front-end of a simple distributed mobile application. The app communicates with a remote **Java RMI-based server** (exposed through HTTP servlets running on Tomcat) to manage vegetable pricing and generate purchase receipts.

The system is split across two repositories:

**Mobile Client (this repo)** | [vegetableclient](https://github.com/AntoneyNyaga/vegetableclient) | Android app — sends HTTP requests to the server 

**Server Engine** | [vegetable_service_engine](https://github.com/leah25/vegetable_service_engine) | Java RMI + Tomcat servlets — processes tasks and returns results |

---

## Architecture

```
┌─────────────────────────────────────┐        HTTP POST
│         Android App (Client)        │ ──────────────────────▶ ┌──────────────────────────┐
│                                     │                          │    Tomcat Web Server     │
│  - Activity / UI Layer              │                          │  (vegetable_service_engine) │
│  - HTTP Requests (HttpURLConnection │ ◀────────────────────── │                          │
│    or Volley/Retrofit)              │     Plain Text Response  │  Servlets → RMI Engine   │
└─────────────────────────────────────┘                          │  → VegetablePriceTable   │
                                                                  └──────────────────────────┘
```

The Android client sends `HTTP POST` requests to servlet endpoints. The server processes each request using the Java RMI compute engine and returns a plain text response.




**API Endpoints**

All endpoints accept `HTTP POST` requests. The server must be running and accessible over the network.

| Endpoint | Parameters | Description |
|----------|-----------|-------------|
| `POST /vegetable/add` | `id`, `name`, `price` | Add a new vegetable |
| `POST /vegetable/update` | `id`, `name`, `price` | Update an existing vegetable |
| `POST /vegetable/delete` | `id` | Delete a vegetable |
| `POST /vegetable/cost` | `id`, `quantity` | Calculate cost for a given quantity (kg) |
| `POST /vegetable/receipt` | `items`, `amountGiven`, `cashierName` | Generate a full purchase receipt |


## ✨ Features

- 🥕 **Add** a new vegetable with its price per kg
- ✏️ **Update** an existing vegetable's price
- 🗑️ **Delete** a vegetable from the price table
- 💰 **Calculate Cost** for a single vegetable by quantity
- 🧾 **Generate Receipt** — multi-item purchase receipt with totals, change due, and cashier name




### Run the Project

1. Clone the repo  

   ```bash
   git clone https://github.com/AntoneyNyaga/vegetableclient.git
   
2. Server side Link

   Kindly find the Backened(Server side), done by team-mate in this repo: 
   https://github.com/leah25/vegetable_service_engine
