package cs316.project_1.memegen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class start_Up extends AppCompatActivity {

    // Creating button.
    ImageButton Uploadpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__up);

        //Move to UploadPage
        final Intent moveToUploadPage = new Intent(this, upload_photos.class);
        Uploadpage = (ImageButton) findViewById(R.id.UploadPicture);
        Uploadpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(moveToUploadPage);
            }
        });
    }
}
