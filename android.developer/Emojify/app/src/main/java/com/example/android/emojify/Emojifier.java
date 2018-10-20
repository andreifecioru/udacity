package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.HashMap;
import java.util.Map;

class Emojifier {
    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    private static final float IS_SMILING_THRESHOLD = 0.5f;
    private static final float IS_LEFT_EYE_OPEN_THRESHOLD = 0.5f;
    private static final float IS_RIGHT_EYE_OPEN_THRESHOLD = 0.5f;

    private static final Map<ImmutableFaceCharacteristics, Integer> emojiTable = new HashMap<>();

    static {
        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(true).isLeftEyeOpen(true).isRightEyeOpen(true).build(), R.drawable.smile);
        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(true).isLeftEyeOpen(true).isRightEyeOpen(false).build(), R.drawable.rightwink);

        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(true).isLeftEyeOpen(false).isRightEyeOpen(true).build(), R.drawable.leftwink);
        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(true).isLeftEyeOpen(false).isRightEyeOpen(false).build(), R.drawable.closed_smile);

        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(false).isLeftEyeOpen(true).isRightEyeOpen(true).build(), R.drawable.frown);
        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(false).isLeftEyeOpen(true).isRightEyeOpen(false).build(), R.drawable.rightwinkfrown);

        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(false).isLeftEyeOpen(false).isRightEyeOpen(true).build(), R.drawable.leftwinkfrown);
        emojiTable.put(ImmutableFaceCharacteristics.builder().isSmiling(false).isLeftEyeOpen(false).isRightEyeOpen(false).build(), R.drawable.closed_frown);
    }

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
                Integer emojiResId = whichEmoji(faces.valueAt(i));
                Log.d(LOG_TAG, "Emoji res id: " + emojiResId);
            }
        }

        // Release the detector
        detector.release();
    }

    private static Integer whichEmoji(Face face) {
        boolean isSmiling = face.getIsSmilingProbability() > IS_SMILING_THRESHOLD;
        boolean isLeftEyeOpen = face.getIsLeftEyeOpenProbability() > IS_LEFT_EYE_OPEN_THRESHOLD;
        boolean isRightEyeOpen = face.getIsRightEyeOpenProbability() > IS_RIGHT_EYE_OPEN_THRESHOLD;

        ImmutableFaceCharacteristics characteristics = ImmutableFaceCharacteristics.builder()
            .isSmiling(isSmiling)
            .isLeftEyeOpen(isLeftEyeOpen)
            .isRightEyeOpen(isRightEyeOpen).build();

        return emojiTable.get(characteristics);
    }

    @org.immutables.value.Value.Immutable()
    public interface FaceCharacteristics {
        boolean isSmiling();
        boolean isLeftEyeOpen();
        boolean isRightEyeOpen();
    }
}
