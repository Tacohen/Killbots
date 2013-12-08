package com.tacohen.cloud;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.HashMultimap;

import android.util.Log;
import android.util.Pair;

public class Cloud {
	
	private static String TAG = "Cloud";
	static JSONObject jObj = null;
	
	public Cloud(){
		super();
	}
	
	public Integer getPlace(Integer score){
		
		return 6;
	}
	
	public HashMultimap<Integer, Pair<String,Integer>> getHighScores(){
		HashMultimap<Integer, Pair<String,Integer>> fullMap = HashMultimap.create(5, 2);
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://killbots.herokuapp.com/scores/HighScores");
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);
			response.getStatusLine().getStatusCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) !=null){
				sb.append(l + nl);
			}
			
			Log.i(TAG, "response from server was: "+sb);

			// try parse the string to a JSON object
			try {
				jObj = new JSONObject(sb.toString());
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			Log.i(TAG, "jObj is: "+jObj);
			JSONArray names = jObj.names();
			for (int i=0;i<names.length();i++){
				JSONObject j = new JSONObject();
				//j = jObj.getJSONObject(names.getString(i));
				String innerJSON = jObj.getString(names.getString(i));
				
				JSONObject innerJson = new JSONObject(innerJSON);
				Log.i(TAG, "Inner JSON is: "+innerJson.toString());
				
				fullMap.put(innerJson.getInt("place"), new Pair<String,Integer>(innerJson.getString("name"),innerJson.getInt("score")));
			}
			in.close();
			


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 

		
		return fullMap;
	}

}
