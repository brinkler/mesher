package dk.brinkler.gl.core;

public class DragEvent {

	public static enum Key {
		NONE(0), ALT(SWTALT), SHIFT(SWTSHIFT), CTRL(SWTCTRL), SUPER(SWTCOMMAND);
		public static Key from(int val) {
			switch (val) {
				case SWTALT:
					return ALT;
				case SWTSHIFT:
					return SHIFT;
				case SWTCTRL:
					return CTRL;
				case SWTCOMMAND:
					return SUPER;
				default:
					return NONE;
			}
		}

		final int keycode;

		Key(int keycode) {
			this.keycode = keycode;
		}

		int keyCode() {
			return keycode;
		}
	}

	// Keycodes from SWT
	static final int SWTALT = 1 << 16;
	static final int SWTSHIFT = 1 << 17;
	static final int SWTCTRL = 1 << 18;
	static final int SWTCOMMAND = 1 << 22;

	private final int x;
	private final int y;
	private final int button;
	private int time;
	private Key key;

	public DragEvent(int button, int x, int y) {
		this.button = button;
		this.x = x;
		this.y = y;
	}

	public DragEvent(int button, int x, int y, int time, int keyCode) {
		this(button, x, y);
		this.time = time;
		key = Key.from(keyCode);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

}
