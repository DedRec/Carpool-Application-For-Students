# Carpooling App (Client Side)

The Carpooling App is an Android application designed to facilitate carpooling services for users. It allows users to find and book rides, manage their profile, and interact with other users through a simple and intuitive interface.

## Features

- **User Authentication:** Users can sign up and log in to access the app's features.
- **Trip Booking:** Users can browse available trips, book rides, and make payments securely.
- **Profile Management:** Users can view and update their profile information.
- **Order History:** Users can view their past ride orders.
- **Firebase Integration:** The app integrates with Firebase for authentication, database, and storage functionalities.
- **Sorting:** Trips are sorted based on time for easy browsing.

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture and is structured as follows:

- **`app/src/main/java/com/example/carpool`:** This directory contains the Java source code for the app.
  - **`activity`:** Contains the activity classes for different screens of the app.
  - **`adapters`:** Contains RecyclerView adapters for displaying trip items and order history.
  - **`helpers`:** Contains helper classes for managing orders, trips, and user authentication.
  - **`items`:** Contains model classes for trip items and order items.
  - **`model`:** Contains the repository, model, and data source classes.
  - **`viewmodel`:** Contains the view model classes for managing UI-related data.
- **`app/src/main/res`:** Contains the app's resources, including layouts, strings, and drawables.
- **`app/src/test/java/com/example/carpool`:** Contains unit tests for the app.

## Setup

To run the Carpooling App:

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Connect your Android device or use an emulator.
4. Build and run the project.

Ensure that you have the necessary dependencies and Firebase configurations set up before running the app.

## Dependencies

The app uses the following dependencies:

- Firebase Authentication
- Firebase Realtime Database
- RecyclerView

Make sure to include these dependencies in your `build.gradle` file.

## Driver App

The driver side application is located in the [DriverApp branch](https://github.com/DedRec/Carpool-Application-For-Students/tree/DriverApp). It provides functionalities for drivers to manage their trips, accept bookings, and track payments.
