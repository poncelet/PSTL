package control

import ZAM.Simulator

class Controler {
	val Simulateur = new Simulator
	
	def getPath = Simulateur.getPath
	
	def getOcmlCode = Simulateur.getOcmlCode
	
	def getThread(it : Int) = if(!Simulateur.IsReady) null else Simulateur.Env.getthread(it)

	def getEnv = if(!Simulateur.IsReady) null else Simulateur.Env
}