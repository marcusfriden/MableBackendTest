package core.csv

import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CsvReaderTest {

    private val identityParser = CsvLineParser { it }

    @Test
    fun `reads single line`() {
        val reader = CsvReader({ StringReader("hello") }, identityParser)

        val result = reader.readAll()

        assertEquals(listOf("hello"), result)
    }

    @Test
    fun `reads multiple lines`() {
        val reader = CsvReader({ StringReader("one\ntwo\nthree") }, identityParser)

        val result = reader.readAll()

        assertEquals(listOf("one", "two", "three"), result)
    }

    @Test
    fun `returns empty list for empty input`() {
        val reader = CsvReader({ StringReader("") }, identityParser)

        val result = reader.readAll()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `skips blank lines`() {
        val reader = CsvReader({ StringReader("one\n\ntwo\n") }, identityParser)

        val result = reader.readAll()

        assertEquals(listOf("one", "two"), result)
    }

    @Test
    fun `re-reads source on each call`() {
        var callCount = 0
        val reader = CsvReader({ callCount++; StringReader("call-$callCount") }, identityParser)

        val first = reader.readAll()
        val second = reader.readAll()

        assertEquals(listOf("call-1"), first)
        assertEquals(listOf("call-2"), second)
    }
}