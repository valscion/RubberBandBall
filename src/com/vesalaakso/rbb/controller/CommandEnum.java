package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Control;
import org.newdawn.slick.command.KeyControl;

/**
 * This enumeration is used to store all <code>BasicCommand</code>s in a
 * programmer friendly constant. The string representation of commands will be
 * the same as the enum values toString() returns. All enums also know the
 * default control bound to the commands.
 * 
 * @author Vesa Laakso
 * 
 */
public enum CommandEnum {
	/** Camera movement in negative x-direction, default key: A */
	CAMERA_MOVE_LEFT(new KeyControl(Input.KEY_A)),
	/** Camera movement in positive x-direction, default key: D */
	CAMERA_MOVE_RIGHT(new KeyControl(Input.KEY_D)),
	/** Camera movement in negative y-direction, default key: W */
	CAMERA_MOVE_UP(new KeyControl(Input.KEY_W)),
	/** Camera movement in positive y-direction, default key: S */
	CAMERA_MOVE_DOWN(new KeyControl(Input.KEY_S));

	/** The <code>BasicCommand</code> that is represented by the enum value. */
	public final BasicCommand basicCommand;
	
	/** The default control associated with this enum and command */
	public final Control defaultControl;

	/**
	 * Constructs an enum and creates a new <code>BasicCommand</code> with the
	 * exact same name as the enums <code>toString()</code> method returns.
	 */
	private CommandEnum(Control defaultControl) {
		this.basicCommand = new BasicCommand(this.toString());
		this.defaultControl = defaultControl;
	}
}
