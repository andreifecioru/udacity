package com.example.android.funtravel.utils;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;


/**
 * Utility class that houses mocked web-server responses to by used by
 * our {@link MockWebServer} instance during testing.
 */
public class MockServerDispatcher {
    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_NOT_FOUND = 404;

    // A successful response containing a list with one recipe/one ingredient/one step.
    public static class SuccessfulRequestDispatcher extends Dispatcher {
        private static final String MOCKED_RESPONSE_BODY = "{ \"offers\": [\n" +
                "        {\n" +
                "            \"id\": \"1\",\n" +
                "            \"type\": \"trekking\",\n" +
                "            \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud\",\n" +
                "            \"title\": \"Lorem ipsum dolor sit amet\",\n" +
                "            \"typeAsEnum\": \"TREKKING\",\n" +
                "            \"price\": 266.0,\n" +
                "            \"avgRating\": 0.0,\n" +
                "            \"photoUrl\": \"http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-001.jpg\",\n" +
                "            \"aspectRatio\": 1.5,\n" +
                "            \"videoUrl\": \"http://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-002.mp4\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        @Override
        public MockResponse dispatch(RecordedRequest request) {
            if (request.getPath().startsWith("/offers/v1/list")) {
                return new MockResponse()
                        .setResponseCode(HTTP_STATUS_CODE_OK)
                        .setBody(MOCKED_RESPONSE_BODY);
            }

            return new MockResponse().setResponseCode(HTTP_STATUS_CODE_NOT_FOUND);
        }
    }
}
