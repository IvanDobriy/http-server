package otus.http.server;

import otus.http.server.container.Application;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Dispatcher {
    private class ServletBinding{
        private final HttpServlet servlet;
        private final Application application;

        public ServletBinding(HttpServlet servlet, Application application) {
            Objects.requireNonNull(servlet);
            Objects.requireNonNull(application);
            this.servlet = servlet;
            this.application = application;
        }

        public HttpServlet getServlet() {
            return servlet;
        }

        public Application getApplication() {
            return application;
        }
    }
    private volatile Map<String, ServletBinding> servletsMap;

    public Dispatcher() {
        servletsMap = new HashMap<>();
    }

    public void addApplication(Application application) {
        Objects.requireNonNull(application);
        final var servlets = application.getHttpServlets();
        final var contextPath = application.getConfig().getContextPath();
        final var servletConfigMap = application.getConfig().getServletConfigMap();
        final var newServletsMap = new HashMap<>(this.servletsMap);
        String servletName;
        for (HttpServlet servlet : servlets) {
            servletName = servlet.getClass().getName();
            if (servletConfigMap.containsKey(servletName)) {
                final var servletConfig = servletConfigMap.get(servletName);
                for (String urlPattern : servletConfig.getUrlPatterns()) {
                    newServletsMap.put(contextPath + urlPattern, new ServletBinding(servlet, application));
                }
            }
        }
        this.servletsMap = newServletsMap;
    }

    public void dispatch(HttpRequest request, HttpResponse response) {
        try {
            if (servletsMap.containsKey(request.getRequestURI())) {
                final var binding = servletsMap.get(request.getRequestURI());
                request.setApplicationConfig(binding.application.getConfig());
                binding.getServlet().service(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
