package ZAM
import scala.collection.mutable.ArrayBuffer

/**
 * Simulateur, point d'entrée de la ZAM. 
 */
class Simulator {
  
  /**
   * Données graphiques
   */
	var path = ""
    var Ready = false
    
  /**
   * Données corps
   */
    val AST = new AST(new ArrayBuffer[Instruction])
	val Env = new GlobalState
	val SManager = new StateManager
	val Eval = new Evaluator

    /**
     * Gestion au path du programme 
     */
	def setprog(newpath : String) = {
	  path = newpath
	}
	
	def Sauvegarder() {
	  /** code (gestion graphique) */
	}
	
	def Charger(s : String){
	  /** code (gestion graphique) */
	}
	
	/**
	 * Preparation de l'ast depuis le source
	 */
	def Preparer() = {
	  Ready = true
	}
	
	/**
	 * Faire avancer la thread t de n pas
	 * Le simulateur gère le liens entre le pc de la thread et l'AST
	 * (la sauvegarde lors d'une avancée est faites dans instruction)
	 */
	def Avancer(t : Int, n : Int) {
	  if(!Ready) throw new Exception("erreur")
	  
	  //avancer n pas
	  for(i<-0 to n-1) {
	    
		  SManager.save(Env, t)
		  
		  Eval.exec(AST.get(Env.getthread(t).getpc),
		      		Env,
		      		t)
	  }
	}
	
		/**
	 * Restaurer de n pas l'état d'une thread
	 * t : Le numéro de thread
	 * n : le nombre de pas à restaurer
	 */
	def Revenir(t : Int, n : Int) {
	  if(!Ready) throw new Exception("erreur")
	  
	  SManager.restaurer(Env, t, n)
	  
	}
	
	override def toString = AST.toString()
	
	def printthread(t: Int) = Env.printT(t)
	
	def printenv() = Env.toString()
	
}