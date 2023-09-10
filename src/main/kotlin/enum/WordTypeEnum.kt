package enum

import entity.WordType

val workTypeEnumList = enumValues<WordTypeEnum>().toList()

/**
 * 单词种别码枚举类
 */
enum class WordTypeEnum(val wordType: WordType) {
    /**
     * 关键字
     */
    CHAR(WordType(101, "char")),
    INT(WordType(102, "int")),
    FLOAT(WordType(103, "float")),
    BREAK(WordType(104, "break")),
    CONST(WordType(105, "const")),
    RETURN(WordType(106, "return")),
    VOID(WordType(107, "void")),
    CONTINUE(WordType(108, "continue")),
    DO(WordType(109, "do")),
    WHILE(WordType(110, "while")),
    IF(WordType(111, "if")),
    ELSE(WordType(112, "else")),
    FOR(WordType(113, "for")),

    /**
     * 界符
     */
    OPEN_BRACE(WordType(301, "{")),
    CLOSE_BRACE(WordType(302, "}")),
    SEMICOLON(WordType(303, ";")),
    COMMA(WordType(304, ",")),

    /**
     * 单词类别
     */
    INTEGER(WordType(400)),
    CHARACTER(WordType(500)),
    STRING(WordType(600)),
    IDENTIFIER(WordType(700)),
    REAL_NUMBER(WordType(800)),


    /**
     * 运算符
     */
    OPEN_PAREN(WordType(201, "(")),
    CLOSE_PAREN(WordType(202, ")")),
    OPEN_BRACKET(WordType(203, "[")),
    CLOSE_BRACKET(WordType(204, "]")),
    EXCLAMATION (WordType(205, "!")),
    STAR (WordType(206, "*")),
    SLASH(WordType(207, "/")),
    PERCENT(WordType(208, "%")),
    PLUS(WordType(209, "+")),
    MINUS(WordType(210, "-")),
    LESS_THAN(WordType(211, "<")),
    LESS_THAN_OR_EQUAL(WordType(212, "<=")),
    GREATER_THAN(WordType(213, ">")),
    GREATER_THAN_OR_EQUAL(WordType(214, ">=")),
    DOUBLE_EQUAL(WordType(215, "==")),
    NOT_EQUAL(WordType(216, "!=")),
    DOUBLE_AND(WordType(217, "&&")),
    PARALLEL(WordType(218, "||")),
    EQUAL(WordType(219, "=")),
    POINT(WordType(220, "."));
}

/**
 * 当前单词种别枚举类是否是关键字
 */
fun WordTypeEnum.isKey(): Boolean {
    return this.wordType.code in (101..113)
}

/**
 * 当前单词种别枚举类是否是界符
 */
fun WordTypeEnum.isBoundary(): Boolean {
    return this.wordType.code in (301..304)
}

/**
 * 当前单词种别枚举类是否是运算符
 */
fun WordTypeEnum.isOperator(): Boolean {
    return this.wordType.code in (201..220)
}

/**
 * 根据条件筛选单词种别枚举类
 */
private fun gets(
    check: (WordTypeEnum) -> Boolean
): List<WordTypeEnum> {
    val result = ArrayList<WordTypeEnum>()
    workTypeEnumList.forEach {
        if (check(it)) {
            result.add(it)
        }
    }
    return result
}


/**
 * 获取所有关键字
 */
fun getKeys(): List<WordTypeEnum> {
    return gets { it.isKey() }
}

/**
 * 获取所有界符
 */
fun getBoundaries(): List<WordTypeEnum> {
    return gets { it.isBoundary() }
}

/**
 * 获取所有运算符
 */
fun getOperators(): List<WordTypeEnum> {
    return gets { it.isOperator() }
}