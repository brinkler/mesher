/**
 * 
 */
package dk.brinkler.brep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.vecmath.Point4f;

import org.junit.Before;
import org.junit.Test;

/**
 * @author riishigh
 * 
 */
public class TestVertex {

	Mesh mesh;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mesh = new Mesh();
	}

	@Test
	public void testCompareTo() {
		Point4f at = new Point4f(0, 0, 0, 0);
		Point4f lessX = new Point4f(-1, 0, 0, 0);
		Point4f lessY = new Point4f(0, -1, 0, 0);
		Point4f lessZ = new Point4f(0, 0, -1, 0);
		Point4f lessW = new Point4f(0, 0, 0, -1);
		Vertex vertex = mesh.vertex(at);
		Vertex vLessX = mesh.vertex(lessX);
		Vertex vLessY = mesh.vertex(lessY);
		Vertex vLessZ = mesh.vertex(lessZ);
		Vertex vLessW = mesh.vertex(lessW);
		assertTrue("Vertex and fetched vertex was not equal", vertex.equals(at));
		assertEquals("compare to returned invalid integer", 0, vertex.compareTo(vertex));
		// Direct comparison
		assertEquals("compare to returned invalid integer", 32, vertex.compareTo(vLessX));
		assertEquals("compare to returned invalid integer", 16, vertex.compareTo(vLessY));
		assertEquals("compare to returned invalid integer", 8, vertex.compareTo(vLessZ));
		assertEquals("compare to returned invalid integer", 4, vertex.compareTo(vLessW));
		// Inverse direct comparison
		assertEquals("compare to returned invalid integer", -32, vLessX.compareTo(vertex));
		assertEquals("compare to returned invalid integer", -16, vLessY.compareTo(vertex));
		assertEquals("compare to returned invalid integer", -8, vLessZ.compareTo(vertex));
		assertEquals("compare to returned invalid integer", -4, vLessW.compareTo(vertex));
		// least distances
		assertEquals("compare to returned invalid integer", -16, vLessX.compareTo(vLessY));
		assertEquals("compare to returned invalid integer", -8, vLessY.compareTo(vLessZ));
		assertEquals("compare to returned invalid integer", -4, vLessZ.compareTo(vLessW));

		assertEquals("compare to returned invalid integer", 28, vLessW.compareTo(vLessX));
		assertEquals("compare to returned invalid integer", 12, vLessW.compareTo(vLessY));

		assertEquals("compare to returned invalid integer", 16, vLessY.compareTo(vLessX));
		assertEquals("compare to returned invalid integer", 8, vLessZ.compareTo(vLessY));
		assertEquals("compare to returned invalid integer", 4, vLessW.compareTo(vLessZ));
	}

	/**
	 * Test method for {@link dk.brinkler.brep.Vertex#Vertex(javax.vecmath.Tuple4f)}.
	 */
	@Test
	public void testVertex() {
		Point4f at = new Point4f(0, 0, 0, 0);
		Vertex vertex = mesh.vertex(at);
		assertTrue("Vertex and fetched vertex was not equal", vertex.equals(at));
		vertex = mesh.vertex(at);
		assertTrue("Vertex and fetched cached vertex was not equal", vertex.equals(at));
		assertEquals("Too many vertices in mesh", 1, mesh.vertices.size());
	}

	/**
	 * Test method for {@link dk.brinkler.brep.Vertex#split(dk.brinkler.brep.Vertex)}.
	 */
	@Test
	public void testSplitVertex() {
		Point4f from = new Point4f(0, 0, 0, 0);
		Point4f to = new Point4f(1, 0, 0, 0);
		Vertex vertex0 = mesh.vertex(from);
		Vertex vertex1 = mesh.vertex(to);
		Edge split = vertex0.split(vertex1);
		assertTrue("Lath0 origin not equal to vertex 0", split.laths.getFirst().origin.equals(vertex0));
		assertTrue("Lath1 origin not equal to vertex 1", split.laths.getLast().origin.equals(vertex1));
		assertTrue("Lath0 target not equal to lath1 of the split edge", split.laths.getFirst().target.equals(split.laths.getLast()));
		assertTrue("Lath1 target not equal to lath0 of the split edge", split.laths.getLast().target.equals(split.laths.getFirst()));
	}

}
