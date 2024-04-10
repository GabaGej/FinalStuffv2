package net.leevanec.finalstuffv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView photoImageView;
    private Button takePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // The usual startup stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Connect stuff from layout
        photoImageView = findViewById(R.id.photoImageView);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }


    // Go to calculator part
    public void launchCalculator(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }

    // Go to convertor part
    public void launchConvertor(View view) {
        Intent intent = new Intent(this, ConvertorActivity.class);

        // Debug toast
        // Toast.makeText(this, "aaaaaaaaaa", 0).show();

        startActivity(intent);
    }

    // This whatever will fetch camera to get a picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    // This thing will display the thingy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoImageView.setImageBitmap(imageBitmap);
            photoImageView.setVisibility(View.VISIBLE);
            saveImage(imageBitmap);
        }
    }

    // And this one will save the picture - for good measure
    private void saveImage(Bitmap imageBitmap) {
        File imagePath = new File(getFilesDir(), "images");
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
        File newFile = new File(imagePath, "photo.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(newFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
