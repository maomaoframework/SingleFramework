package huxg.framework.thrift;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import huxg.framework.vo.Message;

public class ThriftClientTemplate {
	private static final Logger logger = Logger.getLogger(ThriftClientTemplate.class);
	private Map<String, Class> cs = new HashMap<String, Class>();
	private Map<String, String> services;

	/** 服务的IP地址 */
	private String serviceIP;

	/** 服务的端口 */
	private int servicePort;

	/** 连接超时配置 */
	private int timeOut;

	/**
	 * 执行有返回结果的action。
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String execute(String serviceName, ThriftAction thriftAction) {
		TTransport transport = null;
		try {
			transport = _open_();
			TProtocol protocol = new TBinaryProtocol(transport);

			TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol, serviceName);
			Class client = cs.get(serviceName);
			Constructor con = client.getConstructor(TProtocol.class);
			TServiceClient clientObj = (TServiceClient) con.newInstance(mp1);
			return thriftAction.action(clientObj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error( thriftAction + "----------------------------" + serviceName );
			logger.error(e);
			return Message.error();
		} finally {
			if (null != transport) {
				_close_(transport);
			}
		}
	}

	/**
	 * 打开Thrift链接
	 */
	TTransport _open_() throws Exception {
		TTransport transport = new TSocket(this.serviceIP, this.servicePort, this.timeOut);
		transport.open();
		return transport;
	}

	/**
	 * 关闭Thrift链接
	 */
	void _close_(TTransport transport) {
		try {
			transport.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void init() {
		for (Entry<String, String> e : services.entrySet()) {
			String id = e.getKey();
			String value = e.getValue();
			try {
				Class clientClass = Class.forName(value + "$Client");
				cs.put(id, clientClass);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public Map<String, String> getServices() {
		return services;
	}

	public void setServices(Map<String, String> services) {
		this.services = services;
	}

	public String getServiceIP() {
		return serviceIP;
	}

	public void setServiceIP(String serviceIP) {
		this.serviceIP = serviceIP;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * 有返回结果的回调接口定义。
	 */
	public interface ThriftAction {
		String action(TServiceClient client) throws Exception;
	}
}