
import entity.StringIndex
import entity.WordType
import entity.isNotEmpty
import enum.WordTypeEnum
import enum.getBoundaries
import enum.getOperators
import enum.workTypeEnumList
import tools.*
import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    val mArgs = arrayOf("C:\\Users\\16965\\Desktop\\test02.c")
    // 首先判断是否传入参数
    if (mArgs.isEmpty()) {
        println("[.c 文件路径]：对 C 进行词法分析")
        return
    }

    // 参数是否错误
    if (mArgs.size > 1) {
        "参数长度错误".error()
        return
    }

    // File 对象
    val file: File

    // 验证文件是否存在
    try {
        file = File(mArgs[0])
        if (!file.exists() || !file.isFile) {
            // 文件不存在
            "文件不存在，请检查文件路径".error()
            return
        }

        if (file.extension != "c") {
            "仅支持对 C 语言代码进行词法分析".error()
            return
        }
    } catch (e: Exception) {
        println(e.message)
        return
    }

    // 将代码按行读入存储
    val lines = file.readLines()
    // 解析单词
    match(lines)
}

/**
 * 解析单词
 * @param lines 按行分割的集合
 */
fun match(lines: List<String>) {
    // 单词解析的结果
    val resultWordType = ArrayList<WordType>()

    // 单词种别码枚举类匹配 List，用于不停筛选得到最终的单词种别集合
    var wordTypeEnums = workTypeEnumList

    // 字符串缓冲区，用于比对单词
    val buffer = StringBuffer("")

    // 标记斜杠星注释的结束符索引，
    // 如果行列不为 -1 即当前遇到了斜杠星注释符号，
    // 需要跳到结束符位置后继续执行词汇分析
    var slashStarTailIndex = StringIndex()

    // 标记当前是否遇到双斜杠注释
    // 当该变量为 true 时，跳过当前行
    var meetDoubleSlashNote = false


    // 按行遍历
    for (lineIndex in lines.indices) {
        val line = lines[lineIndex]

        if (slashStarTailIndex.isNotEmpty() && lineIndex < slashStarTailIndex.rowIndex) {
            // 斜杠星注释符结束符索引不为空，跳到结束符所在行后继续执行
            continue
        }

        // 按字符遍历
        for (charIndex in line.indices) {
            val char = line[charIndex]

            if (meetDoubleSlashNote) {
                // 遇到双斜杠注释，跳过当前行剩下的词法分析
                meetDoubleSlashNote = false
                break
            }

            // 斜杠星注释符结束符索引不为空，跳到结束符后继续执行
            if (slashStarTailIndex.isNotEmpty()) {
                if (slashStarTailIndex.columnIndex + 1 == line.lastIndex) {
                    // 斜杠星注释结束符是当前行最后的字符，直接跳出当前行的分析
                    // 清除斜杠星注释结束符索引
                    slashStarTailIndex = StringIndex()
                    break
                }

                // 一直检索字符，直到检索到斜杠星注释结束符后一个字符
                if (charIndex != slashStarTailIndex.columnIndex + 2) {
                    continue
                } else {
                    // 当前位置已经到达斜杠星注释结束符后一个字符，清空斜杠星注释结束符索引
                    slashStarTailIndex = StringIndex()
                }
            }

            // 判断当前字符类型
            char.type(
                isLetterOrNumber = {
                    // 将当前字符添加到缓冲区
                    buffer.append(char)
                    // 筛选出以缓冲区开头的单词种别码
                    wordTypeEnums = wordTypeEnums.match {
                        it.wordType.word.startsWith(buffer)
                    }
                },
                isSpaces = {
                    if (buffer.count { it == '"' } == 1) {
                        // 缓冲区现在有一个双引号，所以当前的空格可能是字符串中的空格，在缓冲区加入一个空格并跳过
                        buffer.append(" ")
                        return@type
                    }
                    // 当前字符是空格，先判断单词种别码匹配 List 是否为空
                    if (wordTypeEnums.isNotEmpty() &&
                        wordTypeEnums.size == 1 &&
                        wordTypeEnums[0].wordType.word == buffer.toString()) {
                        // List 不为空，且有唯一数据，且等于缓冲区数据，写入结果集
                        resultWordType.add(wordTypeEnums[0].wordType)
                    } else {
                        // List 为空，或匹配长度大于 1，或数据与缓冲区不一致，直接判断缓冲区内容
                        buffer.toString().getWordType()?.let {
                            resultWordType.add(it)
                        }
                    }
                    // 清空缓冲区
                    buffer.clear()
                    // 刷新单词种别码枚举类匹配 List
                    wordTypeEnums = workTypeEnumList
                },
                isSymbols = { symbol ->
                    // 如果是单双引号也将符号加入到缓冲区，用于后续对字符和字符串进行判断
                    // 前提是缓冲区当前只有一个单双引号，如果有两个将认为字符（串）结束
                    symbol.symbolType(
                        isQuote = { quote ->
                            // 缓冲区里单/双引号数量
                            val quoteCount = buffer.count { it == quote }
                            // 缓冲区中单/双引号数量小于 2
                            if (quoteCount < 2) {
                                // 将当前字符添加到缓冲区
                                buffer.append(char)

                                if (quoteCount == 1) {
                                    // 单/双引号本来只有一个，上面加了一个，现在有两个，构成一个字符/字符串
                                    // 将字符加入结果集
                                    buffer.toString().getWordType()?.let {
                                        resultWordType.add(it)
                                    }
                                    buffer.clear()
                                }
                            }
                        },
                        isBoundary = { boundary ->
                            if (buffer.contains("\"") || buffer.contains("'")) {
                                // 如果缓冲区中有存在单双引号，证明当前的界符可能是字符串形式
                                // 直接将当前界符加入缓冲区，不做其他处理
                                buffer.append(boundary)
                                return@symbolType
                            }


                            // 在遇到界符符后先将缓冲区中的字符串比对加入结果集
                            buffer.toString().getWordType()?.let {
                                resultWordType.add(it)
                            }

                            // 根据当前界符获取到对应的 WordType
                            val boundaries = getBoundaries().match {
                                it.wordType.word == boundary.toString()
                            }
                            // 写入结果集
                            resultWordType.add(boundaries[0].wordType)

                            // 清空缓冲区
                            buffer.clear()
                            // 刷新单词种别码枚举类匹配 List
                            wordTypeEnums = workTypeEnumList
                        },
                        isOperator = { operator ->
                            if (buffer.contains("\"") || buffer.contains("'")) {
                                // 如果缓冲区中有存在单双引号，证明当前的运算符可能是字符串形式
                                // 直接将当前运算符加入缓冲区，不做其他处理
                                buffer.append(operator)
                                return@symbolType
                            }


                            if (buffer.toString().isInteger() && operator == '.') {
                                // 当前运算符是点，并且缓冲区中是整型数字
                                // 证明当前点不是运算符而是小数点
                                // 将小数点加入缓冲区，而不是识别为运算符
                                buffer.append(operator)
                                return@symbolType
                            }

                            // 在遇到运算符后先将缓冲区中的字符串比对加入结果集
                            buffer.toString().getWordType()?.let {
                                resultWordType.add(it)
                            }

                            // 检查一下当前运算符是否可以和上一个运算符组成双目运算符
                            // 这里的判断上一个运算符不为空，意思是防止上一个就是空字符串，
                            // 但是和当前的运算符组合成空字符串 + 运算符的格式，这样可能得到意外的双目运算符匹配结果。
                            if (resultWordType.isNotEmpty() && resultWordType.last().word.isNotEmpty()) {
                                val lastChar = resultWordType.last().word
                                val binaryOperatorMatch = getOperators().match {
                                    it.wordType.word == lastChar + operator
                                }

                                if (binaryOperatorMatch.isNotEmpty()) {
                                    // 双目运算符匹配结果不为空，删除上一个运算符
                                    resultWordType.removeLast()

                                    if (binaryOperatorMatch[0] == WordTypeEnum.SLASH_STAR) {
                                        // 当前 ”双目运算符“ 是斜杠星注释，标记遇到斜杠星注释
                                        // 查找斜杠星注释结束符索引位置
                                        slashStarTailIndex = lines.indexOf(
                                            str = "*/",
                                            startRowIndex = lineIndex
                                        )
                                        return@symbolType
                                    }

                                    if (binaryOperatorMatch[0] == WordTypeEnum.SLASH_SLASH) {
                                        // 当前 ”双目运算符“ 是双斜杠注释，标记遇到双斜杠注释
                                        meetDoubleSlashNote = true
                                        return@symbolType
                                    }

                                    // 将匹配成功的双目运算符加入结果集
                                    resultWordType.add(binaryOperatorMatch[0].wordType)
                                    return@symbolType
                                } else {
                                    // 匹配失败，正常将匹配到的单目运算符加入结果集

                                }
                            }

                            // 根据当前运算符获取到对应的 WordType
                            val operators = getOperators().match {
                                it.wordType.word == operator.toString()
                            }
                            // 写入结果集
                            resultWordType.add(operators[0].wordType)

                            // 清空缓冲区
                            buffer.clear()
                            // 刷新单词种别码枚举类匹配 List
                            wordTypeEnums = workTypeEnumList
                        },
                        isOther = { otherChar ->
                            // 当前字符是除了字母数字和运算符以及界符外的其他未定义的字符

                            if (buffer.contains("\"") || buffer.contains("'")) {
                                // 缓冲区中有单双引号，把当前字符认为是字符串直接加入缓冲区
                                buffer.append(otherChar)
                            } else {
                                // 出现了未定义的符号，并且缓冲区没有引号，这里应该报错
                            }

                        }
                    )
                }
            )
        }
    }

    // 解析结果
    val resultStr = StringBuffer()
    resultWordType.forEach {
        resultStr.append("$it\n")
    }

    // 打印解析结果
    println("$resultStr\n解析结果已经保存到 ." + File.separator + "analysis_result.txt")

    // 将解析结果输出到文件
    val output = File("." + File.separator + "analysis_result.txt")
    output.createNewFile()
    output.writeText(resultStr.toString())
}