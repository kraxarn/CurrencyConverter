package com.crow.currencyconverter;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.crow.currencyconverter.Class.TabPagerAdapter;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Load main activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		// Load tabs from main activity
		TabLayout tabLayout = findViewById(R.id.tabs);

		// Update listeners to change active tab
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
	}
}