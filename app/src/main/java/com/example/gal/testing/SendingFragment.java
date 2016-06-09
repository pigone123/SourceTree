package com.example.gal.testing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by gal on 02/06/2016.
 */
public class SendingFragment extends SingleFragment implements View.OnClickListener {
    private static final int SELECT_PICTURE = 1;
    private EditText details;
    private Button pick, upload, download;
    private FirebaseUser user;
    private Bitmap bitmap, myBitmap;
    private ImageView imageView, dImage;
    private StorageReference imagesRef, storageRef;
    private StorageMetadata metadata;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = storage.getReferenceFromUrl("gs://testing-68ab9.appspot.com");
        imagesRef = storageRef.child("Images/Dog");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sending, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        imageView = (ImageView) v.findViewById(R.id.image);
        dImage = (ImageView) v.findViewById(R.id.dImage);
        details = (EditText) v.findViewById(R.id.details);
        pick = (Button) v.findViewById(R.id.pick);
        download = (Button) v.findViewById(R.id.download);
        upload = (Button) v.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        download.setOnClickListener(this);
        pick.setOnClickListener(this);
    }

    @Override
    public void onClick(View g) {
        switch (g.getId()) {
            case R.id.pick:
                picker();
                break;
            case R.id.download:
                download();
                break;
            case R.id.upload:
                upload();
                break;
        }

    }

    public void picker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK && requestCode == SELECT_PICTURE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Toast.makeText(getActivity(), picturePath, Toast.LENGTH_SHORT).show();
            bitmap = BitmapFactory.decodeFile(picturePath);
            try {
                ExifInterface exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                myBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
            } catch (Exception e) {

            }
            imageView.setImageBitmap(myBitmap);
        }
    }

    public void upload() {
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("details", details.getText().toString())
                .build();

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = imagesRef.putBytes(data, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    public void download() {
        final long ONE_MEGABYTE = 1024 * 1024;
        imagesRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Toast.makeText(getActivity(), storageMetadata.getCustomMetadata("details"), Toast.LENGTH_SHORT).show();
                // Metadata now contains the metadata for 'images/forest.jpg'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                dImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
