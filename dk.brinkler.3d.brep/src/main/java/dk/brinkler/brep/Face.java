/**
 * 
 */
package dk.brinkler.brep;

/**
 * @author riishigh
 *
 */
public interface Face extends Iterable<Lath> {
	/**
	 * Add an edge to the face
	 * 
	 * @param l
	 *            the edge to add to the face
	 * @return
	 */
	Face add(Lath l);

	/**
	 * Remove an edge from the face
	 * 
	 * @param e
	 * @return
	 */
	Face remove(Lath e);
}
