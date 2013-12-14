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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		context = this;

		radioGroup = (RadioGroup)findViewById(R.id.radioGroupUnits);
		radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);


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


		private void SavePreferences(String key, boolean b){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, b);
			editor.commit(); 
		}

		private void LoadPreferences(){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean using_multiplayer = sharedPreferences.getBoolean("multiplayer", false);
			int savedRadioIndex = 0;//default (local)
			if (using_multiplayer){
				savedRadioIndex = 1;
			}
			RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(savedRadioIndex);
			savedCheckedRadioButton.setChecked(true);
		}


}
