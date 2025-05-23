package otus.http.server.container.configuration;

import java.util.List;

public class ApplicationServletConfig {
    private String name;
    private String className;
    private List<String> urlPatterns;

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
