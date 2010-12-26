/**
 * 
 */
package dk.brinkler.gl.glyphs;

/**
 * @author riishigh
 * 
 */
public abstract class AbstractGlyph implements Glyph {

	private boolean active;

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.gl.glyphs.Glyph#active()
	 */
	public boolean active() {
		return active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.gl.glyphs.Glyph#active(boolean)
	 */
	public Glyph active(boolean active) {
		this.active = active;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.gl.glyphs.Glyph#render(javax.media.opengl.GL)
	 */
	public abstract void render();

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.gl.glyphs.Glyph#init(javax.media.opengl.GL)
	 */
	public abstract void init();
}
