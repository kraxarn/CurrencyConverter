package com.crow.currencyconverter.Rate;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crow.currencyconverter.R;

import java.util.ArrayList;

public class RateEntriesAdapter extends RecyclerView.Adapter<RateEntriesViewHolder>
{
	private ArrayList<RateEntry> rateEntries;

	private Context context;

	public RateEntriesAdapter(ArrayList<RateEntry> rateEntries, Context context)
	{
		this.rateEntries = rateEntries;
		this.context     = context;
	}

	public Context getContext()
	{
		return context;
	}

	@NonNull
	@Override
	public RateEntriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		// Inflate item layout and create holder

		// Get inflater
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());

		// Inflate layout
		View entry = inflater.inflate(R.layout.item_rate_entry, parent, false);

		// Return new holder
		return new RateEntriesViewHolder(entry);
	}

	@Override
	public void onBindViewHolder(@NonNull RateEntriesViewHolder holder, int position)
	{
		// Set the view attributes based on the data

		// Get data based on position
		RateEntry entry = rateEntries.get(position);

		// Set item view based on views and data
		TextView textIcon = holder.textIcon;
		textIcon.setText(entry.icon);

		TextView textRate = holder.textRate;
		textRate.setText(entry.getRate());
		/*
			Red:	#f44336
			Green:	#4caf50
			Yellow:	#ffeb3b
		 */
		textRate.setTextColor(Color.parseColor(entry.rate < 0 ? "#f44336" : entry.rate > 0 ? "#4caf50" : "#ffeb3b"));

		TextView textCurrency = holder.textCurrency;
		textCurrency.setText(entry.currency);
	}

	@Override
	public int getItemCount()
	{
		return rateEntries.size();
	}
}