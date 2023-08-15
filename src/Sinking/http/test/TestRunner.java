package Sinking.http.test;

import Sinking.common.Classloader;
import Sinking.common.Tripel;
import Sinking.http.client.Client;
import Sinking.http.server.HttpRouteLoader;
import Sinking.http.server.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TestRunner extends Classloader<Object> {
    //#region colors
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    //#endregion
    private Client client;
    private Server server;
    private final ArrayList<Tripel<Class<?>, String, Method>> tests = new ArrayList<>();
    private int executedTests = 0;
    private int passedTests = 0; // don't track failed test, think positive and track passed tests

    private TestRunner(String packageName) {
        this.packageName = packageName;
    }

    public static void runTests() {
        TestRunner runner = new TestRunner("Sinking.http.test.tests");
        runner.run();
    }

    public void run() {
        setUpServerAndClient();
        loadTests();
        executeTest(0);
    }

    private void executeTest(int index) {
        if (index >= tests.size()) {
            System.out.printf("Executed tests: %d\nPASSED: %d\nFAILED: %d\n", executedTests, passedTests, executedTests - passedTests);
            System.out.println("=========");
            System.out.println("stopping server...");
            server.stop();
            return;
        }
        Tripel<Class<?>, String, Method> test = tests.get(index);
        System.out.printf("[%s -> %s]", test._1().getSimpleName(), test._2());

        Method actualTest = test._3();
        executedTests++;
        try {
            actualTest.invoke(test._1().getDeclaredConstructor().newInstance(), client, (ITestResult) (result) -> {
                if (result) {
                    passedTests++;
                    System.out.println(colorize(GREEN, " PASSED"));
                } else {
                    System.out.println(colorize(RED, " FAILED"));
                }
                executeTest(index + 1);
            });
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException |
                 InstantiationException e) {
            System.out.println(colorize(RED, " FAILED"));
            e.printStackTrace();
            executeTest(index + 1);
        }
    }

    private void loadTests() {
        loadClasses().forEach(this::analyzeClassAndMethods);
        sortTests();
    }

    private void sortTests() {
        Tripel<Class<?>, String, Method>[] array = new Tripel[tests.size()];
        array = tests.toArray(array);
        // https://stackoverflow.com/a/1099389/13079323
        Arrays.sort(array, (o1, o2) -> {
            Method m1 = o1._3();
            Method m2 = o2._3();

            Test or1 = m1.getAnnotation(Test.class);
            Test or2 = m2.getAnnotation(Test.class);
            // nulls last
            if (or1 != null && or2 != null) {
                return or1.order() - or2.order();
            } else if (or1 != null) {
                return -1;
            } else if (or2 != null) {
                return 1;
            }
            return -1;
        });
        tests.clear();
        tests.addAll(Arrays.asList(array));
    }

    private void analyzeClassAndMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Test.class) || !isValidTestMethod(method)) {
                continue;
            }

            Test annotation = method.getAnnotation(Test.class);
            String testTitle = annotation.name();

            tests.add(new Tripel<>(clazz, testTitle, method));
        }
    }

    private boolean isValidTestMethod(Method method) {
        Type[] paramTypes = method.getGenericParameterTypes();
        return paramTypes.length == 2 && paramTypes[0] == Client.class && paramTypes[1] == ITestResult.class;
    }

    private void setUpServerAndClient() {
        int testPort = 3333;
        this.client = new Client("http://localhost:" + testPort, 5000);

        try {
            server = new Server(testPort);
            HttpRouteLoader.loadRoutes("Sinking.http.routes", server);
            server.run();
            System.out.printf("Test server running at %d...%n", testPort);
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }
    }

    @Override
    protected Class<?> filterClasses(String line) {
        return defaultClassFilter(line);
    }


    private String colorize(String color, String text) {
        return color + text + "\033[0m";
    }
}
