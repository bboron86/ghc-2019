import java.io.File

private const val RESOURCE_ROOT = "src/main/resources"

fun readInputFile() {
    File("$RESOURCE_ROOT/example.in").useLines {
        val inputList = it.toList()

        // index 0 = city plan
        val firstLineParts = inputList[0].splitByBlank()
        cityPlan = CityPlan(
            numberRows = firstLineParts[0].toInt(),
            numberCols = firstLineParts[1].toInt(),
            maxWalkingDist = firstLineParts[2].toInt()
        )
        println(cityPlan)

        // find residential buildings
        var projectNumber = 0
        inputList
            .forEachIndexed { index, line ->
                if (line.startsWith("R")) {
                    val rParts = line.splitByBlank()

                    // ext fun
                    val startIndex = index + 1
                    var rMap: Array<CharArray> = arrayOf()
                    for (i in startIndex until (startIndex + rParts[1].toInt())) {
                        rMap = rMap.plus(inputList[i].toCharArray())
                    }

                    val r = ResidentialBuilding(
                        projectNumber++,
                        numberRows = rParts[1].toInt(),
                        numberCols = rParts[2].toInt(),
                        capacity = rParts[3].toInt(),
                        buildingMap = rMap
                    )
                    println(r)
                    residentialBuildings = residentialBuildings.plus(r)

                } else if (line.startsWith("U")) {
                    val uParts = line.splitByBlank()

                    // ext fun
                    val startIndex = index + 1
                    var uMap: Array<CharArray> = arrayOf()
                    for (i in startIndex until (startIndex + uParts[1].toInt())) {
                        uMap = uMap.plus(inputList[i].toCharArray())
                    }

                    val u = UtilityBuilding(
                        projectNumber++,
                        numberRows = uParts[1].toInt(),
                        numberCols = uParts[2].toInt(),
                        type = uParts[3],
                        buildingMap = uMap
                    )
                    println(u)
                    utilityBuildings = utilityBuildings.plus(u)
                }
            }
    }
}

fun String.splitByBlank() = this.split(" ")