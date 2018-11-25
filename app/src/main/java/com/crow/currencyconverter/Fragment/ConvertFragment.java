package com.crow.currencyconverter.Fragment;

import android.content.Context;
import android.os.Bundle;
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

import com.crow.currencyconverter.Class.Converter;
import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.R;

import java.util.Locale;

public class ConvertFragment extends Fragment
{
	private View view;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_convert, container, false);

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

		// Set input listener
		((EditText) view.findViewById(R.id.edit_amount)).addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
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

		// Test Button
		view.findViewById(R.id.button_swap).setOnClickListener(v ->
		{
			// From widgets
			EditText editAmount = view.findViewById(R.id.edit_amount);

			// To widgets
			EditText editAmountAlt = view.findViewById(R.id.edit_amount_alt);

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
			((EditText) view.findViewById(R.id.edit_amount_alt)).setText(null);

			// Return since there's no need to convert it
			return;
		}

		// Get the amount 'from'
		float fromAmount = Float.parseFloat(fromAmountString);

		// Get selected currencies
		ECurrencies fromCurrency = Converter.fromString(((Spinner) view.findViewById(R.id.spinner_currencies)).getSelectedItem().toString());
		ECurrencies toCurrency   = Converter.fromString(((Spinner) view.findViewById(R.id.spinner_currencies_alt)).getSelectedItem().toString());

		// Update 'to' label
		((EditText) view.findViewById(R.id.edit_amount_alt)).setText(String.format(Locale.getDefault(), "%.2f", Converter.convert(fromCurrency, toCurrency, fromAmount)));
	}

	// Highlights 'from amount'
	public void focusAmount()
	{
		if (getActivity() != null)
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view.findViewById(R.id.edit_amount), InputMethodManager.SHOW_IMPLICIT);
	}
}