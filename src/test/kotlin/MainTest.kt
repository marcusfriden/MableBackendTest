import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {

    private fun captureOutput(block: () -> Unit): String {
        val out = ByteArrayOutputStream()
        val original = System.out
        System.setOut(PrintStream(out))
        try {
            block()
        } finally {
            System.setOut(original)
        }
        return out.toString().trim()
    }

    private fun createTempFile(content: String): File =
        File.createTempFile("test", ".csv").apply {
            deleteOnExit()
            writeText(content)
        }

    @Test
    fun `prints final balances after processing transactions`() {
        val balancesFile = createTempFile(
            "1111234522226789,5000.00\n1212343433335665,1200.00\n"
        )
        val transactionsFile = createTempFile(
            "1111234522226789,1212343433335665,500.00\n"
        )

        val output = captureOutput {
            main(arrayOf(balancesFile.path, transactionsFile.path))
        }

        assertTrue(output.contains("1111234522226789"))
        assertTrue(output.contains("4500.00"))
        assertTrue(output.contains("1212343433335665"))
        assertTrue(output.contains("1700.00"))
    }

    @Test
    fun `prints failures for insufficient funds`() {
        val balancesFile = createTempFile(
            "1111234522226789,100.00\n1212343433335665,1200.00\n"
        )
        val transactionsFile = createTempFile(
            "1111234522226789,1212343433335665,500.00\n"
        )

        val output = captureOutput {
            main(arrayOf(balancesFile.path, transactionsFile.path))
        }

        assertTrue(output.contains("1111234522226789"))
        assertTrue(output.contains("100.00"))
        assertTrue(output.contains("1212343433335665"))
        assertTrue(output.contains("1200.00"))
        assertTrue(output.contains("InsufficientFunds"))
    }

    @Test
    fun `processes example data from spec correctly`() {
        val balancesFile = createTempFile(
            """
            1111234522226789,5000.00
            1111234522221234,10000.00
            2222123433331212,550.00
            1212343433335665,1200.00
            3212343433335755,50000.00
            """.trimIndent()
        )
        val transactionsFile = createTempFile(
            """
            1111234522226789,1212343433335665,500.00
            3212343433335755,2222123433331212,1000.00
            3212343433335755,1111234522226789,320.50
            1111234522221234,1212343433335665,25.60
            """.trimIndent()
        )

        val output = captureOutput {
            main(arrayOf(balancesFile.path, transactionsFile.path))
        }

        assertTrue(output.contains("4820.50"))
        assertTrue(output.contains("9974.40"))
        assertTrue(output.contains("1550.00"))
        assertTrue(output.contains("1725.60"))
        assertTrue(output.contains("48679.50"))
    }
}
