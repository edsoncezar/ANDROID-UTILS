package com.droidnova.android.games.vortex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class Triangle {
    private short[] myIndecesArray = {0, 1, 2};
    private int _nrOfVertices = 3;
    
    private float _red = 0f;
    private float _green = 0f;
    private float _blue = 0f;
    
    // a raw buffer to hold indices allowing a reuse of points.
    private ShortBuffer _indexBuffer;
    
    // a raw buffer to hold the vertices
    private FloatBuffer _vertexBuffer;
    
    private Random _rand = new Random();
    public Triangle() {
        _red = _rand.nextFloat();
        _red = _red - (float) Math.floor(_red);
        _green = _rand.nextFloat();
        _green = _green - (float) Math.floor(_green);
        _blue = _rand.nextFloat();
        _blue = _blue - (float) Math.floor(_blue);

        ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(_nrOfVertices * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();
        
        float[] coords = {
            randomValue(_red), randomValue(_red), randomValue(_red), // (x1,y1,z1)
            randomValue(_green), randomValue(_green), randomValue(_green),
            randomValue(_blue), randomValue(_blue), randomValue(_blue)
        };
        
        for (int i = 0; i < _nrOfVertices; i++) {
            for(int j = 0; j < 3; j++) {
                _vertexBuffer.put(coords[i * 3 + j]);
            }
        }
        
        for (int i = 0; i < 3; i++) {
            _indexBuffer.put(myIndecesArray[i]);
         }
         _vertexBuffer.position(0);
         _indexBuffer.position(0);
    }
    
    private float randomValue(float value) {
        int tmp = _rand.nextInt(2);
        if (tmp == 0) {
            tmp = -1;
        }
        return value * tmp;
    }
    
    public FloatBuffer getVertexBuffer() {
        return _vertexBuffer;
    }
    
    public ShortBuffer getIndexBuffer() {
        return _indexBuffer;
    }
    
    public int getNumberOfVertices() {
        return _nrOfVertices;
    }
    
    public float getGreen() {
        return _green;
    }

    public float getRed() {
        return _red;
    }
    
    public float getBlue() {
        return _blue;
    }
}
