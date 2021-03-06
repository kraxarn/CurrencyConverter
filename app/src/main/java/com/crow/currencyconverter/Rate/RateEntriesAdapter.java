package com.crow.currencyconverter.Rate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crow.currencyconverter.Enums.ECurrencies;
import com.crow.currencyconverter.R;

import java.util.ArrayList;

public class RateEntriesAdapter extends RecyclerView.Adapter<RateEntriesViewHolder>
{
	private final ArrayList<RateEntry> rateEntries;

	public RateEntriesAdapter(ArrayList<RateEntry> rateEntries)
	{
		this.rateEntries = rateEntries;
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

		TextView textCurrency = holder.textCurrency;
		textCurrency.setText(entry.currency);
	}

	@Override
	public int getItemCount()
	{
		return rateEntries.size();
	}

	public void setEntryRate(ECurrencies currency, float rate)
	{
		for (int i = 0; i < rateEntries.size(); i++)
		{
			if (rateEntries.get(i).icon.contains(currency.name()))
			{
				rateEntries.get(i).rate = rate;
				notifyItemChanged(i);
				return;
			}
		}
	}
}