package tools

import enum.WordTypeEnum
import enum.getBoundaries
import enum.getKeys
import enum.getOperators

/**
 * 判断当前字符类型
 * @param isLetterOrNumber 字母或数字
 * @param isSpaces 空格
 * @param isSymbols 字符
 */
fun Char.type(
    isLetterOrNumber: (Char) -> Unit,
    isSpaces: (Char) -> Unit,
    isSymbols: (Char) -> Unit
) {
    if (this.isLetterOrDigit()) {
        isLetterOrNumber(this)
    } else if (this.isWhitespace()) {
        isSpaces(this)
    } else {
        isSymbols(this)
    }
}

/**
 * 判断当前字符串是否是界符
 */
fun String.isBoundary(): Boolean {
    val list = getBoundaries().match { it.wordType.word == this }
    return list.isNotEmpty()
}

/**
 * 判断当前字符串是否是操作符
 */
fun String.isOperator(): Boolean {
    val list = getOperators().match { it.wordType.word == this }
    return list.isNotEmpty()
}

/**
 * 判断当前字符串是否是关键字
 */
fun String.isKey(): Boolean {
    val list = getKeys().match { it.wordType.word == this }
    return list.isNotEmpty()
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