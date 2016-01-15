package cc.co.geniusman.entrance;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import cc.co.geniusman.argument.Argument;
import cc.co.geniusman.category.Category;
import cc.co.geniusman.category.CategoryFactory;
import cc.co.geniusman.util.Util;

public class LcxEntrance {
	/** log4j instance **/
	private static Logger log = Logger.getLogger(LcxEntrance.class);

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Argument argument = new Argument();

		// use args4j to parse Argument
		CmdLineParser parser = new CmdLineParser(argument);
		try {
			log.debug("Parse args into fields.");
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			log.error("Parse args into fields, but error occurs.");
			Util.usage();
		}

		Category instance = CategoryFactory.getInstance(argument);
		instance.execute(argument);
	}
}
