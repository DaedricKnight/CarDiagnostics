# VHAL Params Viewer for Android Automotive.

An example of diagnostic tool for Android Automotive OS (AAOS) to monitor Vehicle Hardware Abstraction Layer (VHAL) properties in real-time. Built with a focus on modular architecture and modern Android stack.

## Warning!
You can use this app with real vehicles only when it's parked. AAOS restricts ising app while driving.

## Screenshots
info data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/3662fe75-8160-4c9e-9a5c-a07e39839af6" />


Main driving data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/9db8a84f-3c71-4aca-acc7-e971dc81162c" />


Fuel and energy data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/d69dda44-0a75-4ece-85ca-da69376c31cd" />





## Tech Stack
* **Language:** Kotlin 2.1.0+ (K2 Compiler)
* **UI:** Jetpack Compose (BOM 2024.10.00)
* **DI:** Dagger Hilt (Multi-module setup)
* **Architecture:** Clean Architecture + MVI/MVVM
* **Platform:** Android Automotive OS (API 29+)

## Architecture
The project is split into two main modules:
* `:app` - UI layer and Hilt Entry Point.
* `:core-vhal` - Independent library for interacting with `CarPropertyManager`.

## Key Features
* **Real-time Monitoring:** Tracking RPM, Speed, Gear Selection, and more.
* **Safety Diagnostics:** ABS status, Traction Control, and Tire Pressure.
* **Static Info:** Automatic retrieval of VIN, Fuel Capacity, and Model Year.
* **Modular Design:** The VHAL library can be easily extracted and used in other automotive projects.

## Installation & Setup

1. **Clone the repo:**
   ```bash
   git clone [https://github.com/your-login/vhal-params-viewer.git](https://github.com/your-login/vhal-params-viewer.git)
