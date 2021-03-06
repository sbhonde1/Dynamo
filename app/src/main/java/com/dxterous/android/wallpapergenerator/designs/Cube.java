package com.dxterous.android.wallpapergenerator.designs;


/* *
 * This code actually will draw a cube.
 *
 * Some of the code is used from https://github.com/christopherperry/cube-rotation
 * and changed up to opengl 3.0
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;
import android.util.Log;

import com.dxterous.android.wallpapergenerator.MyGLRenderer;

public class Cube {
    private int programObject;
    private int mMVPMatrixHandle;
    private int mColorHandle;
    private FloatBuffer vertices;
    private float[] verticesData;
    //initial size of the cube.  set here, so it is easier to change later.
    private float size;

    //this is the initial data, which will need to translated into the mVertices variable in the consturctor.



    //vertex shader code
    String vShaderStr =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "}                            \n";
    //fragment shader code.
    String fShaderStr =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "uniform vec4 vColor;	 			 		  	\n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + "  fragColor = vColor;                    	\n"
                    + "}                                            \n";

    String TAG = "Cube";


    //finally some methods
    //constructor
    public Cube(float size) {
        this.size = size;
        setVerticesData(this.size);
        //first setup the mVertices correctly.
        vertices = ByteBuffer
                .allocateDirect(verticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticesData);
        vertices.position(0);

        //setup the shaders
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        // Load the vertex/fragment shaders
        vertexShader = MyGLRenderer.loadShader(GLES30.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = MyGLRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, fShaderStr);

        // Create the program object
        programObject = GLES30.glCreateProgram();

        if (programObject == 0) {
            Log.e(TAG, "So some kind of error, but what?");
            return;
        }

        GLES30.glAttachShader(programObject, vertexShader);
        GLES30.glAttachShader(programObject, fragmentShader);

        // Bind vPosition to attribute 0
        GLES30.glBindAttribLocation(programObject, 0, "vPosition");

        // Link the program
        GLES30.glLinkProgram(programObject);

        // Check the link status
        GLES30.glGetProgramiv(programObject, GLES30.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:");
            Log.e(TAG, GLES30.glGetProgramInfoLog(programObject));
            GLES30.glDeleteProgram(programObject);
            return;
        }

        // Store the program object
        this.programObject = programObject;

        //now everything is setup and ready to draw.
    }

    private void setVerticesData(float size) {
        verticesData = new float[]
                {
                        ////////////////////////////////////////////////////////////////////
                        // FRONT
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        -size, size, size, // top-left
                        -size, -size, size, // bottom-left
                        size, -size, size, // bottom-right
                        // Triangle 2
                        size, -size, size, // bottom-right
                        size, size, size, // top-right
                        -size, size, size, // top-left
                        ////////////////////////////////////////////////////////////////////
                        // BACK
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        -size, size, -size, // top-left
                        -size, -size, -size, // bottom-left
                        size, -size, -size, // bottom-right
                        // Triangle 2
                        size, -size, -size, // bottom-right
                        size, size, -size, // top-right
                        -size, size, -size, // top-left

                        ////////////////////////////////////////////////////////////////////
                        // LEFT
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        -size, size, -size, // top-left
                        -size, -size, -size, // bottom-left
                        -size, -size, size, // bottom-right
                        // Triangle 2
                        -size, -size, size, // bottom-right
                        -size, size, size, // top-right
                        -size, size, -size, // top-left
                        ////////////////////////////////////////////////////////////////////
                        // RIGHT
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        size, size, -size, // top-left
                        size, -size, -size, // bottom-left
                        size, -size, size, // bottom-right
                        // Triangle 2
                        size, -size, size, // bottom-right
                        size, size, size, // top-right
                        size, size, -size, // top-left

                        ////////////////////////////////////////////////////////////////////
                        // TOP
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        -size, size, -size, // top-left
                        -size, size, size, // bottom-left
                        size, size, size, // bottom-right
                        // Triangle 2
                        size, size, size, // bottom-right
                        size, size, -size, // top-right
                        -size, size, -size, // top-left
                        ////////////////////////////////////////////////////////////////////
                        // BOTTOM
                        ////////////////////////////////////////////////////////////////////
                        // Triangle 1
                        -size, -size, -size, // top-left
                        -size, -size, size, // bottom-left
                        size, -size, size, // bottom-right
                        // Triangle 2
                        size, -size, size, // bottom-right
                        size, -size, -size, // top-right
                        -size, -size, -size // top-left
                };
    }

    public void draw(float[] mvpMatrix) {

        // Use the program object
        GLES30.glUseProgram(programObject);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES30.glGetUniformLocation(programObject, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // get handle to fragment shader's vColor member
        mColorHandle = GLES30.glGetUniformLocation(programObject, "vColor");


        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        int VERTEX_POS_INDX = 0;
        vertices.position(VERTEX_POS_INDX);  //just in case.  We did it already though.

        //add all the points to the space, so they can be correct by the transformations.
        //would need to do this even if there were no transformations actually.
        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, 3, GLES30.GL_FLOAT,
                false, 0, vertices);
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);

        //Now we are ready to draw the cube finally.
        int startPos =0;
        int verticesPerface = 6;

        //draw front face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,startPos,verticesPerface);
        startPos += verticesPerface;

        //draw back face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, startPos, verticesPerface);
        startPos += verticesPerface;

        //draw left face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,startPos,verticesPerface);
        startPos += verticesPerface;

        //draw right face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,startPos,verticesPerface);
        startPos += verticesPerface;

        //draw top face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,startPos,verticesPerface);
        startPos += verticesPerface;

        //draw bottom face
        GLES30.glUniform4fv(mColorHandle, 1, MyColor.getRandomColor(), 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,startPos,verticesPerface);
        //last face, so no need to increment.

    }
}
