package ZAM

class PrimitiveManager {

  val primitiveTable = Array[String]("println")
  
  def run(itprim : Int, narg : Int, thread : ThreadState) = {
    itprim match {
      case 0 => {
        var str = ""
        if(narg > 0) str += thread.getaccu.toString
        for(i<-1 to narg-1) str += thread.stack.get(i-1).toString
        println(str)
      }
      case _ => {
        throw new ErreurZam("Erreur, Primitive inconnue \n")
      }
    }
    
  }
}