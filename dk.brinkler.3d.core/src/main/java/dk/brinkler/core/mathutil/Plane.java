/**
 * 
 */
package dk.brinkler.core.mathutil;

import javax.vecmath.Vector3f;


/**
 * Plane specified using a normal vector and another vector which lies in the plane (N, U) the plane is spanned by the
 * vectors (U, V) where V is calculated as the crossproduct of U and N.
 * 
 * \f[\mathbf{V} = \mathbf{U} \times \mathbf{N}\f]
 * 
 * U and N must be orthogonal.
 */
public class Plane {
	private Vector3f U;
	private Vector3f V = null;
	private Vector3f N;
	private Vector3f O;

	/**
	 * Create the plane using the U and N vectors.
	 * 
	 * @param u
	 *            vector which is the vector which lies in the plane.
	 * @param n
	 *            normal vector for the plane.
	 * @param o
	 */
	public Plane(Vector3f u, Vector3f n, Vector3f o) {
		this(u, n);
		O = o;
	}

	public Plane(Vector3f u, Vector3f n) {
		super();
		U = u;
		N = n;
	}

	/**
	 * @return the U vector
	 */
	public Vector3f getU() {
		return U;
	}

	/**
	 * @param u
	 *            the U vector which describes the plane with the normal vector to set
	 */
	public void setU(Vector3f u) {
		U = u;
		V = null;
	}

	/**
	 * This getter performs the cross product operation and returns the result.
	 * 
	 * @return the V vector
	 */
	public Vector3f getV() {
		if (V == null) {
			V = new Vector3f();
			V.cross(getU(), getN());
		}
		return V;
	}

	public void setV(Vector3f v) {
		V = v;
	}

	/**
	 * @return the n
	 */
	public Vector3f getN() {
		return N;
	}

	/**
	 * @param n
	 *            the n to set
	 */
	public void setN(Vector3f n) {
		N = n;
		V = null;
	}

	public Vector3f getOrigin() {
		return O;
	}

	/**
	 * @return the o
	 */
	public Vector3f getO() {
		return O;
	}

	/**
	 * @param o
	 *            the o to set
	 */
	public void setO(Vector3f o) {
		O = o;
	}
}
