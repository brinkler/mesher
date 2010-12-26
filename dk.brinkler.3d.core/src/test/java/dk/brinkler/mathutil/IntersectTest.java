/**
 * 
 */
package dk.brinkler.mathutil;

import static java.lang.Math.abs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import junit.framework.TestCase;
import dk.brinkler.core.mathutil.Intersect;
import dk.brinkler.core.mathutil.Intersection;
import dk.brinkler.core.mathutil.ParallelException;
import dk.brinkler.core.mathutil.Plane;
import dk.brinkler.core.mathutil.Ray;

/**
 * @author riishigh
 * 
 */
public class IntersectTest extends TestCase {
	public void testXZandY() throws ParallelException {
		Plane plane = new Plane(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f));
		plane.setO(new Vector3f());
		float RO = 13.13f;
		Ray ray = new Ray(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -RO, 0.0f));

		Intersection calcIntersection = Intersect.calcIntersection(ray, plane);
		assertEquals(new Vector3f(), calcIntersection.getIntersection());
		assertEquals(RO, calcIntersection.getT());
		assertEquals(new Vector2f(), calcIntersection.getUVCoords());
	}

	public void testYZandX() throws ParallelException {
		float RO = 14.14f;
		Plane plane = new Plane(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f));
		plane.setO(new Vector3f());
		Ray ray = new Ray(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-RO, 0.0f, 0.0f));

		Intersection calcIntersection = Intersect.calcIntersection(ray, plane);
		assertEquals(new Vector3f(), calcIntersection.getIntersection());
		assertEquals(RO, calcIntersection.getT());
		assertEquals(new Vector2f(), calcIntersection.getUVCoords());
	}

	public void testSkewandX() throws ParallelException {
		float RO = 14.14f;
		Vector3f u = new Vector3f(0.0f, -1.0f, 1.0f);
		u.normalize();
		Vector3f n = new Vector3f(1.0f, 1.0f, 1.0f);
		n.normalize();
		Plane plane = new Plane(u, n);
		plane.setO(new Vector3f());
		Ray ray = new Ray(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-RO, 0.0f, 0.0f));

		Intersection calcIntersection = Intersect.calcIntersection(ray, plane);
		Vector3f o = new Vector3f();
		o.sub(calcIntersection.getIntersection());
		assertTrue(o.length() < 0.001f);
		assertTrue(RO - calcIntersection.getT() < 0.001f);
		Vector2f to = new Vector2f();
		to.sub(calcIntersection.getUVCoords());
		assertTrue(to.length() < 0.001f);
	}

	private Collection<Vector3f> loop(double d) {
		int num_slices = 9;
		double constant = 0.1;
		double dtheta = 2 * Math.PI / num_slices;
		Collection<Vector3f> result = new LinkedList<Vector3f>();
		for (int i = 0; i < num_slices + 1; i++) {
			float x = (float) (constant * (Math.cos(dtheta * i)));
			float y = (float) (constant * (Math.sin(dtheta * i)));
			result.add(new Vector3f(x, y, 0.0f));
		}
		return result;
	}

	public void testMultiple() {
		Vector3f rayOrigin = new Vector3f(0.0f, 0.0f, -6.6f);
		Vector3f planeNomal = new Vector3f(0.0f, 0.0f, -1.0f);

		Collection<Vector3f> collisions = loop(0.5f);
		Collection<Ray> rays = new LinkedList<Ray>();
		for (Vector3f target : collisions) {
			Vector3f rayDirection = new Vector3f(target);
			rayDirection.sub(rayOrigin);
			rayDirection.normalize();
			Ray r = new Ray(rayDirection, rayOrigin);
			rays.add(r);
		}

		Plane plane = new Plane(new Vector3f(new Vector3f(1.0f, 0.0f, 0.0f)), planeNomal, new Vector3f());


		Collection<Intersection> calcIntersection = Intersect.calcIntersection(rays, plane);
		Iterator it = calcIntersection.iterator();
		assertEquals(collisions.size(), calcIntersection.size());
		for (Vector3f p : collisions) {
			Intersection next = (Intersection) it.next();
			Vector3f intersection = next.getIntersection();
			Vector2f uvCoords = next.getUVCoords();
			assertTrue(abs(intersection.getX() - uvCoords.getX()) < 0.001f);
			assertTrue(abs(intersection.getY() - uvCoords.getY()) < 0.001f);
			assertTrue(abs(intersection.getZ()) < 0.001f);
			p.sub(intersection);
			assertTrue(p.length() < 0.001f);
		}
	}

	public void testAxisOne() throws ParallelException {
		Vector3f U = new Vector3f(0.0f, 0.0f, 1.0f);
		Vector3f V = new Vector3f();
		Vector3f N = new Vector3f(1.0f, 1.0f, 0.0f);
		N.normalize();
		Vector3f O = new Vector3f();

		Plane p = new Plane(U, N, O);
		p.setV(V);

		Ray r = new Ray(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f));

		Intersection calcIntersection = Intersect.calcIntersection(r, p);
		Vector2f coords = calcIntersection.getUVCoords();
		System.out.println(coords);
	}

	public void testAxisMultiple() {
		Vector3f rayOrigin = new Vector3f(6.0f, 0.0f, 6.0f);

		Vector3f U = new Vector3f(1.0f, 0.0f, 0.0f);
		Vector3f V = new Vector3f();
		Vector3f N = new Vector3f(0.0f, 1.0f, 1.0f);
		N.normalize();
		Vector3f O = new Vector3f();


		Collection<Vector3f> collisions = loop(0.5f);
		Collection<Ray> rays = new LinkedList<Ray>();
		for (Vector3f target : collisions) {
			Vector3f rayDirection = new Vector3f(target);
			rayDirection.sub(rayOrigin);
			rayDirection.normalize();
			Ray r = new Ray(rayDirection, rayOrigin);
			rays.add(r);
		}

		Plane plane = new Plane(U, N, O);
		plane.setV(V);

		Collection<Intersection> calcIntersection = Intersect.calcIntersection(rays, plane);

		Iterator<Intersection> it = calcIntersection.iterator();
		assertEquals(collisions.size(), calcIntersection.size());
		for (Vector3f p : collisions) {
			Intersection next = it.next();
			Vector3f intersection = next.getIntersection();
			Vector2f uvCoords = next.getUVCoords();
			//			assertTrue(abs(intersection.getX() - uvCoords.getX()) < 0.001f);
			//			assertTrue(abs(intersection.getY() - uvCoords.getY()) < 0.001f);
			//			assertTrue(abs(intersection.getZ()) < 0.001f);
			System.out.println(intersection + " x " + uvCoords + " e " +p);
			//			p.sub(intersection);
			//			assertTrue(p.length() < 0.001f);
		}
	}
}
