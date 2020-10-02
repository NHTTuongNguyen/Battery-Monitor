package com.example.batterymonitor;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.batterymonitor.adapter.ViewPagerHomeAdapter;
import com.example.batterymonitor.fragments.InformationFragment;
import com.example.batterymonitor.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private InformationFragment informationFragment;
    private HomeFragment homeFragment;
    private MenuItem prevMenuItem;

    private String tabTitles[] = new String[] { "Information", "Saver" };
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
         bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("pagess", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerHomeAdapter adapter = new ViewPagerHomeAdapter(getSupportFragmentManager());
        informationFragment =new InformationFragment();
        homeFragment = new HomeFragment();
        adapter.addFragment(informationFragment);
        adapter.addFragment(homeFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_information:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_home:
                viewPager.setCurrentItem(1);
                return true;
            default:
                return false;
        }
    }

}