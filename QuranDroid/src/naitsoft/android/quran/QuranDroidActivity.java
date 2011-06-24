package naitsoft.android.quran;

import android.app.Activity;
import android.os.Bundle;

public class QuranDroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuranView qv = new QuranView(this);
        
        setContentView(qv);
    }
}