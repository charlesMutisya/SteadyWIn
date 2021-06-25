package com.winnerpredictor.oddipredictions;
 
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {


    private static final String TAG ="FACEBOOK_ADS" ;
    private com.facebook.ads.InterstitialAd interstitialAd_fb;

    private SectionsPagerAdapter sectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        AudienceNetworkAds.initialize(this);

        FirebaseMessaging.getInstance().subscribeToTopic("jackpot");
        //AdSettings.addTestDevice("a53407f2-88d2-4f43-abad-4a282a22e6df");

        FabSpeedDial fabSpeedDial= findViewById(R.id.fabspeed);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int idc = menuItem.getItemId();
                if (idc== R.id.action_mail){
                    Intent feedback = new Intent(MainActivity.this, FeedBack.class);

                    startActivity(feedback);

                }else if (idc== R.id.action_rate){
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                    }

                }else  if (idc==R.id.action_share){
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Hey there,Check out the best Free prediction tips across all the major soccer leagues. Download here https://play.google.com/store/apps/details?id=com.winnerpredictor.oddipredictions";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Steady Winning");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(sharingIntent);

                }



                return super.onMenuItemSelected(menuItem);
            }
        });



    }

class  SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(MainActivity mainActivity, @NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DailyPicks();
            case 1:
                return new SafePicks();

        }
        return new DailyPicks();


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            //
            //Your tab titles
            //
            case 0:return "Daily Picks";
            case 1: return "Smart Odds ";

            default:return "DailyTips";
        }
    }
}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

}