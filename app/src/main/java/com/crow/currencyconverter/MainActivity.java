package com.crow.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crow.currencyconverter.Class.Converter;
import com.crow.currencyconverter.Class.CountryLocator;
import com.crow.currencyconverter.Class.TabPagerAdapter;

public class MainActivity extends AppCompatActivity
{
	/*
		Preferences
		All preferences are enabled by default
		Auto refresh delay is 1 minute by default
	 */
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Load preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

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
		TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the tab adapter (tab content)
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

		// Get request queue for requests
		RequestQueue requestQueue = Volley.newRequestQueue(this);

		// Update currency values
		// TODO: Check a timestamp and only update if 24h> old
		Converter.refresh(this, requestQueue);

		// Update country location if set
		CountryLocator.refresh(requestQueue);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// We pressed our only button
		if (item.getItemId() == R.id.action_settings)
		{
			// Get layout
			View view = View.inflate(this, R.layout.dialog_settings, null);

			// Setup spinner
			Spinner spinner = view.findViewById(R.id.spinner_setting_auto_refresh_delay);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.refresh_delays, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			// Switch ids
			int[] switchIds = {
					R.id.switch_setting_format_from,
					R.id.switch_setting_format_to,
					R.id.switch_setting_use_si,
					R.id.switch_setting_location_currency,
					R.id.switch_setting_auto_refresh
			};

			// Preference ids
			String[] switchNames = {
					"format_from",
					"format_to",
					"use_si",
					"location_currency",
					"auto_refresh"
			};

			// Load preferences and update values
			for(int i = 0; i < switchIds.length; i++)
				((Switch) view.findViewById(switchIds[i])).setChecked(preferences.getBoolean(switchNames[i], true));

			// Set spinner value from preferences
			((Spinner) view.findViewById(R.id.spinner_setting_auto_refresh_delay)).setSelection(preferences.getInt("auto_refresh_delay", 0));

			// Display as alert dialog
			new AlertDialog.Builder(this)
					.setView(view)
					.setTitle(getString(R.string.item_settings))
					.setPositiveButton(getString(R.string.generic_save),   (dialogInterface, id) ->
					{
						// Create preference editor
						SharedPreferences.Editor editor = preferences.edit();

						// Save values from preferences
						for(int i = 0; i < switchIds.length; i++)
							editor.putBoolean(switchNames[i], ((Switch) view.findViewById(switchIds[i])).isChecked());

						// Save spinner value
						editor.putInt("auto_refresh_delay", ((Spinner) view.findViewById(R.id.spinner_setting_auto_refresh_delay)).getSelectedItemPosition());

						// Save to file
						editor.apply();
					})
					.setNegativeButton(getString(R.string.generic_cancel), (dialogInterface, i) -> {
						// Ignore if cancel was pressed
					})
					.show();
		}

		return super.onOptionsItemSelected(item);
	}
}