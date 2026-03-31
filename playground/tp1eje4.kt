fun main() {
    
fun Int.esPar(): Boolean {
return this % 2 == 0
}
println(4.esPar())  // tiene que dar true
println(7.esPar())  // false
    
    
}

fun Int.esPrimo(): Boolean {
    if (this < 2) return false
    for (i in 2 until this) {
        if (this % i == 0) return false
    }
    return true
}