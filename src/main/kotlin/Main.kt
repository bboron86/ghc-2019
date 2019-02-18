import java.io.File

private const val RESOURCE_ROOT = "src/main/resources"

fun main(args: Array<String>) {
    File("$RESOURCE_ROOT/example.in").useLines { println(it.toList()) }
}