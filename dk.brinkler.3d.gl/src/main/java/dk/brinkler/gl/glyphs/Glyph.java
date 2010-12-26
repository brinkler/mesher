/**
 * 
 */
package dk.brinkler.gl.glyphs;


/**
 * @author riishigh
 *
 */
public interface Glyph {

	/**
	 * @param gl
	 */
	void render();

	boolean active();

	Glyph active(boolean active);

	public void init();

}
