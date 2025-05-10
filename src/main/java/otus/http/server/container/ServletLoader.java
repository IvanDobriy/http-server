package otus.http.server.container;

import javax.servlet.http.HttpServlet;
import java.nio.file.Path;

public class ServletLoader {
    private final Path path;

    public ServletLoader(Path path){
        this.path = path;
    }

    public HttpServlet load(){
        return null;
    }
}
