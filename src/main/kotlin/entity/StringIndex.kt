package entity

/**
 * 用于标识一个字符串在按行分块的字符串的集合中的行列索引
 */
data class StringIndex(val rowIndex: Int = -1, val columnIndex: Int = -1)


/**
 * 用于判断 [StringIndex] 是否行列索引都为 -1
 * @return 如果 [StringIndex] 行列索引均为 -1 则返回 true，反之 false
 */
fun StringIndex.isEmpty(): Boolean {
    return this.rowIndex == -1 && this.columnIndex == -1
}


/**
 * 用于判断 [StringIndex] 是否行列索引都不为 -1
 * @return 如果 [StringIndex] 行列索引均不为 -1 则返回 true，反之 false
 */
fun StringIndex.isNotEmpty(): Boolean {
    return !this.isEmpty()
}