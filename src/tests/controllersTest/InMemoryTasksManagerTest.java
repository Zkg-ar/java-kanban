package tests.controllersTest;


import controllers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach


    public void beforeEach() {
        manager = new InMemoryTaskManager();

    }

}
