package dk.brinkler.core.mathutil;

import java.util.ArrayList;
import java.util.Collection;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/**
 * Intersection class providing a number of static intersection methods.
 * 
 */
public class Intersect {
	private static final float epsilon = 0.000f;

	/**
	 * Do a ray plane intersection calculation using the algorithm outlined below The ray is described as:
	 * 
	 * \f[ r(t) = \mathbf{d}t + \mathbf{p}_o \f]
	 * 
	 * where \f$t \in [0; 1]\f$ and \f$d\f$ is the direction vector and \f$\mathbf{p}_o\f$ is the origin of the ray.
	 * 
	 * The algorithm also needs the characteristic description of the plane intersected by the ray:
	 * 
	 * \f[ \pi_p : \mathbf{n}_f \cdot \mathbf{p} + d_p = 0 \f]
	 * 
	 * where \f$\mathbf{n}_f\f$ describes the normal of the plane, \f$\mathbf{p}\f$ describes any position in the plane
	 * and \f$d_p\f$ denotes the minimum distance from the plane to the origin.
	 * 
	 * @param ray
	 *            the ray which is intersected with the plane
	 * @param plane
	 *            which is intersected by the ray
	 * @return coordinates for the intersection or null if no intersection (ie. the plane is parallel to the ray)
	 * @throws ParallelException
	 *             When the ray and plane cannot intersect.
	 */
	public static final Intersection calcIntersection(Ray ray, Plane plane) throws ParallelException {
		// First check if the ray and plane are parallel.

		float denom = 1.0f;
		denom = ray.getV().dot(plane.getN());
		if (denom == 0.0f) {
			throw new ParallelException();
		}

		Vector3f tmpDir = new Vector3f();
		tmpDir.sub(plane.getO(), ray.getO());
		float t = tmpDir.dot(plane.getN()) / denom;
		// Find intersection point
		Vector3f tmpPt = new Vector3f();
		tmpPt.set(ray.getV());
		tmpPt.scale(t);
		tmpPt.add(ray.getO());
		Intersection intPt = new Intersection();
		intPt.setIntersection(tmpPt);
		intPt.setT(t);
		// Find UV coords
		tmpDir.sub(intPt.getIntersection(), plane.getO());
		Vector2f uvCoords = new Vector2f(tmpDir.dot(plane.getU()), tmpDir.dot(plane.getV()));
		intPt.setUVCoords(uvCoords);
		return intPt;
	}

	/**
	 * Do multiple intersections returning a <code>Collection<Intersect></code> with the valid intersections or
	 * emptyset if all rays are parallel. No exceptions will be thrown.
	 */
	public static final Collection<Intersection> calcIntersection(Collection<Ray> rays, Plane plane) {
		Collection<Intersection> result = new ArrayList<Intersection>(rays.size());
		for (Ray ray : rays) {
			try {
				Intersection calcIntersection = calcIntersection(ray, plane);
				result.add(calcIntersection);
			} catch (ParallelException e) {
				// We just fail silently if ray did not intersect plane.
			}
		}
		return result;
	}
}
