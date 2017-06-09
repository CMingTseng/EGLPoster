package com.wanghong.eglposter.fragment;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.wanghong.eglposter.C;
import com.wanghong.eglposter.EGLPosterRenderer;
import com.wanghong.eglposter.EGLPosterRetrieverTask;

import java.io.IOException;

/**
 * Created by Neo on 2017/6/9.
 */

public class EGLPosterFragment extends Fragment {
    private static final String TAG = EGLPosterFragment.class.getSimpleName();
    private Surface mSurface;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final TextureView textureView = new TextureView(context);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurface = new Surface(surface);
                new EGLPosterRetrieverTask().execute(context, mSurface, (String) getArguments().get(C.FILENAME));
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSurface == null) {
                    return;
                }
                AssetFileDescriptor assetFileDescriptor = null;
                try {
                    assetFileDescriptor = context.getAssets().openFd((String) getArguments().get(C.FILENAME));
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
                            assetFileDescriptor.getLength());
                    mediaPlayer.setSurface(mSurface);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            EGLPosterRenderer.render(colorBitmap(Color.BLACK)).recycleBitmap(true).into(mSurface);
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
        return textureView;
    }

    private static Bitmap colorBitmap(int color) {
        final Bitmap bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        return bitmap;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
    }
}