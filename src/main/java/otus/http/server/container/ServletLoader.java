package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.container.configuration.WebXml;

import javax.servlet.http.HttpServlet;
import java.nio.file.Path;

public class ServletLoader {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final Path path;
    private HttpServlet servlet;

    public ServletLoader(Path path){
        this.path = path;
    }

    public HttpServlet load(){
        if(servlet == null){
            //todo load servlet
            final var webXml = new WebXml(path.resolve("./WEB-INF/web.xml"));
            final var map  = webXml.getServletConfigMap();
            logger.info("map: {}", map);
        }
        return servlet;
    }
}
