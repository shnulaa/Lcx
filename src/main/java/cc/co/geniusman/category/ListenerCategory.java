package cc.co.geniusman.category;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import cc.co.geniusman.combine.CombineSocket;
import cc.co.geniusman.util.Util;

public class ListenerCategory extends AbstractCategory {
	/** log4j instance **/
	private static Logger log = Logger.getLogger(ListenerCategory.class);

	@Override
	protected boolean checkArgument() {
		int listenPort = argument.listenPort;
		// check port
		if (listenPort <= 0 || listenPort > 65535) {
			throw new RuntimeException("listenPort is incorrect.. "
					+ "input the correct listenPort");
		}
		return true;
	}

	@Override
	protected void transmitPort() {
		int listenPort = argument.listenPort;

		// define the ServerSocket to listen a port
		ServerSocket serverSocket = null;

		Socket acceptSocket = null;
		try {
			while (true) {
				// create two server socket
				serverSocket = new ServerSocket(listenPort);

				if (log.isInfoEnabled()) {
					log.info("Successfully listen to port :" + listenPort);
				}
				try {
					// accept with no timeout
					acceptSocket = serverSocket.accept();
				} catch (IOException ex) {
					log.error("Socket IOException occured..", ex);
					Util.close(acceptSocket);
					Thread.sleep(1000);
					continue;
				} catch (SecurityException ex) {
					log.error("Socket SecurityException occured..", ex);
					Util.close(acceptSocket);
					Thread.sleep(1000);
					continue;
				}

				if (log.isInfoEnabled()) {
					log.info("receive socket from client, ip address:"
							+ acceptSocket.getInetAddress().toString()
							+ " port:" + listenPort);
				}

				// create two thread to combine the is and os
				Thread thread1 = new Thread(new CombineSocket(
						acceptSocket.getInputStream(), System.out, false));
				Thread thread2 = new Thread(new CombineSocket(System.in,
						acceptSocket.getOutputStream(), false));
				thread1.start();
				thread2.start();

				try {
					thread1.join();
					thread2.join();
				} catch (InterruptedException e) {
					log.error("Thread Join InterruptedException occured..", e);
					Util.close(acceptSocket);
					Thread.sleep(1000);
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Exception occured..", e);
		} finally {
			Util.close(acceptSocket);
		}

	}

}
