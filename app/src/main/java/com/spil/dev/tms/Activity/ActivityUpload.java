package com.spil.dev.tms.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.spil.dev.tms.Activity.Adapter.BaseRecyclerAdapter;
import com.spil.dev.tms.Activity.Adapter.GalleryAdapter;
import com.spil.dev.tms.Activity.Model.SimpleJob;
import com.spil.dev.tms.Activity.Util.QuickLocation;
import com.spil.dev.tms.R;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityUpload extends BaseActivity {
    public static final int REQ_CODE = 3;

    FirebaseDatabase fbdb;
    FirebaseStorage fbstor;
    DatabaseReference dbref;
    StorageReference storef;

    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;


    GalleryAdapter adapter;
    List<String> photoUrl = new ArrayList<>();

    public static final String INTENT_DATA = "datajob";
    SimpleJob simpleJob;

    String jobId;
    private final int REQUEST_IMAGE1 = 2;
    ProgressDialog pd;
    int counter = 0;

    ChildEventListener cel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.i("CHILDADD", dataSnapshot.getValue().toString());
            photoUrl.add(dataSnapshot.getValue().toString());
            adapter.notifyDataSetChanged();
            setOkResult();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.i("CHILDCHANGE", dataSnapshot.getValue().toString());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void setOkResult() {
        setResult(Activity.RESULT_OK);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Job Gallery");

        simpleJob = getIntent().getParcelableExtra(INTENT_DATA);
        jobId = "photo/job_" + simpleJob.getJobId() + "/";
        fbdb = FirebaseDatabase.getInstance();
        fbstor = FirebaseStorage.getInstance();
        adapter = new GalleryAdapter(photoUrl);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                new ImageViewer.Builder<>(ActivityUpload.this, photoUrl).setCustomDraweeHierarchyBuilder(
                        GenericDraweeHierarchyBuilder.newInstance(getResources())
                                .setFailureImage(R.drawable.ic_broken_image_black_24dp)
                                .setProgressBarImage(R.drawable.ic_image_placeholder)
                                .setPlaceholderImage(R.drawable.ic_image_black_24dp)
                ).setStartPosition(pos).show();
            }
        });
        rvPhoto.setAdapter(adapter);
        rvPhoto.setLayoutManager(new GridLayoutManager(this, 3));

        dbref = fbdb.getReference(jobId);
        storef = fbstor.getReference(jobId);

        dbref.addChildEventListener(cel);

    }

    Uri file;

    public void takeCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE1);
        }
    }

    public void takeCameraWithCrop() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Tambah foto")
                .setRequestedSize(1980, 1080)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE1: {
                if (resultCode == RESULT_OK) {
//                    doUpload();
                    CropImage.activity(file).start(ActivityUpload.this);
                    // set the geotag

                }
                break;
            }
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE: {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    file = result.getUri();
                    QuickLocation.get(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            geoTag(file.getPath(), location.getLatitude(), location.getLongitude());
                        }
                    });
                    doUpload();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }

    public void doUpload() {

        pd = new ProgressDialog(this);
        pd.show();
        pd.setCancelable(false);

        storef.child("job_" + simpleJob.getJobId() + "_" + System.currentTimeMillis()).putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.hide();
                        dbref.push().setValue(taskSnapshot.getMetadata().getDownloadUrl().toString());
                        setOkResult();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ActivityUpload.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("FBUpload", "Uploading " + taskSnapshot.getBytesTransferred() + "/" + taskSnapshot.getTotalByteCount() + " B");
                pd.setMessage("Mengunggah... (" + Math.round(100 * ((float) taskSnapshot.getBytesTransferred() / (float) taskSnapshot.getTotalByteCount())) + "%)");
            }
        });
    }

    public static void start(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityUpload.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        ((Activity) context).startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_gambar) {
            takeCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ToMS");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public void geoTag(String filename, double latitude, double longitude) {
        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int) Math.floor(latitude);
            int num2Lat = (int) Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double) num1Lat + ((double) num2Lat / 60))) * 3600000;

            int num1Lon = (int) Math.floor(longitude);
            int num2Lon = (int) Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double) num1Lon + ((double) num2Lon / 60))) * 3600000;

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat + "/1," + num2Lat + "/1," + num3Lat + "/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon + "/1," + num2Lon + "/1," + num3Lon + "/1000");


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }
}
