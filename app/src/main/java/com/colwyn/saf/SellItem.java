package com.colwyn.saf;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SellItem extends AppCompatActivity {

    //---Firebase Reference---//
    private StorageReference mStorageRef;

    //---Declare Widgets---//
    Button chooseImage;
    Button uploadButton;
    ImageView itemImage;

    private Uri imageUri;

    //---Declare Constant---//
    private static final int PICK_IMAGE_REQUEST = 1;


    //---Choose Image Button---//
    public void chooseImageClicked (View view){

        openFileChooser();
    }

    //---Upload Button---//
    public void uploadClicked (View view){

    }

    //---Cancel Button---//
    public void cancelClicked (View view){
        startActivity(new Intent(SellItem.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        //---Initialise Firebase---//
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //---Get Widgets---//
        chooseImage = findViewById(R.id.chooseImagebtn);
        uploadButton = findViewById(R.id.chooseImagebtn);
        itemImage = findViewById(R.id.itemImageView);


    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!= null && data.getData() != null){
            imageUri=data.getData();

            //Picasso.with(this).load(imageUri).into(itemImage);
            itemImage.setImageURI(imageUri);
        }
    }

    //---Get File Extension of uploaded Image---//
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //---Upload File---//
    private void uploadFile(){

        if (imageUri != null) {

            //Get Unique name for a file (Use Time in miliseconds + File Extension)
            StorageReference fileReference = mStorageRef.child("item_Images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SellItem.this, "Upload successful.",Toast.LENGTH_SHORT).show();
                            //Create a new upload Item
                            //Upload upload = new Upload("Item"), taskSnapshot.getDownloadUrl().toString(); //Part 3 14mins
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellItem.this, "Upload unsuccessful.",Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            Toast.makeText(SellItem.this, "No Image selected!",Toast.LENGTH_SHORT).show();
        }
    }
}
