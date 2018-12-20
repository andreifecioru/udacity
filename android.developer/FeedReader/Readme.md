This submission is a complete rewrite of the starter-code provided in the assignment. Here is why:

I really did not like the fact that the code was based on many deprecated APIs / UI patterns. It also tried to implement UI navigation patterns using a lot of custom views. I understand that this is a rather old code-base and that the more "modern" Android APIs were not available when this code was written.

The biggest problem I had with the starter code is that the UI was quite slow: animations and transition were sluggish even when run on my physical device (a Nexus 5). Running the same started code on emulator was not even possible for me (I work on a MacBook Pro - mid 2012 edition).

With that in mind I hope you don't mind me writing the whole code-base fromm scratch while keeping all the functionality and also hitting almost all requirements in the review rubric document. Below is a short list of changes:
  - downloading/persisting the article in local DB is done using the patterns provided by AAC and a bunch pf libs (room, LiveData, NetworkBoundResource, Retrofit2, Gson and Dagger2 for DI). It's basically the same setup I used for my previous submissions (BakingApp, etc). This way I got rid of all the deprecated APIs (like content loaders and friends)
  - for the ArticleDetailsFragment I used CollapsingToolbarLayout (part of the material design toolkit in app-compat) to achieve a simple, and beautiful UI. Most importantly it is performant: it functions smoothly both on my physical device and emulator.
  - when the UI is stretched on a device beyond 600dp, the UI adapts as follows:
    - the grid-view in the ArticleListActivity gets 3 columns (from the default value of 2)
    - the UI in the ArticleDetailsActivity implements the "paper page" metaphor: the width is limited and centered on the screen.

NOTES:
    - I pulled a nifty little trick in the ArticleDetailsActivity: when viewed on a tablet, the background color changes based on the palette extracted from the article's photo.
    - the only thing that I could not achieve was to set an elevation for the "share" FAB: when anchored to a CollapsibleToolbarLayout, the elevation property is not applied (current limitations in app-compat lib)