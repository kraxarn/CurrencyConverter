package com.crow.currencyconverter.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crow.currencyconverter.Class.Converter;
import com.crow.currencyconverter.Class.CountryLocator;
import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.Listener.LocationUpdatedListener;
import com.crow.currencyconverter.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class ConvertFragment extends Fragment implements LocationUpdatedListener
{
	private SharedPreferences preferences;

	private View view;

	// Preference related
	private boolean prefFormatFrom;
	private boolean prefFormatTo;
	private boolean prefUseSi;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_convert, container, false);

		// Get preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

		// Update preferences
		updatePrefs();

		// Simple error checking and to get rid of warnings
		if (getContext() == null)
			return view;

		// Get spinners
		final Spinner spinner0 = view.findViewById(R.id.spinner_currencies);
		final Spinner spinner1 = view.findViewById(R.id.spinner_currencies_alt);

		// Create array adapter from strings
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.currencies, android.R.layout.simple_spinner_item);

		// Specify layout
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply adapter to spinners
		spinner0.setAdapter(adapter);
		spinner1.setAdapter(adapter);

		// Set value from preferences
		spinner0.setSelection(preferences.getInt("currency_from", 0));
		spinner1.setSelection(preferences.getInt("currency_to",   0));

		// Set input listener
		EditText editAmount = view.findViewById(R.id.edit_amount);

		editAmount.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// Ignore if set not to format
				if (!prefFormatFrom)
					return;

				String amountText = editAmount.getText().toString();

				// Don't try parsing if empty
				if (amountText.length() <= 0)
					return;

				// Parse
				double amount = Double.parseDouble(amountText.replaceAll(",", ""));
				DecimalFormat format = new DecimalFormat("#,###.##");
				String formatted = format.format(amount);

				// Fix . at the end
				if (editAmount.getText().toString().endsWith("."))
					formatted += ".";

				// Change if not already changed
				if (!editAmount.getText().toString().equals(formatted))
				{
					editAmount.setText(formatted);
					editAmount.setSelection(editAmount.length());
				}
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				update();
			}
		});

		AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				update();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		};

		// Set
		spinner0.setOnItemSelectedListener(spinnerListener);
		spinner1.setOnItemSelectedListener(spinnerListener);

		// Swap Button
		view.findViewById(R.id.button_swap).setOnClickListener(v ->
		{
			// To widgets
			TextView editAmountAlt = view.findViewById(R.id.edit_amount_alt);

			// Save from values
			String fromAmount = editAmount.getText().toString();
			int fromCurrency  = spinner0.getSelectedItemPosition();

			// Replace from with to
			editAmount.setText(editAmountAlt.getText());
			spinner0.setSelection(spinner1.getSelectedItemPosition());

			/// Replace to with old from
			editAmountAlt.setText(fromAmount);
			spinner1.setSelection(fromCurrency);
		});

		// Add country event
		CountryLocator.addListener(this);

		return view;
	}

	// Update the currency values
	private void update()
	{
		// Get the from value
		String fromAmountString = ((EditText) view.findViewById(R.id.edit_amount)).getText().toString();

		// Check if it was emptied
		if (fromAmountString.isEmpty())
		{
			// Empty result
			((TextView) view.findViewById(R.id.edit_amount_alt)).setText(null);

			// Return since there's no need to convert it
			return;
		}

		// Get the amount 'from'
		float fromAmount = Float.parseFloat(fromAmountString.replaceAll(",", ""));

		// Get selected currencies
		ECurrencies fromCurrency = Converter.fromString(((Spinner) view.findViewById(R.id.spinner_currencies)).getSelectedItem().toString());
		ECurrencies toCurrency   = Converter.fromString(((Spinner) view.findViewById(R.id.spinner_currencies_alt)).getSelectedItem().toString());

		// Update 'to' label
		((TextView) view.findViewById(R.id.edit_amount_alt)).setText(toNiceValue(Converter.convert(fromCurrency, toCurrency, fromAmount)));
	}

	// Highlights 'from amount'
	public void focusAmount()
	{
		if (getActivity() != null)
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view.findViewById(R.id.edit_amount), InputMethodManager.SHOW_IMPLICIT);
	}

	// Rounds and sets as a nicer value
	private String toNiceValue(float value)
	{
		/*
			1					=> 1
			1,000				=> 1 k / 1 thousand
			1,000,000			=> 1 M / 1 million
			1,000,000,000		=> 1 G / 1 billion
			1,000,000,000,000	=> 1 T / 1 trillion
		 */

		// Check if we shouldn't format
		if (!prefFormatTo)
			return String.format(Locale.getDefault(), "%.2f", value);

		// Trillion
		if (value > 1000000000000f)
			return String.format(Locale.getDefault(), "%.2f %s", value / 1000000000000f, prefUseSi ? "T" : "trillion");

		// Billion
		if (value > 1000000000)
			return String.format(Locale.getDefault(), "%.2f %s", value / 1000000000, prefUseSi ? "G" : "billion");

		// Million
		if (value > 1000000)
			return String.format(Locale.getDefault(), "%.2f %s", value / 1000000, prefUseSi ? "M" : "million");

		// Thousand
		if (value > 1000)
			return String.format(Locale.getDefault(), "%.2f %s", value / 1000, prefUseSi ? "k" : "thousand");

		// Less than a thousand
		return String.format(Locale.getDefault(), "%.2f", value);
	}

	@Override
	public void onStop()
	{
		super.onStop();

		// Get editor to save preferences
		SharedPreferences.Editor editor = preferences.edit();

		// Put used from/to currencies
		editor.putInt("currency_from", ((Spinner) view.findViewById(R.id.spinner_currencies)).getSelectedItemPosition());
		editor.putInt("currency_to",   ((Spinner) view.findViewById(R.id.spinner_currencies_alt)).getSelectedItemPosition());

		// Save changes in the background
		editor.apply();
	}

	@Override
	public void onSetCurrency(ECurrencies currency)
	{
		((Spinner) view.findViewById(R.id.spinner_currencies)).setSelection(currency.ordinal());
	}

	public void updatePrefs()
	{
		// Just to be sure we don't crash
		if (preferences == null)
			preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

		prefFormatFrom = preferences.getBoolean("format_from", true);
		prefFormatTo   = preferences.getBoolean("format_to",   true);
		prefUseSi      = preferences.getBoolean("use_si",      true);

		// Update some values at least
		update();
	}
}