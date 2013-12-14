package com.tacohen.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
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
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://killbots.herokuapp.com/scores/getPlace?score="+score);
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
			int place = jObj.getInt("place");
			Log.i(TAG, "place is: "+place);
			in.close();

			return place;


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

		Log.e(TAG, "Could not get place, asumming 6th!");
		return 6;//Placeholder if we can't actually get the value


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
	
	/**
	 * Returns true if score is a high score, false otherwise
	 * @param score
	 */
	public Boolean isHighScore(Integer score){
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://killbots.herokuapp.com/scores/isScoreHigh?score="+score);
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
			boolean isScoreHigh = jObj.getBoolean("isHighScore");
			Log.i(TAG, "is score high: "+isScoreHigh);
			in.close();

			return isScoreHigh;


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

		Log.e(TAG, "Could not get high score, asumming false!");
		return false;//Placeholder if we can't actually get the value


	}
	
	
	public void sendHighScore(String name, int score, int place ){
		try {
			//tacohen note: login should be something like: /users/login?username=admin&lat=31&lng=30&password=123

			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/scores/addScore?score="+score+"&name="+name+"&place="+place);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	public void sendPlayerLocation(int playerNumber, int xLoc, int yLoc ){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/locations/updatePlayer?playerNumber="+playerNumber+"&xLoc="+xLoc+"&yLoc="+yLoc);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void addPlayer(int playerNumber, int xLoc, int yLoc ){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/players/addPlayer?playerNumber="+playerNumber+"&xLoc="+xLoc+"&yLoc="+yLoc);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void sendRobotLocation(int robotNumber, int xLoc, int yLoc ){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/locations/updateRobot?robotNumber="+robotNumber+"&xLoc="+xLoc+"&yLoc="+yLoc);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void addRobot(int robotNumber, int xLoc, int yLoc ){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/robot/addRobot?robotNumber="+robotNumber+"&xLoc="+xLoc+"&yLoc="+yLoc);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public Pair<Integer,Integer> getRobotLocation(int robotNumber){
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://killbots.herokuapp.com/robot/getRobot?robotNumber="+robotNumber);
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
			Integer xLoc = jObj.getInt("xLoc");
			Integer yLoc = jObj.getInt("yLoc");
			Log.i(TAG, "robot number is: "+robotNumber);
			Log.i(TAG, "xLoc is: "+xLoc);
			Log.i(TAG, "yLoc is: "+yLoc);
			in.close();

			return Pair.create(xLoc, yLoc);


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

		Log.e(TAG, "Could not get robot location, asumming (5,5)!");
		return Pair.create(5, 5);
	}
	
	public Pair<Integer,Integer> getPlayerLocation(int playerNumber){
		try{
			HttpClient client = new DefaultHttpClient();
			URI website = new URI("http://killbots.herokuapp.com/player/getPlayer?playerNumber="+playerNumber);
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
			Integer xLoc = jObj.getInt("xLoc");
			Integer yLoc = jObj.getInt("yLoc");
			Log.i(TAG, "player number is: "+playerNumber);
			Log.i(TAG, "xLoc is: "+xLoc);
			Log.i(TAG, "yLoc is: "+yLoc);
			in.close();

			return Pair.create(xLoc, yLoc);


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

		Log.e(TAG, "Could not get player location, asumming (5,5)!");
		return Pair.create(5, 5);
	}
	
	public void killRobot(Integer robotNumber){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://killbots.herokuapp.com/robots/killRobot?robotNumber="+robotNumber);
			httpPost.setHeader(new BasicHeader("Content-type", "application/json"));
			HttpResponse response = client.execute(httpPost);
			Log.i(TAG, "URI is: "+httpPost.getURI());
			StatusLine statusLine = response.getStatusLine();
			Log.i(TAG, "HTTP response code was: "+statusLine.toString());
			if (statusLine.toString().equals("HTTP/1.1 200 OK")){

			}
			else{
				Log.e(TAG, "HTTP problem!");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
