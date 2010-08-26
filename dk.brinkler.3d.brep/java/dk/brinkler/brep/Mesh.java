/**
 * 
 */
package dk.brinkler.brep;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Tuple4f;

/**
 */
public class Mesh {
	Set<Vertex> vertices;
	Map<Lath, Edge> edges;

	/**
	 * 
	 */
	public Mesh() {
		super();
		vertices = new LinkedHashSet<Vertex>();
		edges = new LinkedHashMap<Lath, Edge>();
	}
	
	Vertex vertex(Tuple4f at) {
		Vertex vertex = null;
		if (vertices.contains(at)) {
			for (Vertex v : vertices) {
				if( vertex.equals(at)) {
					vertex = v;
				}
			}
		} else {
			vertex = new Vertex(this, at);
			vertices.add(vertex);
		}
		return vertex;
	}

	/**
	 * @param lath0
	 * @param lath1
	 * @return
	 */
	public Edge edge(Lath lath0, Lath lath1) {
		Edge edge = null;
		if( edges.containsKey(lath0) && edges.containsKey(lath1) ){
			// Edges contain lath0 so if lath0->target == lath1 we're done
			Edge edge0 = edges.get(lath0);
			Edge edge1 = edges.get(lath1);
			if( edge0 == edge1 && edge0.equals(edge1) ){
				// edge0 and edge1 are identical.... so the lath has already been added as an edge
				edge = edge0;
			}
		}
		if( edge == null ){
			edge = new Edge(this, lath0, lath1);
			edges.put(lath0, edge);
			edges.put(lath1, edge);
		}
		return edge;
	}

	@Override
	public String toString() {
		String s = "";
		s += "{\n";
		for( Lath l : edges.keySet() ){
			s += "   " + l + " e: {" + edges.get(l) + "}\n";
		}
		s += "}\n";
		return s;
	}
}
