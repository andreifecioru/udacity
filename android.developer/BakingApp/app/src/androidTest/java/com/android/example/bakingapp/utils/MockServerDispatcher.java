package com.android.example.bakingapp.utils;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.MockWebServer;


/**
 * Utility class that houses mocked web-server responses to by used by
 * our {@link MockWebServer} instance during testing.
 */
public class MockServerDispatcher {
    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_NOT_FOUND = 404;

    // A successful response containing a list with one recipe/one ingredient/one step.
    public static class SuccessfulRequestDispatcher extends Dispatcher {
        private static final String MOCKED_RESPONSE_BODY = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Nutella Pie\",\n" +
                "    \"servings\": 8,\n" +
                "    \"image\": \"\",\n" +
                "    \"ingredients\": [\n" +
                "      {\n" +
                "        \"quantity\": 2,\n" +
                "        \"measure\": \"CUP\",\n" +
                "        \"ingredient\": \"Graham Cracker crumbs\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"steps\": [\n" +
                "      {\n" +
                "        \"id\": 0,\n" +
                "        \"shortDescription\": \"Recipe Introduction\",\n" +
                "        \"description\": \"Recipe Introduction\",\n" +
                "        \"videoURL\": \"https://example.com/video.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        @Override
        public MockResponse dispatch(RecordedRequest request) {
            if (request.getPath().equals("/baking.json")) {
                return new MockResponse()
                        .setResponseCode(HTTP_STATUS_CODE_OK)
                        .setBody(MOCKED_RESPONSE_BODY);
            }

            return new MockResponse().setResponseCode(HTTP_STATUS_CODE_NOT_FOUND);
        }
    }
}
