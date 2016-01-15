package cc.co.geniusman.http;

import java.io.IOException;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class HttpRequest {

	private static Logger log = Logger.getLogger(HttpRequest.class);
	public static final int HTTP_SOCKET_TIMEOUT = 5000;

	public static ResponseInfo post(String url, String body)
			throws IOException, HTTPException {
		HttpClientParams hcp = new HttpClientParams();
		hcp.setSoTimeout(HTTP_SOCKET_TIMEOUT);
		HttpClient client = new HttpClient(hcp);
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		PostMethod post = new PostMethod(url);
		ResponseInfo info = null;
		try {
			if (body != null) {
				StringRequestEntity e = new StringRequestEntity(body, null,
						null);
				post.setRequestHeader("Content-type", "text/xml");
				post.setRequestEntity(e);
			}
			log.debug("HTTP poster posting to " + url);
			int result = client.executeMethod(post);
			log.debug("Finished post. response code: " + result);
			info = new ResponseInfo();
			info.setCodeResult(result);
			info.setResponseString(post.getResponseBodyAsString());

		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		return info;
	}

	public static ResponseInfo get(String url) throws IOException,
			HTTPException {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		GetMethod get = new GetMethod(url);
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		ResponseInfo info = null;
		try {
			get.setFollowRedirects(false);
			get.setRequestHeader("Content-type", "text/xml");
			log.debug("HTTP poster posting to " + url);
			int result = client.executeMethod(get);
			log.debug("Finished post. response code: " + result);
			info = new ResponseInfo();
			info.setCodeResult(result);
			info.setResponseString(get.getResponseBodyAsString());

		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}
		return info;
	}

	public static void main(String[] args) throws HTTPException, IOException {
		// ResponseInfo postInfo = post("http://www.google.com/", null);
		// System.out.println(postInfo.getCodeResult());
		// System.out.println(postInfo.getResponseString());

		ResponseInfo getInfo = get("http://www.google.com.hk/search?num=100?hl=zh-CN&source=hp&q=allinurl%3A%2Fjmx-console%2FHtmlAdaptor&aq=f&aqi=&aql=&oq=&gs_rfai=");
		System.out.println(getInfo.getCodeResult());
		System.out.println(getInfo.getResponseString());

		// ScriptEngineManager sem = new ScriptEngineManager();
		// ScriptEngine engine = sem.getEngineByExtension("js");
		// try {
		// engine.eval(getInfo.getResponseString());
		// } catch (ScriptException ex) {
		// ex.printStackTrace();
		// }
		// System.out.println((engine.get("flag")));

	}

	public static final class ResponseInfo {
		private int codeResult;

		public int getCodeResult() {
			return codeResult;
		}

		public void setCodeResult(int codeResult) {
			this.codeResult = codeResult;
		}

		public String getResponseString() {
			return responseString;
		}

		public void setResponseString(String responseString) {
			this.responseString = responseString;
		}

		private String responseString;

	}

}
