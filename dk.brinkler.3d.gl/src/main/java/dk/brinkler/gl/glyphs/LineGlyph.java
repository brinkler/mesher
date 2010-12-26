/**
 * 
 */
package dk.brinkler.gl.glyphs;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author riishigh
 * 
 */
class LineGlyph extends AbstractGlyph implements Glyph {

//	private final FloatBuffer vertexBuf;

	/**
	 * Four sides around "X"
	 */
	public LineGlyph(float l, float w) {
		float[] vertexArray = { 0.0f, 0.0f, 0.0f, // origin
				w, -w, 0.0f, // low at origin
				l - w, -w, 0.0f, // low at target
				l, 0.0f, 0.0f, // target
				0.0f, 0.0f, 0.0f, // origin
				// High
				0.0f, 0.0f, 0.0f, // origin
				l, 0.0f, 0.0f, // target
				l - w, w, 0.0f, // high at target
				w, w, 0.0f, // high at origin
				0.0f, 0.0f, 0.0f, // origin
				// Horizontal
				0.0f, 0.0f, 0.0f, // origin
				w, 0.0f, -w, // low at origin
				l - w, 0.0f, -w, // low at target
				l, 0.0f, 0.0f, // target
				0.0f, 0.0f, 0.0f, // origin
				// V
				0.0f, 0.0f, 0.0f, // origin
				l, 0.0f, 0.0f, // target
				l - w, 0.0f, w, // high at target
				w, 0.0f, w, // high at origin
				0.0f, 0.0f, 0.0f, // origin
				// CW
				0.0f, 0.0f, 0.0f, // origin
				l, 0.0f, 0.0f, // target
				l - w, -w, 0.0f, // low at target
				w, -w, 0.0f, // low at origin
				0.0f, 0.0f, 0.0f, // origin
				// High
				0.0f, 0.0f, 0.0f, // origin
				w, w, 0.0f, // high at origin
				l - w, w, 0.0f, // high at target
				l, 0.0f, 0.0f, // target
				0.0f, 0.0f, 0.0f, // origin
				// Horizontal
				0.0f, 0.0f, 0.0f, // origin
				l, 0.0f, 0.0f, // target
				l - w, 0.0f, -w, // low at target
				w, 0.0f, -w, // low at origin
				0.0f, 0.0f, 0.0f, // origin
				// V
				0.0f, 0.0f, 0.0f, // origin
				w, 0.0f, w, // high at origin
				l - w, 0.0f, w, // high at target
				l, 0.0f, 0.0f, // target
				0.0f, 0.0f, 0.0f, // origin
		};
//		vertexBuf = BufferUtil.copyFloatBuffer(FloatBuffer.wrap(vertexArray));
//		vertexBuf.rewind();
	}

	float rotation = 0.0f;

	@Override
	public void render() {
		glPushMatrix();
//		{
//			if (active()) {
//				rotation += 20;
//				glRotated(rotation, 1.0, 0.0, 0.0);
//			}
//			int[] vbo = new int[2];
//			glGenBuffers(2, vbo, 0);
//
//			glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
//			glBufferData(GL_ARRAY_BUFFER, vertexBuf.capacity()
//					* BufferUtil.SIZEOF_FLOAT, vertexBuf, GL_STATIC_DRAW);
//			glVertexPointer(3, GL_FLOAT, 0, 0);
//			glEnableClientState(GL_VERTEX_ARRAY);
//			glColor3d(0.0, 0.0, 1.0);
//			glPolygonMode(GL_FRONT, GL_FILL);
//			glPolygonMode(GL_BACK, GL_FILL);
//			glFrontFace(GL_CW);
//			glDrawArrays(GL_POLYGON, 0, 5);
//			glDrawArrays(GL_POLYGON, 5, 5);
//			glDrawArrays(GL_POLYGON, 10, 5);
//			glDrawArrays(GL_POLYGON, 15, 5);
//			glDrawArrays(GL_POLYGON, 20, 5);
//			glDrawArrays(GL_POLYGON, 25, 5);
//			glDrawArrays(GL_POLYGON, 30, 5);
//			glDrawArrays(GL_POLYGON, 35, 5);
//			glLineWidth(5);
//			glColor3f(0.0f, 0.0f, 0.0f);
//			glDrawArrays(GL_LINE_LOOP, 0, 5);
//			glDrawArrays(GL_LINE_LOOP, 5, 5);
//			glDrawArrays(GL_LINE_LOOP, 10, 5);
//			glDrawArrays(GL_LINE_LOOP, 15, 5);
//			glDrawArrays(GL_LINE_LOOP, 20, 5);
//			glDrawArrays(GL_LINE_LOOP, 25, 5);
//			glDrawArrays(GL_LINE_LOOP, 30, 5);
//			glDrawArrays(GL_LINE_LOOP, 35, 5);
//			int glError = glGetError();
//			assert (glError == 0);
//		}
		glPopMatrix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.gl.glyphs.Glyph#init(javax.media.opengl.GL)
	 */
	public void init() {
	}
}