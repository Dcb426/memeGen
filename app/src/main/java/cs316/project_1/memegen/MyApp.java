package cs316.project_1.memegen;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by natta on 2/12/2018.
 */

public class MyApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
