package cs316.project_1.memegen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class pickingImage extends AppCompatActivity {
    private static  final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button accept_photo;
    private String filepath;
    private EditText topLevel;
    private EditText bottomLevel;
    private TextView topLevelPic;
    private TextView bottomLevelPic;

    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    // Root Database Name for Firebase Database.
    static String Database_Path = "All_Image_Uploads_Database";
    // Creating button.
    Button UploadButton;
    // Creating URI.
    Uri FilePathUri;
    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    Bitmap map;
    private String fname;


    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_capture);
        imageView = (ImageView) findViewById(R.id.result);
        accept_photo = (Button) findViewById(R.id.accept);
        topLevel = (EditText) findViewById(R.id.editText);
        bottomLevel = (EditText) findViewById(R.id.editText2);
        topLevelPic = (TextView) findViewById(R.id.top);
        bottomLevelPic = (TextView) findViewById(R.id.bottom);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);
        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(pickingImage.this);

        accept_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.getDrawable() == null) {
                    Toast.makeText(pickingImage.this, "Please Select Image or Add Image", Toast.LENGTH_LONG).show();
                } else {
                    topLevelPic.setDrawingCacheEnabled(true);
                    bottomLevelPic.setDrawingCacheEnabled(true);
                    Bitmap bmp = Bitmap.createBitmap(topLevelPic.getDrawingCache(true ));
                    Bitmap bmp2 = Bitmap.createBitmap(bottomLevelPic.getDrawingCache(true));
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    Bitmap combined = combineImages(bitmap,bmp,bmp2);
                    map = combined;
                    imageView.setImageResource(android.R.color.transparent);
                    bottomLevelPic.setVisibility(View.INVISIBLE);
                    topLevelPic.setVisibility(View.INVISIBLE);
                    imageView.setImageBitmap(combined);
                    saveImageToExternalStorage(combined);
                }
            }
        });
        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();
                sharePhoto(map);

            }
        });
        topLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = topLevel.getText().toString();
                topLevelPic.setText(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = topLevel.getText().toString();
                topLevelPic.setText(input);
            }
        });


        bottomLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = bottomLevel.getText().toString();
                bottomLevelPic.setText(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = bottomLevel.getText().toString();
                bottomLevelPic.setText(input);
            }
        });
    }
    public void onClick(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        Uri uri = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        try {
            // recyle unused bitmaps
            if (bitmap != null) {
                bitmap.recycle();
            }

            Uri fileUri = data.getData();
            assert fileUri != null;
            stream = getContentResolver().openInputStream(fileUri);
            bitmap = BitmapFactory.decodeStream(stream);
            //ExifInterface exif = new ExifInterface(stream);
            //int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //int rotationInDegrees = exifToDegrees(rotation);
            //Matrix matrix = new Matrix();
            //matrix.postRotate(90);
            //Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bottomLevelPic.setMinWidth(bitmap.getWidth());
            topLevelPic.setMinWidth(bitmap.getWidth());
            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bitmap combineImages(Bitmap background, Bitmap foreground, Bitmap forground2) {

        int width = 0, height = 0;
        Bitmap cs;

        width = imageView.getWidth();
        height = imageView.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        comboImage.drawBitmap(background, 0, 0, null);
        comboImage.drawBitmap(foreground, 0,0, null);
        comboImage.drawBitmap(forground2, 0,725, null);

        return cs;
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = fname.trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")

                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(pickingImage.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(pickingImage.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images_1");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                        FilePathUri = uri;
                    }
                });

    }
    public void sharePhoto(Bitmap look)
    {
        String type = "image/*";
        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(String.valueOf(FilePathUri));
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

    }
}
