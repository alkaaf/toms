package com.andresual.dev.tms.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.andresual.dev.tms.Activity.Adapter.BaseRecyclerAdapter;
import com.andresual.dev.tms.Activity.Adapter.GalleryAdapter;
import com.andresual.dev.tms.Activity.Model.SimpleJob;
import com.andresual.dev.tms.R;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityUpload extends BaseActivity {
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

    ChildEventListener cel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.i("CHILDADD", dataSnapshot.getValue().toString());
            photoUrl.add(dataSnapshot.getValue().toString());
            adapter.notifyDataSetChanged();
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Upload gambar");

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
                        .setProgressBarImage(R.drawable.ic_image_black_24dp)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE1: {
                if (resultCode == RESULT_OK) {
                    doUpload(data);
                }
            }
        }
    }

    public void doUpload(Intent data) {
//        Bitmap bm = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] buff = stream.toByteArray();
        pd = new ProgressDialog(this);
        pd.show();
        pd.setCancelable(false);

        storef.child("job_" + simpleJob.getJobId() + "_" + System.currentTimeMillis()).putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.hide();
                dbref.push().setValue(taskSnapshot.getMetadata().getDownloadUrl().toString());
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
                pd.setMessage("Mengunggah... (" + Math.round(100*((float)taskSnapshot.getBytesTransferred()/(float)taskSnapshot.getTotalByteCount())) + "%)");
            }
        });
    }

    public static void start(Context context, SimpleJob simpleJob) {
        Intent intent = new Intent(context, ActivityUpload.class);
        intent.putExtra(INTENT_DATA, simpleJob);
        context.startActivity(intent);
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
}
