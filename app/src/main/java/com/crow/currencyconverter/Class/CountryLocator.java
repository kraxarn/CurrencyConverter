package com.crow.currencyconverter.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.Listener.LocationUpdatedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryLocator
{
	private static String country;

	private static ArrayList<LocationUpdatedListener> listeners;

	public static ECurrencies getCurrency()
	{
		switch (country)
		{
			case "SE": return ECurrencies.SEK;
			case "US": return ECurrencies.USD;
			case "GB": return ECurrencies.GBP;
			case "CN": return ECurrencies.CNY;
			case "JP": return ECurrencies.JPY;
			case "KP": return ECurrencies.KRW;

			default: return ECurrencies.EUR;
		}
	}

	// Loads values from cache
	public static void load(SharedPreferences preferences)
	{
		country = preferences.getString("cache_country", "??");
	}

	private static void save(SharedPreferences preferences)
	{
		// Create editor
		SharedPreferences.Editor editor = preferences.edit();

		// Save new value
		editor.putString("cache_country", country);

		// Save
		editor.apply();
	}

	public static void refresh(Context context, RequestQueue requestQueue)
	{
		StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://ipinfo.io/json", response ->
		{
			try
			{
				// Try to parse response as JSON
				JSONObject json = new JSONObject(response);

				// Find country
				country = json.getString("country");

				// Trigger listeners
				if (listeners != null)
				{
					for (LocationUpdatedListener listener : listeners)
						listener.onSetCurrency(getCurrency());
				}

				// Save to preferences
				if (context != null)
					save(PreferenceManager.getDefaultSharedPreferences(context));
			}
			catch (JSONException e)
			{
				Log.e("JSON Error", e.getMessage());
			}
		}, error ->
				Log.e("Request Error", error.getMessage()));

		// Send request
		requestQueue.add(stringRequest);
	}

	public static void addListener(LocationUpdatedListener listener)
	{
		if (listeners == null)
			listeners = new ArrayList<>();

		listeners.add(listener);
	}
}