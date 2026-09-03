import java.io.BufferedReader
import java.io.Reader

class CsvReader<T>(private val source: () -> Reader, private val lineParser: CsvLineParser<T>) {

    fun readAll(): List<T> =
        BufferedReader(source()).use { reader ->
            reader.lineSequence()
                .filter { it.isNotBlank() }
                .map { lineParser.parse(it) }
                .toList()
        }
}
