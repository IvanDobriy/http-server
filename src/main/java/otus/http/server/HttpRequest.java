package otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.Socket;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

public class HttpRequest implements HttpServletRequest {
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    private InputStream inputStream;
    private InputStreamReader reader;

    private final String method;
    private final String requestUri;

    private Map<String, String[]> parameters;

    private enum ParameterParserStates {
        PARSE_KEY,
        PARSE_VALUE
    }

    public HttpRequest(Socket socket) {
        try {
            inputStream = socket.getInputStream();
            reader = new InputStreamReader(inputStream);
            parameters = new HashMap<>();
            method = parseMethodName();
            requestUri = parseUriAndParameters();

            logger.info("method: {}, requestUri: {}, parameters: {}", method, requestUri, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String parseBetweenSpaces() throws IOException {
        final var builder = new StringBuilder();
        int symbol;
        while ((symbol = reader.read()) != -1) {
            if ((char) symbol == ' ') {
                break;
            }
            builder.append((char) symbol);
        }
        return builder.toString();
    }

    private String parseMethodName() throws IOException {
        return parseBetweenSpaces();
    }

    private String parseUriAndParameters() throws IOException {
        final var builder = new StringBuilder();
        int symbol;
        while ((symbol = reader.read()) != -1) {
            if ((char) symbol == ' ' || (char) symbol == '?') {
                break;
            }
            builder.append((char) symbol);
        }
        if (symbol == '?') {
            parameters = parseParameters();
        }
        return builder.toString();
    }

    private void addParameter(StringBuilder keyName, StringBuilder value, Map<String, String[]> result) {
        if (keyName.length() != 0) {
            final var name = keyName.toString();
            if (result.containsKey(name)) {
                final var values = result.get(name);
                final var newArray = Arrays.copyOf(values, values.length + 1);
                newArray[newArray.length - 1] = value.toString();
                result.put(name, newArray);
            } else {
                result.put(name, new String[]{value.toString()});
            }
        }
    }


    private Map<String, String[]> parseParameters() throws IOException {
        final var result = new HashMap<String, String[]>();
        var parameterState = ParameterParserStates.PARSE_KEY;
        int symbol;
        StringBuilder keyName = new StringBuilder();
        StringBuilder value = new StringBuilder();
        while ((symbol = reader.read()) != -1) {
            if ((char) symbol == ' ') {
                break;
            }
            if ((char) symbol == '&') {
                addParameter(keyName, value, result);
                parameterState = ParameterParserStates.PARSE_KEY;
                keyName = new StringBuilder();
                value = new StringBuilder();
                continue;
            }
            if ((char) symbol == '=') {
                parameterState = ParameterParserStates.PARSE_VALUE;
                continue;
            }
            if (parameterState == ParameterParserStates.PARSE_KEY) {
                keyName.append((char) symbol);
                continue;
            }
            if (parameterState == ParameterParserStates.PARSE_VALUE) {
                value.append((char) symbol);
            }
        }
        addParameter(keyName, value, result);
        return result;
    }

    @Override
    public String getAuthType() {
        return "";
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return "";
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return "";
    }

    @Override
    public String getPathTranslated() {
        return "";
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getQueryString() {
        return "";
    }

    @Override
    public String getRemoteUser() {
        return "";
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return "";
    }

    @Override
    public String getRequestURI() {
        return requestUri;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return "";
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return "";
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return List.of();
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return "";
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return "";
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name)[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.values().stream().map(arr -> arr[0]).collect(Collectors.toList()));
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public String getProtocol() {
        return "";
    }

    @Override
    public String getScheme() {
        return "";
    }

    @Override
    public String getServerName() {
        return "";
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return "";
    }

    @Override
    public String getRemoteHost() {
        return "";
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return "";
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return "";
    }

    @Override
    public String getLocalAddr() {
        return "";
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}