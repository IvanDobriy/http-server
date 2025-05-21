package otus.http.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponse implements HttpServletResponse {

    private final OutputStream outputStream;
    private final HttpServletOutputStream httpServletOutputStream;
    private final PrintWriter printWriter;
    private final Map<String, String> headerMap;
    private int status;
    private String statusMessage;
    private String characterEncoding;
    private long contentLength;
    private String contentType;
    private int bufferSize;
    private Locale locale;
    private final HttpCap httpCap;

    public HttpResponse(Socket socket) {
        Objects.requireNonNull(socket);
        try {
            status = 200;
            statusMessage = "";
            characterEncoding = StandardCharsets.UTF_8.name();
            headerMap = new HashMap<>();
            contentType = null;
            bufferSize = 1024;
            locale = Locale.getDefault();
            httpCap = new HttpCap(this);

            outputStream = socket.getOutputStream();
            httpServletOutputStream = new HttpServletOutputStream(outputStream, this);
            printWriter = new PrintWriter(httpServletOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String name) {
        return headerMap.containsKey(name);
    }

    @Override
    public String encodeURL(String url) {
        //todo add url encoding
        return "";
    }

    @Override
    public String encodeRedirectURL(String url) {
        //todo add redirect url encoding
        return "";
    }

    @Override
    public String encodeUrl(String url) {
        //todo add url encoding
        return "";
    }

    @Override
    public String encodeRedirectUrl(String url) {
        //todo add url redirect encoding
        return "";
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        status = sc;
        statusMessage = statusMessage;
        //todo send msg;
    }

    @Override
    public void sendError(int sc) throws IOException {
        status = sc;
        //todo send msg
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        status = 300;
        //todo send redirect
    }

    @Override
    public void setDateHeader(String name, long date) {
        headerMap.put(name, String.valueOf(date));
    }

    @Override
    public void addDateHeader(String name, long date) {
        headerMap.put(name, String.valueOf(date));
    }

    @Override
    public void setHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        headerMap.put(name, String.valueOf(value));
    }

    @Override
    public void addIntHeader(String name, int value) {
        headerMap.put(name, String.valueOf(value));
    }

    @Override
    public void setStatus(int sc) {
        status = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        status = sc;
        statusMessage = sm;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getHeader(String name) {
        return headerMap.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return List.of();
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headerMap.values();
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return httpServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        characterEncoding = charset;
    }

    @Override
    public void setContentLength(int len) {
        contentLength = len;
    }

    @Override
    public void setContentLengthLong(long len) {
        contentLength = len;
    }

    @Override
    public void setContentType(String type) {
        contentType = type;
    }

    @Override
    public void setBufferSize(int size) {
        bufferSize = bufferSize;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public void flushBuffer() throws IOException {
        //todo add buffer flushing
    }

    @Override
    public void resetBuffer() {
        //todo add buffer resetting
    }

    @Override
    public boolean isCommitted() {
        //todo add is commited check
        return false;
    }

    @Override
    public void reset() {
        //todo add reset
    }

    @Override
    public void setLocale(Locale loc) {
        locale = loc;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public HttpCap getHttpCap() {
        return httpCap;
    }
}
