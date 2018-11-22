package com.crow.currencyconverter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.crow.currencyconverter.Class.Converter;
import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.Rate.RateEntriesAdapter;
import com.crow.currencyconverter.Rate.RateEntry;
import com.crow.currencyconverter.R;

import java.util.ArrayList;
import java.util.Objects;


public class RateFragment extends Fragment
{
	// For our recycler view
	private ArrayList<RateEntry> rates;

	private RecyclerView viewRates;

	private RateEntriesAdapter adapter;

	private View view;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_rate, container, false);

		// Get rates (require context)
		rates = Converter.getRates(Objects.requireNonNull(getContext()));

		// Set up recycler view
		viewRates = view.findViewById(R.id.view_rates);
		adapter = new RateEntriesAdapter(rates, getContext());
		viewRates.setAdapter(adapter);
		viewRates.setLayoutManager(new LinearLayoutManager(getContext()));
		viewRates.setHasFixedSize(true); // TODO: ?

		// Set up spinner (identical to convert fragment)
		Spinner spinnerBase = view.findViewById(R.id.spinner_base);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.currencies, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBase.setAdapter(adapter);

		// Update after selecting new value
		spinnerBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
		});

		// Call initial update
		update();

		// Return it
		return view;
	}

	private void update()
	{
		// Get selected base currency
		ECurrencies base = ECurrencies.valueOf(((Spinner) view.findViewById(R.id.spinner_base)).getSelectedItem().toString().split(" - ")[0]);

		// Update all values
		adapter.setEntryRate(ECurrencies.EUR, Converter.convert(base, ECurrencies.EUR, 1));
		adapter.setEntryRate(ECurrencies.SEK, Converter.convert(base, ECurrencies.SEK, 1));
		adapter.setEntryRate(ECurrencies.USD, Converter.convert(base, ECurrencies.USD, 1));
		adapter.setEntryRate(ECurrencies.GBP, Converter.convert(base, ECurrencies.GBP, 1));
		adapter.setEntryRate(ECurrencies.CNY, Converter.convert(base, ECurrencies.CNY, 1));
		adapter.setEntryRate(ECurrencies.JPY, Converter.convert(base, ECurrencies.JPY, 1));
		adapter.setEntryRate(ECurrencies.KRW, Converter.convert(base, ECurrencies.KRW, 1));
	}
}