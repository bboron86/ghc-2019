import java.io.File
import kotlin.random.Random

lateinit var cityPlan: CityPlan
var residentialBuildings: List<ResidentialBuilding> = ArrayList()
var utilityBuildings: List<UtilityBuilding> = ArrayList()


fun main(args: Array<String>) {
    readInputFile()

    cityPlan.addResidentialBuilding(residentialBuildings[0])
    cityPlan.addUtilityBuilding(utilityBuildings[Random.nextInt(2)])
    cityPlan.addResidentialBuilding(residentialBuildings[0])
    cityPlan.addUtilityBuilding(utilityBuildings[Random.nextInt(2)])
    cityPlan.addResidentialBuilding(residentialBuildings[0])

    cityPlan.print()
}



data class CityPlan(
    val numberRows: Int,
    val numberCols: Int,
    val maxWalkingDist: Int
) {
    private var cityMap: Array<CharArray> = arrayOf()
    var projectNr = 1

    init {
        for (i in 0 until numberRows)
            cityMap += CharArray(numberCols) {'.'}
    }

    fun print() {
        for (r in 0 until numberRows) {
            println(cityMap[r])
        }
    }

    fun addResidentialBuilding(building: ResidentialBuilding) {
        for (r in Random.nextInt(0,4) until numberRows step Random.nextInt(1,5)) {
            for (c in Random.nextInt(0,4) until numberCols step Random.nextInt(1,5)) {
                if (buildIfPossible(c, r, building.numberCols, building.numberRows, building.buildingMap, projectNr)) {
                    projectNr++
                    return
                }
            }
        }
    }

    fun addUtilityBuilding(building: UtilityBuilding) {
        for (r in 0 until numberRows) {
            for (c in 0 until numberCols) {
                if (buildIfPossible(c, r, building.numberCols, building.numberRows, building.buildingMap, projectNr)) {
                    projectNr++
                    return
                }
            }
        }
    }

    private fun buildIfPossible(startColumn: Int, startRow: Int, numCols: Int, numRows: Int, buildingMap: Array<CharArray>, projectNumber: Int): Boolean {
        // check out of rang
        if ((startColumn + numCols > this.numberCols) ||
            (startRow + numRows > this.numberRows))
            return false

        var x = 0
        var y: Int
        val mapCopy = cityMap.map { it.clone() }.toTypedArray()
        for (r in startRow until (startRow + numRows)) {
            y = 0
            for (c in startColumn until (startColumn + numCols)) {
                if (mapCopy[r][c] != '.') return false
                else mapCopy[r][c] = '.'.takeIf { buildingMap[x][y] == '.' } ?: projectNumber.toString().first()
                y++
            }
            x++
        }
        cityMap = mapCopy
        return true
    }
}

data class ResidentialBuilding(
    val number: Int,
    val numberRows: Int,
    val numberCols: Int,
    val capacity: Int,
    val buildingMap: Array<CharArray>
)

data class UtilityBuilding(
    val number: Int,
    val numberRows: Int,
    val numberCols: Int,
    val type: String,
    val buildingMap: Array<CharArray>
)