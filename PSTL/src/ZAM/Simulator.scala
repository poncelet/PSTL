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
    val Parsor = new Parsor
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
	
	def getPath = path
	
	def getOcmlCode = ""
	  
	/**
	 * Preparation de l'ast depuis le source
	 */
	def Preparer(n : Int) = {
	  if(path != "") {
	    n match {
	      case 1 => {
	         AST.add(new ZAM.Const(2))
	         AST.add(new ZAM.Push)
	         AST.add(new ZAM.Const(4))
	         AST.add(new ZAM.Push)
	         AST.add(new ZAM.Constint(50))
	         AST.add(new ZAM.Setglobal(3))
	         AST.add(new ZAM.Constint(10))
	         AST.add(new ZAM.Setglobal(2))
	         AST.add(new ZAM.Constint(55))
	         AST.add(new ZAM.Setglobal(3))
	         Env.pushthread(new ZAM.ThreadState)
	         Env.pushthread(new ZAM.ThreadState)
	      }
	      case 2 => {
	    	 AST.add(new ZAM.Const('d'))
	    	 AST.add(new ZAM.Pushconst('l'))
	    	 AST.add(new ZAM.Pushconst('r'))
	    	 AST.add(new ZAM.Pushconst('o'))
	    	 AST.add(new ZAM.Pushconst('W'))
	    	 AST.add(new ZAM.Pushconst('o'))
	    	 AST.add(new ZAM.Pushconst('l'))
	    	 AST.add(new ZAM.Pushconst('l'))
	    	 AST.add(new ZAM.Pushconst('e'))
	    	 AST.add(new ZAM.Pushconst('h'))	
	    	 AST.add(new ZAM.Makeblock(10, ZAM.BlockT.string_t.id))
	    	 AST.add(new ZAM.Push)
	    	 AST.add(new ZAM.Setglobal(3))	
	    	 AST.add(new ZAM.Getglobalfield(3,5))
	    	 AST.add(new ZAM.Setglobal(3))
	    	 Env.pushthread(new ZAM.ThreadState)
	         Env.pushthread(new ZAM.ThreadState)
	         Env.pushthread(new ZAM.ThreadState)
	      }
	      case 3 => {
	    	  AST.add(new ZAM.Const(4))
	    	  AST.add(new ZAM.Setglobal(0))
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushgetglobal(0))
	
	    	  AST.add(new ZAM.Integer_comparision("EQ"))
	    	  AST.add(new ZAM.Branchif(26)) //if varglob(0) == 2 -> pc +20
	
	    	  // {
	    	  AST.add(new ZAM.Const(1))
	    	  AST.add(new ZAM.Pushgetglobal(0))
	    	  AST.add(new ZAM.Subint) // var --
	    	  AST.add(new ZAM.Setglobal(0))
	
	    	  AST.add(new ZAM.Getglobal(0))
	    	  AST.add(new ZAM.Pushconst(3))
	    	  AST.add(new ZAM.Integer_comparision("LEINT"))
	    	  AST.add(new ZAM.Branchifnot(9)) //if! ( 3 <=  varglob(0)) -> pc + 20
	
	    	  // {
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Addint) //2+2
	
	    	  AST.add(new ZAM.Pushconst(6))
	    	  AST.add(new ZAM.Pushconst(6))
	    	  AST.add(new ZAM.Mulint) //6*6
	
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Branch(22))
	    	  //}
	    	  //{ ifnot
	    	  AST.add(new ZAM.Const(1))
	    	  AST.add(new ZAM.Negint) //~1
	
	    	  AST.add(new ZAM.Pushconst(3))
	    	  AST.add(new ZAM.Pushconst(9))
	    	  AST.add(new ZAM.Divint) // 9/3
	
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Pushconst(10))
	    	  AST.add(new ZAM.Modint) // 10%2
	
	    	  AST.add(new ZAM.Integer_branch_comparision(0, 13, "BEQ"))
	    	  //}}
	    	  // if {
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushconst(6))
	    	  AST.add(new ZAM.Andint) // 6 & 2
	
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Pushconst(4))
	    	  AST.add(new ZAM.Orint) // 4 | 2
	
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Pushconst(6))
	    	  AST.add(new ZAM.Xorint) // 6 ^ 2
	
	    	  AST.add(new ZAM.Integer_comparision("GTINT")) // 4 > 6
	    	  AST.add(new ZAM.Boolnot) //true
	    	  AST.add(new ZAM.Branchifnot(205)) //not jump
	    	  //}
	
	    	  AST.add(new ZAM.Switch(0, Array(1, 6, 11)))
	
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Lslint) // 2 << 2
	    	  AST.add(new ZAM.Makeblock(3, ZAM.BlockT.double_t.id))
	    	  AST.add(new ZAM.Setglobal(0))
	
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Lsrint) // 2 >> 2
	    	  AST.add(new ZAM.Getglobal(0))
	    	  AST.add(new ZAM.Vectlength)
	
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Pushconst(2))
	    	  AST.add(new ZAM.Asrint) // 2 >> 2
	    	  AST.add(new ZAM.Const(2))
	    	  AST.add(new ZAM.Push)
	    	  AST.add(new ZAM.Getglobal(0))
	    	  AST.add(new ZAM.Setvectitem)
	    	 Env.pushthread(new ZAM.ThreadState)
	         Env.pushthread(new ZAM.ThreadState)
	         Env.pushthread(new ZAM.ThreadState)
	      }
	      case _ => Parsor.parse(path, AST)
	    }
		  Ready = true
	  } else Ready = true //a mettre a false
	}
	
	def IsReady = Ready
	
	/**
	 * Faire avancer la thread t de n pas
	 * Le simulateur gère le liens entre le pc de la thread et l'AST
	 * (la sauvegarde lors d'une avancée est faites dans instruction)
	 */
	def Avancer(t : Int, n : Int) {
	  if(!Ready) throw new Exception("Erreur Simulateur non pret")
	  
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
	  if(!Ready) throw new Exception("Erreur Simulateur non pret")
	  
	  SManager.restaurer(Env, t, n)
	  
	}
	
	def Restart = {
	  
	}
	
	override def toString = AST.toString
	
	def printthread(t: Int) = Env.printT(t)
	
	def printenv = Env.toString
	
}