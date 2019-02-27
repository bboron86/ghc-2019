import kotlin.random.Random

fun forEachCell(
    invoke: (row: Int, col: Int) -> Unit,
    rowStartIdxIncl: Int,
    rowEndIdxExcl: Int,
    colStartIdxIncl: Int,
    colEndIdxExcl: Int,
    step: Int = 1,
    randomOffset: Int = 1
) {
    for (r in Random.nextInt(rowStartIdxIncl,rowStartIdxIncl + randomOffset) until rowEndIdxExcl step Random.nextInt(step,step + randomOffset)) {
        for (c in Random.nextInt(colStartIdxIncl,colStartIdxIncl + randomOffset) until colEndIdxExcl step Random.nextInt(step,step + randomOffset)) {
            invoke(r, c)
        }
    }
}