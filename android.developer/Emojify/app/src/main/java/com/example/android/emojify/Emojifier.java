package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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

    private static final float EMOJI_SCALE_FACTOR = .9f;

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

    static Bitmap detectFacesAndOverlayEmoji(Context context, Bitmap bitmap) {
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

        Bitmap resultBitmap = bitmap;

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, "No faces", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Faces: " + faces.size(), Toast.LENGTH_SHORT).show();
            for (int i =0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);
                Integer emojiResId = whichEmoji(face);
                Log.d(LOG_TAG,"Emoji res id: " + emojiResId);

                if (emojiResId != null) {
                   Bitmap emojiBitmap = BitmapFactory.decodeResource(context.getResources(), emojiResId);
                   resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, face);
                }

            }
        }

        // Release the detector
        detector.release();

        return resultBitmap;
    }

    /**
     * Combines the original picture with the emoji bitmaps
     *
     * @param backgroundBitmap The original picture
     * @param emojiBitmap      The chosen emoji
     * @param face             The detected face
     * @return The final bitmap, including the emojis over the faces
     */
    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {

        // Initialize the results bitmap to be a mutable copy of the original image
        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());

        // Scale the emoji so it looks better on the face
        float scaleFactor = EMOJI_SCALE_FACTOR;

        // Determine the size of the emoji to match the width of the face and preserve aspect ratio
        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);


        // Scale the emoji
        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        // Determine the emoji position so it best lines up with the face
        float emojiPositionX =
                (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY =
                (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;

        // Create the canvas and draw the bitmaps to it
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
    }


    private static Integer whichEmoji(Face face) {
        boolean isSmiling = face.getIsSmilingProbability() > IS_SMILING_THRESHOLD;
        boolean isLeftEyeOpen = face.getIsLeftEyeOpenProbability() > IS_LEFT_EYE_OPEN_THRESHOLD;
        boolean isRightEyeOpen = face.getIsRightEyeOpenProbability() > IS_RIGHT_EYE_OPEN_THRESHOLD;

        ImmutableFaceCharacteristics characteristics = ImmutableFaceCharacteristics.builder()
            .isSmiling(isSmiling)
            .isLeftEyeOpen(isLeftEyeOpen)
            .isRightEyeOpen(isRightEyeOpen).build();

        Log.d(LOG_TAG, "Face characteristics: " + characteristics);

        return emojiTable.get(characteristics);
    }

    @org.immutables.value.Value.Immutable()
    public interface FaceCharacteristics {
        boolean isSmiling();
        boolean isLeftEyeOpen();
        boolean isRightEyeOpen();
    }
}
