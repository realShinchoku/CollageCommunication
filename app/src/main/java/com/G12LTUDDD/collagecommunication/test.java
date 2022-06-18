package com.G12LTUDDD.collagecommunication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class test extends AppCompatActivity {
    // One Button
    Button BSelectImage;

    // One Preview Image
    ImageView IVPreviewImage;
    TextView textView;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // register the UI widgets with their appropriate IDs
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        textView = findViewById(R.id.tvTest);
        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener(v -> imageChooser());
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        
        // pass the constant to compare it
         startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    FirebaseStorage storage;
                    storage = FirebaseStorage.getInstance();
                    String s =  UUID.randomUUID().toString();
                    storage.getReference().child("img/abc").putFile(selectedImageUri);
                    IVPreviewImage.setImageURI(selectedImageUri);
                    textView.setText(s);
                }
            }
        }
    }
}