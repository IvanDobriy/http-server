package otus.http.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import otus.http.server.container.Application;
import otus.http.server.container.configuration.ApplicationConfig;
import otus.http.server.container.configuration.ApplicationServletConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DispatcherTests {
    private class HttpServlet1 extends HttpServlet {
    }

    private class HttpServlet2 extends HttpServlet {
    }

    private class TestData {
        private final Dispatcher dispatcher;
        private final Map<String, HttpServlet> servletMap;

        public TestData(Dispatcher dispatcher, Map<String, HttpServlet> servletMap) {
            Objects.requireNonNull(dispatcher);
            Objects.requireNonNull(servletMap);
            this.dispatcher = dispatcher;
            this.servletMap = servletMap;
        }

        public Dispatcher getDispatcher() {
            return dispatcher;
        }

        public Map<String, HttpServlet> getServletMap() {
            return servletMap;
        }
    }

    private TestData prepareTestData() throws ServletException, IOException {
        final var servletMap = new HashMap<String, HttpServlet>();

        final var petServletConfig = Mockito.mock(ApplicationServletConfig.class);
        Mockito.when(petServletConfig.getUrlPatterns()).thenReturn(List.of("/pet"));
        final var carServletConfig = Mockito.mock(ApplicationServletConfig.class);
        Mockito.when(carServletConfig.getUrlPatterns()).thenReturn(List.of("/car"));
        final var wildcardServletConfig = Mockito.mock(ApplicationServletConfig.class);
        Mockito.when(wildcardServletConfig.getUrlPatterns()).thenReturn(List.of("/element/*"));

        final var applicationConfig = Mockito.mock(ApplicationConfig.class);
        final var application = Mockito.mock(Application.class);
        final var petServlet = Mockito.mock(HttpServlet1.class);
        final var carServlet = Mockito.mock(HttpServlet2.class);
        Mockito.doNothing().when(petServlet).service(Mockito.<HttpRequest>any(), Mockito.<HttpResponse>any());
        Mockito.when(application.getConfig()).thenReturn(applicationConfig);
        Mockito.when(application.getHttpServlets()).thenReturn(List.of(petServlet, carServlet));
        Mockito.when(applicationConfig.getContextPath()).thenReturn("/data");
        Mockito.when(applicationConfig
                        .getServletConfigMap())
                .thenReturn(
                        Map.of(
                                petServlet.getClass().getName(), petServletConfig,
                                carServlet.getClass().getName(), carServletConfig
                        ));
        servletMap.put("car", carServlet);
        servletMap.put("pet", petServlet);
        final var dispatcher = new Dispatcher();
        dispatcher.addApplication(application);
        return new TestData(dispatcher, servletMap);
    }

    @Test
    void petRequestTest() throws ServletException, IOException {
        final var testData = prepareTestData();

        final var petRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(petRequest.getRequestURI()).thenReturn("/data/pet");
        final var petResponse = Mockito.mock(HttpResponse.class);
        testData.dispatcher.dispatch(petRequest, petResponse);

        Mockito.verify(testData.getServletMap().get("pet")).service(petRequest, petResponse);
        Mockito.verify(testData.getServletMap().get("car"), Mockito.never()).service(petRequest, petResponse);

    }

    @Test
    void carRequestTest() throws ServletException, IOException {
        final var testData = prepareTestData();

        final var petRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(petRequest.getRequestURI()).thenReturn("/data/car");
        final var petResponse = Mockito.mock(HttpResponse.class);
        testData.dispatcher.dispatch(petRequest, petResponse);

        Mockito.verify(testData.getServletMap().get("car")).service(petRequest, petResponse);
        Mockito.verify(testData.getServletMap().get("pet"), Mockito.never()).service(petRequest, petResponse);

    }
}
