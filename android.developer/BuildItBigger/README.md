The parent project contains 4 sug-projects:
1. `app`: is the main app containing the main screen where the user can
request to see a joke. It comes in to flavors: `free` and `paid`. In the
free version, a banner add is display on the bottom of the screen.
2. `jokedisplay`: it is an Android library sub-project, which houses the
`JokeDisplayActivity`. It displays the joke text in a `TextView`. The joke
is passed to it via the incoming `Intent` instance.
3. `backend`: it is the `AppEngine` end-point implementation. It delivers
the joke text over the wire.
4. `joketeller`: it is a simple Java library that acts as a "repository"
of jokes. It has a list of predefined (bad) jokes. Each time it is invoked
by the `backend` it issues a random joke from the list of jokes.

The build file contains a task called `:app:runConnectedTests` which does
the following (in order):
  - starts the app engine server
  - runs the connected tests
  - stops the app engine server

There is an instrumented test that loads the `MainActivity` and presses
on the "tell joke" button. We intercept and assert on the contents of the
generated intent (meant to trigger the `JokeDisplayActivity).

I have also placed a "loading indicator" in the `MainActivity` that is
displayed while the joke data is downloaded from the endpoint. Since the
app server is run locally, it is hard to observe (data comes in very fast).
One can put a `Thread.sleep()` in the endpoint code to simulate network delay
and see the loading indicator in action.

The `MainActivity` provides notification about the connectivity status.
When network connectivity is lost/regained a message is displayed on the
top of the screen (similar with the YouTube app). This feature can be tested
by activating/deactivating airplane mode.

NOTE:
  - `app` depends on `jokedisplay`
  - `backend` depends on `joketeller`

NOTE: the dependencies for the `app` sub-project are set in such a way that
the `paid` flavor does not include the deps for Google's ads lib.

NOTE: I have decided not to use AsyncTask for downloading the jokes. I use
instead the AAC patterns with ViewModel and the response from the server
wrapped inside a LiveData. I hope that is inline with the requirements.
