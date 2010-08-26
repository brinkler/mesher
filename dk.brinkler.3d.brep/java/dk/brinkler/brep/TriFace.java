/**
 * 
 */
package dk.brinkler.brep;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Triangles
 * @author riishigh
 */
public class TriFace implements Face {

	private Lath[] laths;

	/**
	 * @param edge
	 */
	public TriFace(Lath... laths) {
		assert (laths.length==3);
		this.laths = laths;
		for( Lath lath : laths ){
			// Register the face in the lath
			lath.face(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.brep.Face#add(dk.brinkler.brep.Edge)
	 */
	@Override
	public TriFace add(Lath l) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.brinkler.brep.Face#remove(dk.brinkler.brep.Edge)
	 */
	@Override
	public TriFace remove(Lath e) {
		if( Arrays.asList(laths).contains(e) ){
			laths[Arrays.asList(laths).indexOf(e)] = null;
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Lath> iterator() {
		return Arrays.asList(laths).iterator();
	}

}
