package cc.co.geniusman.http;

public class HostAndPort {
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol);
		sb.append("://");
		sb.append(hostName);

		if (!port.equals("")) {
			sb.append(":");
			sb.append(port);
		}
		return sb.toString();
	}

	String hostName = "";
	String port = "";
	String protocol = "";
}
