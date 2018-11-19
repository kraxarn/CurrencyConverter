package com.crow.currencyconverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter
{
	private ConvertFragment convertFragment;

	private RateFragment rateFragment;

	TabPagerAdapter(FragmentManager fm)
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
