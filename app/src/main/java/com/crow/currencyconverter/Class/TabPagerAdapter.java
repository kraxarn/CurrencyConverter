package com.crow.currencyconverter.Class;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crow.currencyconverter.Fragment.ConvertFragment;
import com.crow.currencyconverter.Fragment.RateFragment;

public class TabPagerAdapter extends FragmentPagerAdapter
{
	private final ConvertFragment convertFragment;

	private final RateFragment rateFragment;

	public TabPagerAdapter(FragmentManager fm)
	{
		super(fm);

		convertFragment = new ConvertFragment();
		rateFragment    = new RateFragment();
	}

	@Override
	public Fragment getItem(int position)
	{
		switch (position)
		{
			case 0:
				return convertFragment;
			case 1:
				return rateFragment;

			default:
				throw new IllegalStateException("Invalid position: " + position);
		}
	}

	@Override
	public int getCount()
	{
		// Page total
		return 2;
	}
}
