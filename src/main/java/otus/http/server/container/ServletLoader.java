package otus.http.server.container;

import javax.servlet.http.HttpServlet;
import java.nio.file.Path;

public class ServletLoader {
    private final Path path;
    private HttpServlet servlet;

    public ServletLoader(Path path){
        this.path = path;
    }

    public HttpServlet load(){
        if(servlet == null){
            //todo load servlet
        }
        return servlet;
    }
}
