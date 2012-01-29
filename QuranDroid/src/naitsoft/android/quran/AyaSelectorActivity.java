package naitsoft.android.quran;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AyaSelectorActivity extends Activity {
	private static final int TTS_CHECK_CODE = 0;
	public static final int MARK_CODE = 1;

private Spinner suraSpinner;
private SeekBar ayaSeekBar;
private DataBaseHelper dbHelper;
private int ayaCount;
	static int aya = 1;
	static int sura = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ayaSeekBar = (SeekBar) findViewById(R.id.ayaSeekBar);
		suraSpinner = (Spinner) findViewById(R.id.suraSpinner);
		
		ayaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				aya= progress;
			}
		});
		dbHelper = DataBaseHelper.getInstance(this);
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		
		for(int i = 1; i<115 ; i++)
			spinAdapter.add(String.valueOf(i));
		suraSpinner.setAdapter(spinAdapter );
		suraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sura = position+1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		initSeekBar();
	}
	private void initSeekBar(){
//		ayaCount = dbHelper.getAyaCount(suraSpinner.getSelectedItemPosition());
		ayaSeekBar.setMax(ayaCount);
		
	}
}

