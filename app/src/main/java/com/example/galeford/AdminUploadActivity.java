package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.galeford.models.Products;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminUploadActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    Button uploadbtn;
    ImageButton imageButton;
    ImageView imagedressview;
    Uri imagePreview;
    EditText itemname,itemprice,itemdetails;
    ProgressBar productSubmitProgressBar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            imagePreview = data.getData();

            imageButton.setVisibility(imageButton.GONE);
            imagedressview.setVisibility(imagedressview.VISIBLE);

            Picasso.get().load(imagePreview).into(imagedressview);
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

        imageButton = findViewById(R.id.imageButton);
        imagedressview = findViewById(R.id.imagedressview);
        uploadbtn = findViewById(R.id.uploadbtn);
        itemname = findViewById(R.id.itemname);
        itemprice = findViewById(R.id.itemprice);
        itemdetails = findViewById(R.id.itemdetails);
        productSubmitProgressBar = findViewById(R.id.productSubmitProgressBar);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageButton.setVisibility(imageButton.VISIBLE);
                imagedressview.setVisibility(imagedressview.GONE);

                imageUploadPreview();
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
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    public void storeImageToFirestorage() {
        final Products product = new Products();

        if (imagePreview != null) {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imagePreview));

            fileRef.putFile(imagePreview).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                product.setProductImage(downloadUri.toString());
                                product.setProductItemName(itemname.getText().toString());


                                firestore.collection("products").add(product)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                uploadbtn.setVisibility(View.VISIBLE);
                                                productSubmitProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(
                                                        AdminUploadActivity.this,
                                                        "Product saved SUCCESS",
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}
