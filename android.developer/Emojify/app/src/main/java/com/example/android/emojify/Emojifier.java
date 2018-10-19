package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class Emojifier {
    private static final String LOG_TAG = Emojifier.class.getSimpleName();
    static void detectFaces(Context context, Bitmap bitmap) {
        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, "No faces", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Faces: " + faces.size(), Toast.LENGTH_SHORT).show();
            for (int i =0; i < faces.size(); i++) {
                getClassifications(faces.valueAt(i));
            }
        }

        // Release the detector
        detector.release();
    }

    private static void getClassifications(Face face) {
        // Log all the probabilities
        Log.d(LOG_TAG, "getClassifications: smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "getClassifications: leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "getClassifications: rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());
    }
}
