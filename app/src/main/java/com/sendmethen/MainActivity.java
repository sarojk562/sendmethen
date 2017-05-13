package com.sendmethen;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sendmethen.com.sendmethen.fragments.HomeFragment;
import com.sendmethen.com.sendmethen.fragments.ReadPublicFragment;
import com.sendmethen.com.sendmethen.fragments.SendMeFragment;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        sendTextBtn.setOnClickListener(View.OnClickListener);




//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "HOME");
        adapter.addFragment(new ReadPublicFragment(), "READ PUBLIC");
        adapter.addFragment(new SendMeFragment(), "SEND ME");
        viewPager.setAdapter(adapter);
    }


}


