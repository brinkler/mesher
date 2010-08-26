package dk.brinkler.gl.glyphs;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;

import org.lwjgl.BufferUtils;

import dk.brinkler.gl.shader.BlinnShader;

/**
 * @author riishigh
 * 
 */
public class ArrowGlyph extends AbstractGlyph implements Glyph {

	public static FloatBuffer wrap(float... fs) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(fs.length);
		fb.put(fs);
		return fb;
	}

	public static ByteBuffer wrap(String s) {
		ByteBuffer bb = BufferUtils.createByteBuffer(s.getBytes().length + 1);
		bb.put((s + '\0').getBytes());
		bb.rewind();
		return bb;
		// return ByteBuffer.wrap((s + '\0').getBytes());
	}

	Point3d position = new Point3d();
	Quat4d orientation = new Quat4d();

	Point3d scale = new Point3d();
	Mode mode = Mode.NONE;
	final FloatBuffer vertexBuf;

	final FloatBuffer headBuf;

	float rotation = 0.0f;

	private IntBuffer vbos;

	private int blinnProg;
	private BlinnShader blinnShader;

	/**
	 * Four sides around "X"
	 * 
	 * @param barbW
	 */
	public ArrowGlyph(float l, float lineW, float barbW) {

		float[] singleVertexArray = { 0.0f, 0.0f, 0.0f, // origin
				lineW, -lineW, 0.0f, // low at origin
				l - lineW, -lineW, 0.0f, // low at target
				l, 0.0f, 0.0f, // target
				l - lineW, lineW, 0.0f, // high at origin
				lineW, lineW, 0.0f, // high at origin
				0.0f, 0.0f, 0.0f, // origin
		};
		float[] arrowHeadVertexArray = { l, 0.0f, 0.0f, // Arrow origin
				l - lineW, -lineW, 0.0f, // low at target
				l - barbW, -barbW, 0.0f, // arrow barb
				l + 6 * barbW, 0.0f, 0.0f, // arrow point
				l - barbW, barbW, 0.0f, // arrow barb
				l - lineW, lineW, 0.0f, // high at target
				l, 0.0f, 0.0f, // Arrow origin
		};
		// float[] vertexArray = singleVertexArray; //
		// reverse(singleVertexArray);
		vertexBuf = BufferUtils.createFloatBuffer(singleVertexArray.length);
		vertexBuf.put(singleVertexArray);

		headBuf = BufferUtils.createFloatBuffer(arrowHeadVertexArray.length);
		headBuf.put(arrowHeadVertexArray);
		// vertexBuf = FloatBuffer.wrap(vertexArray);
		// vertexBuf.rewind();
		// float[] headArray = reverse(arrowHeadVertexArray);
		// headBuf = FloatBuffer.wrap(headArray);
		// headBuf.rewind();
		init();
	}

	private int glUniformLocation(String name) {
		return glGetUniformLocation(blinnProg, wrap(name));
	}

	@Override
	public void init() {
		vbos = BufferUtils.createIntBuffer(2);
		glGenBuffers(vbos);
		glBindBuffer(GL_ARRAY_BUFFER, vbos.get(0));
		glBufferData(GL_ARRAY_BUFFER, vertexBuf, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, vbos.get(1));
		glBufferData(GL_ARRAY_BUFFER, headBuf, GL_DYNAMIC_DRAW);
		glEnableClientState(GL_VERTEX_ARRAY);
		blinnShader = new BlinnShader();
		// glGetProgram(blinnProg, GL_VALIDATE_STATUS, null);
		// glLinkProgram(blinnProg);
		glEnable(GL_LIGHTING);
	}

	@Override
	public void render() {
		// String name = "amb";
		// int ambientLocation = glUniformLocation(name);
		// glUniform1(ambientLocation, FloatBuffer.wrap(new float[] { 0.1f }));
		// name = "color";
		// int colorLocation = glUniformLocation(name);
		// glUniform3(colorLocation, FloatBuffer.wrap(new float[] { 0.0f, 0.0f,
		// 1.0f }));
		glUseProgram(blinnShader.program());
		glPushMatrix();
		{
			if (active()) {
				rotation += 10;
				glRotatef(rotation, 1.0f, 0.0f, 0.0f);
			}
			glPushMatrix();
			{
				renderArrow();
				glRotatef(90, 1.0f, 0f, 0f);
				renderArrow();
			}
			glPopMatrix();
		}
		glPopMatrix();
	}

	/**
	 * @param gl
	 */
	private void renderArrow() {
		glBindBuffer(GL_ARRAY_BUFFER, vbos.array()[0]);
		glVertexPointer(3, GL_FLOAT, 0, 0);
		glDrawArrays(GL_POLYGON, 0, 7);
		glDrawArrays(GL_POLYGON, 7, 7);
		glBindBuffer(GL_ARRAY_BUFFER, vbos.array()[1]);
		glVertexPointer(3, GL_FLOAT, 0, 0);
		glDrawArrays(GL_POLYGON, 0, 7);
		glDrawArrays(GL_POLYGON, 7, 7);
		int glError = glGetError();
		assert glError == 0;
	}

	/**
	 * @param singleVertexArray
	 * @return
	 */
	private float[] reverse(float[][] singleVertexArray) {
		List<float[]> list = Arrays.asList(singleVertexArray);
		List<float[]> revlist = Arrays.asList(singleVertexArray.clone());
		Collections.reverse(revlist);
		float[] vertexArray = new float[singleVertexArray.length * 6];
		int i = 0;
		for (float[] fs : list) {
			for (float f : fs) {
				vertexArray[i++] = f;
			}
		}
		for (float[] fs : revlist) {
			for (float f : fs) {
				vertexArray[i++] = f;
			}
		}
		return vertexArray;
	}
}
