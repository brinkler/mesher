/**
 * 
 */
package dk.brinkler.brep;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Point4f;

import org.junit.Before;
import org.junit.Test;

/**
 * @author riishigh
 *
 */
public class TestEdge {

	Mesh mesh;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mesh = new Mesh();
	}

	/**
	 * Test method for {@link dk.brinkler.brep.Edge#split(dk.brinkler.brep.Vertex)}.
	 * 
	 * @throws ManifoldException
	 */
	@Test
	public void testSplit() throws ManifoldException {
		Point4f p0 = new Point4f(0, 0, 0, 0);
		Point4f p1 = new Point4f(1, 0, 0, 0);
		Point4f p2 = new Point4f(1, 1, 0, 0);
		Vertex v0 = mesh.vertex(p0);
		Vertex v1 = mesh.vertex(p1);
		Vertex v2 = mesh.vertex(p2);
		// Create and edge from v0 to v1
		Edge edge = v0.split(v1);
		assertEquals("Lath0 origin not equal to vertex 0", edge.laths.getFirst().origin, v0);
		assertEquals("Lath1 origin not equal to vertex 1", edge.laths.getLast().origin, v1);
		assertEquals("Lath0 target not equal to lath1 of the split edge", edge.laths.getFirst().target, edge.laths.getLast());
		assertEquals("Lath1 target not equal to lath0 of the split edge", edge.laths.getLast().target, edge.laths.getFirst());
		// Create a triangle face by splitting the edge with vertex v2
		TriFace splitTri = edge.split(v2);
		Set<Edge> s = new HashSet<Edge>(mesh.edges.values());
		assertEquals("Invalid number of edges in mesh", 3, s.size());
		for( Edge e : s ){
			assertEquals("Incorrect face of first lath", splitTri, e.laths.getFirst().face());
			assertEquals("Incorrect face of last lath", null, e.laths.getLast().face());
		}

	}

}
