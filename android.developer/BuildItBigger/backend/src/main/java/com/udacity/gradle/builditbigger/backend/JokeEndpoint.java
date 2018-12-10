package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/** An endpoint for delivering jokes */
@Api(
    name = "jokes",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "backend.builditbigger.gradle.udacity.com",
        ownerName = "backend.builditbigger.gradle.udacity.com"
    ))
public class JokeEndpoint {
    /** A simple endpoint method that returns the joke data */
    @ApiMethod(name = "tell")
    public JokeBean tell() {
        return new JokeBean();
    }
}
