/**
 * 
 */
package dk.brinkler.brep;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Tuple4f;

/**
 * @author riishigh
 * 
 */
public class Vertex {
	Mesh mesh;
	Tuple4f position;
	List<Lath> originInLath;

	/**
	 * @return the originInLath
	 */
	public List<Lath> laths() {
		return originInLath;
	}

	/**
	 * @param at
	 */
	public Vertex(Mesh mesh, Tuple4f at) {
		this.mesh = mesh;
		this.position = at;
		originInLath = new LinkedList<Lath>();
	}

	public Edge split(Vertex at) {
		Lath lath0 = new Lath(this);
		Lath lath1 = new Lath(at);
		lath0.target(lath1);
		lath1.target(lath0);
		return mesh.edge(lath0, lath1);
	}

	@Override
	public String toString() {
		return position.toString();
	}

	@Override
	public boolean equals(Object o) {
		Tuple4f oPos = null;
		if( o instanceof Vertex ){
			oPos = ((Vertex) o).position;
		}else if( o instanceof Tuple4f ){
			oPos = (Tuple4f) o;
		}else{
			// Can only compare this to vertex or tuple4f
			return false;
		}
		return position.equals(oPos);
	}

	@Override
	public int hashCode() {
		return this.position.hashCode();
	}

	/**
	 * @param other
	 * @return
	 */
	public int compareTo(Vertex other) {
		return (Float.compare(position.x, other.position.x) << 5) + (Float.compare(position.y, other.position.y) << 4)
				+ (Float.compare(position.z, other.position.z) << 3) + (Float.compare(position.w, other.position.w) << 2);

	}
}
