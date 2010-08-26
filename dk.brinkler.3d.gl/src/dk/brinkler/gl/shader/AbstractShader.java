/**
 * 
 */
package dk.brinkler.gl.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.GL_SHADER_TYPE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glGetProgram;

/**
 * @author riishigh
 * 
 */
public abstract class AbstractShader implements Shader {
	@Override
	public String[] arguments() {
		return new String[] {};
	}

	@Override
	public String toString() {
		IntBuffer params = BufferUtils.createIntBuffer(1);
		glGetProgram(program(), GL_SHADER_TYPE, params);
		String shaderType = "GL_SHADER_TYPE = " + (params.get(0) == GL_VERTEX_SHADER ? "GL_VERTEX_SHADER" : "GL_FRAGMENT_SHADER");
		return "AbstractShader [" + shaderType + "]\n";
	}

	@Override
	abstract public ByteBuffer fragmentShader();

	/**
	 * @param fragmentShader
	 *            the fragmentShader to set
	 */
	abstract protected void fragmentShader(ByteBuffer fragmentShader);

	public ByteBuffer getShaderResource(String file) throws IOException {
		StringBuffer sourceBuffer = new StringBuffer();
		ClassLoader loader = getClass().getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(file);

		BufferedInputStream stream = new BufferedInputStream(inputStream);
		while (stream.available() > 0) {
			byte[] b = new byte[stream.available()];
			stream.read(b);
			sourceBuffer.append(b);
		}
		stream.close();
		return ByteBuffer.wrap(sourceBuffer.toString().getBytes());
	}

	protected String getShaderText2(String file) {
		String shader = null;

		try {
			ClassLoader loader = getClass().getClassLoader();
			InputStream inputStream = loader.getResourceAsStream(file);
			BufferedInputStream stream = new BufferedInputStream(inputStream);
			ByteBuffer fileBuffer = BufferUtils.createByteBuffer(1024 * 10);

			byte character;
			while ((character = (byte) stream.read()) != -1) {
				fileBuffer.put(character);
			}

			stream.close();

			fileBuffer.flip();

			byte[] array = new byte[fileBuffer.remaining()];
			fileBuffer.get(array);
			shader = new String(array);

			fileBuffer.clear();
		} catch (IOException e) {
		}

		return shader;
	}

	public ByteBuffer getShaderText(String file) throws IOException {
		// ClassLoader loader = getClass().getClassLoader();
		// InputStream inputStream = loader.getResourceAsStream(file);
		// BufferedInputStream stream = new BufferedInputStream(inputStream);
		//
		// StringBuffer sourceBuffer = new StringBuffer();
		// while (stream.available() > 0) {
		// byte[] b = new byte[stream.available()];
		// stream.read(b);
		// sourceBuffer.append(b);
		// }
		// stream.close();
		// String shaderText = sourceBuffer.toString();
		String shaderText = getShaderText2(file);
		ByteBuffer createByteBuffer = BufferUtils.createByteBuffer(shaderText.length());
		createByteBuffer.put(shaderText.getBytes());
		createByteBuffer.rewind();
		return createByteBuffer;
	}

	@Override
	abstract public ByteBuffer vertexShader();

	abstract protected void vertexShader(ByteBuffer vertexShader);
}
