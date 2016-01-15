# Lcx
    mode    
            1.listen 
            2.client
            3.server
            4.shell

            String info = "Usage: [Option]\n\n"
				+ "Option of listen mode:               -m, -p\n"
				+ "Option of client port transmit:      -m, -o, -t\n"
				+ "Option of server port transmit:      -m, -l, -o, -r, -t\n"
				+ "Option of shell mode:                -m, -r, -t\n"

				+ "Option:\n"
				+ "    -m  :     The mode of lcx (include client, server, listen, shell)\n"
				+ "    -o  :     The original port\n"
				+ "    -t  :     the transmit port \n"
				+ "    -l  :     the local IP address \n"
				+ "    -t  :     the remote IP address \n";

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
