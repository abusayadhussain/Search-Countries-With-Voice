package com.example.ash.mapwithspeechrecognization.Model;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {

    public static final String COUNTRY_KEY = "country";
    public static final float MINIMUM_CONFIDENCE_LEVEL = 0.4f;
    public static final String DEFAULT_COUNTRY_NAME = "Bangladesh";
    public static final double DEFAULT_COUNTRY_LATITUDE = 24.313564;
    public static final double DEFAULT_COUNTRY_LONGITUDE = 89.9896323;
    public static final String DEFAULT_MESSAGE = "Be Happy!";

    private Hashtable<String, String> countriesAndMessages;

    public CountryDataSource(Hashtable<String, String> countriesAndMessages){

        this.countriesAndMessages = countriesAndMessages;

    }

    public String matchWithMinimumConfidenceLevelOfUserWords(ArrayList<String> userWords, float[] confidenceLevels){

        if(userWords == null || confidenceLevels == null){

            return DEFAULT_COUNTRY_NAME;
        }

        int numberOfUserWords = userWords.size();

        Enumeration<String> countries;

        for(int index = 0; index < numberOfUserWords && index < confidenceLevels.length; index++){

            if(confidenceLevels[index] < MINIMUM_CONFIDENCE_LEVEL){

                break;

            }

            String acceptedUserWord = userWords.get(index);

            countries = countriesAndMessages.keys();

            while (countries.hasMoreElements()){

                String selectedCountry = countries.nextElement();

                if(acceptedUserWord.equalsIgnoreCase(selectedCountry)){

                    return  acceptedUserWord;

                }
            }
        }

        return DEFAULT_COUNTRY_NAME;


    }

    public String getTheInfoOfTheCountry(String country){


        return countriesAndMessages.get(country);

    }
}
