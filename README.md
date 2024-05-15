# Carpooling App (Driver Side)

The Carpooling App (Driver Side) is an Android application designed for drivers to manage their trips, accept bookings, and track payments. It complements the client side of the app by providing essential functionalities for drivers.

## Features

1. **Trip Management**: Drivers can manage their trips, start, complete, or cancel trips as needed.
2. **Booking Acceptance**: Drivers can accept or reject ride requests from users.
3. **Order History**: Drivers can view their past trip orders and payment history.
4. **Firebase Integration**: The app integrates with Firebase for authentication, database, and storage functionalities.
5. **Sorting**: Trips are sorted based on time for easy management and tracking.

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture and is structured as follows:

- **app/src/main/java/com/example/drivercarpool**: This directory contains the Java source code for the app.
  - **activity**: Contains the activity classes for different screens of the app.
  - **adapters**: Contains RecyclerView adapters for displaying trip items and order history.
  - **helpers**: Contains helper classes for managing orders, trips, and user authentication.
  - **items**: Contains model classes for trip items and order items.
  - **model**: Contains the repository, model, and data source classes.
  - **viewmodel**: Contains the view model classes for managing UI-related data.
  
- **app/src/main/res**: Contains the app's resources, including layouts, strings, and drawables.

- **app/src/test/java/com/example/drivercarpool**: Contains unit tests for the app.

## Setup

To run the Carpooling App (Driver Side):

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

Make sure to include these dependencies in your build.gradle file.

## Client App

The client side application is located in the main branch. It provides functionalities for users to find and book rides, manage their profile, and interact with other users.

For more information, refer to the README file in the master branch.

