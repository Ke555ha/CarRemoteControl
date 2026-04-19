# RC Car / Lift Controller (Simple)

Simple Spring Boot application for programmatic control of a mini-lift / RC car via an Android app using Appium and multi-touch gestures on a real Android phone connected via USB.

**This version has no REST API.** Control is executed directly from Java code when the application starts.

## What It Does

The application:
- Connects to Appium
- Creates a session on a real Android phone
- Sends multi-touch commands to an Android app
- Controls:
  - Forward movement
  - Reverse movement
  - Forward/backward with left/right steering
  - Lift up/down

## How It Works

**Workflow:**
```
Java / Spring Boot → Appium → Android Phone (via USB) → Android Control App → Equipment
```

Standard `adb shell input` cannot reliably perform two simultaneous touches. Therefore, this project uses:
- Appium
- UiAutomator2
- W3C Actions
- Real Android phone with USB debugging enabled

## Requirements

### On the Computer

- macOS
- Homebrew
- Node.js
- npm
- ADB
- Appium
- Appium UiAutomator2 Driver
- Android Studio / Android SDK
- Java 17
- Gradle

### On the Phone

- Android smartphone
- USB cable
- Installed equipment control app
- Developer mode enabled
- USB debugging enabled

---

## Setup Guide

### 1. Setting Up Mac from Scratch

#### 1.1 Install Homebrew

Open Terminal and run:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Verify:

```bash
brew --version
```

If `brew` is not found, add it to PATH:

```bash
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

Verify again:

```bash
brew --version
```

#### 1.2 Install Node.js and npm

```bash
brew install node
```

Verify:

```bash
node -v
npm -v
```

#### 1.3 Install ADB

ADB is required even when using Appium, because Appium for Android works on top of ADB.

Install:

```bash
brew install android-platform-tools
```

Verify:

```bash
adb version
```

#### 1.4 Install Appium

```bash
npm install -g appium
```

Verify:

```bash
appium -v
```

#### 1.5 Install UiAutomator2 Driver

```bash
appium driver install uiautomator2
```

Verify:

```bash
appium driver list --installed
```

`uiautomator2` should appear in the installed drivers list.

#### 1.6 Install Android Studio and Android SDK

1. Download Android Studio
2. Install it
3. Launch it for the first time
4. Install:
   - Android SDK
   - Platform Tools
   - Build Tools

SDK is typically installed to: `~/Library/Android/sdk`

Verify:

```bash
ls ~/Library/Android/sdk
```

You should see folders:
- `platform-tools`
- `platforms`
- `build-tools`

#### 1.7 Configure Environment Variables

Add to `~/.zshrc`:

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export ANDROID_SDK_ROOT=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$ANDROID_HOME/cmdline-tools/latest/bin
```

Apply changes:

```bash
source ~/.zshrc
```

Verify:

```bash
echo $ANDROID_HOME
echo $ANDROID_SDK_ROOT
adb version
```

#### 1.8 Install Java 17

```bash
brew install openjdk@17
```

Verify:

```bash
java -version
javac -version
```

If Homebrew shows commands to add Java to PATH, execute them.

#### 1.9 Install Gradle

```bash
brew install gradle
```

Verify:

```bash
gradle -v
```

---

### 2. Phone Setup

#### 2.1 Enable Developer Mode

On Android:
1. Open **Settings**
2. Open **About Phone**
3. Tap **Build Number** multiple times
4. Wait for the message that Developer Mode is enabled

#### 2.2 Enable USB Debugging

On Android:
1. Open **Settings**
2. Open **Developer Options**
3. Enable **USB Debugging**

#### 2.3 Connect Phone to Mac

1. Connect phone via USB
2. On the phone, confirm the prompt:
   - **Allow USB debugging**
3. If prompted "Always allow from this computer" — you can enable it

#### 2.4 Open the Equipment Control App

Before launching:
- Open the app manually
- Navigate to the control screen
- Ensure the following are visible:
  - Throttle
  - Steering
  - Lift buttons

#### 2.5 Do Not Change Orientation Programmatically

If the app itself locks to landscape, do not change orientation via Appium.

---

### 3. Determine Phone UDID

**Option 1: Via `adb devices`**

Connect phone via USB and run:

```bash
adb devices
```

Example output:

```
List of devices attached
343776544200EIQ	device
```

`343776544200EIQ` is the **phone UDID**.

**Option 2: Via `adb get-serialno`**

```bash
adb get-serialno
```

> **Important:** UDID is the identifier of the **phone**, not the car/lift. If you change equipment but keep the same phone, do **not** change the UDID. If you change the phone, get a new UDID via `adb devices`.

---

### 4. Verify Phone Connection

Run:

```bash
adb devices
```

The phone should appear with status `device`:

```
List of devices attached
343776544200EIQ	device
```

