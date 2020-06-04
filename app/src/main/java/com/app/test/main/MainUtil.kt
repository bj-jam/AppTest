package com.app.test.main

/**
 */
fun main(args: Array<String>) {
    //    print("3333")
//    print("\n" + sum(1, 3))
//    print("\n" + sum1(1, 5))
//    print("\n" + sum2(1, 5))
//    printsum(a, b)
//    vars("1", "2", "3")
//    for (cv in c){
//        print(cv)
//    }
//    默认 | 用作边界前缀，但你可以选择其他字符并作为参数传入，比如 trimMargin(">")。
//    var c = """
//        1FADSFA
//        1DFASDF
//        1DFADSF
//        """.trimMargin("1")
//    print(c)

//    val i = "FASDFASDF"
//    val s = "i = $i" // 求值结果为 "i = 10"
//    val s1= "i = ${i.length}" // 求值结果为 "i = 10"
//    println(s1)
//    val a = 3;
//    val b = 4
////    val max = if (a > b) a else b
//    val max = if (a > b) {
//        print("Choose a")
//        a
//    } else {
//        print("Choose b")
//        b
//    }
//        print(max)
//    val x = "prefix"
//    hasPrefix(x)
//    println("----while 使用-----")
//    var x = 5
//    while (x > 5) {
//        println(x--)
//    }
//    println("----do...while 使用-----")
//    var y = 5
//    do {
//        println(y--)
//    } while (y > 6)
//    loop@ for (i in 1..100) {
//        print("i$i")
//        for (j in 1..100) {
//            print("j$j")
//            if (j == 4) break@loop
//        }
//    }
//    foo()
//    var address: String? = null
//    val result = address ?: fail("No address")
//    println(result.length) // 编译通过
//    val p1 = Point(10, 30)
//    val p2 = Point(20, 30)
//    println("plus: ${p1 + p2}")
//    println("minus: ${p1 - p2}")
//    println("times: ${p1 * p2}")
//    println("div: ${p1 / p2}")
//    print(lazyValue)
//    print(lazyValue)
//    print(lazyValue)
//    print(lazyValue)
    print(testApply())
//    testLet()
//    testRun()
}

fun testRun() {
    val data: String? = "123456"
    val result = data.run {
        this?.substring(2)
        this?.length
    }
    println(result)
    val list: MutableList<String> = mutableListOf("A", "B", "C")
    val change = list.run {
        add("D")
        add("E")
        this.add("F")
        size
    }
    println("list = $list")
    println("change = $change")
}


fun testLet() {
    var data: String? = "abcde"
    val sub = data?.let {
        it.substring(1)
        println(data)
        it
    }
    println(sub)

    val list: MutableList<String> = mutableListOf("A", "B", "C")
    val change = list.let {
        it.add("D")
        it.add("E")
        it.size
    }
    println("list = $list")
    println("change = $change")
}

fun testApply() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I konw this alphabet")
    length
}

val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}


val ints = intArrayOf(1, 2, 3)


fun foo() {
    ints.forEach lit@{
        if (it == 2) {
            return
        }
        print(it)
    }
}

fun hasPrefix(x: Any) = when (x) {
    is String -> print(x.plus("fdasdfad"))
    is Int -> x.rangeTo(8)
    else -> print(false)
}

var a: Int = 3
var b = 7
val c = Array(5, { i -> (i * 3) })
val d = arrayOf("3", "4")
val e = arrayListOf<String>()

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun sum1(a: Int, b: Int) = a + b
public fun sum2(a: Int, b: Int): Int = a + b
public fun printsum(a: Int, b: Int) {
    print(a + b)
}

fun vars(vararg v: String) {
    for (st in v)
        print(st)

}

class Point(var x: Int, var y: Int) {
    //Potin有两个成员变量x、y

    //加法(+)
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    //减法(-)
    operator fun dec(): Point {
        return Point(x - 1, y - 1)
    }

    //乘法(*)
    operator fun times(other: Point): Point {
        return Point(x * other.x, y * other.y)
    }

    //除法(/)
    operator fun div(other: Point): Point {
        return Point(x / other.x, y / other.y)
    }

    //取模(%)
    operator fun rem(other: Point): Point {
        return Point(x % other.x, y % other.y)
    }

    override fun toString(): String {
        return "Point[$x, $y]"
    }
}