package info.kuyur.justblog.stateful.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListenerImpl implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Initialization job here
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Finalization job here
	}

}
