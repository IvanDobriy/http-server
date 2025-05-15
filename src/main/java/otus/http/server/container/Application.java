package otus.http.server.container;

import otus.http.server.container.configuration.ApplicationConfig;
import otus.http.server.container.configuration.ServletConfig;

import javax.servlet.http.HttpServlet;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Application {
    private final URLClassLoader classLoader;
    private final ApplicationConfig config;
    private final List<HttpServlet> httpServlets;

    public Application(ApplicationConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
        this.httpServlets = new ArrayList<>();
        try {
            final var servletMap = config.getServletConfigMap();
            final var parentClassLoader = Thread.currentThread().getContextClassLoader();
            final List<URL> urlList = new ArrayList<>(getJarsURL());
            urlList.add(config.getClassesPath().toUri().toURL());
            classLoader = new URLClassLoader(urlList.toArray(new URL[0]), parentClassLoader);
            Class<?> servletClass;
            for (ServletConfig servletConfig : servletMap.values()) {
                servletClass = classLoader.loadClass(servletConfig.getClassName());
                httpServlets.add((HttpServlet) servletClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void init() {
        for (HttpServlet httpServlet : httpServlets) {
            try {
                httpServlet.init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<URL> getJarsURL() {
        final var urlList = new ArrayList<URL>();

        return urlList;
    }
}
