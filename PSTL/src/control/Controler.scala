package control

import ZAM.Simulator

class Controler {
	var Simulateur = new Simulator
	
	def getPath = Simulateur.getPath
	
	def getOcmlCode = Simulateur.getOcmlCode
	
	def getThread(it : Int) = if(!Simulateur.IsReady) null else Simulateur.Env.getthread(it)

	def getEnv = Simulateur.Env
	
	def prepare(Ex : Int) = Simulateur.Preparer(Ex)
	
	def getpc(it : Int) = if(Simulateur.IsReady) Simulateur.Env.getthread(it).getpc else -1
	
	def getASTSize = Simulateur.AST.size
	
	def Avancer(it : Int, n : Int) = if(Simulateur.IsReady) Simulateur.Avancer(it, n)
	
	def Reculer(it : Int, n : Int) = if(Simulateur.IsReady) Simulateur.Revenir(it, n)
	
	def isReady = Simulateur.IsReady
	
	def setPath(path : String) = Simulateur.setprog(path)
	
	def restart = {//Simulateur.Restart
	  Simulateur = new Simulator
	}
}