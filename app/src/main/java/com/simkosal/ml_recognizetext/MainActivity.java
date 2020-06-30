package com.simkosal.ml_recognizetext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button mButtonLoadImage;
    private TextView mTextView;
    private ImageView mImageView;
    public  static  final int PIC_IMAGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonLoadImage = (Button) findViewById(R.id.button_click);
        mTextView = (TextView) findViewById(R.id.text_preview);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mButtonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), PIC_IMAGE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIC_IMAGE){
            mImageView.setImageURI(data.getData());

            //Create firebase VisionImage object from file
            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());


                //Pass the image to the processImage
                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();
                textRecognizer.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                // Task completed successfully
                                // ...

                                Log.i("---result",result.getText().toString());
                                 mTextView.setText(result.getText());
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("---exception",e.toString());
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
