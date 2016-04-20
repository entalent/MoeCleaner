package cn.edu.bit.cs.moecleaner.systemmonitor.customview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 不能直接放在xml里
 */
public class CustomGLSurfaceView extends GLSurfaceView {

    static String glRenderer,
                glVendor,
                glVesion,
                glExtensions;

    GLRenderer mRenderer;

    public CustomGLSurfaceView (Context context) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        mRenderer = new GLRenderer();
        setRenderer(mRenderer);
    }

    public static class GLRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            Log.d("SystemInfo", "GL_RENDERER = " +gl.glGetString(GL10.GL_RENDERER));
            Log.d("SystemInfo", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
            Log.d("SystemInfo", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
            Log.d("SystemInfo", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));

            glRenderer = gl.glGetString(GL10.GL_RENDERER);
            glVendor = gl.glGetString(GL10.GL_VENDOR);
            glVesion = gl.glGetString(GL10.GL_VERSION);
            glExtensions = gl.glGetString(GL10.GL_EXTENSIONS);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
