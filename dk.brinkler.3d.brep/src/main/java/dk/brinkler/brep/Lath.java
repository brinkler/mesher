/**
 * 
 */
package dk.brinkler.brep;

/**
 * A Lath has a origin point and a target lath and a reference to the edge it is part of A lath can only be part of one
 * edge, and may only have one target lath and only be part in one face.
 * 
 */
public class Lath implements Comparable<Lath> {

	Lath target;
	Edge edge;
	private Face face;

	final Vertex origin;

	/**
	 * @return the edge
	 */
	public Edge edge() {
		return edge;
	}

	/**
	 * @param origin
	 */
	public Lath(Vertex origin) {
		this.origin = origin;
		origin.originInLath.add(this);
	}

	/**
	 * @param origin
	 * @param target
	 */
	public Lath(Vertex origin, Lath target) {
		this(origin);
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public Lath target() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void target(Lath target) {
		this.target = target;
	}

	/**
	 * @param face
	 *            the face to set
	 */
	public void face(Face face) {
		this.face = face;
	}

	/**
	 * @return the face
	 */
	public Face face() {
		return face;
	}

	/**
	 * @return the origin
	 */
	public Vertex origin() {
		return origin;
	}

	@Override
	public String toString() {
		return "o: " + origin + "->" + (target != null ? target.origin() : "null") + " f: " + face;
	}

	@Override
	public boolean equals(Object o) {
		if( o instanceof Lath ){
			Lath lath = (Lath) o;
			return (this.compareTo(lath) == 0);
		}else{
			return false;
		}
	}

	public int compareTo(Lath o) {
		// compare two refs
		if( o == this ){
			return 0;
		}
		if( o instanceof Lath ){
			Lath l = (Lath) o;
			int resultOrigin = this.origin.compareTo(l.origin);
			int resultTargetOrigin = this.target().origin.compareTo(l.target.origin());
			int result = resultOrigin | resultTargetOrigin;
			return result;
		}
		return Integer.MAX_VALUE;
	}

	public int hashCode() {
		int result = this.origin.hashCode() * 31 + this.target.origin.hashCode();
		return result;
	}
}
