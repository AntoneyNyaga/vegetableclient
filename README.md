
- **app/** – Contains Android app code and resources  
- **gradle/** – Gradle configuration files  
- **build.gradle.kts** – Main Gradle build file

---

## Features (Example)

✔ Displays a list of vegetables  
✔ UI screens with basic navigation  
✔ Built with Android best practices  
✔ Modular Gradle configuration  

> You can expand it with backend integration, local database, or additional screens.

---

## Getting Started

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


## Features

- **Add** a new vegetable with its price per kg
- **Update** an existing vegetable's price
- **Delete** a vegetable from the price table
- **Calculate Cost** for a single vegetable by quantity
- **Generate Receipt** — multi-item purchase receipt with totals, change due, and cashier name
  

## Prerequisites

Before running this app, make sure you have:

- **Android Studio** (latest stable release)
- **JDK 11+**
- **Android SDK** (API level compatible with the project's `minSdk`)
- The **backend server** up and running — see [vegetable_service_engine](https://github.com/leah25/vegetable_service_engine)


---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/AntoneyNyaga/vegetableclient.git
cd vegetableclient
```

### 2. Open in Android Studio

1. Launch **Android Studio**
2. Select **File > Open** and navigate to the cloned folder
3. Let Gradle sync complete

### 3. Configure the server address

Find the file(s) where the server URL / IP address is set (e.g., a constants file or inside the Activity making HTTP calls) and update it to match your server's IP:

```java
// Example — update to your server's IP address
private static final String BASE_URL = "http://192.168.x.x:8080/VegetableRMI";
```

> **Tip:** If running on a local network, use the server machine's local IP. If running on an emulator pointing to your own machine, use `http://10.0.2.2:8080/VegetableRMI`.

### 4. Start the backend server

Refer to the [vegetable_service_engine README](https://github.com/leah25/vegetable_service_engine) for full instructions. In short:

1. Open the server project in IntelliJ IDEA
2. Run `VegetableComputeEngine.main()` — starts the RMI registry on port **1099**
3. Deploy the WAR to Tomcat (or use the embedded Tomcat run config)
4. Confirm both services are active before launching the Android app

### 5. Run the app

- Connect an **Android device** (with USB debugging enabled) or start an **Android Emulator**
- Click **Run ▶** in Android Studio

---
