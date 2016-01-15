package cn.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

public class TestPost {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService service = null;
		List<Task> postArray = new ArrayList<Task>();
		for (int i = 0; i < 1000; i++) {
			postArray.add(new Task());
		}
		try {
			service = Executors.newFixedThreadPool(100);
			service.invokeAll(postArray);
		} finally {
			if (service != null) {
				service.shutdown();
			}
		}
	}

	static class Task implements Callable<Object> {
		@Override
		public Object call() throws Exception {
			String url = "http://192.168.1.79:10000";
			for (int i = 0; i < 100; i++) {
				PostMethod postMethod = new PostMethod(url);
				try {
					InputStream inputStream = new ByteArrayInputStream(
							url.getBytes());
					InputStreamRequestEntity e = new InputStreamRequestEntity(
							inputStream);
					postMethod.setRequestEntity(e);
					// postMethod.addRequestHeader("Connection", "close");

					HttpClientParams hcp = new HttpClientParams();
					hcp.setSoTimeout(5000);
					hcp.setConnectionManagerTimeout(500);

					HttpClient httpClient = new HttpClient(hcp);
					// httpClient.getParams().setBooleanParameter(
					// "http.protocol.expect-continue", false);

					try {
						httpClient.executeMethod(postMethod);
					} catch (HttpException e1) {

					} catch (IOException e1) {

					}
				} finally {
					if (postMethod != null) {
						postMethod.releaseConnection();
					}
				}
			}
			return null;
		}

	}
}
