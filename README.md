# Scope Homework

The application is constructed using Clean architecture + MVVM, which is the current Googles recomendation on how to structure applications.
Code is split into Data -> Domain -> Presentation layers. Layers are connected with dependency injection, library used: HILT.
The Data stream between layers is connected with suspend functions and Flows.

![image](https://user-images.githubusercontent.com/45454489/151356087-8ad29988-b4c7-40ef-8013-8aaf252b8067.png)

For API calls application uses Retrofit 2, and RoomDB to store the data.

On the first screen you can see a user list, with their images and data. Colors and designs are used from Material Design.
Images are loaded with kt:coil library, it uses an internal caching mechanism, so the .jpg are stored locally for reuse. Database data is used as the single source of truth, a new list is fetched from the API only when the data is outdated, or it is empty.

![first_view](https://user-images.githubusercontent.com/45454489/151355214-b3f87734-70ab-407a-8976-572701923161.jpg)

When clicking on a user you are redirected to a screen that contains a map, and the overlay that contains the vehicle information.
The map library used - MapBox. I did not use gms, as I had issues with my account billing for address Geocoding and Directions API'S

When the location is found the map animates/zooms so the user can see all the markers. Vehicle markers match the information on the overlay, so in case of multiple markers, you can diferentiate betweeen them. User address is found from the MapBox geocoding API.

![second_view](https://user-images.githubusercontent.com/45454489/151355269-1a7938fb-1265-4fc1-bfef-1e0ec439687c.jpg)

When clicking on a marker a route is planned from the MapBox directions API, and visualised on the screen. When a valid route is found the map animates/zooms so the whole route is visible. In case of multiple markers, when clicking on a second one, the previous rout disappears and the new one shows up.

![third_view](https://user-images.githubusercontent.com/45454489/151355268-9cc66cfb-bd2d-4b40-8ab8-6878bdba5838.jpg)

When multiple markers are visible, the map zooms in so both markers are visible on the screen

![multiple_markers](https://user-images.githubusercontent.com/45454489/151355266-beb764cf-51fa-4198-bbea-d86eb4623e27.jpg)

![multiple_markers_route](https://user-images.githubusercontent.com/45454489/151355273-83eb68db-5f00-4d2c-ac7d-e393d63694ad.jpg)

In case if an API call error occurs a popup is shown, and the user can retry the API call.

![error_view](https://user-images.githubusercontent.com/45454489/151358320-06bc3bef-e1ed-496c-9177-92445f376e97.jpg)

# Possible Improvements:

Refactor the repositry to return Flows instead of having suspend functions, as it would give more options on how to handle the data stream.

# Known Issues
The route dissapears after a new vehicle location data has been loaded from the API.
Some possible exception handling is missing.
Geocoded address is not being cached and an API call is made each time it needs to show it.
