package com.crow.currencyconverter;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.crow.currencyconverter.Class.TabPagerAdapter;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Load main activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set navigation bar color
		if (Build.VERSION.SDK_INT >= 21)
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

		// Load toolbar (title and tabs)
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Create adapter to return a fragment for each tab
		// Takes care of the tabs
		TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the tab adapter (tab content)
		// Hosts section contents
		ViewPager viewPager = findViewById(R.id.container);
		viewPager.setAdapter(pagerAdapter);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int i, float v, int i1)
			{
			}

			@Override
			public void onPageSelected(int i)
			{
				// If leaving convert fragment, hide keyboard
				if (i != 0 && getCurrentFocus() != null)
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

				pagerAdapter.onPageSelected(i);
			}

			@Override
			public void onPageScrollStateChanged(int i)
			{
			}
		});

		// Load tabs from main activity
		TabLayout tabLayout = findViewById(R.id.tabs);

		// Update listeners to change active tab
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
	}
}