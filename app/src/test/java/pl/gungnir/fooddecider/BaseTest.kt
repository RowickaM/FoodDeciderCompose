package pl.gungnir.fooddecider

import org.junit.After
import org.junit.Before
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.OngoingStubbing

abstract class BaseTest {

    companion object {

        const val MOCK_STRING = "MOCK"
    }

    @Before
    open fun setup() = MockitoAnnotations.initMocks(this)

    @After
    open fun tearDown() = Unit

    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    protected fun <T> any(c: Class<T>): T {
        Mockito.any<T>()
        return uninitialized()
    }

    protected fun <T> ArgumentCaptor<T>.kCapture(): T {
        capture()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    protected fun <T> whenever(methodCall: T): OngoingStubbing<T> =
        Mockito.`when`(methodCall)
}