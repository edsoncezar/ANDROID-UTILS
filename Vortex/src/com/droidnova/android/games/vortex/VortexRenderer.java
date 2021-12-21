package com.droidnova.android.games.vortex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class VortexRenderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = VortexRenderer.class.getSimpleName();
    
    private float _red = 0f;
    private float _green = 0f;
    private float _blue = 0f;

    // a raw buffer to hold indices allowing a reuse of points.
    private ShortBuffer _indexBuffer;
    
    // a raw buffer to hold the vertices
    private FloatBuffer _vertexBuffer;
    
    private short[] _indicesArray = {0, 1, 2};
    private int _nrOfVertices = 3;

    private float _angle;
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // preparation
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        initTriangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }
    
    public void setAngle(float angle) {
        _angle = angle;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // define the color we want to be displayed as the "clipping wall"
        gl.glClearColor(_red, _green, _blue, 1.0f);
        
        // clear the color buffer to show the ClearColor we called above...
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        // set rotation
        gl.glRotatef(_angle, 0f, 1f, 0f);
    
        gl.glColor4f(0.5f, 0f, 0f, 0.5f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
    
    private void initTriangle() {
        // float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        // short has 4 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();
        
        float[] coords = {
            -0.5f, -0.5f, 0f, // (x1, y1, z1)
            0.5f, -0.5f, 0f, // (x2, y2, z2)
            0f, 0.5f, 0f // (x3, y3, z3)
        };
        
        _vertexBuffer.put(coords);
        
        _indexBuffer.put(_indicesArray);
        
        _vertexBuffer.position(0);
        _indexBuffer.position(0);
    }
    
    public void setColor(float r, float g, float b) {
        _red = r;
        _green = g;
        _blue = b;
    }
}