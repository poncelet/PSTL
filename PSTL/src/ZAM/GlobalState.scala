package ZAM
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

/**
 * L'état global du programme, il possède les variable globales et les threads existantes
 * 
 */
class GlobalState {
  val glob = new HashMap[Int, Value]() // Tas
  val Threads = new ArrayBuffer[ThreadState]
  
  /**
   * Gestion des variables globales
   */
  def getglob = glob
  def addglob(i : Int, v : Value) = glob.put(i, v)
  def atglob(i : Int) : Value = glob(i)
  
  /**
   * Gestion des threads
   */
  def pushthread(t : ThreadState) = Threads += t
  def removethread(i : Int) = Threads.remove(i)
  def getthread(i : Int) : ThreadState = Threads(i)
 
  override def toString() = { var str = "Environnement global : "
	  						if(glob.size == 0) str = str + "[]"
	  						else str=str + glob.toString
	  						str
  							}
  def printT(t : Int) = "Thread n°" + t + "::" + Threads(t).toString()
}