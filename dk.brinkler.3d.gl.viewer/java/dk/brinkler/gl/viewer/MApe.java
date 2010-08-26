package dk.brinkler.gl.viewer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;

import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.opengl.QGLFormat;
import com.trolltech.qt.opengl.QGLWidget;

import dk.brinkler.gl.Renderer;

public class MApe extends QWidget {
	private QLabel label;
	private GLWidget glwidget;

	public MApe() {
		label = new QLabel("Hello world", this);
		glwidget = new GLWidget(this);
		QLabel nativeLabel = new QLabel(tr("Native"));
		nativeLabel.setAlignment(Qt.AlignmentFlag.AlignHCenter);
		QLabel openGLLabel = new QLabel(tr("OpenGL"));
		openGLLabel.setAlignment(Qt.AlignmentFlag.AlignHCenter);

		QGridLayout layout = new QGridLayout();
		layout.addWidget(glwidget, 0, 1);
		layout.addWidget(openGLLabel, 1, 1);
		setLayout(layout);
		
		glwidget.initializeGL();
		glwidget.setMinimumSize(800, 600);
//		QTimer timer = new QTimer(this);
//		timer.timeout.connect(glwidget, "animate()");
//		timer.setInterval(1);
//		timer.start(50);

		setWindowTitle(tr("2D Painting on Native and OpenGL Widgets"));
		setWindowIcon(new QIcon("classpath:com/trolltech/images/qt-logo.png"));
	}

	public static void main(String[] args) {
		QApplication.initialize(args);

		MApe editor = new MApe();
		editor.show();

		QApplication.exec();
	}

	class GLWidget extends QGLWidget {
		private Renderer renderer;

		public GLWidget(QWidget parent) {
			super(parent);
			renderer = new Renderer();
		}

		public void initializeGL() {
			this.makeCurrent();
			try {
				GLContext.useContext(this.context());
				renderer.init();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}

		public void resizeGL(int w, int h) {
			renderer.reshape(0, 0, w, h);
		}

		public void paintGL() {
			try {
				GLContext.useContext(this.context());
				renderer.display();
				this.swapBuffers();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			
		}

	}
}
