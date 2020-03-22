# campus-guide
Concordia Campus Guide application - SOEN390


## Objective:
Create a campus guide application to help students locate their classrooms.

## Team Members:

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
2. [Get an API key for Google Maps](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
3. Clone the repository onto your computer
4. Create a copy of the file `app\src\debug\res\values\google_maps_api.example.xml` in the same folder.
5. Rename that file `google_maps_api.xml`
6. The file contains a single `<string>` tag. Change the `name` property from `google_maps_key_ex` to `google_maps_key`. Change the contents of the tag from `google_maps_key` to the value of your API key (it starts with `AIza`)
7. Perform steps 4 through 6 with the file `app\src\release\res\values\google_maps_api.example.xml`. This step is only necessary to build the app in Release configuration.
8. Open the project in Android Studio
9. Run the project using an emulator or using a connected android device.
