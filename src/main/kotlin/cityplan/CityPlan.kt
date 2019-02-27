import cityplan.ResidentialBuilding
import cityplan.UtilityBuilding

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
        forEachCell({r,c ->
            if (buildIfPossible(c, r, building.numberCols, building.numberRows, building.buildingMap, projectNr)) {
                projectNr++
                return@forEachCell
            }
        }, 0, numberRows, 0, numberCols)
    }

    fun addUtilityBuilding(building: UtilityBuilding) {
        forEachCell({ r,c ->
            if (buildIfPossible(c, r, building.numberCols, building.numberRows, building.buildingMap, projectNr)) {
                projectNr++
                return@forEachCell
            }
        }, 0, numberRows, 0, numberCols)
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