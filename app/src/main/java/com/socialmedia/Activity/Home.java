package com.socialmedia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.socialmedia.Adapter.Post_Adapter;
import com.socialmedia.Model.PostModel;
import com.socialmedia.R;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private final String TAG = "NativeAdActivity".getClass().getSimpleName();
    private NativeAd nativeAd;
    RecyclerView storyRecyclerView, postRecyclerView;

    ArrayList<PostModel> postList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ImageView button3;
    ImageView Home_image;
    FirebaseStorage storage;
    ActivityResultLauncher<String> StoryLauncher;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance( );
        storage = FirebaseStorage.getInstance( );
        auth = FirebaseAuth.getInstance( );
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        Home_image = findViewById(R.id.Home_image);
        AudienceNetworkAds.initialize(this);
        LoadNativeAd();
        Home_image.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, NotificationActivity.class);
                startActivity(intent);
            }
        });




        postRecyclerView = findViewById(R.id.postsRv);
        postList = new ArrayList<>( );
        Post_Adapter postAdapter = new Post_Adapter(postList, getBaseContext( ));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext( ));
        postRecyclerView.setLayoutManager(layoutManager1);
        postRecyclerView.setNestedScrollingEnabled(false);
        postRecyclerView.setAdapter(postAdapter);
        database.getReference( ).child("posts").addValueEventListener(new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear( );
                for (DataSnapshot dataSnapshot : snapshot.getChildren( )) {

                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey( ));
                    postList.add(postModel);
                }
                postAdapter.notifyDataSetChanged( );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


 bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener( ) {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home:
                        return true;

                    case R.id.Inbox:
                        startActivities(new Intent[]{new Intent(getApplicationContext(), ChatActivity.class)});
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Page:
                        startActivities(new Intent[]{new Intent(getApplicationContext(), LogIn.class)});
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void LoadNativeAd() {
        NativeBannerAd nativeBannerAd = new NativeBannerAd(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        NativeAdListener nativeAdListener = new NativeAdListener() {


            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);

            }

            private void inflateAd(NativeBannerAd nativeBannerAd) {
                nativeBannerAd.unregisterView();

                // Add the Ad view into the ad container.
                nativeAdLayout = findViewById(R.id.native_banner_ad_container);
                LayoutInflater inflater = LayoutInflater.from(Home.this);
                // Inflate the Ad view.  The layout referenced is the one you created in the last step.
                adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
                nativeAdLayout.addView(adView);

                // Add the AdChoices icon
                RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
                AdOptionsView adOptionsView = new AdOptionsView(Home.this, nativeBannerAd, nativeAdLayout);
                adChoicesContainer.removeAllViews();
                adChoicesContainer.addView(adOptionsView, 0);

                // Create native UI using the ad metadata.
                TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
                Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
                nativeAdCallToAction.setVisibility(
                        nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
                nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
                sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };
        // load the ad
        nativeBannerAd.loadAd(
                nativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }
}









