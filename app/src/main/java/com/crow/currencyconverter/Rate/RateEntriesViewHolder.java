package com.crow.currencyconverter.Rate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crow.currencyconverter.R;

class RateEntriesViewHolder extends RecyclerView.ViewHolder
{
	final TextView textIcon;

	final TextView textRate;

	final TextView textCurrency;

	RateEntriesViewHolder(View itemView)
	{
		super(itemView);

		textIcon     = itemView.findViewById(R.id.text_icon);
		textRate     = itemView.findViewById(R.id.text_rate);
		textCurrency = itemView.findViewById(R.id.text_currency);
	}
}