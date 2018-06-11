package com.spil.dev.tms.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spil.dev.tms.Activity.Adapter.SebelumnyaListAdapter;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UploadFotoActivity extends AppCompatActivity {

    private Button btnSave;
    private ImageView imageView8;
    private TextView textView3;
    private ImageView ivPhoto1;
    private ImageView ivPhoto2;
    private static final int REQUEST_IMAGE1 = 111;
    private static final int REQUEST_IMAGE2 = 112;
    Bitmap imageBitmap1, imageBitmap2;
    Uri mImageUri1, mImageUri2;
    byte[] byteArray1, byteArray2;
    String encodedImage1, encodedImage2, idAdmin1, idAdmin2;
    String generatedFilePath;
    SimpleJob modelData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_upload_foto);
        modelData = getIntent().getParcelableExtra(SebelumnyaListAdapter.INTENT_DATA);
        initView();

        ivPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera1();
            }
        });

        ivPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera2();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile1();
                uploadFile2();
            }
        });

        if (ivPhoto1.isActivated()) {
            ivPhoto2.setVisibility(View.VISIBLE);
        } else {
            ivPhoto2.setVisibility(View.INVISIBLE);
        }
    }

    public void onLaunchCamera1() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE1);
        }
    }

    public void onLaunchCamera2() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap1 = (Bitmap) extras.get("data");
            ivPhoto1.setImageBitmap(imageBitmap1);
            ivPhoto1.setImageURI(mImageUri1);
            mImageUri1 = data.getData();
            Log.i("onActivityResult: ", String.valueOf(mImageUri1));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray1 = stream.toByteArray();
            encodedImage1 = Base64.encodeToString(byteArray1, Base64.DEFAULT);

            uploadFile1();
        } else if (requestCode == REQUEST_IMAGE2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap2 = (Bitmap) extras.get("data");
            ivPhoto2.setImageBitmap(imageBitmap2);
            ivPhoto2.setImageURI(mImageUri2);
            mImageUri2 = data.getData();
            Log.i("onActivityResult: ", String.valueOf(mImageUri2));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray2 = stream.toByteArray();
            encodedImage2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
            uploadFile2();
        }
    }

    private void uploadFile1() {

        if (byteArray1 != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            final StorageReference riversRef = storageReference.child("end_job/" + modelData.getJobId()+".jpg");
            riversRef.putBytes(byteArray1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            generatedFilePath = downloadUri.toString();
                            Log.i("onSuccess: ", generatedFilePath);
                            progressDialog.dismiss();


                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UploadFotoActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    Uri downloadUri = riversRef.getMetadata().getDownloadUrl();
//                    generatedFilePath = downloadUri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(this, "uri kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile2() {

        if (byteArray2 != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            final StorageReference riversRef = storageReference.child("end_job/" + "container2.jpg");
            riversRef.putBytes(byteArray2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                            generatedFilePath = downloadUri.toString();
                            Log.i("onSuccess: ", generatedFilePath);
                            progressDialog.dismiss();


                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    Uri downloadUri = riversRef.getMetadata().getDownloadUrl();
//                    generatedFilePath = downloadUri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(this, "uri kosong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initView() {
        btnSave = findViewById(R.id.btn_save);
        imageView8 = findViewById(R.id.imageView8);
        textView3 = findViewById(R.id.textView3);
        ivPhoto1 = findViewById(R.id.iv_photo1);
        ivPhoto2 = findViewById(R.id.iv_photo2);
    }
}
