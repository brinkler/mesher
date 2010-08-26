package dk.brinkler.gl;

import static dk.brinkler.gl.Render.drawMoveGlyph;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_MODELVIEW;
import static javax.media.opengl.GL.GL_PROJECTION;
import static javax.media.opengl.GL.GL_RENDER;
import static javax.media.opengl.GL.GL_SELECT;
import static javax.media.opengl.GL.GL_VIEWPORT;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3d;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.BufferUtil;

import dk.brinkler.gl.Render.Mode;

/**
 * Small example showing how to implement picking of 3d objects using jogl.
 * 
 * Main window and continous redraw adapted from the SWT.GLsample snippet found on http://www.eclipse.org. The
 * processHits method from and the Picking example from the jogl-demos (and the redbook)
 * 
 * @author Peter R. Brinkler
 * 
 *         Feel free to use or not if you feel like it.. No strings attached and no guarantees given.
 */
public class GlSample implements GLEventListener, MouseListener, MouseMotionListener {

	private float view_rotx = 20.0f, view_roty = 30.0f;
	final float view_rotz = 0.0f;
	private int prevMouseX, prevMouseY;
	private boolean mouseRButtonDown = false;
	private GL gl;
	GLU glu = null;
	protected Double xRot = new Double(0.0);
	private final Double zRot = 0.0;
	private Mode selectedAxis = Mode.NONE;
	Point3d o = null;
	private final GLCanvas glCanvas;

