/**
 * 
 *
 * public final class UserAccount {
    private final String uuid;
    private final String email;
    private final Double balance;

    public UserAccount(String uuid, String email, Double balance) {
        this.uuid = uuid;
        this.email = email;
        this.balance = balance;
    }

    // getters, toString(), equals(), y hashCode().
}
 */
    fun main() {
    println("Hola Nachoo")
    data class UserAccount(
    val uuid: String,
    var email: String,
    var balance: Double
)

val user1 = UserAccount("123", "test@mail.com", 100.0)
user1.email = "nuevo@mail.com"   // setter automático
val user2 = UserAccount("123", "test@mail.com", 100.0)
user2.balance = 200.0
    
//equals() y hashCode().
println("son iguales: ${user1 == user2}");      // equals()
println(user1.hashCode());      // hashCode()
println(user1.toString());       // toString()

//copy
var userCopy = user1.copy() // realiza copia de user1
println("copia del usuario  ${userCopy}");
   
   /**
    * Ejercicio 3
Kotlin distingue entre tipos que pueden ser nulos y los que no. Esta distinción es
verificada estáticamente. La tarea es implementar una función que reciba una lista
de montos transaccionales (algunos de los cuales pueden ser null). A partir de esta
lista, utilizar el operador de llamada segura (?.) y el operador Elvis (?:) para retornar
la suma total, tratando los valores nulos como 0.0.
Restricción: No usar de sentencias if (x != null).
   */
    
   
  /*  (it)  cada elemento de la lista (Double?)
	  (?:) reemplaza los null por 0.0
	  (sumOf) acumula todos los valores*/
    
fun sumTransactions(amounts: List<Double?>): Double {
    return amounts.sumOf { it ?: 0.0 }
}    
    
 val transactions = listOf(100.0, null, 50.5, null, 25.0)
    println(sumTransactions(transactions)) // 175.5
    
}