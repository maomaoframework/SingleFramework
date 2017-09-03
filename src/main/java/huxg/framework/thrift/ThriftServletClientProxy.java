package huxg.framework.thrift;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

@SuppressWarnings("deprecation")
public class ThriftServletClientProxy {

	/**
	 * servlet 地址
	 */
	private String servletUrl;

	public String getServletUrl() {
		return servletUrl;
	}

	public void setServletUrl(String servletUrl) {
		this.servletUrl = servletUrl;
	}

	/**
	 * thrift 接口
	 */
	private String serviceInterface;

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	private static BasicHttpParams params;

	static {
		params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		// Disable Expect-Continue
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		// Enable staleness check
		params.setParameter("http.connection.stalecheck", true);
		HttpConnectionParams.setSoTimeout(params, 10000); // 10 secondes
		HttpConnectionParams.setConnectionTimeout(params, 10000); // 10 secondes
		ConnManagerParams.setMaxTotalConnections(params, 20);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getClient() {
		Object object = null;
		try {
			BasicClientConnectionManager cm = new BasicClientConnectionManager(
					getSchemeRegistry());
			THttpClient thc = new THttpClient(getServletUrl(),
					new DefaultHttpClient(cm, params));
			TProtocol loPFactory = new TCompactProtocol(thc);
			Class client = Class.forName(getServiceInterface() + "$Client");
			Constructor con = client.getConstructor(TProtocol.class);
			object = con.newInstance(loPFactory);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}

	public SchemeRegistry getSchemeRegistry() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		URL url;
		try {
			// 分析url，取出端口
			url = new URL(getServletUrl());
			String protocol = url.getProtocol();
			int port = url.getPort();
			if (-1 == port) {
				if ("https".equals(protocol)) {
					port = 443;
				} else {
					port = 80;
				}
			}
			if ("https".equals(protocol)) {
				schemeRegistry.register(new Scheme("https", port,
						SSLSocketFactory.getSocketFactory()));
			} else {
				schemeRegistry.register(new Scheme("http", port,
						PlainSocketFactory.getSocketFactory()));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return schemeRegistry;

	}
}
