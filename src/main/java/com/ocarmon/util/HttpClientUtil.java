package com.ocarmon.util;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * HttpClient工具类
 */
public class HttpClientUtil {
	private static CloseableHttpClient httpClient;
	private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
	private static HttpHost proxy;
	private static RequestConfig requestConfig;

	public HttpClientUtil() {
		new HttpClientUtil(null,null);
	}

	public HttpClientUtil(String host, Integer port) {
		if (host != null) {
			System.out.println("更换代理：" + host + ":" + port );
			proxy = new HttpHost(host, port);
		}
		// proxy=new HttpHost("182.92.242.11", 80);
		try {
			SSLContext sslContext = SSLContexts.custom()
					.loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							return true;
						}
					}).build();

			SSLConnectionSocketFactory sslSFactory = new SSLConnectionSocketFactory(sslContext);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslSFactory).build();

			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);

			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(Constants.TIMEOUT).setTcpNoDelay(true)
					.build();
			connManager.setDefaultSocketConfig(socketConfig);

			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(500);
			connManager.setDefaultMaxPerRoute(300);
			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					if (executionCount > 2) {
						return false;
					}
					if (exception instanceof InterruptedIOException) {
						return true;
					}
					if (exception instanceof ConnectTimeoutException) {
						return true;
					}
					if (exception instanceof UnknownHostException) {
						return true;
					}
					if (exception instanceof SSLException) {
						return true;
					}
					HttpRequest request = HttpClientContext.adapt(context).getRequest();
					if (!(request instanceof HttpEntityEnclosingRequest)) {
						return true;
					}
					return false;
				}
			};
			HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connManager)
					.setRetryHandler(retryHandler).setDefaultCookieStore(new BasicCookieStore())
					.setUserAgent(userAgent);
			if (proxy != null) {
				httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
			}
			httpClient = httpClientBuilder.build();
			requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT)
					.setConnectTimeout(Constants.TIMEOUT).setConnectionRequestTimeout(Constants.TIMEOUT)
					.setCookieSpec(CookieSpecs.STANDARD).build();
		} catch (Exception e) {
		}
	}



	public String getWebPage(String url) throws IOException {

		HttpGet request = new HttpGet(url);

		return getWebPage(request, "utf-8");
	}

	public  String getWebPage(HttpRequestBase request) throws IOException {
		return getWebPage(request, "utf-8");
	}

	/**
	 * @param encoding
	 *            字符编码
	 * @return 网页内容
	 */
	public  String getWebPage(HttpRequestBase request, String encoding) throws IOException {
		CloseableHttpResponse response = null;
		response = getResponse(request);
		String content = EntityUtils.toString(response.getEntity(), encoding);
		request.releaseConnection();
		return content;
	}

	public  CloseableHttpResponse getResponse(HttpRequestBase request) throws IOException {
		if (request.getConfig() == null) {
			request.setConfig(requestConfig);
		}
		request.setHeader("User-Agent",
				Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)]);
		HttpClientContext httpClientContext = HttpClientContext.create();
		CloseableHttpResponse response = httpClient.execute(request, httpClientContext);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new IOException("status code is:" + statusCode);
		}
		return response;
	}

	public  CloseableHttpResponse getResponse(String url) throws IOException {
		HttpGet request = new HttpGet(url);
		return getResponse(request);
	}

}
