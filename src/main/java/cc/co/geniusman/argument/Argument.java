package cc.co.geniusman.argument;

import org.kohsuke.args4j.Option;

public class Argument {
	/** Argument Option **/
	@Option(name = "-m", usage = "the mode of lcx", required = true)
	public String mode;

	@Option(name = "-p", usage = "the listen of port")
	public int listenPort;

	@Option(name = "-o", usage = "the original port")
	public int orgPort;

	@Option(name = "-t", usage = "the transmit port")
	public int tranPort;

	@Option(name = "-l", usage = "the local host ip address")
	public String localIp;

	@Option(name = "-r", usage = "the remote host ip address")
	public String remoteIp;
}
