# Driver Carpool Application

This repository contains the source code for the Driver side of a carpooling application. The application allows drivers to manage their trips, handle trip requests, and view their trip history.

## Overview

The Driver side application consists of several Java classes and layout XML files organized into packages. Here's a brief overview of the main components:

### Activities

1. **MainActivity**: The main activity of the application, responsible for navigation between home, profile, and settings fragments.
2. **SignupActivity**: Activity for user registration, allowing drivers to sign up with their name, email, username, and password.
3. **TrackTripActivity**: Activity to track ongoing trips, allowing drivers to start, complete, or cancel trips.
4. **TripHistoryActivity**: Activity to view the history of completed or canceled trips.

### Fragments

1. **HomeFragment**: Fragment for displaying home screen content.
2. **ProfileFragment**: Fragment for displaying user profile information and handling sign-out functionality.
3. **SettingsFragment**: Fragment for displaying settings options, including trip and request history.

### Adapters

1. **RequestAdapter**: Adapter for displaying trip requests in a RecyclerView.
2. **RequestHistoryAdapter**: Adapter for displaying trip request history in a RecyclerView.
3. **StatusAdapter**: Adapter for displaying ongoing trips in a RecyclerView.
4. **TripHistoryAdapter**: Adapter for displaying trip history in a RecyclerView.

### Helpers

1. **HelperTrip**: Helper class to represent trip data.
2. **TimeComparator**: Comparator class for sorting trip items based on time.
3. **TimeComparatorHelperTrip**: Comparator class for sorting helper trip items based on time.

### Model

1. **User**: Model class representing user data.

### Other Files

1. **FirebaseDB**: Class for interacting with Firebase Realtime Database.
2. **UserViewModel**: ViewModel class for managing user data using Android Architecture Components.

## Installation

To run the application, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the application on an emulator or physical device.

Ensure you have the necessary dependencies configured, including Firebase setup for database interactions.