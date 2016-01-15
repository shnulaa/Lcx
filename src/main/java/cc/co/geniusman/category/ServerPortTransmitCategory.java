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

public class ServerPortTransmitCategory extends AbstractCategory {
	/** log4j instance **/
	private static Logger log = Logger
			.getLogger(ServerPortTransmitCategory.class);

	@Override
	protected boolean checkArgument() {
		int orgPort = argument.orgPort;
		int transPort = argument.tranPort;
		// check port
		if ((orgPort <= 0 || orgPort > 65535)
				|| (transPort <= 0 || transPort > 65535)) {
			throw new RuntimeException("orgPort or transPort is incorrect.. "
					+ "input the correct orgPort or transPort");
		}

		/*
		 * String localIp = argument.localIp; String remoteIp =
		 * argument.remoteIp;
		 * 
		 * // check IP address if (!Util.checkIp(localIp) ||
		 * !Util.checkIp(remoteIp)) { throw new
		 * RuntimeException("Ip address is incorrect.. " +
		 * "input the correct ip address"); }
		 */
		return true;
	}

	@Override
	public void transmitPort() {
		// local Port and remote port
		int orgPort = argument.orgPort;
		int transPort = argument.tranPort;

		// local IP address and remote IP address
		String localIp = argument.localIp;
		String remoteIp = argument.remoteIp;

		// define the two socket
		Socket orgSocket = null;
		Socket transSocket = null;

		try {
			while (true) {
				try {
					orgSocket = new Socket();
					orgSocket.connect(
							new InetSocketAddress(InetAddress
									.getByName(localIp), orgPort),
							Util.SOCKET_TIME);
				} catch (UnknownHostException ex) {
					log.error("Socket UnknownHostException occured..", ex);
					Thread.sleep(1000);
					continue;
				} catch (SocketTimeoutException ex) {
					log.error("SocketTimeoutException occured..", ex);
					Thread.sleep(1000);
					continue;
				} catch (ConnectException ex) {
					log.error("ConnectException occured..", ex);
					Thread.sleep(1000);
					continue;
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					Thread.sleep(1000);
					continue;
				}

				if (log.isInfoEnabled()) {
					log.info("Sucessfully Connected to " + localIp
							+ " in port: " + orgPort);
				}

				try {
					transSocket = new Socket();
					transSocket.connect(
							new InetSocketAddress(InetAddress
									.getByName(remoteIp), transPort),
							Util.SOCKET_TIME);
				} catch (UnknownHostException ex) {
					log.error("Socket UnknownHostException occured..", ex);
					Util.close(orgSocket);
					Thread.sleep(1000);
					continue;
				} catch (SocketTimeoutException ex) {
					log.error("SocketTimeoutException occured..", ex);
					Util.close(orgSocket);
					Thread.sleep(1000);
					continue;
				} catch (ConnectException ex) {
					log.error("ConnectException occured..", ex);
					Util.close(orgSocket);
					Thread.sleep(1000);
					continue;
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					Util.close(orgSocket);
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
				Thread thread1 = new Thread(new CombineSocket(
						orgSocket.getInputStream(),
						transSocket.getOutputStream()));
				Thread thread2 = new Thread(new CombineSocket(
						transSocket.getInputStream(),
						orgSocket.getOutputStream()));
				thread1.start();
				thread2.start();

				try {
					thread1.join();
					thread2.join();
				} catch (InterruptedException e) {
					log.error("Thread Join InterruptedException occured..", e);
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception occured..", e);
		} finally {
			Util.close(orgSocket);
			Util.close(transSocket);
		}
	}
}
