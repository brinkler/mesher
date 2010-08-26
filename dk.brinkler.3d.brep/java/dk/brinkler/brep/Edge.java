/**
 * 
 */
package dk.brinkler.brep;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Edge {
	LinkedList<Lath> laths = new LinkedList<Lath>();
	private final Mesh mesh;

	/**
	 * @param mesh
	 * @param lath0
	 * @param lath1
	 */
	public Edge(Mesh mesh, Lath lath) {
		this.mesh = mesh;
		laths.add(lath);
	}

	/**
	 * @param mesh2
	 * @param lath0
	 * @param lath1
	 */
	public Edge(Mesh mesh, Lath lath0, Lath lath1) {
		this(mesh, lath0);
		laths.add(lath1);
		lath0.edge = this;
		lath1.edge = this;
	}

	/**
	 * @return the lath0
	 */
	public List<Lath> laths() {
		return laths;
	}

	/**
	 * @param lath
	 *            the lath to add
	 */
	public void lath(Lath lath) {
		laths.add(lath);
	}

	/**
	 * Split the edge into a face
	 * 
	 * @param vertex
	 * @return
	 * @throws ManifoldException
	 */
	TriFace split(Vertex vertex) throws ManifoldException {
		Lath l0_0 = null;
		if( laths.getFirst().face() == null ){
			l0_0 = laths.getFirst();
		}else if( laths.getLast().face() == null ){
			l0_0 = laths.getLast();
		}else{
			throw new ManifoldException();
		}
		// Create 2 new laths l1, l2
		Lath l1_0 = new Lath(l0_0.target().origin());
		Lath l2_0 = new Lath(vertex);
		
		l0_0.target(l1_0);
		l1_0.target(l2_0);
		l2_0.target(l0_0);

		Lath l1_1 = new Lath(l1_0.origin, l1_0);
		Lath l2_1 = new Lath(l2_0.origin, l2_0);

		mesh.edge(l1_0, l1_1);
		mesh.edge(l2_0, l2_1);

		TriFace f = new TriFace(l0_0, l1_0, l2_0);

		// Create a new face
		// Need to find the suitable edges if they already exist.

		// Add face to faceless lath.. It now has saved face.
		return f;
	}

}
