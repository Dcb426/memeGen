package cs316.project_1.memegen;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by natta on 2/12/2018.
 */

public class SplashActivity extends AppCompatActivity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            Intent intent = new Intent(this,start_Up.class);
            startActivity(intent);
            finish();
        }
}
