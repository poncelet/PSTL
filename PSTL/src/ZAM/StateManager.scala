package ZAM
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

/**
 * Gestionnaire de sauvegarde d'état
 * Il garde et gère chaque état pour chaque thread
 */
class StateManager {

  /**
   * Tableau de chemin pour chaque tread
   */
	val chemin = new ArrayBuffer[ArrayBuffer[State]]

	/**
	 * Sauve dans le chemin correspondant, l'état de la thread
	 */
	def save(MyEnv : GlobalState, itT : Int) = {	
	  val thread = MyEnv.getthread(itT)
	  // Copy de pile
		val stk = new Array[Value](thread.stack.size)
		for(i<-0 to thread.stack.size-1) stk.update(i, thread.stack.get(i))
		//Copy de tas
		val globtmp = new HashMap[Int, Value]()
		MyEnv.getglob.foreach(ent => globtmp.+=(ent))
		var envtmp = new Array[Value](thread.sizeEnv)
		for(i <-0 to thread.sizeEnv-1) envtmp.update(i, thread.getenv(i))
		
		if(chemin.length == itT) chemin += new ArrayBuffer[State](0)
		chemin(itT) += new State(thread.getpc, stk, thread.getaccu, envtmp ,globtmp, thread.getextra)
	}
	
	/**
	 * Restaure le n° chemin (et met aussi à jour le chemin)
	 */
	def restaurer(MyEnv : GlobalState, itT : Int, n : Int) = { 
	  
	  val stateit = chemin(itT)(chemin(itT).size - n)
	  val thread = MyEnv.getthread(itT)
		// Restaurer Pile
		thread.stack.clear
		for(i<-0 to stateit.getsp.size-1) thread.stack.push(stateit.getsp(i))
		// Valeurs globales : Restauration des anciennes valeurs sans suppressions du tout 
		// (évite de faire disparaître les variables d'autres threads)
		stateit.getglob.foreach(ent=> MyEnv.getglob += (ent))
		//EtatGlobal.setglob(new HashMap[Int, Value](stateit.getglob))
		// Environnement
		thread.clearenv
		stateit.getenv.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))

		// Restaurer tas

		thread.setpc(stateit.getpc)
		thread.setaccu(stateit.getaccu)
		thread.setextra(stateit.getextra)
		// Restaurer le chemin
		for(i<-0 to n) chemin(itT).remove(chemin(itT).length-1)
	}

}