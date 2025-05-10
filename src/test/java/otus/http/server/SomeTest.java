package otus.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SomeTest {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    class Car{
        private int number;

        public Car(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
    @Test
    void someTest(){
        final var car = Mockito.mock(Car.class);
        Mockito.when(car.getNumber()).thenReturn(10);
        logger.info("car number {}", car.getNumber());
    }

}
