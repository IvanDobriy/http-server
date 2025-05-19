package otus.http.server.container;

import otus.http.server.container.configuration.ApplicationConfig;
import otus.http.server.container.configuration.ServletConfig;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Application {
    private final URLClassLoader classLoader;
    private final ApplicationConfig config;
    private final List<HttpServlet> httpServlets;
    private final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.jar");

    public Application(ApplicationConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
        this.httpServlets = new ArrayList<>();
        try {
            final var servletMap = config.getServletConfigMap();
            final var parentClassLoader = Thread.currentThread().getContextClassLoader();
            final List<URL> urlList = new ArrayList<>(getJarsURL());
            urlList.add(config.getClassesPath().toUri().toURL());
            //todo добавить проверку на существование jar(библиотеки) или классика в родителе
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
        try {
            if (!Files.exists(config.getLibPath())) {
                return List.of();
            }
            return Files.list(config.getLibPath()).filter((path) -> {
                return pathMatcher.matches(path.getFileName());
            }).map((path) -> {
                try {
                    return path.toUri().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    public List<HttpServlet> getHttpServlets() {
        return httpServlets;
    }
}
