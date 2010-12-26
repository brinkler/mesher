/**
 * 
 */
package dk.brinkler.core.mathutil;

import javax.vecmath.Vector3f;

/**
 * A ray in 3D space. The ray is described as
 * 
 * \f[ \mathbf{r}(t) = \mathbf{v}t + \mathbf{o} \f]
 * 
 */
public class Ray {
	// / the direction of the ray
	private Vector3f v;
	// / the origin of the ray.
	private Vector3f o;

	public Ray(Vector3f direction, Vector3f origin) {
		v = direction;
		o = origin;
	}

	/**
	 * @return the vector v describing direction of the ray
	 */
	public Vector3f getV() {
		return v;
	}

	/**
	 * @param u
	 *            the v to set
	 */
	public void setV(Vector3f u) {
		this.v = u;
	}

	/**
	 * @return the origin o of the ray.
	 */
	public Vector3f getO() {
		return o;
	}

	/**
	 * @param o
	 *            the o to set
	 */
	public void setO(Vector3f o) {
		this.o = o;
	}
}