	public static void main(String[] args) {
		Frame frame = new Frame("GLSample");
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(new GlSample(canvas));
		frame.add(canvas);
		frame.setSize(900, 900);
		final Animator animator = new Animator(canvas);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		frame.show();
		animator.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
	public void display(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();
		if( (drawable instanceof GLJPanel) && !((GLJPanel) drawable).isOpaque()
				&& ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent() ){
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		}else{
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}

		gl.glPushMatrix();
		gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

		gl.glPushMatrix();
		gl.glTranslatef(-2.0f, -1.0f, 0.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		render(gl);
		gl.glFlush();
		// moveGlyph.render(gl);

		gl.glPopMatrix();
		gl.glPopMatrix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.opengl.GLEventListener#displayChanged(javax.media.opengl.GLAutoDrawable, boolean, boolean)
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean arg1, boolean arg2) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable drawable) {
		// Use debug pipeline
		// drawable.setGL(new DebugGL(drawable.getGL()));
		GL gl = drawable.getGL();
		System.err.println("INIT GL IS: " + gl.getClass().getName());
		// System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		gl.setSwapInterval(1);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		drawable.addMouseListener(this);
		drawable.addMouseMotionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		float h = (float) height / (float) width;
		gl.glMatrixMode(GL.GL_PROJECTION);
		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		System.out.println("Moron clicked the mouse");
		HitInfo info = select(e, glCanvas);
		int winX = e.getX();
		int winY = e.getY();
		selectedAxis = info.name != Mode.NONE.ordinal() ? Mode.values()[info.name] : null;
		if( selectedAxis != null ){
			// Something was selected... info contains zmin and
			// zmax.
			render(gl);
			int[] viewPort = new int[4];
			gl.glGetIntegerv(GL_VIEWPORT, viewPort, 0);
			System.out.println("viewport : " + Arrays.toString(viewPort));
			assert (gl.glGetError() == 0);

			double[] mvmatrix = new double[16];
			gl.glGetDoublev(GL_MODELVIEW, mvmatrix, 0);
			System.out.println("mvmatrix : " + Arrays.toString(mvmatrix));
			System.out.println(" glError " + glu.gluErrorString(gl.glGetError()));
			assert (gl.glGetError() == 0);

			double[] projmatrix = new double[16];
			gl.glGetDoublev(GL_PROJECTION, projmatrix, 0);
			System.out.println("projmatrix : " + Arrays.toString(projmatrix));
			assert (gl.glGetError() == 0);

			double[] worldxyz = new double[3];

			double zavg = (info.getZMax() + info.getZMin()) / 2.0;
			glu.gluUnProject(e.getX(), viewPort[3] - e.getY() - 1, zavg, mvmatrix, 0, projmatrix, 0, viewPort, 0, worldxyz, 0);
			System.out.println("world  : " + Arrays.toString(worldxyz));
		}
		System.out.println("hit data : " + info);
	}

	// selectedAxis = select(e);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 ){
			mouseRButtonDown = true;
		}
		selectedAxis = Mode.NONE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse released");
		if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 ){
			mouseRButtonDown = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged : " + e.getPoint());
		int x = e.getX();
		int y = e.getY();
		Dimension size = e.getComponent().getSize();
		float thetaY = 360.0f * ((float) (x - prevMouseX) / size.width);
		float thetaX = 360.0f * ((float) (prevMouseY - y) / size.height);
		prevMouseX = x;
		prevMouseY = y;
		view_rotx += thetaX;
		view_roty += thetaY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		// System.out.println("Mouse moved" + e.getPoint());
	}

	private HitInfo select(MouseEvent e, GLCanvas glCanvas) throws GLException {
		HitInfo hitInfo = null;
		GL gl = glCanvas.getGL();
		int buffsize = 512;
		double x = e.getX(), y = e.getY();
		int[] viewPort = new int[4];

		Rectangle bounds = glCanvas.getBounds();
		float aspect = (float) bounds.width / (float) bounds.height;
		gl.glViewport(0, 0, bounds.width, bounds.height);

		IntBuffer selectBuffer = BufferUtil.newIntBuffer(buffsize);
		int hits = 0;

		gl.glSelectBuffer(buffsize, selectBuffer);
		gl.glGetIntegerv(GL_VIEWPORT, viewPort, 0);
		gl.glRenderMode(GL_SELECT);
		gl.glInitNames();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPickMatrix(x, viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
		glu.gluPerspective(45.0f, aspect, 0.5f, 400.0f);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		render(gl);
		gl.glFlush();

		hits = gl.glRenderMode(GL_RENDER);
		hitInfo = processHits(hits, selectBuffer);
		selectBuffer.clear();
		// glContext.release();

		// if( e.stateMask == SWT.SHIFT ){
		// // Adding stuff.
		// }else{
		// replace
		if( hitInfo.getName() >= 0 && hitInfo.getName() < Render.Mode.values().length ){
			selectedAxis = hitInfo.getMode();
		}
		// }
		return hitInfo;
	}

	/**
	 * Setup a super simple shell containig a GLCanvas.
	 */
	public GlSample(GLCanvas canvas) {
		super();
		glCanvas = canvas;
		glu = new GLU();
	}

	private void render(GL gl) {
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glTranslatef(-1.0f, 0.0f, -5.0f);
		gl.glRotated(xRot, 0.0, 1.0, 0.0);
		gl.glRotated(zRot, 1.0, 0.0, 0.0);

		// gl.glPushMatrix();
		// gl.glPolygonMode(GL_FRONT, GL_FILL);
		// gl.glColor3f(0.7f, 0.7f, 0.7f);
		// gl.glTranslated(0.0, 0.0, -7);
		// drawTorus(gl, 0.5f, 1.5f, 6, 6);
		// gl.glPopMatrix();
		gl.glPushMatrix();
		drawMoveGlyph(gl, selectedAxis);
		gl.glPopMatrix();

		gl.glColor3f(1.0f, 0.0f, 0.0f);
	}

	class HitInfo {
		private int name = Mode.NONE.ordinal();
		private float zMin = 0.0f;
		private float zMax = 0.0f;

		/**
		 * Constructor for Hit information.
		 * 
		 * @param name
		 * @param min
		 * @param max
		 */
		public HitInfo(int name, float min, float max) {
			super();
			this.name = name;
			zMin = min;
			zMax = max;
		}

		/**
		 * Accessor for the name
		 * 
		 * @return
		 */
		public int getName() {
			return name;
		}

		/**
		 * Set the opengl name of the hit
		 * 
		 * @param name
		 */
		public void setName(int name) {
			this.name = name;
		}

		/**
		 * Accessor for maximal zValue
		 * 
		 * @return
		 */
		public float getZMax() {
			return zMax;
		}

		/**
		 * Set maximum zValue
		 * 
		 * @param max
		 */
		private void setZMax(float max) {
			zMax = max;
		}

		/**
		 * Accessor for the minimal zValue
		 * 
		 * @return minimal zvalue
		 */
		public float getZMin() {
			return zMin;
		}

		/**
		 * Set the minimum zValue
		 * 
		 * @param min
		 */
		private void setZMin(float min) {
			zMin = min;
		}

		/**
		 * @return The hit as a Mode.
		 */
		public Mode getMode() {
			if( name < Mode.values().length ){
				return Mode.values()[name];
			}else{
				return Mode.NONE;
			}
		}

		/**
		 * Tostring implementation to give something reasonable as debug info
		 */
		@Override
		public String toString() {
			return " {name='" + Mode.values()[name] + "' zMin='" + zMin + "' zMax='" + zMax + "'}";
		}
	}

	/**
	 * Adapted from demos.misc.Picking from jogl-demos (which adapts the RedBook version)
	 * 
	 * @param hits
	 * @param buffer
	 * @return
	 */
	public HitInfo processHits(int hits, IntBuffer buffer) {
		System.out.println("---------------------------------");
		System.out.println(" HITS: " + hits);
		int offset = 0;
		int names;
		float z1, z2;
		HitInfo result = new HitInfo(Mode.NONE.ordinal(), 0.0f, 0.0f);
		for( int i = 0; i < hits; i++ ){
			System.out.println("- - - - - - - - - - - -");
			System.out.println(" hit: " + (i + 1));
			names = buffer.get(offset);
			offset++;
			z1 = (float) buffer.get(offset) / 0x7fffffff;
			offset++;
			z2 = (float) buffer.get(offset) / 0x7fffffff;
			offset++;
			System.out.println(" number of names: " + names);
			System.out.println(" z1: " + z1);
			System.out.println(" z2: " + z2);
			System.out.println(" names: ");

			for( int j = 0; j < names; j++ ){
				int selected = buffer.get(offset);
				System.out.print("       " + selected);
				if( j == (names - 1) ){
					result = new HitInfo(selected, z1, z2);
					System.out.println("<-");
				}else{
					System.out.println();
				}
				offset++;
			}
			System.out.println("- - - - - - - - - - - -");
		}
		System.out.println("---------------------------------");
		return result;
	}

}
