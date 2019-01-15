package com.example.android.funtravel.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;


/** An endpoint for delivering travel offers listing */
@Api(
    name = "offers",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "backend.funtravel.android.example.com",
        ownerName = "backend.funtravel.android.example.com"
    ))
public class OffersEndpoint {
    /** A simple endpoint method that returns the travel offers listing */
    @ApiMethod(name = "offers", path = "list")
    public OffersBean listOffers(@Named("count") int count, @Named("flags") int flags) {
        return new OffersBean(count, flags);
    }
}
