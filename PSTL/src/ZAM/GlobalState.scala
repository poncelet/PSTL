package ZAM
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

/**
 * L'état global du programme, il possède les variable globales et les threads existantes
 * 
 */
class GlobalState {
  val glob = new HashMap[Int, Value] // Tas
  val Threads = new ArrayBuffer[ThreadState]
  
  val atom = Array[Value](new Zamblock(BlockT.normal_t, 0, null), new Zamblock(BlockT.foward_t, 0, null), new Zamblock(BlockT.infix_t, 0, null),
      new Zamblock(BlockT.object_t, 0, null), new Zamblock(BlockT.closure_t, 0, null), new Zamblock(BlockT.lazy_t, 0, null), new Zamblock(BlockT.abstract_t, 0, null),
      new Zamblock(BlockT.string_t, 0, null), new Zamblock(BlockT.double_t, 0, null), new Zamblock(BlockT.doublearray_t, 0, null), new Zamblock(BlockT.custom_t, 0, null))
  
  val runtime = new PrimitiveManager
  
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