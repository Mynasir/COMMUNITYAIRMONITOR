package com.communityairmonitor.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.communityairmonitor.MainActivity;
import com.communityairmonitor.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends com.communityairmonitor.BaseActivity {

    private ViewPager2 viewPager;
    private Button btnNext, btnBack, btnSkip;

    private static class OnboardingSlide {
        String emoji;
        String title;
        String description;

        OnboardingSlide(String emoji, String title, String description) {
            this.emoji = emoji;
            this.title = title;
            this.description = description;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.onboardingViewPager);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);

        List<OnboardingSlide> slides = new ArrayList<>();
        slides.add(new OnboardingSlide("🌍", "Monitor Air Quality", "Get real-time air quality data from community monitoring stations."));
        slides.add(new OnboardingSlide("🛡️", "Understand Environmental Risks", "Know the risks and protect your health."));
        slides.add(new OnboardingSlide("🌱", "Build Healthier Communities", "Together for cleaner air and a healthier future."));

        viewPager.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_slide, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                OnboardingSlide slide = slides.get(position);
                TextView tvEmoji = holder.itemView.findViewById(R.id.tvSlideEmoji);
                TextView tvTitle = holder.itemView.findViewById(R.id.tvSlideTitle);
                TextView tvDesc = holder.itemView.findViewById(R.id.tvSlideDescription);

                tvEmoji.setText(slide.emoji);
                tvTitle.setText(slide.title);
                tvDesc.setText(slide.description);
            }

            @Override
            public int getItemCount() {
                return slides.size();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    btnBack.setVisibility(View.GONE);
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText("Next");
                } else if (position == slides.size() - 1) {
                    btnBack.setVisibility(View.VISIBLE);
                    btnSkip.setVisibility(View.GONE);
                    btnNext.setText("Get Started");
                } else {
                    btnBack.setVisibility(View.VISIBLE);
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText("Next");
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < slides.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                finishOnboarding();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
