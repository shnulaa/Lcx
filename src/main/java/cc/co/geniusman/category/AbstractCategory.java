package cc.co.geniusman.category;

import cc.co.geniusman.argument.Argument;

public abstract class AbstractCategory implements Category {
	/** argument **/
	protected Argument argument;

	/**
	 * execute
	 */
	@Override
	public void execute(Argument argument) {
		setArgument(argument);
		if (checkArgument()) {
			transmitPort();
		}
	}

	/** checkArgument **/
	protected abstract boolean checkArgument();

	/** transmitPort **/
	protected abstract void transmitPort();

	/**
	 * set Argument
	 */
	private void setArgument(Argument argument) {
		this.argument = argument;
	}
}
