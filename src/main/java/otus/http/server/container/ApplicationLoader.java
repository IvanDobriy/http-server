package otus.http.server.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.http.server.container.configuration.ServletConfig;
import otus.http.server.container.configuration.WebXml;

import javax.servlet.http.HttpServlet;
import java.nio.file.Path;

public class ApplicationLoader {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private final Path path;
    private HttpServlet servlet;

    public ApplicationLoader(Path path){
        this.path = path;
    }



    public HttpServlet load(){
        if(servlet == null){
            //todo load servlet
            final var webXml = new WebXml(path.resolve("./WEB-INF/web.xml"));
            final var map  = webXml.getServletConfigMap();
            for(ServletConfig config: map.values()){

            }

            logger.info("map: {}", map);
        }
        return servlet;
    }
}
