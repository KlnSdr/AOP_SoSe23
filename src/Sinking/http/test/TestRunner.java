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

public class TestRunner extends Classloader<Object> {
    private Client client;
    private Server server;
    private ArrayList<Tripel<Class<?>, String, Method>> tests = new ArrayList<>();
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
        System.out.printf("[%s]", test._2());

        Method actualTest = test._3();
        executedTests++;
        try {
            actualTest.invoke(test._1().getDeclaredConstructor().newInstance(), client, (ITestResult) (result) -> {
                if (result) {
                    passedTests++;
                    System.out.println(" PASSED");
                } else {
                    System.out.println(" FAILED");
                }
                executeTest(index + 1);
            });
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException |
                 InstantiationException e) {
            System.out.println(" FAILED");
            e.printStackTrace();
            executeTest(index + 1);
        }
    }

    private void loadTests() {
        loadClasses().forEach(this::analyzeClassAndMethods);
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
}
