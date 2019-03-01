import cityplan.splitByBlank
import java.io.File
import java.util.Comparator


fun main(args: Array<String>) {

    listOf(
//        "a_example", "c_memorable_moments", "b_lovely_landscapes"
//        "d_pet_pictures"
        "e_shiny_selfies"
    ).forEach { processFile(it) }
}

data class Photo(val id: Int, val type: Char)

private fun processFile(currentFile: String) {
    println(currentFile)

    val photoMap = HashMap<String, MutableList<Photo>>()

    File("src/main/resources/$currentFile.txt").useLines {
        val inputList = it.toList()

        // index 0 = number photos

        // index 1 - n = photos
        val hPhotos = mutableListOf<HorizontalPhoto>()
        val vPhotos = mutableListOf<VerticalPhoto>()

        inputList.forEachIndexed { index, line ->
            if (index == 0) return@forEachIndexed

            val tags = line.splitByBlank().filterIndexed { id, s -> id > 1 }

            tags.forEach { t ->
                photoMap.putIfAbsent(t, mutableListOf())
                photoMap[t]!!.add(Photo(index-1, line.first()))
            }
        }

//        println(photoMap)

        /******************* SLIDE SHOW CREATION ************************************/
        val slideShow = mutableListOf<Slide>()

        val sortedMap = photoMap.toList().sortedByDescending { (_, v) -> v.size }.toMap()

        val usedIds = mutableListOf<Int>()
        var idx = 0

        sortedMap.forEach { t, pics ->
            val parts = pics
                .filterNot { p -> usedIds.contains(p.id) }
                .partition { p -> p.type == 'H' }

            parts.first.forEach { hPic ->
                slideShow.add(Slide(idx++, listOf(hPic.id)))
                usedIds.add(hPic.id)
            }    // H
            parts.second.chunked(2).forEach { vChunk ->
                if (vChunk.size == 2) {
                    slideShow.add(Slide(idx++, listOf(vChunk[0].id, vChunk[1].id)))
                    usedIds.add(vChunk[0].id)
                    usedIds.add(vChunk[1].id)
                }
            } // V
            println(slideShow.size)
        }

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

data class HorizontalPhoto(val id: Int, val tags: List<Int>)

data class VerticalPhoto(val id: Int, val tags: List<Int>)

data class Slide(val id: Int, val photoIds: List<Int>, val tags: Set<Int> = emptySet())

fun File.appendWithNewLine(text: String) = this.appendText("$text\n")