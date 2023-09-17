package tools

import entity.StringIndex
import entity.WordType
import enum.*
import java.lang.Exception

/**
 * 判断当前字符类型
 * @param isLetterOrNumber 字母或数字
 * @param isSpaces 空格
 * @param isSymbols 字符
 */
inline fun Char.type(
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
 * 判断当前字符串单词类型
 * @return 返回根据类型判断封装好的 WordType 类，如果是空字符就返回 null
 */
fun String.getWordType(): WordType? {
    return when {
        this.isInteger() -> {
            // 整数单词种别码，用来获取整数的编码
            val integerWorkType = WordTypeEnum.INTEGER.wordType
            WordType(integerWorkType.code, this)
        }
        this.isFloat() -> {
            // 实数单词种别码，用来获取实数的编码
            val floatWorkType = WordTypeEnum.REAL_NUMBER.wordType
            WordType(floatWorkType.code, this)
        }
        this.isCharFormat() -> {
            // 字符单词种别码，用来获取字符的编码
            val charWorkType = WordTypeEnum.CHARACTER.wordType
            WordType(charWorkType.code, this.substring(1, 2))
        }
        this.isStringFormat() -> {
            // 字符串单词种别码，用来获取字符串的编码
            val stringWorkType = WordTypeEnum.STRING.wordType
            WordType(stringWorkType.code, this.substring(1, this.lastIndex))
        }
        this == "" -> {
            null
        }
        else -> {
            // 标识符单词种别码，用来获取标识符的编码
            val identifierWorkType = WordTypeEnum.IDENTIFIER.wordType
            WordType(identifierWorkType.code, this)
        }
    }
}

/**
 * 判断当前字符符号类型
 * @param isQuote 是引号（包括单双引号）
 * @param isBoundary 界符
 * @param isOperator 运算符
 * @param isOther 其他字符
 */
fun Char.symbolType(
    isQuote: (Char) -> Unit,
    isBoundary: (Char) -> Unit,
    isOperator: (Char) -> Unit,
    isOther: (Char) -> Unit = {}
) {
    when {
        this == '\'' || this == '"' -> isQuote(this)
        this.toString().isBoundary() -> isBoundary(this)
        this.toString().isOperator() -> isOperator(this)
        else -> isOther(this)
    }
}

/**
 * 判断字符串是否是数字
 * @return 整数返回 true，反之 false
 */
fun String.isInteger(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * 判断字符串是否是实数（Float）
 * @return 整数返回 true，反之 false
 */
fun String.isFloat(): Boolean {
    return try {
        this.toFloat()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * 判断字符串是否是字符格式（不算用整数表示的字符）
 * @return 如果是字符就返回 true，反之 false
 */
fun String.isCharFormat(): Boolean {
    // 此处未来可能添加代码异常，因为单引号中间不应该有多个字符、或者只有一个单引号
    return this.startsWith("'") && this.endsWith("'") && this.length == 3
}

/**
 * 判断字符串是否是字符串格式
 * @return 如果是字符串就返回 true，反之 false
 */
fun String.isStringFormat(): Boolean {
    // 此处未来可能添加代码异常，因为字符串应该有两个双引号
    return this.startsWith("\"") && this.endsWith("\"") && this.length >= 2
}

/**
 * 判断当前字符串是否是界符
 * @return 界符返回 true，反之 false
 */
fun String.isBoundary(): Boolean {
    val list = getBoundaries().match { it.wordType.word == this }
    return list.isNotEmpty()
}

/**
 * 判断当前字符串是否是运算符
 * @return 运算符返回 true，反之 false
 */
fun String.isOperator(): Boolean {
    val list = getOperators().match { it.wordType.word == this }
    return list.isNotEmpty()
}

/**
 * 判断当前字符串是否是关键字
 * @return 关键字返回 true，反之 false
 */
fun String.isKey(): Boolean {
    val list = getKeys().match { it.wordType.word == this }
    return list.isNotEmpty()
}


/**
 * 单词种别码枚举类匹配筛选
 * @param check 筛选条件
 * @return 根据条件筛选出的单词种别码集合
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


/**
 * 清空 StringBuffer 数据
 */
fun StringBuffer.clear() {
    this.setLength(0)
}


/**
 * 检索一个字符串在按行分割的集合中的行列索引
 * @param str 要检索的文字
 * @param startRowIndex 开始检索的行索引
 * @return 找到文字返回包含行和列索引的 [StringIndex] 对象，否则返回行列均为 -1 的 [StringIndex] 对象
 */
fun List<String>.indexOf(
    str: String,
    startRowIndex: Int
): StringIndex {
    for (lineIndex in startRowIndex..< this.lastIndex) {
        val line = this[lineIndex]
        val columnIndex = line.indexOf(str)
        if (columnIndex != -1) {
            return StringIndex(lineIndex, columnIndex)
        }
    }
    return StringIndex()
}


