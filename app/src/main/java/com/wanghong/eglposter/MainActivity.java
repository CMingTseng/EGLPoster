package com.wanghong.eglposter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wanghong.R.layout.activity_main);

        findViewById(com.wanghong.R.id.poster_on_texture_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EGLPosterActivity.class));
            }
        });
        findViewById(com.wanghong.R.id.poster_on_gl_surface_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EGLOutputActivity.class));
            }
        });
    }
}
