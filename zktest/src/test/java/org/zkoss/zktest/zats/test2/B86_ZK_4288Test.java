/* B86_ZK_4288Test.java

	Purpose:
		
	Description:
		
	History:
		Fri May 17 10:21:58 CST 2019, Created by rudyhuang

Copyright (C) 2019 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zktest.zats.test2;

import static io.github.pixee.security.ObjectInputFilters.createSafeObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

import org.zkoss.test.webdriver.BaseTestCase;
import org.zkoss.test.webdriver.ExternalZkXml;
import org.zkoss.test.webdriver.ForkJVMTestOnly;
import org.zkoss.test.webdriver.PrototypeServer;

/**
 * @author rudyhuang
 */
@ForkJVMTestOnly
public class B86_ZK_4288Test extends BaseTestCase {
	@RegisterExtension
	@Order(1)
	public static final ExternalZkXml CONFIG = new ExternalZkXml(B86_ZK_4288Test.class);
	@RegisterExtension
	public static final InternalServer prototypeServer = new InternalServer();
	public static class InternalServer extends PrototypeServer {
		private Server server;
		public void postProcessTestInstance(Object testInstance,
				ExtensionContext eContext) throws Exception {

			Server server = new Server(new InetSocketAddress(getHost(), 0));

			final WebAppContext context = new WebAppContext();
			context.setContextPath(getContextPath());
			context.setBaseResource(Resource.newResource("./src/main/webapp/"));
			context.getSessionHandler().setSessionIdPathParameterName(null);
			Filter sessionRepositoryFilter = new SessionRepositoryFilter<>(new B86_ZK_4288SessionRepository());
			context.addFilter(new FilterHolder(sessionRepositoryFilter), "/*",  EnumSet.of(DispatcherType.REQUEST));
			server.setHandler(new HandlerList(context, new DefaultHandler()));
			B86_ZK_4288Test base = (B86_ZK_4288Test) testInstance;
			server.start();
			base.initServer(server);
		}
		public Server getServer() {
			return server;
		}
	}

	@Test
	public void test() {
		connect();
		waitResponse();
		assertNoJSError();
	}

	private static class B86_ZK_4288SessionRepository implements SessionRepository<Session> {
		private static final Logger LOG = LoggerFactory.getLogger(B86_ZK_4288SessionRepository.class);
		private final Map<String, byte[]> sessionDataCache = new ConcurrentHashMap<>();

		@Override
		public Session createSession() {
			return new MapSession();
		}

		@Override
		public void save(Session session) {
			LOG.debug("save: {}", session.getId());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				LOG.debug("writeObject: {}", session.getId());
				new ObjectOutputStream(baos).writeObject(session);
			} catch (Exception e) {
				LOG.error("", e);
			}
			sessionDataCache.put(session.getId(), baos.toByteArray());
		}

		@Override
		public Session findById(String id) {
			LOG.debug("findById: {}", id);
			byte[] bytes = sessionDataCache.get(id);
			if (bytes != null) {
				try {
					LOG.debug("readObject: {}", id);
					Session restoredSession = (Session) createSafeObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
					MapSession newSession = new MapSession(id);
					for (String attributeName : restoredSession.getAttributeNames()) {
						newSession.setAttribute(attributeName, restoredSession.getAttribute(attributeName));
					}
					return newSession;
				} catch (Exception e) {
					LOG.error("", e);
				}
			}
			return null;
		}

		@Override
		public void deleteById(String id) {
			LOG.debug("deleteById: {}", id);
			sessionDataCache.remove(id);
		}
	}
}
