/**
 * 
 */
package dk.brinkler.gl;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;

import com.sun.opengl.util.Animator;

import dk.brinkler.gl.glyphs.Glyph;
import dk.brinkler.gl.glyphs.ArrowGlyph;

/**
 * @author riishigh
 * 
 */
public class GLTestBed implements GLEventListener, MouseListener, MouseMotionListener {
	public static void main(String[] args) {
		Frame frame = new Frame("GLTestBed");
		GLCanvas canvas = new GLCanvas();

		canvas.addGLEventListener(new GLTestBed());
		frame.add(canvas);
		frame.setSize(300, 300);
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

	private float view_rotx = 20.0f, view_roty = 30.0f;
	final float view_rotz = 0.0f;
	private int prevMouseX, prevMouseY;
	private boolean mouseRButtonDown = false;
	private final Glyph moveGlyph = new MoveGlyph();

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

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		if ((drawable instanceof GLJPanel) && !((GLJPanel) drawable).isOpaque()
				&& ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		} else {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}

		gl.glPushMatrix();
		gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

		gl.glPushMatrix();
		gl.glTranslatef(-2.0f, -1.0f, 0.0f);

		moveGlyph.render(gl);

		gl.glPopMatrix();
		gl.glPopMatrix();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	// Methods required for the implementation of MouseListener
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 ){
			mouseRButtonDown = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 ){
			mouseRButtonDown = false;
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	// Methods required for the implementation of MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Dimension size = e.getComponent().getSize();

		float thetaY = 360.0f * ((float) (x - prevMouseX) / (float) size.width);
		float thetaX = 360.0f * ((float) (prevMouseY - y) / (float) size.height);

		prevMouseX = x;
		prevMouseY = y;

		view_rotx += thetaX;
		view_roty += thetaY;
	}

	public void mouseMoved(MouseEvent e) {
	}
}
