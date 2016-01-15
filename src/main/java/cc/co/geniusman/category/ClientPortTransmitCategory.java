package cc.co.geniusman.category;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import cc.co.geniusman.combine.CombineSocket;
import cc.co.geniusman.util.Util;

public class ClientPortTransmitCategory extends AbstractCategory {

	/** log4j instance **/
	private static Logger log = Logger.getLogger(ClientPortTransmitCategory.class);

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
		return true;
	}

	@Override
	protected void transmitPort() {
		int orgPort = argument.orgPort;
		int transPort = argument.tranPort;

		// define the two ServerSocket to listen a port
		ServerSocket orgServerSocket = null;
		ServerSocket transServerSocket = null;

		Socket acceptSocket = null;
		Socket transSocket = null;
		try {
			// create two server socket
			orgServerSocket = new ServerSocket(orgPort);
			transServerSocket = new ServerSocket(transPort);

			while (true) {
				try {
					// accept with no timeout
					acceptSocket = orgServerSocket.accept();
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					Thread.sleep(1000);
					continue;
				} catch (SecurityException ex) {
					log.error("Socket SecurityException occured..", ex);
					Thread.sleep(1000);
					continue;
				}

				if (log.isInfoEnabled()) {
					log.info("receive socket from client, ip address:"
							+ acceptSocket.getInetAddress().toString()
							+ " port:" + orgPort);
				}

				try {
					// accept with no timeout
					transSocket = transServerSocket.accept();
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					// close and release the resource
					Util.close(acceptSocket);
					Util.close(orgServerSocket);
					Thread.sleep(1000);
					continue;
				} catch (SecurityException ex) {
					log.error("Socket SecurityException occured..", ex);
					// close and release the resource
					Util.close(acceptSocket);
					Util.close(orgServerSocket);
					Thread.sleep(1000);
					continue;
				}

				if (log.isInfoEnabled()) {
					log.info("receive socket from client, ip address:"
							+ transSocket.getInetAddress().toString()
							+ " port:" + transPort);
				}

				// create two thread to combine the is and os
				Thread thread1 = new Thread(new CombineSocket(
						acceptSocket.getInputStream(),
						transSocket.getOutputStream()));
				Thread thread2 = new Thread(new CombineSocket(
						transSocket.getInputStream(),
						acceptSocket.getOutputStream()));
				thread1.start();
				thread2.start();
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception occured..", e);
		} finally {
			Util.close(orgServerSocket);
			Util.close(transServerSocket);

			Util.close(acceptSocket);
			Util.close(transSocket);
		}
	}
}
