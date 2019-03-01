import cityplan.splitByBlank
import java.io.File


fun main(args: Array<String>) {

    listOf(
//        "a_example", "c_memorable_moments", "b_lovely_landscapes",
        "d_pet_pictures",
        "e_shiny_selfies"
    ).forEach { processFile(it) }
}

data class Photo(val id: Int, val type: Char, val tags: Array<String>)

private fun processFile(currentFile: String) {
    println(currentFile)

    val photoMap = HashMap<String, MutableList<Photo>>()

    File("src/main/resources/$currentFile.txt").useLines {
        val inputList = it.toList()

        // index 0 = number photos

        // index 1 - n = photos
        inputList.forEachIndexed { index, line ->
            if (index == 0) return@forEachIndexed

            val tags = line.splitByBlank().filterIndexed { id, s -> id > 1 }

            tags.forEach { t ->
                photoMap.putIfAbsent(t, mutableListOf())
                photoMap[t]!!.add(Photo(index-1, line.first(), tags.toTypedArray()))
            }
        }

        /******************* SLIDE SHOW CREATION ************************************/
        val sortedMap = photoMap.toList().sortedByDescending { (_, v) -> v.size }.toMap()

        val usedIds = mutableListOf<Int>()
        var idx = 0


        val slideMap = HashMap<String, MutableList<Slide>>()

        sortedMap.forEach { t, pics ->
            val parts = pics
                .filterNot { p -> usedIds.contains(p.id) }
                .partition { p -> p.type == 'H' }

            parts.first.forEach { hPic ->
                val picSlide = Slide(idx++, listOf(hPic.id), hPic.tags.toSet())
//                slideShow.add(picSlide)
                usedIds.add(hPic.id)
                picSlide.tags.forEach { t ->
                    slideMap.putIfAbsent(t, mutableListOf())
                    slideMap[t]!!.add(picSlide)
                }
            }    // H
            parts.second.chunked(2).forEach { vChunk ->
                if (vChunk.size == 2) {
                    val chunkSlide = Slide(idx++, listOf(vChunk[0].id, vChunk[1].id), vChunk[0].tags.toSet() + vChunk[1].tags.toSet())
//                    slideShow.add(chunkSlide)
                    usedIds.add(vChunk[0].id)
                    usedIds.add(vChunk[1].id)
                    chunkSlide.tags.forEach { t ->
                        slideMap.putIfAbsent(t, mutableListOf())
                        slideMap[t]!!.add(chunkSlide)
                    }
                }
            } // V
            println(idx)
        }



        /********************** OUTPUT GENERATION ***********************************/
        File("src/main/resources/$currentFile.out.txt").delete()
        File("src/main/resources/$currentFile.out.txt").appendWithNewLine("$idx")
        val sortedSlides = slideMap.toList().sortedByDescending { (_, v) -> v.size }.toMap()
        val usedSlides = mutableListOf<Int>()
        sortedSlides.forEach { t, slides ->
            slides
                .filterNot { s -> usedSlides.contains(s.id) }
                .forEach { slide ->
                    usedSlides.add(slide.id)
                    if (slide.photoIds.size == 2) {
                        File("src/main/resources/$currentFile.out.txt").appendWithNewLine("${slide.photoIds[0]} ${slide.photoIds[1]}")
                        println(--idx)
                    } else {
                        File("src/main/resources/$currentFile.out.txt").appendWithNewLine("${slide.photoIds[0]}")
                        println(--idx)
                    }
            }
        }
    }
}

data class HorizontalPhoto(val id: Int, val tags: List<Int>)

data class VerticalPhoto(val id: Int, val tags: List<Int>)

data class Slide(val id: Int, val photoIds: List<Int>, val tags: Set<String> = emptySet())

fun File.appendWithNewLine(text: String) = this.appendText("$text\n")