package otus.http.server.container.configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Objects;

public class HttpServletConfig implements ServletConfig {
    private final ApplicationServletConfig applicationServletConfig;
    private final HttpServletConfig servletConfig;
    private final HttpServletContext servletContext;

    public HttpServletConfig(ApplicationServletConfig applicationServletConfig) {
        Objects.requireNonNull(applicationServletConfig);
        this.applicationServletConfig = applicationServletConfig;
        this.servletConfig = new HttpServletConfig(applicationServletConfig);
        this.servletContext = new HttpServletContext(applicationServletConfig);
    }

    @Override
    public String getServletName() {
        return applicationServletConfig.getName();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return servletConfig.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return servletConfig.getInitParameterNames();
    }
}
