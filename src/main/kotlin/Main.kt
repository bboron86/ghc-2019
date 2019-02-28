import cityplan.CityBuilder
import cityplan.splitByBlank
import java.io.File

private const val FILE = "src/main/resources/a_example"


fun main(args: Array<String>) {

    listOf(
        "a_example",
        "c_memorable_moments"
    ).forEach { processFile(it) }
}

private fun processFile(currentFile: String) {
    File("src/main/resources/$currentFile.txt").useLines {
        val inputList = it.toList()

        // index 0 = number photos

        // index 1 - n = photos
        val hPhotos = mutableListOf<HorizontalPhoto>()
        val vPhotos = mutableListOf<VerticalPhoto>()

        inputList.forEachIndexed { index, line ->
            if (index == 0) return@forEachIndexed

            val tags = line.splitByBlank().filterIndexed { id, s -> id > 1 }

            if (line.startsWith('H')) {
                hPhotos.add(HorizontalPhoto(index - 1, tags))
            } else if (line.startsWith('V')) {
                vPhotos.add(VerticalPhoto(index - 1, tags))
            }
        }

        println(hPhotos)
        println(vPhotos)

        /******************* SLIDE SHOW CREATION ************************************/
        val slideShow = mutableListOf<Slide>()
        hPhotos.forEach { hPhoto ->
            slideShow.add(Slide(listOf(hPhoto.id), hPhoto.tags.toSet()))
        }

        vPhotos.chunked(2).forEach { chunk ->
            if (chunk.size == 2) {
                slideShow.add(Slide(listOf(chunk[0].id, chunk[1].id), chunk[0].tags.toSet() + chunk[1].tags.toSet()))
            }
        }

        println(slideShow)


        /********************** OUTPUT GENERATION ***********************************/
        File("src/main/resources/$currentFile.out.txt").delete()
        File("src/main/resources/$currentFile.out.txt").appendWithNewLine(slideShow.size.toString())
        slideShow.forEach { slide ->
            if (slide.photoIds.size == 2)
                File("src/main/resources/$currentFile.out.txt").appendWithNewLine("${slide.photoIds[0]} ${slide.photoIds[1]}")
            else
                File("src/main/resources/$currentFile.out.txt").appendWithNewLine("${slide.photoIds[0]}")
        }
    }
}

data class HorizontalPhoto(val id: Int, val tags: List<String>)

data class VerticalPhoto(val id: Int, val tags: List<String>)

data class Slide(val photoIds: List<Int>, val tags: Set<String>)

fun File.appendWithNewLine(text: String) = this.appendText("$text\n")