package naitsoft.android.quran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class QuranDroidActivity extends Activity implements OnInitListener {
    private static final int MY_DATA_CHECK_CODE = 0;
	/** Called when the activity is first created. */
	
	private TextToSpeech mTts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
        QuranView qv = new QuranView(this);
        
        setContentView(qv);
    }
    
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

	@Override
	public void onInit(int status) {
		
		String myText1 = "\u0631\u064e\u0628\u0651\u0650\u064a \u0641\u0650\u064a \u0643\u0650\u062a\u064e\u0627\u0628\u064d " +
		"\u06d6 \u0644\u0651\u064e\u0627 \u064a\u064e\u0636\u0650\u0644\u0651\u0650 \u064f";
		String myText2 = "I hope so, because it's time to wake up.";
		mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);		
	}
}