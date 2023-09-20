package exceptions

/**
 * 分析错误异常类
 */
class AnalysisException: Exception {
    /**
     * 默认仅带有错误提示的构造方法
     */
    constructor(errMsg: String): super(errMsg)

    /**
     * 附带按行分割的代码集合以及出错的行列索引，以便输出更精准的错误提示
     * @param errMsg 错误信息
     * @param lines 按行分割的代码集合
     * @param rowIndex 出错的行索引
     * @param colIndex 出错的列索引
     */
    constructor(errMsg: String, lines: List<String>, rowIndex: Int, colIndex: Int):
            super(generateErrMsg(errMsg, lines, rowIndex, colIndex))

    companion object {
        /**
         * 生成错误信息
         * 生成一个 ^ 符号指向代码错误的地方
         * @param errMsg 错误信息
         * @param lines 按行分割的代码集合
         * @param rowIndex 出错的行索引
         * @param colIndex 出错的列索引
         */
        private fun generateErrMsg(errMsg: String, lines: List<String>, rowIndex: Int, colIndex: Int): String {
            val errLine = lines[rowIndex]
            val indicator = StringBuffer("")
            for (i in 0..<colIndex) {
                indicator.append(" ")
            }
            indicator.append("^")
            return "$errMsg（ $rowIndex 行 ）\n$errLine\n$indicator"
        }
    }
}