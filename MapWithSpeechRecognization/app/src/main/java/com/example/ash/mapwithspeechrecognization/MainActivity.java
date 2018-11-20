package com.example.ash.mapwithspeechrecognization;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ash.mapwithspeechrecognization.Model.CountryDataSource;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SPEAK_REQUEST = 10;
    private TextView txt_value;
    private Button btn_voice_intent;

    public static CountryDataSource countryDataSource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_value = findViewById(R.id.txtValue);
        btn_voice_intent = findViewById(R.id.btnVoiceIntent);

        btn_voice_intent.setOnClickListener(this);

        Hashtable<String, String> countriesAndMessage = new Hashtable<>();
        countriesAndMessage.put("Bangladesh", "Welcome to Bangladesh, Happy visiting.");
        countriesAndMessage.put("Canada", "Welcome to Canada, Happy visiting.");
        countriesAndMessage.put("France", "Welcome to France, Happy visiting.");
        countriesAndMessage.put("Brazil", "Welcome to Brazil, Happy visiting.");
        countriesAndMessage.put("United States", "Welcome to United States, Happy visiting.");
        countriesAndMessage.put("Japan", "Welcome to Japan, Happy visiting.");
        countriesAndMessage.put("India", "Welcome to India, Happy visiting.");
        countriesAndMessage.put("China", "Welcome to China, Happy visiting.");
        countriesAndMessage.put("Pakistan", "Welcome to Pakistan, Happy visiting.");
        countriesAndMessage.put("Australia", "Welcome to Australia, Happy visiting.");
        countriesAndMessage.put("Switzerland", "Welcome to Switzerland, Happy visiting.");
        countriesAndMessage.put("America", "Welcome to America, Happy visiting.");
        countriesAndMessage.put("Mexico", "Welcome to Mexico, Happy visiting.");
        countriesAndMessage.put("Cambodia", "Welcome to Cambodia, Happy visiting.");
        countriesAndMessage.put("Iceland", "Welcome to Iceland, Happy visiting.");
        countriesAndMessage.put("Saudi Arabia", "Welcome to Saudi Arabia, Happy visiting.");
        countriesAndMessage.put("South Korea", "Welcome to South Korea, Happy visiting.");
        countriesAndMessage.put("Greece", "Welcome to Greece, Happy visiting.");
        countriesAndMessage.put("South Africa", "Welcome to South Africa, Happy visiting.");




        countryDataSource = new CountryDataSource(countriesAndMessage);

        PackageManager packageManager = this.getPackageManager();
        List<ResolveInfo> listOfInformation = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(listOfInformation.size() > 0){

            Toast.makeText(this, "Your device does support speech Recognition!", Toast.LENGTH_SHORT).show();
            listenToTheUserVoice();

        } else{

            Toast.makeText(this, "Your device doesn't support speech Recognition!", Toast.LENGTH_SHORT).show();
        }
    }

    private void listenToTheUserVoice(){

        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to Me!");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(voiceIntent, SPEAK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SPEAK_REQUEST && resultCode == RESULT_OK){

            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            String countryMatchedWithUserWord = countryDataSource.matchWithMinimumConfidenceLevelOfUserWords(voiceWords, confidLevels);

            Intent myMapIntent = new Intent(this, MapsActivity.class);
                myMapIntent.putExtra(CountryDataSource.COUNTRY_KEY, countryMatchedWithUserWord);
                startActivity(myMapIntent);
        }
    }

    @Override
    public void onClick(View view) {

        listenToTheUserVoice();
    }
}
