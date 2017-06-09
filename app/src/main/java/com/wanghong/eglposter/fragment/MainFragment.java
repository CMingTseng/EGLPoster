package com.wanghong.eglposter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanghong.R;
import com.wanghong.eglposter.C;

/**
 * Created by Neo on 2017/6/9.
 */

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View child = inflater.inflate(R.layout.fragment_main, container, false);
        final Bundle arguments = new Bundle();
        arguments.putString(C.FILENAME, "testsrc2.mp4");
        child.findViewById(com.wanghong.R.id.poster_on_texture_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment = new EGLPosterFragment();
                fragment.setArguments(arguments);
                setFragment(fragment);
            }
        });
        child.findViewById(com.wanghong.R.id.poster_on_gl_surface_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Fragment fragment = new EGLOutputFragment();
                fragment.setArguments(arguments);
                setFragment(fragment);
            }
        });
        return child;
    }

    private void setFragment(Fragment fragment) {
        try {
            getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .addToBackStack(MainFragment.class.getSimpleName())
                    .replace(R.id.main_content, fragment)
                    .commit();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "setFragment(): illegal state: " + ex.getMessage());
        }
    }
}