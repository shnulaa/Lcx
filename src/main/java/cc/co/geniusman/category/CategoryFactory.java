package cc.co.geniusman.category;

import cc.co.geniusman.argument.Argument;
import cc.co.geniusman.argument.LcxMode;

public class CategoryFactory {
	/**
	 * getInstance
	 * 
	 * @param argument
	 * @return Category instance
	 */
	public static Category getInstance(Argument argument) {
		String lcxMode = argument.mode;
		if (lcxMode.equalsIgnoreCase(LcxMode.CLIENT.name())) {
			return new ClientPortTransmitCategory();
		} else if (lcxMode.equalsIgnoreCase(LcxMode.SERVER.name())) {
			return new ServerPortTransmitCategory();
		} else if (lcxMode.equalsIgnoreCase(LcxMode.LISTEN.name())) {
			return new ListenerCategory();
		} else if (lcxMode.equalsIgnoreCase(LcxMode.SHELL.name())) {
			return new BlindShellCategory();
		}
		return null;
	}
}
