package com.colwyn.saf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class addItem extends AppCompatActivity {

    //---Firebase---//
    private StorageReference mStorageRef;

    //---Declare Widgets---//
    Button chooseButton;
    Button uploadButton;
    ImageView itemImageView;

    ImageView chooseImageButton2;

    private Uri imgUrl;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 1;
    private StorageTask mUploadTask;

    //---Cancel Button---//
    public void cancelClicked (View view){
        startActivity(new Intent(addItem.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //---Choose Button Clicked---//
    public void chooseClicked (View view){

        //Choose Image Method
        chooseImage();

        //Hide Choose Button 2
        chooseImageButton2 = findViewById(R.id.choosebtn2);
        chooseImageButton2.setVisibility(View.GONE);

        //Show Choose Button (Change Image Button)
        chooseButton = findViewById(R.id.choosebtn);
        chooseButton.setVisibility(View.VISIBLE);
    }

    //---Upload Button Clicked---//
    public void uploadClicked (View view){

        //Upload Image Method
        uploadImage();

        //---Save Item to Firestore (Database)---//

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //---Firebase---//
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //---Get Widgets---//
        chooseButton = findViewById(R.id.chooseImagebtn);
        uploadButton = findViewById(R.id.uploadbtn);
        itemImageView = findViewById(R.id.itemImageView);



    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent .setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filepath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());
            mUploadTask = ref.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            progressDialog.dismiss();
                            Toast.makeText(addItem.this, "Uploaded Complete. " + downloadUrl, Toast.LENGTH_SHORT).show();

                            StorageMetadata snapshotMetadata= taskSnapshot.getMetadata();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(addItem.this, "Uploaded Failed.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath= data.getData();

            Picasso.with(this).load(filepath).into(itemImageView);
            itemImageView.setImageURI(filepath);
        }
    }
}
