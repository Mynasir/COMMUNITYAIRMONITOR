package com.communityairmonitor;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Base activity that applies the modern edge-to-edge window setup consistently.
 * MainActivity opts out of the generic padding because it owns a bottom navigation bar.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
            View root = contentRoot();
            if (root != null && root.findViewById(R.id.main) == null) {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                root.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            }
            return insets;
        });
    }

    private View contentRoot() {
        ViewGroup content = findViewById(android.R.id.content);
        return content != null && content.getChildCount() > 0 ? content.getChildAt(0) : null;
    }
}
