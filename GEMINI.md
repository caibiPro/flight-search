# Project Overview

This is an Android application for searching flights. It is built with modern Android development technologies, including Kotlin, Jetpack Compose for the UI, Hilt for dependency injection, Room for local database storage, and Kotlin Flows for reactive data streams.

The app allows users to search for airports, view available flight routes, and save their favorite routes. The UI is designed to be simple and intuitive, with a single screen that dynamically updates to show search results, flight routes, or favorite routes based on the user's actions.

## Building and Running

To build and run this project, you will need Android Studio and an Android device or emulator.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/flight-search.git
    ```
2.  **Open the project in Android Studio.**
3.  **Build the project:**
    *   Click on **Build > Make Project** in the Android Studio menu.
    *   Alternatively, you can run the following command in the terminal:
        ```bash
        ./gradlew build
        ```
4.  **Run the application:**
    *   Select a run configuration (usually `app`) and a target device in the Android Studio toolbar.
    *   Click on the **Run** button (green play icon).
    *   Alternatively, you can run the following command in the terminal:
        ```bash
        ./gradlew installDebug
        ```

## Development Conventions

*   **Language:** The project is written entirely in Kotlin.
*   **Architecture:** The app follows a clean architecture pattern, with a separation of concerns between the UI (presentation), business logic (domain), and data layers.
*   **UI:** The UI is built with Jetpack Compose, a modern declarative UI toolkit for Android.
*   **Dependency Injection:** Hilt is used for dependency injection, which helps to decouple components and make the code more modular and testable.
*   **Database:** Room is used for local database storage. The database is pre-populated with a list of airports from a SQL file.
*   **Asynchronous Programming:** Kotlin Coroutines and Flows are used for asynchronous programming and managing data streams.
*   **State Management:** The UI state is managed by a `ViewModel`, which exposes a `StateFlow` of the UI state to the UI layer.
*   **Testing:** The project includes unit tests for the `ViewModel` and other components.

## TODO

*   **Write unit tests for `FlightSearchViewModel`:** The existing unit tests were deleted during the refactoring. New tests should be written to cover the new state management logic and business logic.
*   **Improve database pre-population:** The airport data is currently hardcoded in `FlightDatabaseCallback.kt`. This should be moved to a separate file (e.g., a JSON or CSV file) to make it easier to update.
*   **Use a lifecycle-aware `CoroutineScope` in `FlightDatabaseCallback`:** The current implementation uses a global `CoroutineScope`, which can lead to memory leaks. A lifecycle-aware `CoroutineScope` should be injected and used instead.
*   **Add a `Loading` state to `FlightScreenState`:** The UI should have a dedicated loading state to represent the initial loading state of the screen.
*   **Refactor the `onEvent` function in `FlightSearchViewModel`:** The `onEvent` function is a bit long and could be broken down into smaller, more focused functions.
*   **Improve error handling in `createRoutesState`:** The error handling in the `createRoutesState` function is a bit basic. It should be improved to show more specific error messages to the user.