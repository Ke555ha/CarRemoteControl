package ru.raif.rccar.service;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import ru.raif.rccar.config.AppiumProperties;
import ru.raif.rccar.model.CarPoint;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
public class CarControllerService {

    private static final CarPoint GAS_NEUTRAL = CarPoint.of(460, 460);
    private static final CarPoint GAS_FORWARD = CarPoint.of(460, 300);
    private static final CarPoint GAS_BACKWARD = CarPoint.of(460, 620);

    private static final CarPoint STEER_NEUTRAL = CarPoint.of(1920, 460);
    private static final CarPoint STEER_LEFT = CarPoint.of(1720, 460);
    private static final CarPoint STEER_RIGHT = CarPoint.of(2050, 460);

    private static final CarPoint LIFT_UP = CarPoint.of(700, 980);
    private static final CarPoint LIFT_DOWN = CarPoint.of(950, 980);

    private final AppiumProperties properties;
    private AndroidDriver driver;

    public CarControllerService(AppiumProperties properties) {
        this.properties = properties;
    }

    public void connect() {
        if (driver != null) {
            return;
        }

        try {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setDeviceName("Android")
                    .setUdid(properties.getUdid())
                    .setNoReset(properties.isNoReset())
                    .setNewCommandTimeout(Duration.ofSeconds(properties.getNewCommandTimeoutSeconds()));

            driver = new AndroidDriver(new URL(properties.getUrl()), options);

            System.out.println("Session started");
            System.out.println("Orientation: " + driver.getOrientation());
            //System.out.println("Rect: " + driver.manage().window().getRect());

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Appium", e);
        }
    }

    public void forward(int ms) {
        ensureConnected();
        performSingleTouch(GAS_FORWARD, ms);
    }

    public void forwardLeft(int ms) {
        ensureConnected();
        performTwoTouch(GAS_FORWARD, STEER_LEFT, ms);
    }

    public void forwardRight(int ms) {
        ensureConnected();
        performTwoTouch(GAS_FORWARD, STEER_RIGHT, ms);
    }

    public void backward(int ms) {
        ensureConnected();
        performSingleTouch(GAS_BACKWARD, ms);
    }

    public void backwardLeft(int ms) {
        ensureConnected();
        performTwoTouch(GAS_BACKWARD, STEER_LEFT, ms);
    }

    public void backwardRight(int ms) {
        ensureConnected();
        performTwoTouch(GAS_BACKWARD, STEER_RIGHT, ms);
    }

    public void neutral(int ms) {
        ensureConnected();
        performTwoTouch(GAS_NEUTRAL, STEER_NEUTRAL, ms);
    }

    public synchronized void liftUp(int ms) {
        ensureConnected();
        performSingleTouch(LIFT_UP, ms);
    }

    public synchronized void liftDown(int ms) {
        ensureConnected();
        performSingleTouch(LIFT_DOWN, ms);
    }

    private void ensureConnected() {
        if (driver == null) {
            throw new IllegalStateException("Driver is not connected");
        }
    }

    private void performSingleTouch(CarPoint point, int holdMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger_lift");

        Sequence sequence = new Sequence(finger, 0);
        sequence.addAction(finger.createPointerMove(
                Duration.ofMillis(0),
                PointerInput.Origin.viewport(),
                point.getX(),
                point.getY()
        ));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, Duration.ofMillis(holdMs)));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));

        try {
            driver.resetInputState();
        } catch (Exception ignored) {
        }
    }

    private void performTwoTouch(CarPoint gasPoint, CarPoint steerPoint, int holdMs) {
        PointerInput fingerGas = new PointerInput(PointerInput.Kind.TOUCH, "finger_gas");
        PointerInput fingerSteer = new PointerInput(PointerInput.Kind.TOUCH, "finger_steer");

        Sequence gasSequence = new Sequence(fingerGas, 0);
        gasSequence.addAction(fingerGas.createPointerMove(
                Duration.ofMillis(0),
                PointerInput.Origin.viewport(),
                gasPoint.getX(),
                gasPoint.getY()
        ));
        gasSequence.addAction(fingerGas.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        gasSequence.addAction(new Pause(fingerGas, Duration.ofMillis(holdMs)));
        gasSequence.addAction(fingerGas.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence steerSequence = new Sequence(fingerSteer, 0);
        steerSequence.addAction(fingerSteer.createPointerMove(
                Duration.ofMillis(0),
                PointerInput.Origin.viewport(),
                steerPoint.getX(),
                steerPoint.getY()
        ));
        steerSequence.addAction(fingerSteer.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        steerSequence.addAction(new Pause(fingerSteer, Duration.ofMillis(holdMs)));
        steerSequence.addAction(fingerSteer.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(gasSequence, steerSequence));

        try {
            driver.resetInputState();
        } catch (Exception ignored) {
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void shutdown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
