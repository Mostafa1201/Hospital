package com.example.ospidalia2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noura_000 on 8/19/2017.
 */

public class DataParser {

    private HashMap getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String longitude = "";
        String latitude = "";
        String reference = "";

            try {
                if(!googlePlaceJson.isNull("name")) {
                    placeName = googlePlaceJson.getString("name");
                }
                if(!googlePlaceJson.isNull("vicinity"))
                {
                    vicinity = googlePlaceJson.getString("vicinity");
                }
                latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = googlePlaceJson.getString("reference");

                googlePlaceMap.put("place_name" , placeName);
                googlePlaceMap.put("vicinity" , vicinity);
                googlePlaceMap.put("lat" , latitude);
                googlePlaceMap.put("lng" , longitude);
                googlePlaceMap.put("reference" , reference);
                Log.d("getPlace", "Putting Places");//
            }
            catch (JSONException e) {
                Log.d("getPlace", "Error");//
                e.printStackTrace();

        }
        return googlePlaceMap;
    }
    private List<HashMap<String,String>> getPlaces(JSONArray jsonArr)
    {
        int count = jsonArr.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placesMap = null;
        Log.d("Places", "getPlaces");//
        for(int i = 0 ; i < count ; i++)
        {
            try
            {
                placesMap = getPlace((JSONObject) jsonArr.get(i));
                placesList.add(placesMap);
                Log.d("Places", "Adding places");//
            }
            catch (JSONException e)
            {
                Log.d("Places", "Error in Adding places");//
                e.printStackTrace();
            }
        }
        return placesList;
    }
    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArr = null;
        JSONObject jsonObj ;
        try {
            Log.d("Places", "parse");//
            jsonObj = new JSONObject((String)jsonData);
            jsonArr = jsonObj.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArr);
    }
}
