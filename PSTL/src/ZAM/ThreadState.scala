package ZAM
import scala.collection.mutable.ArrayBuffer

/**
 * Etat dynamique d'une thread
 * Elle comporte ses valeurs locales et son pc.
 */
class ThreadState {
	var trapsp = 0 // [0] trap pc, [1] ancien trapsp, [2] env, [3] extra arg
	val stack = new ThreadStack
	var accu : Value = new Zamint(0) //unit
	var env = new MyArray(0)
	var pc = 0
	var extra_args = 0
	
	/**
	 * Acces trap
	 */
	def setTrapsp(sp : Int) = trapsp = sp
	
	def getTrapsp = trapsp
	
	/**
	 * Acces à l'environnement
	 */
	def getenv(i : Int) = env.at(i)
	def setenv(i : Int, v : Value) = if(i == env.tab.size) env.add(v) else env.update(i, v)
	def sizeEnv = env.tab.size
	def clearenv = env.clear
	def getTabenv = env
	
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
	  str += " : env [" + env.tab.foreach(ent=> (str += ent + " "))
	  str += "] : pc " + pc
	  str
	}

}