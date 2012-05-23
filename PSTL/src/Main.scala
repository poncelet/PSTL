object Main {
	def main(args: Array[String]) {
	  
	  val Simulateur = new ZAM.Simulator
	  
	 /**
	   * Test implantation
	   * 5) thread, arithmetic et sauts
	   */
	 
	 Simulateur.AST.add(new ZAM.Const(4))
	 Simulateur.AST.add(new ZAM.Setglobal(0))
	 
	 Simulateur.AST.add(new ZAM.Const(2))
	 Simulateur.AST.add(new ZAM.Pushgetglobal(0))
	 
	 Simulateur.AST.add(new ZAM.Integer_comparision("EQ"))
	 Simulateur.AST.add(new ZAM.Branchif(26)) //if varglob(0) == 2 -> pc +20
	 
	 // {
	 Simulateur.AST.add(new ZAM.Const(1))
	 Simulateur.AST.add(new ZAM.Pushgetglobal(0))
	 Simulateur.AST.add(new ZAM.Subint) // var --
	 Simulateur.AST.add(new ZAM.Setglobal(0))
	 
	 Simulateur.AST.add(new ZAM.Getglobal(0))
	 Simulateur.AST.add(new ZAM.Pushconst(3))
	 Simulateur.AST.add(new ZAM.Integer_comparision("LEINT"))
	 Simulateur.AST.add(new ZAM.Branchifnot(9)) //if! ( 3 <=  varglob(0)) -> pc + 20
	 
	 // {
	 Simulateur.AST.add(new ZAM.Const(2))
	 Simulateur.AST.add(new ZAM.Pushconst(2))
	 Simulateur.AST.add(new ZAM.Addint) //2+2
	 
	 Simulateur.AST.add(new ZAM.Pushconst(6))
	 Simulateur.AST.add(new ZAM.Pushconst(6))
	 Simulateur.AST.add(new ZAM.Mulint) //6*6
	 
	 Simulateur.AST.add(new ZAM.Pushconst(2))
	 Simulateur.AST.add(new ZAM.Branch(22))
	 //}
	 //{ ifnot
	 Simulateur.AST.add(new ZAM.Const(1))
	 Simulateur.AST.add(new ZAM.Negint) //~1
	 
	 Simulateur.AST.add(new ZAM.Pushconst(3))
	 Simulateur.AST.add(new ZAM.Pushconst(9))
	 Simulateur.AST.add(new ZAM.Divint) // 9/3
	 
	 Simulateur.AST.add(new ZAM.Pushconst(2))
	 Simulateur.AST.add(new ZAM.Pushconst(10))
	 Simulateur.AST.add(new ZAM.Modint) // 10%2
	 
	 Simulateur.AST.add(new ZAM.Integer_branch_comparision(0, 13, "BEQ"))
	 //}}
	 // if {
	Simulateur.AST.add(new ZAM.Const(2))
	Simulateur.AST.add(new ZAM.Pushconst(6))
	Simulateur.AST.add(new ZAM.Andint) // 6 & 2
	
	Simulateur.AST.add(new ZAM.Pushconst(2))
	Simulateur.AST.add(new ZAM.Pushconst(4))
	Simulateur.AST.add(new ZAM.Orint) // 4 | 2
	
	Simulateur.AST.add(new ZAM.Pushconst(2))
	Simulateur.AST.add(new ZAM.Pushconst(6))
	Simulateur.AST.add(new ZAM.Xorint) // 6 ^ 2
	
	Simulateur.AST.add(new ZAM.Integer_comparision("GTINT")) // 4 > 6
	Simulateur.AST.add(new ZAM.Boolnot) //true
	Simulateur.AST.add(new ZAM.Branchifnot(205)) //not jump
	//}
	
	Simulateur.AST.add(new ZAM.Switch(0, Array(1, 6, 11)))
	
	Simulateur.AST.add(new ZAM.Const(2))
	Simulateur.AST.add(new ZAM.Pushconst(2))
	Simulateur.AST.add(new ZAM.Lslint) // 2 << 2
	Simulateur.AST.add(new ZAM.Makeblock(3, ZAM.BlockT.double_t.id))
	Simulateur.AST.add(new ZAM.Setglobal(0))
	
	Simulateur.AST.add(new ZAM.Const(2))
	Simulateur.AST.add(new ZAM.Pushconst(2))
	Simulateur.AST.add(new ZAM.Lsrint) // 2 >> 2
	Simulateur.AST.add(new ZAM.Getglobal(0))
	Simulateur.AST.add(new ZAM.Vectlength)
	
	Simulateur.AST.add(new ZAM.Const(2))
	Simulateur.AST.add(new ZAM.Pushconst(2))
	Simulateur.AST.add(new ZAM.Asrint) // 2 >> 2
	Simulateur.AST.add(new ZAM.Const(2))
	Simulateur.AST.add(new ZAM.Push)
	Simulateur.AST.add(new ZAM.Getglobal(0))
	Simulateur.AST.add(new ZAM.Setvectitem)
	
	 Simulateur.Preparer
	 //print
	 print(Simulateur.toString)
	 
	 //un thread
	 Simulateur.Env.pushthread(new ZAM.ThreadState)
	 Simulateur.Env.pushthread(new ZAM.ThreadState)
	 Simulateur.Env.pushthread(new ZAM.ThreadState)
	 
	 Simulateur.Avancer(0, 2)
	 Simulateur.Avancer(1, 2)
	 Simulateur.Avancer(2, 2)
	 
	 Simulateur.Avancer(0, 20)
	 println("*********************\n")
	 println(Simulateur.printthread(0))
	 println(Simulateur.printenv)
	 
	 Simulateur.Avancer(1, 21)
	 println("*********************\n")
	 println(Simulateur.printthread(1))
	 println(Simulateur.printenv)
	 
	 Simulateur.Avancer(2, 16)
	 println("*********************\n")
	 println(Simulateur.printthread(2))
	 println(Simulateur.printenv)
	 
	 println("*********************\n")
	 println("**	apres switch  **\n")
	  
	 Simulateur.Avancer(1, 6)
	 println("*********************\n")
	 println(Simulateur.printthread(1))
	 println(Simulateur.printenv)
	 
	 Simulateur.Avancer(2, 6)
	 println("*********************\n")
	 println(Simulateur.printthread(2))
	 println(Simulateur.printenv)
	 
	 Simulateur.Avancer(0, 8)
	 println("*********************\n")
	 println(Simulateur.printthread(0))
	 println(Simulateur.printenv)

	 
	  
	  /**
	   * Test graphique
	   */
	  val c = new IHM.Connector
	}
}