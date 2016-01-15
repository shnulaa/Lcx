package cc.co.geniusman.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

/**
 * 
 * @author liuyq
 * 
 */
public class ParserGoogleSearch {

	private static Map<String, HostAndPort> resultList = new HashMap<String, HostAndPort>();
	// static final String keyword = "allinurl:/jmx-console/HtmlAdaptor";
	static final String keyword = "allintitle: MBean Inspector";
	static final int perSearch = 100;
	static int alreadySearched = 0;
	private static Logger log = Logger.getLogger(ParserGoogleSearch.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		getResult();
	}

	public static Map<String, HostAndPort> getResult() {

		boolean hasSearchResult = true;
		while (hasSearchResult) {
			String url = "http://www.google.com.hk/search?q="
					+ encodeValue(keyword)
					+ "&num="
					+ perSearch
					+ "&hl=zh-CN&newwindow=1&safe=strict&biw=1440&bih=775&prmd=imvns&ei=9R8iULvqIKGJmQWpy4HABg&start="
					+ alreadySearched + "&sa=N";
			String html = getUrlHtmlByHttpClient(url);
			hasSearchResult = parseHtmlLink(html);
			alreadySearched += perSearch;

			log.info("already searched count:" + alreadySearched);
		}

		// for (Entry<String, HostAndPort> entry : resultList.entrySet()) {
		// HostAndPort result = entry.getValue();
		// System.out.println(result.toString());
		// }

		System.out.println(resultList.size());

		return resultList;
	}

	private static String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return value;
	}

	private static boolean parseHtmlLink(String htmlstr) {
		try {
			Parser parser = Parser.createParser(htmlstr, "utf-8");

			TagNameFilter filter = new TagNameFilter("h3");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			Node[] arrays = nodes.toNodeArray();
			if (arrays.length == 0) {
				return false;
			}

			for (Node node : arrays) {
				LinkTag childen = (LinkTag) node.getFirstChild();

				String result = childen.getLink().replace("/url?q=", "");

				int pos = result.indexOf("/jmx-console");
				if (pos >= 0) {
					result = result.substring(0, pos);
				} else {
					continue;
				}

				String[] splited = result.split(":");
				HostAndPort hostAndPort = new HostAndPort();
				if (splited != null && splited.length >= 3) {
					hostAndPort.setProtocol(splited[0]);
					hostAndPort.setHostName(splited[1].replace("//", ""));
					hostAndPort.setPort(splited[2]);
				} else {
					hostAndPort.setProtocol(splited[0]);
					hostAndPort.setHostName(splited[1].replace("//", ""));
				}

				String key = hostAndPort.getHostName();
				if (key == "")
					continue;
				if (resultList.get(key) == null) {
					resultList.put(key, hostAndPort);
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static String getUrlHtmlByHttpClient(String url) {
		// String searchHtml = null;
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(50000);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		String ret = null;
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			// InputStream bodyIs = getMethod.getResponseBodyAsStream();//
			// System.out.println("get reoponse body stream:" + bodyIs);

			ret = getMethod.getResponseBodyAsString();

			// BufferedReader br = new BufferedReader(new InputStreamReader(
			// bodyIs, "GBK"));
			// BufferedReader br = new BufferedReader(
			// new InputStreamReader(bodyIs));
			// StringBuffer sb = new StringBuffer();
			// String line = null;
			// while ((line = br.readLine()) != null) {
			// sb.append(line);
			// }
			// searchHtml = sb.toString();
			return ret;
		} catch (HttpException e) {
			System.out.println("Please check your http address!");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			getMethod.releaseConnection();
		}

	}

}
