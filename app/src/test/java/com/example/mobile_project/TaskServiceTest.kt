package com.example.mobile_project
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import com.example.todolist.TASKDAO
import com.example.todolist.TASK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull

class TaskServiceTest {
    @Mock
    private lateinit var mockTaskDAO: TASKDAO
    private lateinit var taskService: TaskService

    @Before
    fun setUp() {
        // Initialize all @Mock annotations
        MockitoAnnotations.openMocks(this)
        //Please create an instance of TaskService, and
        //automatically inject the mocks into its constructor
        taskService = TaskService(mockTaskDAO)
    }

    @After
    fun tearDown() {
        // Cleanup after each test
    }
    @Test
    fun `test AddTask`() = runTest  {
        val title = "Task 1"
        val description = "Description 1"


        taskService.addTask(title,description)
        //any():
        //I don't care what exact value or object was passed to this method;
        // just verify that the method was called with some argument of the correct type.
        verify(mockTaskDAO).addTask(any())
    }

    @Test
    fun `test GetAllTasks returns the corrected tasks`() = runTest {

        val expectedTasks = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1"),
            TASK(id = 2, title = "Task 2", description = "Description 2")
        )
        //Defines what the mock should return when a method is called
        whenever(mockTaskDAO.getAllTasks()).thenReturn(expectedTasks)
        //true return value
        val result = taskService.getAllTasks()

        assertEquals(2, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Task 2", result[1].title)

        assertEquals("Description 1", result[0].description)
        assertEquals("Description 2", result[1].description)
    }

    @Test
    fun `test GetAllTasks Returns Empty`() = runTest {

        whenever(mockTaskDAO.getAllTasks()).thenReturn(emptyList())
        val result = taskService.getAllTasks()

        assertTrue(result.isEmpty())
        assertEquals(0, result.size)
    }

    @Test
    fun `test getTasksByStatus`() = runTest {
        val list = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true),
            TASK(id = 2, title = "Task 2", description = "Description 2", isCompleted = false)
        )
        val expected = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true)
        )
        whenever(mockTaskDAO.getTasksByStatus(true)).thenReturn(expected)
        val result = taskService.getTasksByStatus(true)

        assertTrue(result.all { it.isCompleted })
        verify(mockTaskDAO).getTasksByStatus(true)
    }

    @Test
    fun `test getTasksById true`() = runTest {
        val list = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true),
            TASK(id = 2, title = "Task 2", description = "Description 2", isCompleted = false)
        )
        val expected = (
                TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true)
                )
        whenever(mockTaskDAO.getTaskById(1)).thenReturn(expected)
        val result = taskService.getTaskById(1)

        assertEquals(expected, result)
        verify(mockTaskDAO).getTaskById(1)

    }

    @Test
    fun `test GetTaskById NotFound`() = runTest {
        whenever(mockTaskDAO.getTaskById(999)).thenReturn(null)

        val result = taskService.getTaskById(999)
        assertNull(result)
    }

    @Test
    fun `test getTotalTasksCount correct`() = runTest {

        val expectedTasks = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1"),
            TASK(id = 2, title = "Task 2", description = "Description 2")
        )
        whenever(mockTaskDAO.getTotalTasksCount()).thenReturn(expectedTasks.size)
        val result = taskService.getTotalTasksCount()

        assertEquals(2, result)
    }

    @Test
    fun `test getCompletedTasksCount true`() = runTest {
        val list = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true),
            TASK(id = 2, title = "Task 2", description = "Description 2", isCompleted = false),
            TASK(id = 3, title = "Task 3", description = "Description 3", isCompleted = true),
        )
        val expected = listOf(
            TASK(id = 1, title = "Task 1", description = "Description 1", isCompleted = true),
            TASK(id = 3, title = "Task 3", description = "Description 3", isCompleted = true),
        )
        whenever(mockTaskDAO.getCompletedTasksCount()).thenReturn(expected.size)
        val result = taskService.getCompletedTasksCount()

        assertEquals(result, expected.size)
    }

    @Test
    fun `test UpdateTaskSuccess`() = runTest {
        val id = 1
        val title = "Updated Task"
        val description = "Updated Description"
        val isCompleted = true
        val result =taskService.updateTask(id,title,description,isCompleted)
        assertTrue(result)
        verify(mockTaskDAO).updateTask(any())
    }

    @Test
    fun `test DeleteTaskSuccess`() = runTest {
        val taskToDelete = TASK(id = 1, title = "Task to Delete")

        taskService.deleteTask(taskToDelete)
        verify(mockTaskDAO).deleteTask(taskToDelete)
    }


    @Test
    fun `test GetTotalTasksCount`() = runTest {
        whenever(mockTaskDAO.getTotalTasksCount()).thenReturn(10)

        val count = taskService.getTotalTasksCount()

        assertEquals(10, count)
        assertTrue(count > 0)
    }

}