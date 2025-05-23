package otus.http.server.container.configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Objects;

public class HttpServletConfig implements ServletConfig {
    private final ApplicationServletConfig applicationServletConfig;

    public HttpServletConfig(ApplicationServletConfig applicationServletConfig) {
        Objects.requireNonNull(applicationServletConfig);
        this.applicationServletConfig = applicationServletConfig;
    }

    @Override
    public String getServletName() {
        return applicationServletConfig.getName();
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return "";
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }
}
