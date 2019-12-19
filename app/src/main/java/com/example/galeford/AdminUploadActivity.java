package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.galeford.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminUploadActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MULTIPLE_IMG_GALLERY = 2;

    RadioGroup genderRadioBtn;
    RadioButton femaleRadioBtn, maleRadioBtn;
    Button uploadbtn, chooseMultipleImgBtn;
    ImageButton imageButton,imageButton1,imageButton2,imageButton3;
    ImageView imagedressview,imagedressview1,imagedressview2,imagedressview3;
    List<Uri> imagePreview = new ArrayList<>();
    EditText itemname,itemprice,itemdetails;
    ProgressBar productSubmitProgressBar;

    int fileUploadsCount = 0;

    String selectGender;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MULTIPLE_IMG_GALLERY &&
                resultCode == RESULT_OK) {

            ClipData clipData = data.getClipData();


            if (clipData != null && clipData.getItemCount() <= 4) {

                ImageButton[] ibs = {imageButton, imageButton1, imageButton2, imageButton3};
                ImageView[] ivs = {imagedressview, imagedressview1, imagedressview2, imagedressview3};

                for (int i=0; i<clipData.getItemCount(); i++) {
                    ibs[i].setVisibility(View.GONE);
                    ivs[i].setVisibility(View.VISIBLE);
                    Picasso.get().load(clipData.getItemAt(i).getUri()).into(ivs[i]);
                }


                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imagePreview.add(clipData.getItemAt(i).getUri());
                    Log.d("ADMIN_UPLAOD", clipData.getItemAt(i).getUri().toString());
                }

            }
            else {
                Toast.makeText(AdminUploadActivity.this, "images cannot be more than 4", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("productUploads");

        chooseMultipleImgBtn = findViewById(R.id.chooseMultipleImgBtn);

        imageButton = findViewById(R.id.imageButton);
        imagedressview = findViewById(R.id.imagedressview);

        imageButton1 = findViewById(R.id.imageButton1);
        imagedressview1 = findViewById(R.id.imagedressview1);

        imageButton2 = findViewById(R.id.imageButton2);
        imagedressview2 = findViewById(R.id.imagedressview2);


        imageButton3 = findViewById(R.id.productSortBtn);
        imagedressview3 = findViewById(R.id.imagedressview3);

        uploadbtn = findViewById(R.id.uploadbtn);

        itemname = findViewById(R.id.itemname);
        itemprice = findViewById(R.id.itemprice);
        itemdetails = findViewById(R.id.itemdetails);
        genderRadioBtn = findViewById(R.id.genderRadioBtn);
        productSubmitProgressBar = findViewById(R.id.productSubmitProgressBar);
        maleRadioBtn = findViewById(R.id.maleRadioBtn);
        femaleRadioBtn = findViewById(R.id.femaleRadioBtn);

        chooseMultipleImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUploadPreview();
            }
        });

        maleRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) selectGender = maleRadioBtn.getText().toString();
            }
        });


        femaleRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) selectGender = femaleRadioBtn.getText().toString();
            }
        });


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadbtn.setVisibility(View.GONE);
                productSubmitProgressBar.setVisibility(View.VISIBLE);
                storeImageToFirestorage();
            }
        });

    }

    private void imageUploadPreview(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select multiple images"), MULTIPLE_IMG_GALLERY);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    public void storeImageToFirestorage() {
        final Products product = new Products();

        final List<String> downloadImgUrls = new ArrayList<>();

        if (imagePreview.size() > 0) {
            for (Uri imgUri : imagePreview) {
                final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

                fileRef.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        fileUploadsCount += 1;

                        fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                downloadImgUrls.add(task.getResult().toString());

                                if (fileUploadsCount == imagePreview.size()) {
                                    Log.d("ADMIN_UPLOAD", downloadImgUrls.toString());
                                    Log.d("ADMIN_COUNT", Integer.toString(fileUploadsCount));

                                    product.setProductImage(downloadImgUrls);

                                    insertToDatabase(product);
                                }

                            }
                        });

                    }
                });


            }

        }

    }




    public void insertToDatabase(final Products product) {

        product.setProductItemName(itemname.getText().toString());
        product.setProductItemPrice(itemprice.getText().toString());
        product.setProductItemDescription(itemdetails.getText().toString());
        product.setProductGender(selectGender);
        product.setProductTimeStamp(Timestamp.now());
        product.setUserLikedCount(0);

        firestore.collection("products").add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        uploadbtn.setVisibility(View.VISIBLE);
                        imageButton.setVisibility(View.VISIBLE);
                        imageButton1.setVisibility(View.VISIBLE);
                        imageButton2.setVisibility(View.VISIBLE);
                        imageButton3.setVisibility(View.VISIBLE);
                        productSubmitProgressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                AdminUploadActivity.this,
                                "Product saved SUCCESS",
                                Toast.LENGTH_LONG
                        ).show();

                        Uri imguri = null;
                        Picasso.get().load(imguri).into(imagedressview);
                        Picasso.get().load(imguri).into(imagedressview1);
                        Picasso.get().load(imguri).into(imagedressview2);
                        Picasso.get().load(imguri).into(imagedressview3);

                        itemname.setText("");
                        itemprice.setText("");
                        itemdetails.setText("");

                        genderRadioBtn.clearCheck();

                        selectGender = null;
                    }
                });
    }

}
