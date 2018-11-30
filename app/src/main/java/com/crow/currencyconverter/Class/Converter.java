package com.crow.currencyconverter.Class;

import android.app.AlertDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.R;
import com.crow.currencyconverter.Rate.RateEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Converter
{
	/*
		Updated
		2018-11-18 at 17:00 UTC
	 */

	/*
		Rates from EUR
		(This will be replaced by refresh())
	 */
	private static float eur = 1f;
	private static float sek = 10.28f;
	private static float usd = 1.14f;
	private static float gbp = 0.89f;
	private static float cny = 7.92f;
	private static float jpy = 128.79f;
	private static float krw = 1279.52f;

	// When we last updated currency values (timestamp)
	private static long lastUpdate;

	// Minutes delay (default off)
	private static int minuteDelay = 0;

	// For refreshing later from this class
	private static RequestQueue requestQueue;

	public static float convert(ECurrencies from, ECurrencies to, float amount)
	{
		// If they are the same, nothing as to be done
		if (from == to)
			return amount;

		/*
			Check if time to update currency
			Because this is done in the background,
			the app isn't slowed down
			(Disabled if minuteDelay is 0)
		 */
		if (minuteDelay > 0 && System.currentTimeMillis() > lastUpdate + minuteDelay * 60000)
			refresh(null, requestQueue);

		// Base EUR
		float base = from == ECurrencies.EUR ? amount : convertToEur(from, amount);

		// Return converted
		switch (to)
		{
			case EUR: return base * eur;
			case SEK: return base * sek;
			case USD: return base * usd;
			case GBP: return base * gbp;
			case CNY: return base * cny;
			case JPY: return base * jpy;
			case KRW: return base * krw;
		}

		// If not returned yet, something went wrong
		throw new IllegalStateException("Unknown currency");
	}

	private static float convertToEur(ECurrencies from, float amount)
	{
		// Return converted
		switch (from)
		{
			case EUR: return amount / eur;
			case SEK: return amount / sek;
			case USD: return amount / usd;
			case GBP: return amount / gbp;
			case CNY: return amount / cny;
			case JPY: return amount / jpy;
			case KRW: return amount / krw;
		}

		// If not returned yet, something went wrong
		throw new IllegalStateException("Unknown currency");
	}

	public static ECurrencies fromString(String currency)
	{
		// The first 3 characters is the currency
		currency = currency.substring(0, 3);

		// Try and get
		switch (currency)
		{
			case "EUR": return ECurrencies.EUR;
			case "SEK": return ECurrencies.SEK;
			case "USD": return ECurrencies.USD;
			case "GBP": return ECurrencies.GBP;
			case "CNY": return ECurrencies.CNY;
			case "JPY": return ECurrencies.JPY;
			case "KRW": return ECurrencies.KRW;
		}

		// If not returned yet, something went wrong
		throw new IllegalStateException("Unknown currency");
	}

	public static ArrayList<RateEntry> getRates(Context context)
	{
		// Get all currencies saved in strings.xml
		String[] currencies = context.getResources().getStringArray(R.array.currencies);

		// Create list for our entries
		ArrayList<RateEntry> entries = new ArrayList<>();

		// Convert strings to entries
		for (String currency : currencies)
		{
			String[] parts = currency.split(" - ");
			entries.add(new RateEntry(parts[0], 0, parts[1]));
		}

		// Return new entries
		return entries;
	}

	// Refreshes currency values
	public static void refresh(Context context, RequestQueue requestQueue)
	{
		// Set request queue
		Converter.requestQueue = requestQueue;

		// URL to get new values from
		String url = "http://data.fixer.io/api/latest?access_key=0ee863af5ead09bc864944ba302b10b3";

		// Main request
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response ->
		{
			try
			{
				// Try to parse response as JSON
				JSONObject json = new JSONObject(response);

				// All currency values
				JSONObject rates = json.getJSONObject("rates");

				// Set values
				eur = (float) rates.getDouble("EUR");
				sek = (float) rates.getDouble("SEK");
				usd = (float) rates.getDouble("USD");
				gbp = (float) rates.getDouble("GBP");
				cny = (float) rates.getDouble("CNY");
				jpy = (float) rates.getDouble("JPY");
				krw = (float) rates.getDouble("KRW");

				// Update last update
				lastUpdate = System.currentTimeMillis();
			}
			catch (JSONException e)
			{
				// A JSON parse failed
				if (context != null)
					displayMessage(context, "JSON parse failed, using (some) old values");
			}
		}, error ->
		{
			// A HTTP Request failed
			if (context != null)
				displayMessage(context, "Request failed, using old values");
		});

		requestQueue.add(stringRequest);
	}

	private static void displayMessage(Context context, String message)
	{
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setPositiveButton(context.getString(R.string.generic_ok), (dialogInterface, i) -> {})
				.show();
	}

	public static void setMinuteDelay(int minutes)
	{
		minuteDelay = minutes;
	}
}
