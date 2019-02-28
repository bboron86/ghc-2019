import cityplan.CityBuilder
import cityplan.splitByBlank
import java.io.File

private const val FILE = "src/main/resources/a_example"


fun main(args: Array<String>) {

    listOf(
        "a_example",
//        "b_lovely_landscapes",
//        "d_pet_pictures", "e_shiny_selfies",
        "c_memorable_moments"
    ).forEach { processFile(it) }
}

private fun processFile(currentFile: String) {
    println(currentFile)
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


        val vPhotosPairs: MutableList<Pair<VerticalPhoto, VerticalPhoto>> = mutableListOf()
        val usedIds = mutableListOf<Int>()
        vPhotos.forEach v1@{ vPhoto ->
            if (usedIds.contains(vPhoto.id)) return@v1

            val p1 = vPhoto
            var p2BestSize = 0
            var p2: VerticalPhoto? = null

            vPhotos
                .filter { v -> v.id != vPhoto.id }
                .forEach v2@{ v2Photo ->
                    if (usedIds.contains(v2Photo.id)) return@v2

                    val tmpSet = p1.tags.toSet() + v2Photo.tags.toSet()
                    if (tmpSet.size > p2BestSize) {
                        p2BestSize = tmpSet.size
                        p2 = v2Photo
                    }
                }

            if (p2 != null) {
                usedIds += p1.id
                usedIds += p2!!.id
                vPhotosPairs += Pair(p1, p2!!)
//                println(p2BestSize)
            }
        }

        vPhotosPairs.forEach { pair ->
            slideShow.add(Slide(listOf(pair.first.id, pair.second.id), pair.first.tags.toSet() + pair.second.tags.toSet()))
        }

        println(slideShow)


        /********************** OUTPUT GENERATION ***********************************/
        File("src/main/resources/$currentFile.out.txt").delete()
        File("src/main/resources/$currentFile.out.txt").appendWithNewLine(slideShow.size.toString())
        slideShow.sortByDescending { s -> s.tags.size }
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