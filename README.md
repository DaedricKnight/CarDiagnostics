# VHAL Params Viewer for Android Automotive.

An example of diagnostic tool for Android Automotive OS (AAOS) to monitor Vehicle Hardware Abstraction Layer (VHAL) properties in real-time. Built with a focus on modular architecture and modern Android stack.

## Warning!
You can use this app with real vehicles only when it's parked. AAOS restricts using app while driving.

## Screenshots
info data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/bc9adf1d-92e0-4662-8e1c-f9f536c13da2" />


Main driving data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/631128ba-9f2c-4ca8-a080-3b889e1cf9df" />


<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/75927c08-e2b1-4f32-8076-d81d20d0e3af" />



Fuel and energy data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/1f77d63b-0f51-40cd-a3e3-7e9d063b4591" />


Switching between mock and real data:





https://github.com/user-attachments/assets/f2ef6825-503a-479b-aa99-4442184ac19d









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
