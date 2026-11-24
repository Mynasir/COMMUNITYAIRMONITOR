package com.communityairmonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.communityairmonitor.MainActivity;
import com.communityairmonitor.R;
import com.communityairmonitor.model.AirQualityNode;
import com.communityairmonitor.view.AqiGaugeView;
import com.communityairmonitor.viewmodel.AirViewModel;

public class HomePulseFragment extends Fragment {

    private AirViewModel viewModel;
    private TextView tvHeroAqiNumber, tvHeroAqiStatus, tvHeroAqiBadge, tvHeroPm25, tvHeroPm10;
    private AqiGaugeView heroAqiGauge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_pulse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AirViewModel.class);

        tvHeroAqiNumber = view.findViewById(R.id.tvHeroAqiNumber);
        tvHeroAqiStatus = view.findViewById(R.id.tvHeroAqiStatus);
        tvHeroAqiBadge = view.findViewById(R.id.tvHeroAqiBadge);
        tvHeroPm25 = view.findViewById(R.id.tvHeroPm25);
        tvHeroPm10 = view.findViewById(R.id.tvHeroPm10);
        heroAqiGauge = view.findViewById(R.id.heroAqiGauge);

        view.findViewById(R.id.btnExploreLive).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(R.id.nav_map);
            }
        });

        view.findViewById(R.id.btnHowItWorks).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(R.id.nav_dashboard);
            }
        });

        viewModel.getNodesLiveData().observe(getViewLifecycleOwner(), nodes -> {
            if (nodes != null && !nodes.isEmpty()) {
                AirQualityNode node = nodes.get(0);
                tvHeroAqiNumber.setText(String.valueOf(node.getAqi()));
                heroAqiGauge.setAqi(node.getAqi());
                tvHeroPm25.setText(String.format("%.1f µg/m³", node.getPm2_5()));
                tvHeroPm10.setText(String.format("%.1f µg/m³", node.getPm10()));

                if (node.getAqi() <= 50) {
                    tvHeroAqiStatus.setText("GOOD");
                    tvHeroAqiBadge.setText("OPTIMAL AIR");
                } else if (node.getAqi() <= 100) {
                    tvHeroAqiStatus.setText("MODERATE");
                    tvHeroAqiBadge.setText("ACCEPTABLE");
                } else {
                    tvHeroAqiStatus.setText("ALERT");
                    tvHeroAqiBadge.setText("HAZARD STATE");
                }
            }
        });
    }
}
