package com.tech.nyax.farmers_information_management;

import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tech.nyax.farmers_information_management.R;

public class MyViewPagerActivity extends AppCompatActivity {
	
    private static final String TAG = MyViewPagerActivity.class.getName();
    private ntharenePagerAdapter mFragmentAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
		//Apply the Adapter
        mFragmentAdapter = new ntharenePagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mFragmentAdapter);
		
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected. Animation is not necessarily complete.
            @Override
            public void onPageSelected(int position) {
// Your code
            }

            // This method will be invoked when the current page is scrolled, either as part of
// a programmatically initiated smooth scroll or a user initiated touch scroll.
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
// Your code
            }

            // Called when the scroll state changes. Useful for discovering when the user begins
// dragging, when the pager is automatically settling to the current page,
// or when it is fully stopped/idle.
            @Override
            public void onPageScrollStateChanged(int state) {
// Your code
            }
        });

		
		
    }

    private class ntharenePagerAdapter extends FragmentPagerAdapter {

        public ntharenePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
		}

        //Returns the fragment to display for that page
		@Override
         public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    Log.e(TAG, "FragmentOne");
                    return new FragmentOne().newInstance("");
                case 1:
                    Log.e(TAG, "FragmentOne");
                    return new FragmentTwo().newInstance("");
                case 2:
                    Log.e(TAG, "FragmentOne");
                    return new FragmentThree().newInstance("");
                default:
                    return null;
            }
        } 

         // Returns total number of pages
        @Override
        public int getCount() {
            return 3;
        } 
   }
   
   
   
   
   
}
