# campus-guide
Concordia Campus Guide application - SOEN390


## Objective
Create a campus guide application to help students locate their classrooms.

## Team Members

| Name                    |Student ID| 
|:------------------------|:--------:|
| Felix Lapierre          | 40057409 |
| Rebecca (Jun) Loke      | 40025838 |
| Brianne O'Gallagher-Roy | 40058629 |
| Mike Brichko            | 40059723 |
| Jon Zlotnik             | 40030143 |
| Sharon Chee Yin Ho      | 40044575 |
| François LaBerge        | 40062066 |
| Anissa Kouki            | 40032516 |
| Maxim Pobudzey          | 40004308 |
| Jacob Guirguis          | 40062827 |

## Installation Instructions
1. Download and install [Android Studio](https://developer.android.com/studio)
1. [Get an API key for Google Maps](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
1. Clone the repository onto your computer
1. Create a file `app\src\main\res\values\google_maps_api.xml`
1. Write the following to the file: `<resources><string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">*API_KEY_GOES_HERE*</string></resources>`
1. Obtain a `credentials.json` file by [configuring a Google API Console project](https://developers.google.com/identity/sign-in/android/start-integrating#configure_a_project) to enable login and put the file in the `app` level/directory of the project
1. For the above `credentials.json` file, the package name is `com.example.campusguide` and instructions on how to obtain the SHA-1 signing certificate is found in this [link](https://developers.google.com/android/guides/client-auth) or can be done via Android Studio as shown [here](https://i.imgur.com/7RF6gsI.png)
1. Open the project in Android Studio
1. Run the project using an emulator or using a connected Android device
