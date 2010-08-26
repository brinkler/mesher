package dk.brinkler.gl.shader;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class BlinnShader extends AbstractShader implements Shader {
	ByteBuffer fragmentShader;
	ByteBuffer vertexShader;
	final String[] args = { "color", "amb" };
	private final int blinnProg;

	public BlinnShader() {
		try {
			ByteBuffer fragmentShaderText = getShaderText("blinn.fs");
			fragmentShader(fragmentShaderText);
			ByteBuffer vertexShaderText = getShaderText("blinn.vs");
			vertexShader(vertexShaderText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		blinnProg = glCreateProgram();

		int blinnVS = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(blinnVS, vertexShader());
		glCompileShader(blinnVS);
		glAttachShader(blinnProg, blinnVS);

		int blinnFS = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(blinnFS, fragmentShader());
		glCompileShader(blinnFS);
		glAttachShader(blinnProg, blinnFS);

		glValidateProgram(blinnProg);
		glLinkProgram(blinnProg);
		System.out.println("Shader : " + this.toString());

	}

	/**
	 * @return the fragmentShader
	 */
	@Override
	public ByteBuffer fragmentShader() {
		return fragmentShader;
	}

	/**
	 * @param fragmentShader
	 *            the fragmentShader to set
	 */
	@Override
	public void fragmentShader(ByteBuffer fragmentShader) {
		this.fragmentShader = fragmentShader;
	}

	/**
	 * @return the vertexShader
	 */
	@Override
	public ByteBuffer vertexShader() {
		return vertexShader;
	}

	/**
	 * @param vertexShader
	 *            the vertexShader to set
	 */
	@Override
	public void vertexShader(ByteBuffer vertexShader) {
		this.vertexShader = vertexShader;
	}

	/**
	 * @return the args
	 */
	@Override
	public String[] arguments() {
		return args;
	}

	@Override
	public int program() {
		return blinnProg;
	}
}
