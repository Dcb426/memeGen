package cs316.project_1.memegen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class start_Up extends AppCompatActivity {

    // Creating button.
    ImageButton Uploadpage;
    ImageButton Uploadpage2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__up);

        //Move to UploadPage
        final Intent moveToUploadPage = new Intent(this, pickingImage.class);
        Uploadpage = (ImageButton) findViewById(R.id.uploadPicture);
        Uploadpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(moveToUploadPage);
            }
        });

        final Intent moveToUploadPage2 = new Intent(this, DIA.class);
        Uploadpage2 = (ImageButton) findViewById(R.id.uploadPicture2);
        Uploadpage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(moveToUploadPage2);
            }
        });


    }
}