If the list is empty or status is `unauthorized`, check:
- USB cable
- USB debugging is enabled
- Access is confirmed on the phone

---

### 5. Control Coordinates

**Throttle:**
- Neutral: `(460, 460)`
- Forward max: `(460, 330)`
- Reverse max: `(460, 660)`

**Steering:**
- Neutral: `(1920, 460)`
- Left: `(1720, 460)`
- Right: `(2050, 460)`

**Lift:**
- Up: `(700, 980)`
- Down: `(950, 980)`

---

### 6. Project Configuration

#### 6.1 Specify UDID in Config

Open:

```
src/main/resources/application.yml
```

Set your UDID:

```yaml
appium:
  url: http://127.0.0.1:4723
  udid: 343776544200EIQ
  no-reset: true
  new-command-timeout-seconds: 120
```

Replace `343776544200EIQ` with your phone's UDID.

#### 6.2 Verify Java and Gradle

```bash
java -version
gradle -v
```

---

### 7. Simple Project Structure

```
rc-car-controller-simple/
├── README.md
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── rccar/
        │               ├── RcCarControllerApplication.java
        │               ├── config/
        │               │   └── AppiumProperties.java
        │               ├── model/
        │               │   ├── CarCommandType.java
        │               │   └── CarPoint.java
        │               └── service/
        │                   └── CarControllerService.java
        └── resources/
            └── application.yml
```

---

### 8. How to Run the Full Setup

1. **Connect phone via USB**

2. **Verify phone settings:**
   - Developer mode enabled
   - USB debugging enabled
   - USB access confirmed

3. **Check phone visibility:**

   ```bash
   adb devices
   ```

4. **Open the equipment control app**

   Manually open the control screen on the phone.

5. **Start Appium**

   In a separate terminal:

   ```bash
   ANDROID_HOME=$HOME/Library/Android/sdk ANDROID_SDK_ROOT=$HOME/Library/Android/sdk appium
   ```

   After startup, Appium is typically available at: `http://127.0.0.1:4723`

6. **Run Spring Boot Application**

   From the project root:

   ```bash
   ./gradlew bootRun
   ```

   or:

   ```bash
   gradle bootRun
   ```

---

### 9. How the Simple Version Works

The simple version has no REST API. The scenario is defined directly in Java code, typically in:

- `RcCarControllerApplication.java`
- or `CarControllerService.java`

**Example sequence:**
- Move forward 500 ms
- Move forward-left 300 ms
- Move backward 200 ms
- Move backward-right 100 ms
- Lift up 300 ms
- Lift down 300 ms

---

### 10. Example Launch Logic

Approximate order inside the application:
1. Connect to Appium
2. Execute command sequence
3. Complete execution

---

### 11. Full Launch Checklist

1. Connect phone via USB
2. Enable Developer Mode on phone
3. Enable USB Debugging on phone
4. Confirm USB Debugging access
5. Run: `adb devices`
6. Ensure phone is visible
7. Open equipment control app on phone
8. Start Appium:

   ```bash
   ANDROID_HOME=$HOME/Library/Android/sdk ANDROID_SDK_ROOT=$HOME/Library/Android/sdk appium
   ```

9. In another terminal, run Spring Boot:

   ```bash
   ./gradlew bootRun
   ```

After this, the Java application should execute the scenario defined in the code.

---

### 12. Useful Commands

**Check Homebrew:**

```bash
brew --version
```

**Check Node.js and npm:**

```bash
node -v
npm -v
```

**Check ADB:**

```bash
adb version
adb devices
```

**Determine UDID:**

```bash
adb devices
```

or:

```bash
adb get-serialno
```

**Check screen size:**

```bash
adb shell wm size
```

**Check Appium:**

```bash
appium -v
```

**Check installed Appium drivers:**

```bash
appium driver list --installed
```

---

### 13. Troubleshooting

**Phone not visible in `adb devices`**

Check:
- USB cable
- USB debugging is enabled
- Access is confirmed on phone
- USB port is not faulty

**Status `unauthorized`**

Look at the phone screen and confirm USB debugging permission.

**Appium cannot find Android SDK**

Check:

```bash
echo $ANDROID_HOME
echo $ANDROID_SDK_ROOT
```

**`brew` not found**

Add Homebrew to PATH:

```bash
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

**`npm` not found**

Install Node.js:

```bash
brew install node
```

**Commands fail**

Check:
- Is Appium running?
- Is the correct app open on the phone?
- Is the UDID correct?
- Are coordinates correct?
- Is the app on the correct screen?

**One of the controls doesn't work**

Check:
- Coordinates
- App orientation
- Whether the app interface changed after equipment swap

---

## Summary

After proper setup, the workflow is:

```
Spring Boot App → Appium → Android via USB → Android App → Movement and Lift Commands
```

This allows running pre-defined control scenarios directly from Java code.
