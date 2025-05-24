package otus.http.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class HttpServletOutputStream extends ServletOutputStream {
    private final OutputStream outputStream;
    private final HttpResponse response;
    private Boolean isFirstByte = true;
    private WriteListener writeListener;

    public HttpServletOutputStream(OutputStream outputStream, HttpResponse response) {
        Objects.requireNonNull(outputStream);
        Objects.requireNonNull(response);
        this.outputStream = outputStream;
        this.response = response;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        this.writeListener = writeListener;
    }

    @Override
    public void write(int b) throws IOException {
        if (isFirstByte) {
            isFirstByte = false;
            final var cap = response.getHttpCap().build().toByteArray();
            outputStream.write(cap);
        }
        outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        if (isFirstByte) {
            isFirstByte = false;
            final var cap = response.getHttpCap().build().toByteArray();
            outputStream.write(cap);
        }
        super.close();
        outputStream.close();
    }
}
