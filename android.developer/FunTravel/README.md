# Running the project
The project also provides the backend code which feeds data into the app.

To run the whole setup, the backend needs to be available to provide data:

```bash
./gradlew clean :backend:appengineStart
```

The app can be deployed via gradle, but keep in ming that we have two product flavors:

```bash
# install the "free" flavor
./gradlew clean :app:installFreeRelease

# install the "paid" flavor
./gradlew clean :app:installPaidRelease
```

# Running the instrumented tests
The instrumented tests are using a mock for the backend, so there is no need to have the actual backend up-and-running:

Just run:

```bash
./gradlew clean connectedAndroidTest
```

# About the tooling
In the document submitted for phase 1 of the capstone project I mentioned the use of Android Studio 3.2. However, recently Android Studio 3.3 came out which cuts off support for the old-style support libs. For this reason I had to (painfully) migrate the whole project to AndroidX.

# About the fixture "data"
As explained in the design doc, the backend creates an in-memory DB with all the offer/review data coming from a pool of hard-coded "sample" data. Creating these fixtures is a tedious process which is not very rewarding (I am not learning anything new from it). As a result, the data set is somewhat limited to:
  - 10 offers per type (trekking, cityscape  and resort) leading to a total of 30 offers
  - a total of 10 reviews. From these pool of reviews we assign randomly reviews to each of the 30 offfers.
  - one video resources per offer type. There is one .mp4 movie shared by all offers of the same type. Scraping sample videos from YouTube is not my idea of fun. 

As a consequence, one would notice some content that gets repeated (same offer listed multiple times, reviews shared between offfers, same video for all offers of the same type). These are not bugs: the code behind it all is sound. It is just a consequence of the limited set of "fixture" data.
