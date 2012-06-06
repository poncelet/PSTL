package ZAM

class PrimitiveManager {

  val primitiveTable = Array[String]("println")
  
  def run(itprim : Int, narg : Int, env : GlobalState, it : Int) = {
    itprim match {
      case 0 => {
        var str = ""
        if(narg > 0) str += env.getthread(it).getaccu.toString
        for(i<-1 to narg-1) str += env.getthread(it).stack.get(i-1).toString
        env.addOutStream(str)
      }
      case _ => {
        throw new ErreurZam("Erreur, Primitive inconnue \n", env)
      }
    }
    
  }
}