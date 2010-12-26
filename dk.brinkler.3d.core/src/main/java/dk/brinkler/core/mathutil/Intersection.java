/**
 * 
 */
package dk.brinkler.core.mathutil;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Class encapsulating intersection coords in various representation (World, UV)
 * 
 */
public class Intersection {
	Vector3f intersection;
	Vector2f uvCoords;
	private float t;

	/**
	 * @return the intersection
	 */
	public Vector3f getIntersection() {
		return intersection;
	}

	/**
	 * @param intersection
	 *            the intersection to set
	 */
	public void setIntersection(Vector3f intersection) {
		this.intersection = intersection;
	}

	/**
	 * @return the uvCoords
	 */
	public Vector2f getUVCoords() {
		return uvCoords;
	}

	/**
	 * @param uvCoords
	 *            the uvCoords to set
	 */
	public void setUVCoords(Vector2f uvCoords) {
		this.uvCoords = uvCoords;
	}

	public void setT(float t) {
		this.t = t;
	}

	/**
	 * @return the t
	 */
	public float getT() {
		return t;
	}

	@Override
	public String toString() {
		return "{intersection='" + intersection + "', uvCoords='" + uvCoords + "', t='" + t + "'}";
	}
}
