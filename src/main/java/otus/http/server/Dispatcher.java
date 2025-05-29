package otus.http.server;

import otus.http.server.container.Application;

import javax.servlet.http.HttpServlet;
import java.util.*;

public class Dispatcher {
    private volatile Map<String, HttpServlet> servletsMap;
    private volatile List<Map.Entry<String, HttpServlet>> wildcards;

    public Dispatcher() {
        servletsMap = new HashMap<>();
        wildcards = new ArrayList<>();
    }

    public void addApplication(Application application) {
        Objects.requireNonNull(application);
        final var servlets = application.getHttpServlets();
        final var contextPath = application.getConfig().getContextPath();
        final var servletConfigMap = application.getConfig().getServletConfigMap();
        final var newServletsMap = new HashMap<>(this.servletsMap);
        final var newWildcards = new ArrayList<>(this.wildcards);
        String servletName;
        for (HttpServlet servlet : servlets) {
            servletName = servlet.getClass().getName();
            if (servletConfigMap.containsKey(servletName)) {
                final var servletConfig = servletConfigMap.get(servletName);
                for (String urlPattern : servletConfig.getUrlPatterns()) {
                    if (urlPattern.endsWith("*")) {
                        newWildcards.add(new AbstractMap.SimpleEntry<>(contextPath + urlPattern.substring(0, urlPattern.length() - 1), servlet));
                    } else {
                        newServletsMap.put(contextPath + urlPattern, servlet);
                    }
                }
            }
        }
        this.wildcards = newWildcards;
        this.servletsMap = newServletsMap;
    }

    public void dispatch(HttpRequest request, HttpResponse response) {
        try {
            if (servletsMap.containsKey(request.getRequestURI())) {
                final var servlet = servletsMap.get(request.getRequestURI());
                servlet.service(request, response);
                return;
            }
            for(Map.Entry<String, HttpServlet> wildcard: wildcards){
                if(request.getRequestURI().startsWith(wildcard.getKey())){
                    final var servlet = wildcard.getValue();
                    servlet.service(request, response);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, HttpServlet> getServletsMap() {
        return servletsMap;
    }
}
