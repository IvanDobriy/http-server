package otus.http.server.container.configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

public class HttpServletConfig implements ServletConfig {
    private final ApplicationServletConfig applicationServletConfig;
    private final HttpServletContext servletContext;

    public HttpServletConfig(ApplicationServletConfig applicationServletConfig) {
        Objects.requireNonNull(applicationServletConfig);
        this.applicationServletConfig = applicationServletConfig;
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
        return applicationServletConfig.getInitParameters().get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(applicationServletConfig.getInitParameters().keySet());
    }
}
