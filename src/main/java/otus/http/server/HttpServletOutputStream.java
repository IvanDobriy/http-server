package otus.http.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class HttpServletOutputStream extends ServletOutputStream {
    private final OutputStream outputStream;

    private Boolean isFirstByte = true;

    public HttpServletOutputStream(OutputStream outputStream, HttpResponse response) {
        Objects.requireNonNull(outputStream);
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        if (isFirstByte) {
            isFirstByte = false;
        }
        outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        if(isFirstByte){
            isFirstByte = false;
        }
        super.close();
        outputStream.close();
    }
}
