package cc.co.geniusman.combine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import cc.co.geniusman.util.Util;

public class CombineSocket implements Runnable {
	private static final int BUFFER_SIZE = 4096;
	/** log4j instance **/
	private static Logger log = Logger.getLogger(CombineSocket.class);
	private boolean outputLog = true;

	public CombineSocket(InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
	}

	public CombineSocket(InputStream is, OutputStream os, boolean outputLog) {
		this.is = is;
		this.os = os;
		this.outputLog = outputLog;
	}

	private InputStream is;
	private OutputStream os;

	@Override
	public void run() {
		// BufferedReader br = null;
		// BufferedWriter bw = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			// br = new BufferedReader(new InputStreamReader(is));
			// bw = new BufferedWriter(new OutputStreamWriter(os));

			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(os);

			byte[] b = new byte[BUFFER_SIZE];
			int readed;
			while ((readed = bis.read(b)) != -1) {
				if (log.isInfoEnabled() && outputLog) {
					log.info("receive message from inputStream.. readed:"
							+ readed);
				}
				bos.write(b, 0, readed);
				bos.flush();
			}

			// String readed;
			// while ((readed = br.readLine()) != null) {
			// if (log.isInfoEnabled() && outputLog) {
			// log.info("receive message from inputStream.. readed:"
			// + readed);
			// }
			// bw.write(readed);
			// bw.flush();
			// }
		} catch (IOException e) {
			log.error("IOException occured when combine"
					+ " the input and output stream..", e);
		} finally {
			Util.close(bis);
			Util.close(bos);

			// Util.close(br);
			// Util.close(bw);
		}
	}

}
