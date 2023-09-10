
import entity.WordType
import enum.WordTypeEnum
import tools.error
import tools.match
import tools.type
import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    val mArgs = arrayOf("C:\\Users\\16965\\Desktop\\ad.c")
    // 首先判断是否传入参数
    if (mArgs.isEmpty()) {
        println("cls [.c 文件路径]：对 C 进行词法分析")
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

    // 单词种别码枚举类，用于不停筛选得到最终的单词种别集合
    var wordTypeEnums = enumValues<WordTypeEnum>().toList()

    // 字符串缓冲区，用于比对单词
    val buffer = StringBuffer("")

    // 按行遍历
    lines.forEach { line ->
        // 按字符遍历
        line.forEach { char ->
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
                isSpaceOrSymbols = {
                    // 判断单词种别码集合是否还有选项
                    if (wordTypeEnums.isEmpty()) {
                        // 集合已经为空，判断一下是否是
                    }
                }
            )
        }
    }
}