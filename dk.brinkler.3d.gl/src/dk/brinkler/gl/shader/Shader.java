package dk.brinkler.gl.shader;

import java.nio.ByteBuffer;

public interface Shader {

	/**
	 * @return the args
	 */
	public String[] arguments();

	/**
	 * @return the fragmentShader
	 */
	public ByteBuffer fragmentShader();

	/**
	 * @return the vertexShader
	 */
	public ByteBuffer vertexShader();

	int program();

}
