package com.wanghong.eglposter.fragment;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.wanghong.R;
import com.wanghong.eglposter.C;
import com.wanghong.output.EGLOutputRenderer;
import com.wanghong.output.EGLOutputSurface;
import com.wanghong.output.EGLOutputSurfaceCallback;

import java.io.IOException;

/**
 * Created by Neo on 2017/6/9.
 */

public class EGLOutputFragment extends Fragment {
    private static final String TAG = EGLOutputFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final EGLOutputSurface eglOutputSurface = new EGLOutputSurface(context);
        eglOutputSurface.setEglOutputSurfaceCallback(new EGLOutputSurfaceCallback() {
            @Override
            public void onOutputSurfaceCreated(SurfaceTexture surfaceTexture) {
                Log.d(TAG, "onOutputSurfaceCreated");
                eglOutputSurface.setOutputRenderType(EGLOutputRenderer.OUTPUT_RENDER_TYPE_STILL_BITMAP);
                eglOutputSurface.setStillBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            }

            @Override
            public void onOutputSurfaceChanged(int width, int height) {

            }
        });
        eglOutputSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eglOutputSurface.setOutputRenderType(EGLOutputRenderer.OUTPUT_RENDER_TYPE_CONTINUOUS_PICTURES);
                AssetFileDescriptor assetFileDescriptor = null;
                try {
                    assetFileDescriptor = context.getAssets().openFd((String) getArguments().get(C.FILENAME));
                    final MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                            assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    final Surface surface = new Surface(eglOutputSurface.getSurfaceTexture());
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.release();
                            surface.release();
                        }
                    });
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (assetFileDescriptor != null) {
                        try {
                            assetFileDescriptor.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return eglOutputSurface;
    }
}