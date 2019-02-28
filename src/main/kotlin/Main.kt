import cityplan.CityBuilder
import java.io.File

private const val FILE = "src/main/resources/b_lovely_landscapes"


fun main(args: Array<String>) {
//    CityBuilder().build()

    File("$FILE.txt").useLines {
        val inputList = it.toList()

        // index 0 = number photos

        // index 1 - n = photos
        val hPhotos = mutableListOf<HorizontalPhoto>()
        val vPhotos = mutableListOf<VerticalPhoto>()

        inputList.forEachIndexed { index, line ->
            if (line.startsWith('H')) {
                hPhotos.add(HorizontalPhoto(index - 1))
            } else if (line.startsWith('V')) {
                vPhotos.add(VerticalPhoto(index - 1))
            }
        }

        val slideShow = mutableListOf<Slide>()
        hPhotos.forEach { hPhoto -> slideShow.add(Slide(listOf(hPhoto.id))) }
        vPhotos.chunked(2).forEach { chunk ->
            if (chunk.size == 2) {
                slideShow.add(Slide(listOf(chunk[0].id, chunk[1].id)))
            }
        }

//        println(hPhotos)
//        println(vPhotos)
        println(slideShow)

//        File("$FILE.out.txt").createNewFile()
//        val outPutFile = File("$FILE.out.txt").writer()
//        outPutFile.write(slideShow.size)
//        outPutFile.flush()
        File("$FILE.out.txt").delete()
        File("$FILE.out.txt").appendWithNewLine(slideShow.size.toString())
        slideShow.forEach { slide ->
            if (slide.photos.size == 2)
                File("$FILE.out.txt").appendWithNewLine("${slide.photos[0]} ${slide.photos[1]}")
            else
                File("$FILE.out.txt").appendWithNewLine("${slide.photos[0]}")
        }
    }
}

data class HorizontalPhoto(val id: Int)

data class VerticalPhoto(val id: Int)

data class Slide(val photos: List<Int>)

fun File.appendWithNewLine(text: String) = this.appendText("$text\n")