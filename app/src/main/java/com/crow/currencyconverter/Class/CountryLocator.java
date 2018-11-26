package com.crow.currencyconverter.Class;

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
	private static String country = "AA";

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

	public static void refresh(RequestQueue requestQueue)
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