package glredbook11;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.Screenshot;

/**
 * This program demonstrates vertex arrays.
 * 
 * @author Kiet Le (Java conversion)
 */

public class varray extends JFrame implements GLEventListener, KeyListener, MouseListener {
	// private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLCapabilities caps;
	private GLCanvas canvas;
	private static int POINTER = 1;
	private static int INTERLEAVED = 2;

	private static int DRAWARRAY = 1;
	private static int ARRAYELEMENT = 2;
	private static int DRAWELEMENTS = 3;

	private static int setupMethod = POINTER;
	private static int derefMethod = DRAWARRAY;
	private static IntBuffer verticesBuf;
	private static FloatBuffer colorsBuf;
	private static FloatBuffer intertwinedBuf;
	Screenshot capture;

	//
	public varray() {
		super("varray");
		//
		caps = new GLCapabilities();
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		//
		getContentPane().add(canvas);
	}

	public void run() {
		setSize(512, 256);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		canvas.requestFocusInWindow();
	}

	public static void main(String[] args) {
		new varray().run();
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		//
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		setupPointers(gl);
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		//
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		if (derefMethod == DRAWARRAY) {
			gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		} else if (derefMethod == ARRAYELEMENT) {
			gl.glBegin(GL.GL_TRIANGLES);
			gl.glArrayElement(2);
			gl.glArrayElement(3);
			gl.glArrayElement(5);
			gl.glEnd();
		} else if (derefMethod == DRAWELEMENTS) {
			int indices[] = new int[] { 0, 1, 3, 4 };
			IntBuffer indicesBuf = BufferUtil.newIntBuffer(indices.length);
			for (int i = 0; i < indices.length; i++)
				indicesBuf.put(indices[i]);
			indicesBuf.rewind();
			gl.glDrawElements(GL.GL_POLYGON, 4, GL.GL_UNSIGNED_INT, indicesBuf);
		}
		gl.glFlush();

		// gl calls from C example's mouse routine are moved here
		if (setupMethod == INTERLEAVED)
			setupInterleave(gl);
		else
			setupPointers(gl);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		GL gl = drawable.getGL();
		//
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0, (double) w, 0.0, (double) h);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	private void setupPointers(GL gl) {
		int vertices[] = new int[] { 25, 25, 100, 325, 175, 25, 175, 325, 250, 25, 325, 325 };
		float colors[] = new float[] { 1.0f, 0.2f, 0.2f, 0.2f, 0.2f, 1.0f, 0.8f, 1.0f, 0.2f, 0.75f, 0.75f, 0.75f, 0.35f, 0.35f, 0.35f,
				0.5f, 0.5f, 0.5f };
		IntBuffer tmpVerticesBuf = BufferUtil.newIntBuffer(vertices.length);
		FloatBuffer tmpColorsBuf = BufferUtil.newFloatBuffer(colors.length);
		for (int i = 0; i < vertices.length; i++)
			tmpVerticesBuf.put(vertices[i]);
		for (int j = 0; j < colors.length; j++)
			tmpColorsBuf.put(colors[j]);
		tmpVerticesBuf.rewind();
		tmpColorsBuf.rewind();
		//
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);
		//
		gl.glVertexPointer(2, GL.GL_INT, 0, tmpVerticesBuf);
		gl.glColorPointer(3, GL.GL_FLOAT, 0, tmpColorsBuf);
		this.verticesBuf = tmpVerticesBuf;
		this.colorsBuf = tmpColorsBuf;

	}

	private void setupInterleave(GL gl) {
		float intertwined[] = new float[] { 1.0f, 0.2f, 1.0f, 100.0f, 100.0f, 0.0f, 1.0f, 0.2f, 0.2f, 0.0f, 200.0f, 0.0f, 1.0f, 1.0f, 0.2f,
				100.0f, 300.0f, 0.0f, 0.2f, 1.0f, 0.2f, 200.0f, 300.0f, 0.0f, 0.2f, 1.0f, 1.0f, 300.0f, 200.0f, 0.0f, 0.2f, 0.2f, 1.0f,
				200.0f, 100.0f, 0.0f };
		FloatBuffer tmpIntertwinedBuf = BufferUtil.newFloatBuffer(intertwined.length);
		tmpIntertwinedBuf.rewind();
		//
		gl.glInterleavedArrays(GL.GL_C3F_V3F, 0, tmpIntertwinedBuf);
		this.intertwinedBuf = tmpIntertwinedBuf;
	}

	public void keyTyped(KeyEvent key) {
	}

	public void keyPressed(KeyEvent key) {
		switch (key.getKeyChar()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;

		default:
			break;
		}
	}

	public void keyReleased(KeyEvent key) {
	}

	public void mouseClicked(MouseEvent mouse) {
	}

	public void mousePressed(MouseEvent mouse) {
		if (mouse.getButton() == MouseEvent.BUTTON1) {
			if (setupMethod == POINTER) {
				setupMethod = INTERLEAVED;
				// setupInterleave(gl);don't call
			} else if (setupMethod == INTERLEAVED) {
				setupMethod = POINTER;
				// setupPointers(gl);
			}
			// validate();
		}
		if (mouse.getButton() == MouseEvent.BUTTON2 || mouse.getButton() == MouseEvent.BUTTON3) {
			if (derefMethod == DRAWARRAY)
				derefMethod = ARRAYELEMENT;
			else if (derefMethod == ARRAYELEMENT)
				derefMethod = DRAWELEMENTS;
			else if (derefMethod == DRAWELEMENTS)
				derefMethod = DRAWARRAY;
			// validate();
		}
		canvas.display();
	}

	public void mouseReleased(MouseEvent mouse) {
	}

	public void mouseEntered(MouseEvent mouse) {
	}

	public void mouseExited(MouseEvent mouse) {
	}

}