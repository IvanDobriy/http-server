package otus.http.server;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HttpServletInputStream extends ServletInputStream {
    private final InputStream inputStream;
    public HttpServletInputStream(InputStream inputStream){
        Objects.requireNonNull(inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
