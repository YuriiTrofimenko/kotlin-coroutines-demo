import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.tyaa.kotlin.coroutines.profileCoroutines2

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    @Test
    fun workerTestCase() = runBlocking<Unit> () {
        var result = 0
        GlobalScope.launch {
            result = profileCoroutines2().await()
        }.join()
        println(result)
        assert(result == 705082704)
        // assert(result == 0)
    }
}