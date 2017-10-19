package com.example.andrearodriguez.gestionmultimedia;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by andrearodriguez on 10/18/17.
 */

public class MyRenderer implements GLSurfaceView.Renderer{

    private Cubo cubo;

    private static float anguloCubo = 0;
    private static float speedCubo = -1.5f;

    public MyRenderer(Context context) {
        cubo = new Cubo();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl10.glLoadIdentity();
        gl10.glTranslatef(0.0f, 0.0f, -0.5f);
        gl10.glScalef(0.8f, 0.8f, 0.8f);
        gl10.glRotatef(anguloCubo,1.0f,1.0f,0.0f);

        cubo.dibujar(gl10);

        anguloCubo += speedCubo;

    }
}
