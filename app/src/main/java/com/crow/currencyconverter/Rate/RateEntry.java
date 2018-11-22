package com.crow.currencyconverter.Rate;

import java.util.Locale;

public class RateEntry
{
	public String icon;

	public float rate;

	public String currency;

	public RateEntry(String icon, float rate, String currency)
	{
		this.icon     = icon;
		this.rate     = rate;
		this.currency = currency;
	}

	public String getRate()
	{
		return rate < 0.01f ? "<0.01" : String.format(Locale.getDefault(), "%.2f", rate);
	}
}