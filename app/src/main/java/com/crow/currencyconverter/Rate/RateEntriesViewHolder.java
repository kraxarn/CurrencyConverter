package com.crow.currencyconverter.Rate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crow.currencyconverter.R;

public class RateEntriesViewHolder extends RecyclerView.ViewHolder
{
	public TextView textIcon;

	public TextView textRate;

	public TextView textCurrency;

	public RateEntriesViewHolder(View itemView)
	{
		super(itemView);

		textIcon     = itemView.findViewById(R.id.text_icon);
		textRate     = itemView.findViewById(R.id.text_rate);
		textCurrency = itemView.findViewById(R.id.text_currency);
	}
}