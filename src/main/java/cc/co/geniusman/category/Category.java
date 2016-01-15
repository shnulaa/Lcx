package cc.co.geniusman.category;

import java.io.IOException;
import java.net.UnknownHostException;

import cc.co.geniusman.argument.Argument;

public interface Category {
	public void execute(Argument argument) throws UnknownHostException, IOException;
}
