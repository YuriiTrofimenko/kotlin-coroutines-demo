package org.tyaa.kotlin.coroutines

import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

// import org.tyaa.kotlin.coroutines.Workers.useWorkerAsync

suspend fun main() {
    /* 1 */
    /*println("Start")
    // Start a coroutine
    delay(1000)
    println("Done")
    Thread.sleep(2000) // wait for 2 seconds
    println("Stop")*/

    /* 2 */
    /* runBlocking {

    } */

    /* 3a */
    /* coroutineScope{
        useWorkerAsync()
        // awaitAll(useWorkerAsync())
    } */

    /* 4 */
    // https://oeis.org/A000045
    /* val fibonacciSeq = sequence {
        var a = 0
        var b = 1

        yield(1)

        while (true) {
            yield(a + b)

            val tmp = a + b
            a = b
            b = tmp
        }
    }

    println(fibonacciSeq.take(50).toList())

    val bulkSeq = sequence {

        yield(0)
        yieldAll(fibonacciSeq)
    }
    println("***")
    println(bulkSeq.take(51).toList()) */

    /* suspend fun SequenceScope<Int>.yieldIfOdd(x: Int) {
        if (x % 2 != 0) yield(x)
    }

    val oddSeq: Sequence<Int> = sequence {
        for (i in 1..20) yieldIfOdd(i)
    }

    oddSeq.forEach { print("$it ") } */

    /* 5a */
    // profileThreads()
    // profileCoroutines ()
    profile("Threads") {
        GlobalScope.launch {
            println(profileThreads2().await())
        }
    }
    profile("Coroutines") {
        GlobalScope.launch {
            println(profileCoroutines2().await())
        }
    }

    delay(3000)

    /* 6 */
    /* GlobalScope.launch { // launch a new coroutine in background and continue
        delay(1000L)
        println("World!")
    }
    println("Hello,") // main thread continues here immediately
    runBlocking {     // but this expression blocks the main thread
        delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
    } */

    /* 7 */
    // uncomment '5b' and use the test folder
}

/* 3b */
/* object Workers {
    private fun workerAsync() = GlobalScope.async {
        println("Working...")
        delay(1000)
        "Done"
    }

    fun useWorkerAsync() = GlobalScope.async {
        println("Start")
        val result = awaitAll(workerAsync())
        println("Stop. Result: $result")
    }
} */

/* 5b */
fun profile (tag: String, function: () -> Unit) {
    val startTime = System.currentTimeMillis()
    function()
    println("$tag: ${(System.currentTimeMillis() - startTime) / 1000}")
}

fun profileThreads () {
    val c = AtomicInteger()
    for (i in 1..1_000_000)
        thread(start = true) {
            c.addAndGet(i)
        }.join()
    println(c.get())
}

fun profileCoroutines () {
    val c = AtomicInteger()

    for (i in 1..1_000_000)
        GlobalScope.launch {
            c.addAndGet(i)
        }

    println(c.get())
}

fun profileThreads2 () = GlobalScope.async {
    val c = AtomicInteger()
    runBlocking {
        for (i in 1..100_000) {
            thread(start = true) {
                c.addAndGet(i)
            }.join()
        }
    }
    c.get()
}

fun profileCoroutines2 () = GlobalScope.async {
    val c = (1..100_000).map { n ->
        GlobalScope.async {
            // delay(1000)
            n
        }
    }
    var sum = 0
    runBlocking {
        sum = c.sumBy { it.await() }
        // println("Sum: $sum")
    }
    sum
}