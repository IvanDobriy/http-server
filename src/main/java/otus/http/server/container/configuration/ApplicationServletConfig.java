package otus.http.server.container.configuration;

import java.util.List;
import java.util.Map;

public class ApplicationServletConfig {
    private String name;
    private String className;
    private List<String> urlPatterns;
    private Map<String, String> initParameters;

    private Map<String, String> contextParameters;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
    }

    public Map<String, String> getContextParameters() {
        return contextParameters;
    }

    public void setContextParameters(Map<String, String> contextParameters) {
        this.contextParameters = contextParameters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("name: ").append(name)
                .append(", className: ").append(className)
                .append(", urlPatterns: ").append(urlPatterns)
                .append("}");
        return builder.toString();
    }
}
