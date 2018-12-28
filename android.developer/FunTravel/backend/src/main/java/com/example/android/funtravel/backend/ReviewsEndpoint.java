package com.example.android.funtravel.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;


/** An endpoint for delivering travel offers listing */
@Api(
    name = "reviews",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "backend.funtravel.android.example.com",
        ownerName = "backend.funtravel.anddroid.example.com"
    ))
public class ReviewsEndpoint {
    /** A simple endpoint method that returns the travel offers listing */
    @ApiMethod(name = "reviews", path = "list")
    public ReviewBean listReviews(@Named("offer_id") long offerId) {
        return new ReviewBean(offerId);
    }
}
