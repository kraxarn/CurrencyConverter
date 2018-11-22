package com.crow.currencyconverter.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crow.currencyconverter.Class.Converter;
import com.crow.currencyconverter.Rate.RateEntriesAdapter;
import com.crow.currencyconverter.Rate.RateEntry;
import com.crow.currencyconverter.R;

import java.util.ArrayList;
import java.util.Objects;


public class RateFragment extends Fragment
{
	// For our recycler view
	ArrayList<RateEntry> rates;

	RecyclerView viewRates;

	RateEntriesAdapter adapter;


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_rate, container, false);

		// Get rates (require context)
		rates = Converter.getRates(Objects.requireNonNull(getContext()));

		// Set up recycler view
		viewRates = view.findViewById(R.id.view_rates);
		adapter = new RateEntriesAdapter(rates, getContext());
		viewRates.setAdapter(adapter);
		viewRates.setLayoutManager(new LinearLayoutManager(getContext()));
		viewRates.setHasFixedSize(true); // TODO: ?

		// Return it
		return view;
	}
}