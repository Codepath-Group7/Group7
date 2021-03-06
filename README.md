# Group Project *Food Trux*

**Food Trux** is a one stop App for all food truck lovers. We customize and show food truck in and around your location. Find awesome food and drinks through our fun and elegant app. The app utilizes [Yelp REST API](https://www.yelp.com/developers/documentation/v3).

The following **required** functionality is completed:

* [X] User should be able to login the app using either gmail or Facebook.
* [X] User should be able to see a list of food trucks on the home screen.
* [X] User should be able to see the food trucks in a map view.
* [X] The list of food trucks should sorted based on user’s location.
* [X] User should be able to search a cuisine or a specific food truck.
* [X] User can click on a food truck in list view or map view to see the details of the food trucks. The following should be shown:
  * [X] Business hours
  * [X] Pictures
  * [X] Ratings
  * [X] Reviews
  * [X] Address
  * [X] Cuisine
* [X] User can post photos and reviews about the food truck.
* [X] User can store his cuisine and location preferences in application settings.

The following **optional** functionality is completed:
* [ ] Autocomplete keywords for search feature.
* [ ] Filter results in the list view.
* [X] User in similar geo location can connect with each other through a chat room and discuss about food trucks in the area.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

[Video Walkthrough](https://www.dropbox.com/s/6ldkpxywgowqdz4/projectDemo.mp4?dl=0)

### After UI Polish
<img src='https://i.imgur.com/z0xL27i.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />      <img src='https://i.imgur.com/crzqR0v.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /><img src='https://i.imgur.com/vkZxpl5.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Before UI Polish
<img src='http://i.imgur.com/EOxiwhw.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />      <img src='http://i.imgur.com/6cckdg6.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /><img src='https://i.imgur.com/ZkGRjyJ.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /><img src='https://i.imgur.com/pyR7QgS.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' /><img src='https://i.imgur.com/7Aya7pK.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />


GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used

- [Retrofit](https://github.com/square/retrofit) - Asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Gson](https://github.com/google/gson) - library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.
- [Mosby](https://github.com/sockeqwe/mosby) - A Model-View-Presenter library for Android apps.

## To Run the project
- Create /values/keys.xml with the following content and replace <YOUR_KEY> with your API keys
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_api_key_enabled"><YOUR_KEY></string>
    <string name="google_web_client_id"><YOUR_KEY></string>
    <string name="yelp_api_key"><YOUR_KEY></string>
</resources>
```
## License

    Copyright 2017 Akshay Mathur, Robert Lee, Sai Pranesh Mukkala

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
