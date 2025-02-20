import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todolistapp.common.UiState
import com.example.todolistapp.domain.usecase.AddTodoUseCase
import com.example.todolistapp.presentation.viewmodel.AddTodoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTodoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddTodoViewModel
    private val addTodoUseCase: AddTodoUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddTodoViewModel(addTodoUseCase)
    }

    @Test
    fun `addTodo should update state to Success on successful add`() = runTest {
        coEvery { addTodoUseCase(any()) } returns Unit

        viewModel.addTodo("New Task")

        testDispatcher.scheduler.advanceUntilIdle() // Ensures coroutine completes

        assertEquals(UiState.Success, viewModel.state.value)
    }

    @Test
    fun `addTodo should update state to Error when add fails`() = runTest {
        coEvery { addTodoUseCase(any()) } throws RuntimeException("Failed")

        viewModel.addTodo("New Task")

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state.value is UiState.Error)
    }
}
