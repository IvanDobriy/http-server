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
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DispatcherTests {
    private class HttpServlet1 extends HttpServlet {
    }

    private class HttpServlet2 extends HttpServlet {
    }

    @Test
    void positiveTest() throws ServletException, IOException {
        final var petServletConfig = Mockito.mock(ApplicationServletConfig.class);
        Mockito.when(petServletConfig.getUrlPatterns()).thenReturn(List.of("/pet"));
        final var carServletConfig = Mockito.mock(ApplicationServletConfig.class);
        Mockito.when(carServletConfig.getUrlPatterns()).thenReturn(List.of("/car"));

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

        final var dispatcher = new Dispatcher();
        dispatcher.addApplication(application);

        final var request1 = Mockito.mock(HttpRequest.class);
        Mockito.when(request1.getRequestURI()).thenReturn("/data/pet");
        final var response1 = Mockito.mock(HttpResponse.class);
        dispatcher.dispatch(request1, response1);
        Mockito.verify(petServlet).service(request1, response1);
    }
}
