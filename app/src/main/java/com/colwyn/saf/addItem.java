package com.colwyn.saf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addItem extends AppCompatActivity {

    //---Firebase---//
    private StorageReference mStorageRef;

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //---Declare Widgets---//
    Button chooseButton;
    Button uploadButton;
    ImageView itemImageView;
    ImageView chooseImageButton2;
    EditText titleEditText;
    RadioButton accessoriesrbtn, badgesrbtn, bodyrtn, brakesrbtn, clutchrbtn;
    RadioButton coolingrbtn, drivetrainrbtn, electricsrbtn, enginerbtn, exhaustrbtn;
    RadioButton exteriorrbtn, filtersrbtn, fuelrbtn, gagesrbtn, gearboxrbtn;
    RadioButton interiorrbtn, steeringrbtn, suspensionrbtn, wheelsrbtn, otherrbtn;
    EditText descriptionEditText;
    EditText brandEditText;
    RadioButton newrbtn, usedrbtn;
    EditText priceEditText;
    EditText deliveryNotesEditText;
    //TextView urlTextView;


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

        //---Populate Data if the user is editing a listing---//
        if (userData.listingEdit_Global == true){
            //An Edit

            //Get Widgets
            titleEditText = findViewById(R.id.titleEditText);
            //Radiobuttons
            accessoriesrbtn = findViewById(R.id.accessoriesrbtn);
            badgesrbtn = findViewById(R.id.badgesrbtn);
            bodyrtn = findViewById(R.id.bodyrbtn);
            brakesrbtn = findViewById(R.id.brakesrbtn);
            clutchrbtn = findViewById(R.id.clutchrbtn);
            coolingrbtn = findViewById(R.id.coolingrbtn);
            drivetrainrbtn = findViewById(R.id.drivetrainrbtn);
            electricsrbtn = findViewById(R.id.electricsrbtn);
            enginerbtn = findViewById(R.id.enginerbtn);
            exhaustrbtn = findViewById(R.id.exhaustrbtn);
            exteriorrbtn = findViewById(R.id.exteriorrbtn);
            filtersrbtn = findViewById(R.id.filtersrbtn);
            fuelrbtn = findViewById(R.id.fuelrbtn);
            gagesrbtn = findViewById(R.id.gagesrbtn);
            gearboxrbtn = findViewById(R.id.gearboxrbtn);
            interiorrbtn = findViewById(R.id.interiorrbtn);
            steeringrbtn = findViewById(R.id.steeringrbtn);
            suspensionrbtn = findViewById(R.id.suspensionrbtn);
            wheelsrbtn = findViewById(R.id.wheelsrbtn);
            otherrbtn = findViewById(R.id.otherrbtn);

            newrbtn = findViewById(R.id.newrbtn);
            usedrbtn = findViewById(R.id.usedrbtn);

            descriptionEditText = findViewById(R.id.descriptionEditText);
            brandEditText = findViewById(R.id.brandEditText);
            priceEditText = findViewById(R.id.priceEditText);
            deliveryNotesEditText = findViewById(R.id.deliveryNotesEditText);

            //Get ID of Item Clicked to run search
            String documentID = userData.userItemClicked_Global;

            //Get data from Firestore
            try{
                DocumentReference docRef = db.collection("listings").document(documentID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //Retrieve data and store as string
                                String FSTitle = document.getString("Title");
                                String FSPrice = document.getString("Price");
                                String FSDelivery = document.getString("Delivery_Notes");
                                String FSCondition = document.getString("Condition");
                                String FSBrand = document.getString("Brand");
                                String FSDescription = document.getString("Description");
                                String FSImageURL = document.getString("ImageURL");
                                String FSCategory = document.getString("Category");

                                //Display data
                                titleEditText.setText(FSTitle);
                                priceEditText.setText(FSPrice);
                                deliveryNotesEditText.setText(FSDelivery);
                                brandEditText.setText(FSBrand);
                                descriptionEditText.setText(FSDescription);

                                if(FSCategory.equals("Accessories")){
                                    accessoriesrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Badges,Decals")){
                                    badgesrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Body")){
                                    bodyrtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Brakes")){
                                    brakesrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Clutch")){
                                    clutchrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Cooling,Heating")){
                                    coolingrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Drivetrain")){
                                    drivetrainrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Electrics")){
                                    electricsrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Engine")){
                                    enginerbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Exhaust")){
                                    exhaustrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Exterior")){
                                    exteriorrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Filters")){
                                    filtersrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Fuel")){
                                    fuelrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Gages")){
                                    gagesrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Gearbox")){
                                    gearboxrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Interior")){
                                    interiorrbtn.isChecked();
                                    interiorrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Steering")){
                                    steeringrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Suspension")){
                                    suspensionrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Wheels")){
                                    wheelsrbtn.setChecked(true);
                                }
                                else if(FSCategory.equals("Other")){
                                    otherrbtn.setChecked(true);
                                }

                                if(FSCondition.equals("New")){
                                    newrbtn.setChecked(true);
                                }
                                else if(FSCondition.equals("Used")){
                                    usedrbtn.setChecked(true);
                                }

                                //Hide Choose Button 2
                                chooseImageButton2 = findViewById(R.id.choosebtn2);
                                chooseImageButton2.setVisibility(View.GONE);
                                //Show Choose Button (Change Image Button)
                                chooseButton = findViewById(R.id.choosebtn);
                                chooseButton.setVisibility(View.VISIBLE);
                                //Display Photo
                                Picasso.get().load(FSImageURL).into(itemImageView);
                                //Save as Global (So it can be stored to database again
                                userData.FSImageURL_Global = FSImageURL;


                            } else {
                                //Document Doesnt Exist
                                Toast.makeText(addItem.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }

            catch (Exception e){

            }


        }
        else{
            //Not an edit
        }




    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent .setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void uploadImage() { //and get URL
        //needed if new image has been added in an edit.
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
                            //Image is uploaded and URL link has been found. Now save data to firestore
                            //make url available to savedata function
                            //urlTextView = findViewById(R.id.urlTextView);
                            //urlTextView.setText(downloadUrl.toString());
                            userData.downloadurl_Global = downloadUrl.toString();
                            savedata();

                            //Toast.makeText(addItem.this, "Uploaded Complete. " + downloadUrl, Toast.LENGTH_SHORT).show();
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

        else{
            //Check if this is an edit or a or it is just blank

            if(userData.listingEdit_Global == true){
                //There is no new image (URL Already Generated)
                //userData.FSImageURL_Global
                savedata();
                //Toast.makeText(addItem.this, "You are using the same Image", Toast.LENGTH_SHORT).show();

            }
            else{
                //Not an edit, an image needs to be added to the new listing
                Toast.makeText(addItem.this, "Please add an image of your item.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void savedata(){

        //---Save Item to Firestore (Database)---//

        //---Get Widgets---//
        titleEditText = findViewById(R.id.titleEditText);
        //radiobuttons
        accessoriesrbtn = findViewById(R.id.accessoriesrbtn);
        badgesrbtn = findViewById(R.id.badgesrbtn);
        bodyrtn = findViewById(R.id.bodyrbtn);
        brakesrbtn = findViewById(R.id.brakesrbtn);
        clutchrbtn = findViewById(R.id.clutchrbtn);
        coolingrbtn = findViewById(R.id.coolingrbtn);
        drivetrainrbtn = findViewById(R.id.drivetrainrbtn);
        electricsrbtn = findViewById(R.id.electricsrbtn);
        enginerbtn = findViewById(R.id.enginerbtn);
        exhaustrbtn = findViewById(R.id.exhaustrbtn);
        exteriorrbtn = findViewById(R.id.exteriorrbtn);
        filtersrbtn = findViewById(R.id.filtersrbtn);
        fuelrbtn = findViewById(R.id.fuelrbtn);
        gagesrbtn = findViewById(R.id.gagesrbtn);
        gearboxrbtn = findViewById(R.id.gearboxrbtn);
        interiorrbtn = findViewById(R.id.interiorrbtn);
        steeringrbtn = findViewById(R.id.steeringrbtn);
        suspensionrbtn = findViewById(R.id.suspensionrbtn);
        wheelsrbtn = findViewById(R.id.wheelsrbtn);
        otherrbtn = findViewById(R.id.otherrbtn);

        newrbtn = findViewById(R.id.newrbtn);
        usedrbtn = findViewById(R.id.usedrbtn);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        brandEditText = findViewById(R.id.brandEditText);
        priceEditText = findViewById(R.id.priceEditText);
        deliveryNotesEditText = findViewById(R.id.deliveryNotesEditText);

        //---Get Data---//
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String brand = brandEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String deliverynotes = deliveryNotesEditText.getText().toString().trim();
        String downloadurl = userData.downloadurl_Global;




        //Category radio buttons
        String category;
        if(accessoriesrbtn.isChecked()){
            category = "Accessories";
        }
        else if(badgesrbtn.isChecked()){
            category = "Badges,Decals";
        }
        else if(bodyrtn.isChecked()){
            category = "Body";
        }
        else if(brakesrbtn.isChecked()){
            category = "Brakes";
        }
        else if(clutchrbtn.isChecked()){
            category = "Clutch";
        }
        else if(coolingrbtn.isChecked()){
            category = "Cooling,Heating";
        }
        else if(drivetrainrbtn.isChecked()){
            category = "Drivetrain";
        }
        else if(electricsrbtn.isChecked()){
            category = "Electrics";
        }
        else if(enginerbtn.isChecked()){
            category = "Engine";
        }
        else if(exhaustrbtn.isChecked()){
            category = "Exhaust";
        }
        else if(exteriorrbtn.isChecked()){
            category = "Exterior";
        }
        else if(filtersrbtn.isChecked()){
            category = "Filters";
        }
        else if(fuelrbtn.isChecked()){
            category = "Fuel";
        }
        else if(gagesrbtn.isChecked()){
            category = "Gages";
        }
        else if(gearboxrbtn.isChecked()){
            category = "Gearbox";
        }
        else if(interiorrbtn.isChecked()){
            category = "Interior";
        }
        else if(steeringrbtn.isChecked()){
            category = "Steering";
        }
        else if(suspensionrbtn.isChecked()){
            category = "Suspension";
        }
        else if(wheelsrbtn.isChecked()){
            category = "Wheels";
        }
        else if(otherrbtn.isChecked()){
            category = "Other";
        }
        else{
            //used to catch noncompletion of field.
            category = null;
        }

        //Condition radio buttons
        String condition;
        if(newrbtn.isChecked()){
            condition = "New";
        }
        else if(usedrbtn.isChecked()){
            condition = "Used";
        }
        else{
            condition = null;
        }


        //---Check all fields have been filled in---//
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(addItem.this, "Please enter a title for your item.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(addItem.this, "Please select the category your item belongs to.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(addItem.this, "Please enter a description.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(brand)) {
            Toast.makeText(addItem.this, "Please enter the brand of your item.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(condition)) {
            Toast.makeText(addItem.this, "Please tell us the condition of the item.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(addItem.this, "Please enter the price of your item.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(deliverynotes)) {
            Toast.makeText(addItem.this, "Please describe your delivery policies.", Toast.LENGTH_SHORT).show();
            return;
        }


        //---Get userid & email---//
        String userID = userData.userID_Global;

        //---Add Item to firestore Database---//
        if(userData.listingEdit_Global == false) {

            Map<String, Object> listings = new HashMap<>();
            listings.put("UserID", userID);
            listings.put("Title", title);
            listings.put("Category", category);
            listings.put("Description", description);
            listings.put("Brand", brand);
            listings.put("Condition", condition);
            listings.put("Price", price);
            listings.put("Delivery_Notes", deliverynotes);
            listings.put("ImageURL", downloadurl);


            // Add a new document with a generated ID
            db.collection("listings")
                    .add(listings)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Go back to Selling Activity
                            startActivity(new Intent(addItem.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            userData.downloadurl_Global = null;
                            userData.userItemClicked_Global = null;
                            userData.FSImageURL_Global = null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(addItem.this, "Database Error ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        //---Edit to listing to firestore database---//
        else{
            Map<String, Object> listings = new HashMap<>();
            listings.put("UserID", userID);
            listings.put("Title", title);
            listings.put("Category", category);
            listings.put("Description", description);
            listings.put("Brand", brand);
            listings.put("Condition", condition);
            listings.put("Price", price);
            listings.put("Delivery_Notes", deliverynotes);
            if(downloadurl != null) {
                listings.put("ImageURL", downloadurl);
            }
            else{
                listings.put("ImageURL", userData.FSImageURL_Global);
            }


            db.collection("listings").document(userData.userItemClicked_Global)
                    .set(listings)//, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(addItem.this, "Listing Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addItem.this, Selling.class));
                            //Reset Values
                            userData.downloadurl_Global = null;
                            userData.userItemClicked_Global = null;
                            userData.FSImageURL_Global = null;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error writing document", e);
                            Toast.makeText(addItem.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });






        }

    }

    //Picasso Image Viewer
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath= data.getData();

            Picasso.get().load(filepath).into(itemImageView);
            itemImageView.setImageURI(filepath);
            //Toast.makeText(addItem.this, filepath + "", Toast.LENGTH_SHORT).show();
        }
    }
}
