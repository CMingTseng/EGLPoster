package com.wanghong.eglposter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanghong.R;
import com.wanghong.eglposter.EGLOutputActivity;
import com.wanghong.eglposter.EGLPosterActivity;

/**
 * Created by Neo on 2017/6/9.
 */

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View child = inflater.inflate(R.layout.fragment_main, container, false);
        child.findViewById(com.wanghong.R.id.poster_on_texture_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EGLPosterActivity.class));
            }
        });
        child.findViewById(com.wanghong.R.id.poster_on_gl_surface_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EGLOutputActivity.class));
            }
        });
        return child;
    }
}