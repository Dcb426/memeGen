package cs316.project_1.memegen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


//import com.bumptech.glide.Glide;


public class Library extends AppCompatActivity {
    public ImageView imageView;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    List<ImageUploadInfo> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        imageView = findViewById(R.id.image);
        databaseReference = FirebaseDatabase.getInstance().getReference(pickingImage.Database_Path);

        String url = "http://wallpaper-gallery.net/images/cool-pictures/cool-pictures-2.jpg";
        Glide.with(getApplicationContext()).load(url).into(imageView);
    }


}
