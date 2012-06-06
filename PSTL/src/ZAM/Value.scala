package ZAM


/**
 * Les valeurs gérées par la ZAM
 */
abstract class Value {

	override def toString : String
	
}
/**
 * Un long
 */
class Zamlong (value : Long) extends Value {
  def getval = value
  override def toString = "long : " + value
}

/**
 * Un entier
 */
class Zamint (value : Int) extends Value {
  def getval = value
  override def toString = "entier : " + value 
}

/**
 * Un entier non signé
 */
class Zamuint (value : Int) extends Value {
  def getval = value
  override def toString = "entier signe: " + value 
}

/**
 * Un bloc
 */

class Zamblock (tag : BlockT.blockT, size : Long, value : MyArray) extends Value {
  
  def getval = value
  def getsize = size
  def gettag = tag
  
  def at(n : Int) = value.at(n) 
  def set(n : Int, v : Value) =  value.update(n, v) 
  
  override def toString = tag match {
    case BlockT.closure_t => {var str = "tableau de valeur de " + size + " : "
    						value.tab.foreach(ent => str += ent.toString + ", ")
    						str}
    case BlockT.abstract_t => "abstract de " + size + " : " + value.toString 
    case BlockT.string_t => {var str = "string de " + size + " : "
    						value.tab.foreach(ent => str += ent.asInstanceOf[Zamint].getval.asInstanceOf[Char])
    						str}
    
    case BlockT.double_t => {var str = "double de " + size + " : "
    						value.tab.foreach(ent => str += ent.asInstanceOf[Zamint].getval + ",")
    						str}
    
    case BlockT.doublearray_t => {var str = "tableau de double de " + size + " : "
    							 value.tab.foreach(ent => str += ent + ", ")
    							 str}
    
    case BlockT.foward_t => "foward de " + size + " : " + value.toString 
    case BlockT.infix_t => "infix de " + size + " : " + value.toString 
    case BlockT.object_t => "object de " + size + " : " + value.toString 
    case BlockT.closure_t => "closure de " + size + " : " + value.toString 
    }
}