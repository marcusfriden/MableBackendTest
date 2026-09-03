package core.csv

import java.io.BufferedReader
import java.io.Reader

/**
 * CSV reader
 *
 * This CSV reader has been written with composability in mind. Both the source function and the lineParser can be
 * composed as necessary. The source function used in this project just returns a FileReader for a local file, but it
 * could just as well be replaced with a function that connects to an S3 bucket and returns an InputStreamReader for a file there.
 * In the tests you will notice that a StringReader has been used to test this class.
 *
 * @property source A function returning a Reader object for reading the content of the source file.
 * @property lineParser A CSV line parser of desired type.
 */
class CsvReader<T>(private val source: () -> Reader, private val lineParser: CsvLineParser<T>) {

    /**
     * Reads from the source file and returns a List of objects based on the provided parser.
     */
    fun readAll(): List<T> =
        BufferedReader(source()).use { reader ->
            reader.lineSequence()
                .filter { it.isNotBlank() }
                .map { lineParser.parse(it) }
                .toList()
        }
}
