The app downloads the recipe list from the link provided in the assignment notes: https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json. Should you want to change that, modify the NetModule class (in the com.android.example.bakingapp.di.module package) and change the value of the BASE_URL constant.

The app provides "offline" capabilities: the recipes are cached locally in the local DB (except the video/image resources). Once the recipes are downloaded, the user can navigate between them and look at all the text-based recipe info (ingredients, preparation step instructions etc.). The app uses a full-fledged implementation of the "network-based resource" abstraction described in the official docs for AAC. It is largely the same code I used for the submission of the PopularMovies app. There are a few sections where I drew inspiration from various places on the Internet (these sections are marked appropriately with source attribution).

When the app detects that there is no internet connectivity, the user is informed via a text strip sliding from the top (the yaw the YouTube app does it). The same happens when the connectivity is regained.

As far as UI testing goes, I did not aim form code coverage. I provide UI tests only for the RecipeListActivity class, but those tests cover all the testing techniques covered in the course material: UI testing, idling resources and intent testing.

I used several 3rd party libs: ButterKnife for UI binding, Picassa for image loading, Retrofit for interaction with remote end-point, Dagger2 for DI.

The app's widget displays info about the last recipe that was accessed in the RecipeDetailsActivity. When the user looks at a new recipe from the list, the widget updates itself with the information of that recipe. Here is a short description of how it works:
  - when the widget is first added to the home screen, it is empty: it instructs the user to first look at a recipe (in the RecipeDetailsActivity).
  - once the user selects a recipe and loads it in the RecipeListActivity, the widget will be updated with the info about that recipe (name and the list of ingredients). This can be verified by minimizing the app (press the home button).
  - when going back to the app and loading another recipe in the RecipeListActivity, the widget will be updated accordingly.
  - if the user taps on the top of the widget (where the name of the recipe is displayed) the RecipeListActivity is brought to the foreground with and the recipe that is currently displayed by the widget is loaded. 
