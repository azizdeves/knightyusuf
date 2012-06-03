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
import android.widget.Button;
import android.widget.EditText;
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
private EditText ayaTextSelect;
private Button goButton;
	static int aya = 1;
	static int sura = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selector);
		ayaSeekBar = (SeekBar) findViewById(R.id.ayaSeekBar);
		suraSpinner = (Spinner) findViewById(R.id.suraSpinner);
		ayaTextSelect = (EditText) findViewById(R.id.ayaTextSelect);
		goButton = (Button) findViewById(R.id.go_btn);
		
		Bundle bund = getIntent().getExtras();
		if(bund != null){
			aya = bund.getInt("aya");
			sura = bund.getInt("sura");
		}
		
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
				aya= progress+1;
				ayaTextSelect.setText(String.valueOf(aya));
			}
		});
		dbHelper = DataBaseHelper.getInstance(this);
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		
		for(int i = 1; i<115 ; i++)
			spinAdapter.add("	"+String.valueOf(i) +"	"+ QuranDroidActivity.suratName[i]+"	");
		suraSpinner.setAdapter(spinAdapter );
		suraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sura = position+1;
				aya = 1;
				initSeekBar();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		goButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendResult();
			}
		});
		
		suraSpinner.setSelection(sura-1);
		initSeekBar();
	}
	
	private void initSeekBar(){
//		ayaCount = 10 dbHelper.getAyaCount(suraSpinner.getSelectedItemPosition());
		ayaCount = QuranDroidActivity.nbrAyatBySura[suraSpinner.getSelectedItemPosition()+1];
		ayaSeekBar.setMax(ayaCount);
		ayaSeekBar.setProgress(aya-1);
		
	}
	
	protected void sendResult() {
		Intent intent = new Intent();
		intent.putExtra("sura",sura);
		intent.putExtra("aya", Integer.parseInt(ayaTextSelect.getText().toString()));
		setResult(QuranDroidActivity.MARK_CODE, intent);
		finish();		
	}
}

