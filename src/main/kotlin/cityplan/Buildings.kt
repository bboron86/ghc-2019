package cityplan

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