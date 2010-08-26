package dk.brinkler.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.nio.IntBuffer;

import javax.vecmath.Point3d;

import org.lwjgl.opengl.DisplayMode;

import dk.brinkler.gl.core.DragEvent;
import dk.brinkler.gl.glyphs.ArrowGlyph;
import dk.brinkler.gl.glyphs.Glyph;

public class Renderer {
	enum State {
		NOTHING, UPDATE, SELECT
	}

	/** The width of the game display area */
	private final static int width = 1000;

	/** The height of the game display area */
	private final static int height = 1000;

	// public static void main(String[] args) {
	// Renderer r = new Renderer();
	// // Create a fullscreen window with 1:1 orthographic 2D projection, and with
	// // mouse, keyboard, and gamepad inputs.
	// try {
	// setDisplayMode();
	// Display.setTitle("Renderer");
	// // Enable vsync if we can
	// Display.setVSyncEnabled(true);
	// Display.create();
	//
	// // Start up the sound system
	// AL.create();
	// // Init
	// r.init();
	// r.run();
	// } catch (LWJGLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // Stop the sound
	// AL.destroy();
	//
	// // Close the window
	// Display.destroy();
	//
	// }

	/**
	 * Sets the display mode for fullscreen mode
	 */
	public static boolean setDisplayMode() {
		try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);

			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width=" + width, "height=" + height, "freq=" + 60, //
					"bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
			});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
		}

		return false;
	}

	State cmd = State.UPDATE;
	int mouse_x, mouse_y;
	private final float view_rotx = 20.0f, view_roty = 30.0f;
	final float view_rotz = 0.0f;
	private Glyph glyph;
	private boolean dirty = true;
	private boolean active = false;

	private boolean finished;

	public void display() {
		if (dirty) {
			dirty = active;
			switch (cmd) {
				case UPDATE:
					drawScene();
					break;
				case SELECT:
					int buffsize = 512;
					float x = mouse_x,
					y = mouse_y;
					IntBuffer viewPort = IntBuffer.allocate(4);
					IntBuffer selectBuffer = IntBuffer.allocate(buffsize);
					int hits = 0;
					glGetInteger(GL_VIEWPORT, viewPort);
					glSelectBuffer(selectBuffer);
					glRenderMode(GL_SELECT);
					glInitNames();
					glMatrixMode(GL_PROJECTION);
					glPushMatrix();
					glLoadIdentity();
					gluPickMatrix(x, viewPort.array()[3] - y, 5.0f, 5.0f, viewPort);
					setProjection();
					drawScene();
					glMatrixMode(GL_PROJECTION);
					glPopMatrix();
					glFlush();
					hits = glRenderMode(GL_RENDER);
					processHits(hits, selectBuffer);
					cmd = State.UPDATE;
					break;
			}
		}
	}

	public void displayChanged(boolean modeChanged, boolean deviceChanged) {
	}

	public void drawScene() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		{
			glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
			glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
			glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
			glPushMatrix();
			{
				glTranslatef(0f, 0.5f, 0.0f);
				glScaled(0.5, 0.5, 0.5);
				glPushMatrix();
				{
					glyph.render();
				}
				glPopMatrix();
			}
			glPopMatrix();
		}
		glPopMatrix();
		glFlush();
	}

	public void init() {
		glyph = new ArrowGlyph(1.0f, 0.01f, 0.05f);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_NORMALIZE);
		glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
		dirty = true;
		glyph.init();
	}

	private void initViewPort() {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		setProjection();
		dirty = true;
	}

	public Point3d mouseClicked(DragEvent dragEvent) {
		active = false;
		dirty = true;
		cmd = State.UPDATE;
		drawScene();
		// GLU.gluUnProject(dragEvent.x(), dragEvent.y(), 0 , modelMatrix,
		// projMatrix, viewport, obj_pos)
		return null;
	}

	public void mouseDragged(DragEvent dragEvent) {
		dirty = true;
		cmd = State.UPDATE;
		drawScene();
	}

	public void mousePressed(DragEvent dragEvent) {
		dirty = true;
		active = true;
		glyph.active(active);
		cmd = State.SELECT;
		mouse_x = dragEvent.x();
		mouse_y = dragEvent.y();
	}

	public void mouseReleased(DragEvent e) {
		active = false;
		dirty = true;
		cmd = State.UPDATE;
	}

	public void processHits(int hits, IntBuffer buffer) {
		System.out.println("---------------------------------");
		System.out.println(" HITS: " + hits);
		int offset = 0;
		int names;
		float z1, z2;
		if (hits > 0) {
			active = true;
		}
		for (int i = 0; i < hits; i++) {
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
			for (int j = 0; j < names; j++) {
				System.out.print("       " + buffer.get(offset));
				if (j == names - 1) {
					System.out.println("<-");
				} else {
					System.out.println();
				}
				offset++;
			}
			System.out.println("- - - - - - - - - - - -");
		}
		System.out.println("---------------------------------");
	}

	public void reshape(int x, int y, int width, int height) {
		glViewport(0, 0, width, height);
		initViewPort();
	}

	//
	// /**
	// * Runs the game (the "main loop")
	// */
	// public void run() {
	// while (!finished) {
	// // Always call Window.update(), all the time
	// if (Display.isCloseRequested()) {
	// // Check for O/S close requests
	// finished = true;
	// } else if (Display.isActive()) {
	// // The window is in the foreground, so we should play the game
	// Display.sync(60);
	// display();
	// Display.update();
	// } else {
	// // The window is not in the foreground, so we can allow other stuff to
	// // run and infrequently update
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	// }
	// if (Display.isVisible() || Display.isDirty()) {
	// // Only bother rendering if the window is visible or dirty
	// display();
	// Display.update();
	// }
	// }
	// }
	// }

	/**
	 * 
	 */
	private void setProjection() {
		gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);
	}

	public int viewPortHeight() {
		IntBuffer viewPort = IntBuffer.allocate(4);
		glGetInteger(GL_VIEWPORT, viewPort);
		return viewPort.array()[3];
	}

	public int viewPortWidth() {
		IntBuffer viewPort = IntBuffer.allocate(4);
		glGetInteger(GL_VIEWPORT, viewPort);
		return viewPort.array()[2];
	}
}