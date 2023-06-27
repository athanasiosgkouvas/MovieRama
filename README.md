# MovieRama
MovieRama is an Android app that serves as a movie catalog, allowing users to explore popular movies, search for movies, and view detailed information about each movie. The app is built using the MVI (Model-View-Intent) architecture, with various modern libraries and technologies.

# Features
The MovieRama app incorporates the following features:

# Home Screen - Popular Movies
Displays a list of popular movies, including basic information such as the movie poster, title, release date, rating, and favorite status.
Utilizes infinite scrolling to load additional movies as users reach the end of the list.
Allows users to mark movies as favorites, storing the information locally.
Retains the user's position and list state when returning to the app.
Supports pull-to-refresh gesture to refresh the current list.

# Home Screen - Search for Movies
Enables users to search for movies by typing into a search box.
Performs real-time searching as users type, using the TMDB API's search endpoint.
Implements infinite scrolling to load more search results as users approach the end of the list.

# Movie Details Screen
Provides detailed information about a selected movie, including its genre, title, poster, overview, director, cast, similar movies, reviews (up to 2), and favorite status.
Fetches additional data from multiple TMDB endpoints to populate the movie details screen.
Allows users to mark a movie as a favorite directly from the movie details screen.
Offers a navigation option to return to the previous list.

# Additional Technologies Used
The MovieRama app leverages the following technologies and libraries:

- Kotlin - The primary programming language used for development.
- Jetpack Compose - The modern UI toolkit for building declarative user interfaces.
- Koin - A lightweight dependency injection framework for Kotlin.
- Room - A SQLite database library for storing and managing local data.
- Coroutines - Kotlin's native solution for asynchronous and concurrent programming.
- Flow - A reactive streams library for handling asynchronous data streams.
- Retrofit - A type-safe HTTP client for making API calls.
- kotlinx.serialization - A Kotlin library for parsing and serializing JSON objects.

# Development Environment
To set up the development environment and run the MovieRama app:

Clone the repository: git clone https://github.com/athanasiosgkouvas/MovieRama.git
Open the project in Android Studio.
Build and run the app on an emulator or physical device.

# Future Enhancements
There are several areas for potential future enhancements:

Implementing additional features such as user authentication, user reviews, and movie recommendations.
Enhancing the UI with animations, transitions, and visual polish.
Adding unit tests and integration tests

### Note:
Please add apiKey="XXXXXXXXXXXXXXXXXXX" in local.properties for the project to build successfuly. 
