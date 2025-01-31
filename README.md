# Getting Started

This guide will walk you through the steps to clone this repository from GitHub and run it in Android Studio. This project uses Google Sign-In with Firebase Authentication and fetches space station information using an external API.

## Prerequisites

Before you begin, ensure you have the following installed and configured:

* **Android Studio:**  Make sure you have the latest stable version of Android Studio installed on your machine. You can download it from [https://developer.android.com/studio](https://developer.android.com/studio).
* **Git (Optional but Recommended):** While Android Studio has built-in Git support, having Git installed separately can be beneficial. You can download it from [https://git-scm.com/](https://git-scm.com/).
* 
## Cloning the Repository

There are two main ways to clone the repository:

**1. Using Android Studio's built-in Git:**

   * **Open Android Studio:** Start Android Studio.
   * **Check out project from Version Control:**  On the welcome screen, click "Get from Version Control" or if you already have a project open, go to *File -> New -> Project from Version Control*.
   * **Select Git:** Choose "Git" from the options.
   * **Enter Repository URL:** Paste the repository URL (`https://github.com/rasel003/SpaceStation.git`) into the URL field.
   * **Choose Directory:** Select the local directory where you want to clone the repository.
   * **Click Clone:** Click the "Clone" button.  Android Studio will now clone the repository to your local machine.

**2. Using the command line (Git Bash, Terminal, etc.):**

   * **Open your terminal:** Open your terminal or Git Bash.
   * **Navigate to Directory:** Navigate to the directory where you want to clone the repository using the `cd` command (e.g., `cd /path/to/your/projects/directory`).
   * **Clone the Repository:** Use the following command,

     ```bash
     git clone https://github.com/rasel003/SpaceStation.git
     ```

## Opening the Project in Android Studio

1. **Open Android Studio:** Launch Android Studio.
2. **Open an Existing Project:** On the welcome screen, click "Open an Existing Project" or if you already have a project open, go to *File -> Open*.
3. **Navigate to the Project Directory:** Browse to the directory where you cloned the repository. Select the project's root folder (the folder containing the `build.gradle` file).
4. **Click Open:** Click "Open".

## Building and Running the App

1. **Gradle Sync:** Android Studio will automatically start a Gradle sync.  This might take a while the first time. Make sure you have a stable internet connection.  Address any Gradle sync errors that may arise.
2. **Connect a Device or Emulator:** Connect your Android device to your computer or start an Android emulator.
3. **Run the App:** Click the "Run" button (the green play icon) in the toolbar.  Select your connected device or emulator.

## Troubleshooting

* **Gradle Sync Issues:** If you encounter Gradle sync errors, try the following:
    * Check your internet connection.
    * Ensure you have the correct Android SDK and Build Tools versions installed in the SDK Manager (Tools -> SDK Manager).
    * Try cleaning and rebuilding the project (Build -> Clean Project, then Build -> Rebuild Project).
    * Check the Gradle files (`build.gradle`) for any errors.
* **Emulator Issues:** If you have problems with the emulator, try creating a new emulator or restarting the existing one.
* **Other Issues:** If you encounter other issues, please refer to the Android Studio documentation, Firebase documentation, the API documentation, or search online forums for solutions.
