# VHAL Params Viewer for Android Automotive.

An example of diagnostic tool for Android Automotive OS (AAOS) to monitor Vehicle Hardware Abstraction Layer (VHAL) properties in real-time. Built with a focus on modular architecture and modern Android stack.

## Warning!
You can use this app with real vehicles only when it's parked. AAOS restricts using app while driving.

## Screenshots
info data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/e92ddf1e-31e6-4458-b9ec-0396330d253e" />


Main driving data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/9ef49363-8ddf-4200-b68e-227d432e39d3" />

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/a8156dcc-904c-4e9b-beab-58fe1fd2b6ae" />



Fuel and energy data:

<img width="1408" height="792" alt="image" src="https://github.com/user-attachments/assets/44b455b0-a7f2-4906-84ad-34c79b431c1f" />

Switching between mock and real data:



https://github.com/user-attachments/assets/e520d290-50e4-4d38-9823-8b5a0f70363a








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
