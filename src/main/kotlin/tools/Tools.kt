package tools

import enum.WordTypeEnum
import enum.getBoundaries

/**
 * 判断当前字符类型
 * @param isLetterOrNumber 字母或数字
 * @param isSpaceOrSymbols 空格或符号
 */
fun Char.type(
    isLetterOrNumber: (Char) -> Unit,
    isSpaceOrSymbols: (Char) -> Unit
) {
    if (this.isLetterOrDigit()) {
        isLetterOrNumber(this)
    } else if (this.isWhitespace() || !this.isLetterOrDigit()) {
        isSpaceOrSymbols(this)
    }
}

/**
 * 判断当前字符是否是界符
 */
fun Char.isBoundary(): Boolean {
    val list = getBoundaries()
    list.forEach {

    }
}


/**
 * 单词种别码枚举类匹配筛选
 * @param check 筛选条件
 */
fun List<WordTypeEnum>.match(
    check: (WordTypeEnum) -> Boolean
): List<WordTypeEnum> {
    val resultList = ArrayList<WordTypeEnum>()
    this.forEach {
        if (check(it)) {
            resultList.add(it)
        }
    }
    return resultList
}