# android-app-gitify

## Description
A simple android application for signing in with user's GitHub account and displaying the profile data and repositories of the GitHub user.

## Features
- SignIn/SignOut using GitHub OAuth
- Share GitHub Profile
- View user's own profile info
- View and Direct to user's GitHub Repositories
- Material3 Dynamic Theme (Android 12 and above)
- Offline support using cached data after initial data load 

## Android APK
https://github.com/rohan09-raj/android-app-gitify/releases/download/v1.0/app-release.apk

## Activities
 SignIn Activity | Profile Activity | Repository Activity      
:-------------------------:|:-------------------------:|:-------------------------:
![signin](https://user-images.githubusercontent.com/78433013/223970556-dcb527a2-3a81-420f-8418-82bafe9965f6.png) | ![profile](https://user-images.githubusercontent.com/78433013/223970566-863a566c-6d69-44f5-97f3-c8470e696107.png) | ![repos](https://user-images.githubusercontent.com/78433013/223970571-7f1f39af-bc4e-4834-880d-0eb80580ea94.png)

## Android App in Action
https://user-images.githubusercontent.com/78433013/223974902-dc695772-258d-4d4c-aff7-174655c29cc0.mp4

## Dependencies Used
- Glide
- RoomDB
- Retrofit + GSON
- ViewModel and LiveData
- Dagger Hilt

## How to setup locally?
- Clone the repository to your local machine
- Create a GitHub OAuth App
- Add the following details while creating the OAuth App
```
Application name: Gitify
Homepage URL: http://localhost
Authorization callback URL: gitify://callback
```
- Create a apikey.properties file in the root directory and add the following lines
```
GITHUB_CLIENT_ID="<YOUR_GITHUB_CLIENT_ID>"
GITHUB_CLIENT_SECRET="<YOUR_GITHUB_CLIENT_SECRET>"
```
- Build and Run the app
