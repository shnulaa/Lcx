package cc.co.geniusman.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Util {
	private static Logger log = Logger.getLogger(Util.class);
	public static final int SOCKET_TIME = 10000;
	public static final String OS_NAME = System.getProperty("os.name")
			.toUpperCase();
	public static final String WINDOWS = "WIN";
	public static final String LINUX = "LINUX";

	/**
	 * safeClose resource
	 * 
	 * @param InputStream
	 */
	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				log.error("close InputStream error.", e);
			}
		}
	}

	public static void close(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
				br = null;
			} catch (IOException e) {
				log.error("close BufferedReader error.", e);
			}
		}
	}

	public static void close(BufferedWriter bw) {
		if (bw != null) {
			try {
				bw.close();
				bw = null;
			} catch (IOException e) {
				log.error("close BufferedWriter error.", e);
			}
		}
	}

	/**
	 * safeClose resource
	 * 
	 * @param OutputStream
	 */
	public static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				log.error("close OutputStream error.", e);
			}
		}
	}

	public static void close(ServerSocket serverSocket) {
		if (serverSocket != null) {
			try {
				serverSocket.close();
				serverSocket = null;
			} catch (IOException e) {
				log.error("close serverSocket error.", e);
			}
		}
	}

	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				log.error("close socket error.", e);
			}
		}
	}

	public static void close(Process process) {
		if (process != null) {
			process.destroy();
			process = null;
		}
	}

	public static boolean checkIp(String sip) {
		String[] parts = sip.split("\\.");
		for (String s : parts) {
			int i = Integer.parseInt(s);
			if (i < 0 || i > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * usage details
	 */
	public static void usage() {
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

		log.info(info);
		System.exit(-1);
	}

}
