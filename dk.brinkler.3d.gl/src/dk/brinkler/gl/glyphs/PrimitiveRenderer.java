package dk.brinkler.gl.glyphs;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class PrimitiveRenderer extends Object {

	public static class Cone {
		List<Double> verticesList = new ArrayList<Double>();
		double[] vertices = null;
		// new double[3*(num_slices*2+1)];
		int[] faceIndex;
		List<Integer> facesIndexList = new ArrayList<Integer>();
		private final DoubleBuffer verticesBuf = null;
		private final IntBuffer facesBuf;

		public Cone(double length, double width, int slices) {
			double dtheta = 2 * Math.PI / slices;
			verticesList.add(0.0);
			verticesList.add(0.0);
			verticesList.add(length);
			facesIndexList.add(0);
			for( int i = 0; i < slices + 1; i++ ){
				double x1 = width * Math.cos(dtheta * i);
				double y1 = width * Math.sin(dtheta * i);
				double z1 = 0.0;

				facesIndexList.add(i + 1);
				verticesList.add(x1);
				verticesList.add(y1);
				verticesList.add(z1);
			}
			// verticesBuf = BufferUtil.newDoubleBuffer(verticesList.size());
			DoubleBuffer.allocate(verticesList.size());
			for( Double xyz : verticesList ){
				verticesBuf.put(xyz);
			}
			verticesBuf.rewind();
			facesBuf = IntBuffer.allocate(facesIndexList.size());
			for( Integer n : facesIndexList ){
				facesBuf.put(n);
			}
			facesBuf.rewind();
		}

		public void render() {

			glEnableClientState(GL_VERTEX_ARRAY);

			glVertexPointer(3, GL_DOUBLE, verticesBuf);
			glDrawElements(GL_TRIANGLE_FAN, facesBuf);
			// glDrawElements(GL_TRIANGLE_FAN, facesIndexList.size(),
			// GL_UNSIGNED_INT, facesBuf);

			glDisableClientState(GL_VERTEX_ARRAY);
		}
	}

	public static final float PLANEWIDTH = 0.5f;

	public static LineGlyph drawArrow(float length) {
		LineGlyph lineGlyph = new LineGlyph(length, 0.1f);
		lineGlyph.render();
		{
			glTranslated(0.0f, 0.0f, length);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		}
		return lineGlyph;
	}

	public static void drawBox(int spanX, int spanY, int spanZ) {
		double dx = 1.0 / spanX;
		double dy = 1.0 / spanY;
		double dz = 1.0 / spanZ;
		// Draw front and back
		for( int i = 0; i < spanX; ++i ){
			for( int j = 0; j < spanY; ++j ){
				glBegin(GL_QUADS);
				glVertex3d(-0.5 + i * dx, -0.5 + j * dy, 0.5);
				glVertex3d(-0.5 + i * dx + dx, -0.5 + j * dy, 0.5);
				glVertex3d(-0.5 + i * dx + dx, -0.5 + j * dy + dy, 0.5);
				glVertex3d(-0.5 + i * dx, -0.5 + j * dy + dy, 0.5);
				glEnd();
				glBegin(GL_QUADS);
				glVertex3d(-0.5 + i * dx + dx, -0.5 + j * dy, -0.5);
				glVertex3d(-0.5 + i * dx, -0.5 + j * dy, -0.5);
				glVertex3d(-0.5 + i * dx, -0.5 + j * dy + dy, -0.5);
				glVertex3d(-0.5 + i * dx + dx, -0.5 + j * dy + dy, -0.5);
				glEnd();
			}
		}
		// Draw left and right
		for( int i = 0; i < spanZ; ++i ){
			for( int j = 0; j < spanY; ++j ){
				glBegin(GL_QUADS);
				glVertex3d(-0.5, -0.5 + j * dy, -0.5 + i * dz);
				glVertex3d(-0.5, -0.5 + j * dy, -0.5 + i * dz + dz);
				glVertex3d(-0.5, -0.5 + j * dy + dy, -0.5 + i * dz + dz);
				glVertex3d(-0.5, -0.5 + j * dy + dy, -0.5 + i * dz);
				glEnd();
				glBegin(GL_QUADS);
				glVertex3d(0.5, -0.5 + j * dy, -0.5 + i * dz + dz);
				glVertex3d(0.5, -0.5 + j * dy, -0.5 + i * dz);
				glVertex3d(0.5, -0.5 + j * dy + dy, -0.5 + i * dz);
				glVertex3d(0.5, -0.5 + j * dy + dy, -0.5 + i * dz + dz);
				glEnd();
			}
		}
		// Draw top and bottom
		for( int i = 0; i < spanX; ++i ){
			for( int j = 0; j < spanY; ++j ){
				glBegin(GL_QUADS);
				glVertex3d(-0.5 + i * dx, 0.5, -0.5 + j * dy);
				glVertex3d(-0.5 + i * dx + dx, 0.5, -0.5 + j * dy);
				glVertex3d(-0.5 + i * dx + dx, 0.5, -0.5 + j * dy + dy);
				glVertex3d(-0.5 + i * dx, 0.5, -0.5 + j * dy + dy);
				glEnd();
				glBegin(GL_QUADS);
				glVertex3d(-0.5 + i * dx, -0.5, -0.5 + j * dy);
				glVertex3d(-0.5 + i * dx + dx, -0.5, -0.5 + j * dy);
				glVertex3d(-0.5 + i * dx + dx, -0.5, -0.5 + j * dy + dy);
				glVertex3d(-0.5 + i * dx, -0.5, -0.5 + j * dy + dy);
				glEnd();
			}
		}
	}

	public static void drawMoveGlyph(Mode mode) {
		// X direction arrow.
		if( mode == Mode.NONE || mode == Mode.X || mode == Mode.XY || mode == Mode.XZ || mode == Mode.XYZ ){
			glPushMatrix();
			if( mode == Mode.X || mode == Mode.XY || mode == Mode.XZ || mode == Mode.XYZ ){
				// Highlight
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(1.0f, 0.0f, 0.0f);
			}
			glRotatef(90, 0.0f, 1.0f, 0.0f);
			glPushName(Mode.X.ordinal());
			glPopName();
			glPopMatrix();
		}
		// Y direction arrow.
		if( mode == Mode.NONE || mode == Mode.Y || mode == Mode.XY || mode == Mode.ZY || mode == Mode.XYZ ){
			glPushMatrix();
			if( mode == Mode.Y || mode == Mode.XY || mode == Mode.ZY || mode == Mode.XYZ ){
				// Highlight
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(0.0f, 1.0f, 0.0f);
			}
			glRotatef(-90, 1.0f, 0.0f, 0.0f);
			glPushName(Mode.Y.ordinal());
			// drawArrow(gl, 0.2, 1.0);
			glPopName();
			glPopMatrix();
		}
		// Z direction arrow.
		if( mode == Mode.NONE || mode == Mode.Z || mode == Mode.XZ || mode == Mode.ZY || mode == Mode.XYZ ){
			glPushMatrix();

			if( mode == Mode.Z || mode == Mode.XZ || mode == Mode.ZY || mode == Mode.XYZ ){
				// Highlight
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(0.0f, 0.0f, 1.0f);
			}
			glRotatef(-90, 0.0f, 0.0f, 1.0f);
			glPushName(Mode.Z.ordinal());
			// drawArrow(gl, 0.2, 1.0);
			glPopName();
			glPopMatrix();
		}

		// These ought to be sorted relatively to the z-buffer depth.
		// XY plane
		if( mode == Mode.NONE || mode == Mode.XY ){
			glPushMatrix();
			if( mode == Mode.XY ){
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(0.0f, 0.0f, 1.0f);
			}
			glPushName(Mode.XY.ordinal());
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glBegin(GL_POLYGON);
			glColor4f(0.7f, 0.0f, 0.0f, 0.5f);
			glVertex3f(PLANEWIDTH, 0.0f, 0.0f);
			glColor4f(0.35f, 0.35f, 0.0f, 0.5f);
			glVertex3f(PLANEWIDTH, PLANEWIDTH, 0.0f);
			glColor4f(0.0f, 0.7f, 0.0f, 0.5f);
			glVertex3f(0.0f, PLANEWIDTH, 0.0f);
			glColor4f(0.35f, 0.35f, 0.0f, 0.5f);
			glVertex3f(0.0f, 0.0f, 0.0f);
			glEnd();
			glDisable(GL_BLEND);
			glPopName();
			glPopMatrix();
		}
		// ZY plane
		if( mode == Mode.NONE || mode == Mode.ZY ){
			glPushMatrix();
			if( mode == Mode.XY ){
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(0.0f, 0.0f, 1.0f);
			}
			glPushName(Mode.ZY.ordinal());
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glBegin(GL_POLYGON);
			glColor4f(0.0f, 0.0f, 0.7f, 0.5f);
			glVertex3f(0.0f, 0.0f, PLANEWIDTH);
			glColor4f(0.0f, 0.35f, 0.35f, 0.5f);
			glVertex3f(0.0f, PLANEWIDTH, PLANEWIDTH);
			glColor4f(0.0f, 0.7f, 0.0f, 0.5f);
			glVertex3f(0.0f, PLANEWIDTH, 0.0f);
			glColor4f(0.0f, 0.35f, 0.35f, 0.5f);
			glVertex3f(0.0f, 0.0f, 0.0f);
			glEnd();
			glDisable(GL_BLEND);
			glPopName();
			glPopMatrix();
		}
		// XZ plane
		if( mode == Mode.NONE || mode == Mode.XZ ){
			glPushMatrix();
			if( mode == Mode.XY ){
				glColor3f(1.0f, 1.0f, 0.0f);
			}else{
				glColor3f(0.0f, 0.0f, 1.0f);
			}
			glPushName(Mode.XZ.ordinal());
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glBegin(GL_POLYGON);
			glColor4f(0.0f, 0.0f, 0.7f, 0.5f);
			glVertex3f(0.0f, 0.0f, PLANEWIDTH);
			glColor4f(0.35f, 0.0f, 0.35f, 0.5f);
			glVertex3f(PLANEWIDTH, 0.0f, PLANEWIDTH);
			glColor4f(0.7f, 0.0f, 0.0f, 0.5f);
			glVertex3f(PLANEWIDTH, 0.0f, 0.0f);
			glColor4f(0.35f, 0.0f, 0.35f, 0.5f);
			glVertex3f(0.0f, 0.0f, 0.0f);
			glEnd();
			glDisable(GL_BLEND);
			glPopName();
			glPopMatrix();
		}
	}

	public static void drawTorus(float r, float R, int nsides, int rings) {
		// System.out.println("    drawTorus()");
		float ringDelta = 2.0f * (float) Math.PI / rings;
		float sideDelta = 2.0f * (float) Math.PI / nsides;
		float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
		// gl.glPushName(1);
		for( int i = rings - 1; i >= 0; i-- ){
			float theta1 = theta + ringDelta;
			float cosTheta1 = (float) Math.cos(theta1);
			float sinTheta1 = (float) Math.sin(theta1);
			glPushName(i);
			glBegin(GL_QUAD_STRIP);
			float phi = 0.0f;
			for( int j = nsides; j >= 0; j-- ){
				phi += sideDelta;
				float cosPhi = (float) Math.cos(phi);
				float sinPhi = (float) Math.sin(phi);
				float dist = R + r * cosPhi;
				glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
				glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
				glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
				glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
			}
			glEnd();
			glPopName();
			theta = theta1;
			cosTheta = cosTheta1;
			sinTheta = sinTheta1;
		}
		// gl.glPopName();
	}

	private static void loop(double d) {
		int num_slices = 9;
		double constant = 0.1;
		double dtheta = 2 * Math.PI / num_slices;
		for( int i = 0; i < num_slices + 1; i++ ){
			glBegin(GL_LINES);
			glVertex3d(constant * Math.cos(dtheta * (i + 1)), constant * Math.sin(dtheta * (i + 1)), 0.0);
			glVertex3d(constant * Math.cos(dtheta * i), constant * Math.sin(dtheta * i), 0.0);
			glEnd();
		}
	}

	private static void nullLine(double length) {
		glBegin(GL_LINES);
		glVertex3d(0.0, 0.0, 0.0);
		glVertex3d(0.0, 0.0, length);
		glEnd();
	}

	public PrimitiveRenderer() {
		super();
	}

}