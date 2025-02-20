import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todolistapp.domain.model.TodoItem
import com.example.todolistapp.domain.usecase.GetTodosUseCase
import com.example.todolistapp.presentation.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val getTodosUseCase: GetTodosUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val fakeTodos = listOf(
            TodoItem(1, "Buy milk"),
            TodoItem(2, "Call mom"),
            TodoItem(3, "Go for a run")
        )

        val todoFlow = MutableStateFlow(fakeTodos) // Properly emit data

        coEvery { getTodosUseCase() } returns todoFlow.asStateFlow()

        viewModel = MainViewModel(getTodosUseCase)

        // Wait for coroutines to execute
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `fetchTodos should update todos list successfully`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val todos = viewModel.filteredTodos.value
        println("Test received Todos: $todos")

        assertEquals(3, todos.size)
        assertEquals("Buy milk", todos[0].text)
    }

    @Test
    fun `onSearchQueryChanged should filter todos based on query`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChanged("Call")
        testDispatcher.scheduler.advanceTimeBy(2000) // Simulate debounce delay
        testDispatcher.scheduler.advanceUntilIdle()

        val filteredTodos = viewModel.filteredTodos.value
        assertEquals(1, filteredTodos.size)
        assertEquals("Call mom", filteredTodos[0].text)
    }

    @Test
    fun `onSearchQueryChanged with empty query should return all todos`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSearchQueryChanged("")
        testDispatcher.scheduler.advanceTimeBy(2000)
        testDispatcher.scheduler.advanceUntilIdle()

        val allTodos = viewModel.filteredTodos.value
        assertEquals(3, allTodos.size)
    }
}
