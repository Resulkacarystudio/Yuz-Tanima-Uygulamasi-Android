package com.example.yuztanimasistemi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FaceDetection";

    private PreviewView previewView;
    private OverlayView overlayView;

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    // Dikey çözünürlük: Ön kameranın dikey görüntüsüne göre ayarla
    private static final int ANALYSIS_WIDTH = 1080;   // yatay çözünürlük
    private static final int ANALYSIS_HEIGHT = 1920;  // dikey çözünürlük

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.previewView);
        overlayView = findViewById(R.id.overlayView);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            setupCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void setupCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT) // Ön kamera
                        .build();

                Preview preview = new Preview.Builder()
                        .build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // PreviewView hazır olduktan sonra boyutları alıp overlay'a gönderiyoruz
                previewView.post(() -> {
                    int w = previewView.getWidth();
                    int h = previewView.getHeight();
                    Log.d(TAG, "PreviewView size: " + w + "x" + h);
                    overlayView.setPreviewSize(w, h);
                });

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(ANALYSIS_WIDTH, ANALYSIS_HEIGHT))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();

                FaceDetector detector = FaceDetection.getClient(options);

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    if (imageProxy == null || imageProxy.getImage() == null) {
                        imageProxy.close();
                        return;
                    }

                    InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(),
                            imageProxy.getImageInfo().getRotationDegrees());

                    detector.process(inputImage)
                            .addOnSuccessListener(faces -> {
                                List<RectF> rects = new ArrayList<>();
                                for (Face face : faces) {
                                    Rect box = face.getBoundingBox();
                                    rects.add(new RectF(box));
                                    Log.d(TAG, "Face box: " + box.flattenToString());
                                }
                                overlayView.setFaceRectangles(rects);
                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Face detection failed", e))
                            .addOnCompleteListener(task -> imageProxy.close());
                });

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera setup failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera();
            } else {
                Log.e(TAG, "Camera permission denied");
            }
        }
    }
}
