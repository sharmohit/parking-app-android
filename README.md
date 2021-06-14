# 🅿️arking App (Android)
&nbsp;
## App Requirements
Create an Android application for the **Parking App** with the following functionalities:
### User Profile  
Your app must provide appropriate interface to sign-up, sign-in, sign-out, update profile and delete account. Consider requesting name, email, password, contact number and car plate number from user when they create their account.
If you are inclined and interested in adding profile picture functionality, you may add that in your app. However, it is not the official requirement of the project. No additional grades will be awarded or deducted for including (or not including) this functionality.
### Add Parking
The add parking facility should allow the user to create a new parking record with the following information.
information.
- Building code (exactly 5 alphanumeric characters)
- No. of hours intended to park (1-hour or less, 4-hour, 12-hour, 24-hour)
- Car License Plate Number (min 2, max 8 alphanumeric characters)
- Suit no. of host (min 2, max 5 alphanumeric characters)
- Parking location (street address, lat and lng)
- date and time of parking (use system date/time)
  
You should allow the user to input the parking location in two ways:
- enter street name [In this case the app should obtain location coordinates using geocoding]
- use current location of the device [In this case the app should use reverse geocoding to obtain street address]

**Note:**
- The user can have a single car plate in their profile but they must be able to add parking with a different car plate.
- Multiple user can use same car plate number when adding a parking.
  
After accepting and verifying all information, all parking information must be saved to database. You must use either Room DB or Cloud Firestore to save the records. When adding the parking information in the database, make sure that you associate the record with the currently logged in user.
### View Parking
This facility will allow the user to view the list of all the parking they have made. You should display the list with most recent parking first. You should also display the detail view of each parking when user taps on any item of the list. When displaying detail view, display all the information about the parking in appropriate format. In the detail view of parking, allow the user to open the parking location on map.
### Edit Parking
User must be allowed to edit the parking information except datetime of parking. Also, the app must allow user to delete any parking from the list.
##
&nbsp;
## Task Lists
- [x] 1. Create Sign-In Screen @MohitSharma(101342267)
  - [x] Design sign-in layout
  - [x] Implement sign-in with remember me
  - [x] Validate user
  - [x] Add navigation to sign-up and home screens

- [ ] 2. Create Sign-Up Screen @JavteshSinghBhullar(101348129)
  - [ ] Design sign-up layout
  - [ ] Validate user input
  - [ ] Implement sign-up
  - [ ] Add navigation to sign-in and home screens

- [x] 3. Configure Firestore Console and Firebase in Project @MohitSharma(101342267)
  - [x] Create firebase app and configure firestore in console
  - [x] Add GoogleService-Info.plist in Android project
  - [x] Add firebase gradle dependencies
  - [x] Test firestore with test data

- [ ] 4. Create Update Profile Screen @JavteshSinghBhullar(101348129)
  - [ ] Design update profile screen
  - [ ] Fetch user input
  - [ ] Validate user input
  - [ ] Update user profile with firestore

- [ ] 5. Create Home and Parking View Screen @MohitSharma(101342267)
  - [x] Design home and parking view screen
  - [x] Implement Jetpack Navigation with fragments
  - [x] Create home screen with bottom navigation
  - [x] Implement recycler view for view parking screen
  - [x] Fetch user parking from firestore
  - [ ] Link parkings with parking detail screen
 
- [ ] 6. Create Parking Detail View Screen @JavteshSinghBhullar(101348129)
  - [ ] Design parking detail view screen
  - [ ] Fetch parking detail from firestore
  - [ ] Display parking detail
  
- [x] 7. Create Add Parking Screen @MohitSharma(101342267)
  - [x] Design add parking screen
  - [x] Fetch and convert user location
  - [x] Validate user input
  - [x] Add parking to firestore

- [x] 8. Create location helper @MohitSharma(101342267)
  - [x] Create MapView Screen
  - [x] Configure google map api 
  - [x] Implement location controller
  - [x] Implement reverse geocoding
  - [x] Implement location geocoding
##
