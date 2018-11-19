package com.crow.currencyconverter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConvertFragment extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_convert, container, false);

		// Simple error checking and to get rid of warnings
		if (getContext() == null)
			return view;

		// Get spinner
		Spinner spinnerCurrencies = view.findViewById(R.id.spinner_currencies);

		// Create array adapter from out strings
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.currencies, android.R.layout.simple_spinner_item);

		// Specify layout
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply adapter
		spinnerCurrencies.setAdapter(adapter);


		return view;
	}
}