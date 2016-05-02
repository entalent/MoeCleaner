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

    static String glRenderer;
    static String glVendor;
    static String glVersion;
    static String glExtensions;

    public static String getGlRenderer() {
        return glRenderer;
    }

    public static String getGlVendor() {
        return glVendor;
    }

    public static String getGlVersion() {
        return glVersion;
    }

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

            glRenderer = gl.glGetString(GL10.GL_RENDERER);
            glVendor = gl.glGetString(GL10.GL_VENDOR);
            glVersion = gl.glGetString(GL10.GL_VERSION);
            glExtensions = gl.glGetString(GL10.GL_EXTENSIONS);

            Log.d("SystemInfo", "GL_RENDERER = " + glRenderer);
            Log.d("SystemInfo", "GL_VENDOR = " + glVendor);
            Log.d("SystemInfo", "GL_VERSION = " + glVersion);
            Log.d("SystemInfo", "GL_EXTENSIONS = " + glExtensions);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
