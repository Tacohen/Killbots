package com.tacohen.killbots.UI;

import com.tacohen.killbots.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Settings extends Activity{

	private Context context;
	RadioGroup radioGroup;
	RadioGroup radioGroupPlayerNumber;
	RadioGroup radioGroupMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		context = this;

		radioGroup = (RadioGroup)findViewById(R.id.radioGroupUnits);
		radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
		
		radioGroupPlayerNumber = (RadioGroup)findViewById(R.id.radioGroupUnitsPlayerNumber);
		radioGroupPlayerNumber.setOnCheckedChangeListener(radioGroupOnCheckedChangeListenerPlayerNumber);
		
		radioGroupMusic = (RadioGroup)findViewById(R.id.radioGroupMusic);
		radioGroupMusic.setOnCheckedChangeListener(radioGroupOnCheckedChangeListenerMusic);
		
		LoadPreferences();


	}

	OnCheckedChangeListener radioGroupOnCheckedChangeListener =
			new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
			int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

			//User selected local
			if (checkedIndex == 1){
				SavePreferences("multiplayer", true);
			}

			//User selected multiplayer
			if (checkedIndex == 0){
				SavePreferences("multiplayer", false);
			}

		}};
		
		OnCheckedChangeListener radioGroupOnCheckedChangeListenerPlayerNumber =
				new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				RadioButton checkedRadioButton = (RadioButton)radioGroupPlayerNumber.findViewById(checkedId);
				int checkedIndex = radioGroupPlayerNumber.indexOfChild(checkedRadioButton);

				//User selected 1
				if (checkedIndex == 0){
					SavePreferences("playerNumber", 1);
				}

				//User selected 2
				if (checkedIndex == 1){
					SavePreferences("playerNumber", 2);
				}

			}};
			
			OnCheckedChangeListener radioGroupOnCheckedChangeListenerMusic =
					new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					RadioButton checkedRadioButton = (RadioButton)radioGroupMusic.findViewById(checkedId);
					int checkedIndex = radioGroupMusic.indexOfChild(checkedRadioButton);

					//User selected off
					if (checkedIndex == 1){
						SavePreferences("musicOn", false);
					}

					//User selected on
					if (checkedIndex == 0){
						SavePreferences("musicOn", true);
					}

				}};



		private void SavePreferences(String key, boolean b){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, b);
			editor.commit(); 
		}
		
		private void SavePreferences(String key, int i){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt(key, i);
			editor.commit(); 
		}

		private void LoadPreferences(){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean using_multiplayer = sharedPreferences.getBoolean("multiplayer", false);
			int savedRadioIndex = 0;//default (local)
			if (using_multiplayer){
				savedRadioIndex = 1;
			}
			boolean musicOn = sharedPreferences.getBoolean("musicOn", true);
			int savedRadioIndex3 = 0;//default (musicOn)
			if (!musicOn){
				savedRadioIndex3 = 1;
			}
			int playerNumber = sharedPreferences.getInt("playerNumber", 1);
			int savedRadioIndex2 = 0;//default (player1)
			if (playerNumber == 2){
				savedRadioIndex2 = 1;
			}
			RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(savedRadioIndex);
			savedCheckedRadioButton.setChecked(true);
			RadioButton savedCheckedRadioButtonPlayer = (RadioButton) radioGroupPlayerNumber.getChildAt(savedRadioIndex2);
			savedCheckedRadioButtonPlayer.setChecked(true);
			RadioButton savedCheckedRadioButtonMusic = (RadioButton) radioGroupMusic.getChildAt(savedRadioIndex3);
			savedCheckedRadioButtonMusic.setChecked(true);
		}


}
