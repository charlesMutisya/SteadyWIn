package com.winnerpredictor.oddipredictions;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;

import com.facebook.ads.AdError;

import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

public class PostDetails extends AppCompatActivity {

    private final String TAG = PostDetails.class.getSimpleName();
    private com.facebook.ads.InterstitialAd interstitialAd_fb;
   com.facebook.ads.AdView ban_adView2;

    DatabaseReference mRef;


    String postKey;
    TextView tvTitle, tvBody, tvTime;
    ImageView imgBody;
    ProgressDialog pd;
    String selection;
    AutoLinkTextView autoLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_tip_details);
        postKey = getIntent().getExtras().getString("postkey");
        selection=getIntent().getExtras().getString("selection");
        tvBody =  findViewById(R.id.tvBody);
        tvTitle =  findViewById(R.id.tvTitle);
        tvTime =  findViewById(R.id.post_time);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

       // AdSettings.addTestDevice("a53407f2-88d2-4f43-abad-4a282a22e6df");


        autoLinkTextView = findViewById(R.id.autoLinkrate);
        autoLinkTextView.addAutoLinkMode(AutoLinkMode.MODE_CUSTOM);
        autoLinkTextView.setCustomRegex("\\sHere\\b");


        autoLinkTextView.setAutoLinkText("We give you our best games motivate us by rating the App : Here");

        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (autoLinkMode == AutoLinkMode.MODE_CUSTOM)
                    try {
                        Intent RateIntent =
                                new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(RateIntent);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Unable to connect try again later...",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

            }
        });



        if (postKey != null){

            mRef = FirebaseDatabase.getInstance().getReference().child("jackpot").child(selection).child(postKey);

        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue().toString();
                String body = dataSnapshot.child("body").getValue().toString();
                Long time = (Long) dataSnapshot.child("time").getValue();

                if(title!=null){
                    tvTitle.setText(title.toUpperCase());
                    pd.dismiss();
                } else {
                    Toast.makeText(PostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                if (body != null) {
                    tvBody.setText(body);

                }
                if (time != null) {
                    setTime(time);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
            finish();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menub, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        if (id == R.id.feedback){
            startActivity(new Intent(this, FeedBack.class));

        }else if (id == R.id.menu_share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey there,Check out the best Free prediction tips across all the major soccer leagues. Download here https://play.google.com/store/apps/details?id=com.winnerpredictor.oddipredictions";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Steady Winning");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }else if ( id== R.id.rate){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }else  if (id== R.id.about){
            Uri uri = Uri.parse("https://winstedy.blogspot.com/2019/02/readMe.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }else if (id == R.id.privacyP){
            Intent feedback = new Intent(this,PrivacyPolicy.class);

            startActivity(feedback);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTime(Long time) {
        TextView txtTime = (TextView) findViewById(R.id.post_time);
        //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
        long elapsedTime;
        long currentTime = System.currentTimeMillis();
        int elapsed = (int) ((currentTime - time) / 1000);
        if (elapsed < 60) {
            if (elapsed < 2) {
                txtTime.setText("Just Now");
            } else {
                txtTime.setText(elapsed + " sec ago");
            }
        } else if (elapsed > 604799) {
            elapsedTime = elapsed / 604800;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " week ago");
            } else {

                txtTime.setText(elapsedTime + " weeks ago");
            }
        } else if (elapsed > 86399) {
            elapsedTime = elapsed / 86400;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " day ago");
            } else {
                txtTime.setText(elapsedTime + " days ago");
            }
        } else if (elapsed > 3599) {
            elapsedTime = elapsed / 3600;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " hour ago");
            } else {
                txtTime.setText(elapsedTime + " hours ago");
            }
        } else if (elapsed > 59) {
            elapsedTime = elapsed / 60;
            txtTime.setText(elapsedTime + " min ago");


        }

    }
    @Override
    public void onDestroy() {
        if (ban_adView2 != null) {
            ban_adView2.destroy();
        }
        super.onDestroy();
    }
}
