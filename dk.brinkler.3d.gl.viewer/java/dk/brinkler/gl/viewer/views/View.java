package dk.brinkler.gl.viewer.views;

import javax.vecmath.Point3d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;

import dk.brinkler.gl.Renderer;
import dk.brinkler.gl.core.DragEvent;

public class View extends ViewPart {

	private abstract class GLListener implements Listener {
		private final GLCanvas canvas;

		private GLListener(GLCanvas canvas) {
			this.canvas = canvas;
		}

		@Override
		public void handleEvent(Event event) {
			canvas.setCurrent();
			try {
				GLContext.useContext(canvas);
				if (event.button > 0) {
					handleEventInner(event);
				}
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}

		public abstract void handleEventInner(Event event);
	}

	public static final String ID = "dk.brinkler.gl.viewer.view";
	private Renderer renderer;
	protected int mouseDown;
	protected int clickTime = 100;

	@Override
	public void createPartControl(final Composite parent) {
		GLData data = new GLData();
		data.doubleBuffer = false;
		final GLCanvas canvas = new GLCanvas(parent, SWT.NONE, data);
		canvas.setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		renderer = new Renderer();
		parent.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle bounds = canvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				renderer.reshape(0, 0, bounds.width, bounds.height);
			}
		});
		canvas.addListener(SWT.Resize, new GLListener(canvas) {
			@Override
			public void handleEventInner(Event event) {
				System.out.println("GLResizing: " + event);
				Rectangle bounds = canvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				renderer.reshape(0, 0, bounds.width, bounds.height);
			}
		});
		canvas.addListener(SWT.MouseMove, new GLListener(canvas) {
			@Override
			public void handleEventInner(Event event) {
				renderer.mouseDragged(new DragEvent(event.button, event.x, event.y));
			}
		});
		canvas.addListener(SWT.MouseDown, new GLListener(canvas) {
			@Override
			public void handleEventInner(Event event) {
				mouseDown = event.time;
				renderer.mousePressed(new DragEvent(event.button, event.x, event.y, event.time, event.keyCode));
			}
		});
		canvas.addListener(SWT.MouseUp, new GLListener(canvas) {
			@Override
			public void handleEventInner(Event event) {
				if (event.time - mouseDown < clickTime) {
					Point3d point = renderer.mouseClicked(new DragEvent(event.button, event.x, event.y, event.time, event.keyCode));
				} else {
					renderer.mouseReleased(new DragEvent(event.button, event.x, event.y, event.time, event.keyCode));
				}
			}
		});

		renderer.init();
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!canvas.isDisposed()) {
					canvas.setCurrent();
					try {
						GLContext.useContext(canvas);
						renderer.display();
						canvas.swapBuffers();
						parent.getDisplay().asyncExec(this);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
