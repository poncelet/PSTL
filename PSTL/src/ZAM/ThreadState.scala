package ZAM
import scala.collection.mutable.ArrayBuffer

/**
 * Etat dynamique d'une thread
 * Elle comporte ses valeurs locales et son pc.
 */
class ThreadState {
	val stack = new ThreadStack
	var accu : Value = new Zamint(0) //unit
	var env = new Array[Value](4)
	var pc = 0
	var extra_args = 0
	
	/**
	 * Acces à l'environnement
	 */
	def getenv(i : Int) = env(i)
	def setenv(i : Int, v : Value) = env.update(i, v)
	
	/**
	 * Acces au pointeur d'instruction
	 */
	def getpc = pc
	def setpc(newpc : Int) = pc = newpc
  
	/**
	 * Acces à son accumulateur
	 */
	def getaccu = accu
	def setaccu(v : Value) = accu = v
	
	/**
	 * Extra arg
	 */
	def getextra = extra_args
	def setextra(v : Int) = extra_args = v 

	override def toString() = { var str = " sp [" + stack;
	  str += "] : accu " + accu
	  str += " : env [" + env.foreach(ent=> (str += ent + " "))
	  str += "] : pc " + pc
	  str
	}

}