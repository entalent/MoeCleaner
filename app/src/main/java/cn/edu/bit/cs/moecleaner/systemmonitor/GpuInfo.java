package cn.edu.bit.cs.moecleaner.systemmonitor;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by entalent on 2016/4/18.
 */
public class GpuInfo {

    class GLRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }

    class CustomGLSurfaceView extends GLSurfaceView {
        GLRenderer mRenderer;

        public CustomGLSurfaceView (Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            mRenderer = new GLRenderer();
            setRenderer(mRenderer);
        }
    }
}
