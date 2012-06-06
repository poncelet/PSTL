package IHM
import scala.collection.mutable.ArrayBuffer

class Connector {
	val Controler = new control.Controler
	val threadFen = new ArrayBuffer[WMainEvents]
	
	val threadMin = new ArrayBuffer[Boolean]
	val threadName = new ArrayBuffer[String]
	
	val BreakPoints = new ArrayBuffer[Int]
	
	
	threadName += "main"
	threadMin += true
	
	threadFen += new WMainEvents(null, this, 0)
	
	def getThreadName(it : Int) = threadName(it)
	
	def step(it : Int) = {
	  if(Controler.getpc(it) <= Controler.getASTSize - 1) {
	    Controler.Avancer(it, 1)
	    threadFen.foreach(ent =>{ent.MajView})
	  }
	}
	
	def backstep(it : Int) = {
	  if(Controler.getpc(it) > 0) {
	    Controler.Reculer(it, 1)
	    threadFen.foreach(ent =>{ent.MajView})
	  }
	}
	
	def avance(it : Int) = {
	  val pc = Controler.getpc(it)
	  var nextstop = -1
	  var i = 0 
	  if(BreakPoints.size == 0) {nextstop = Controler.getASTSize}
	  else {
		  while( (pc >= nextstop) & (i < BreakPoints.size)) {
			  nextstop = BreakPoints(i)
			  i = i + 1
		  }
		  if(pc >= nextstop) nextstop = Controler.getASTSize
	  }
	  Controler.Avancer(it, nextstop - pc)
	  threadFen.foreach(ent =>{ent.MajView})
	}
	
	def prepare(Ex : Int) = Controler.prepare(Ex)
	
	def IsReady = Controler.isReady
	  
	def getnbthread = threadName.size
	
	def getPath = Controler.getPath
	
	def getOcmlCode = Controler.getOcmlCode
	
	def getThread(it : Int) = Controler.getThread(it)
	
	def getEnv = Controler.getEnv
	
	def restart = {
	  Controler.restart
	  threadName.clear
	  threadMin.clear
	  val oldv = threadFen(0).getviews
	  for(i<-0 to threadFen.size -1) threadFen(i).close
	  threadFen.clear
	  BreakPoints.clear
	  
	  threadName += "main"
	  threadMin += true
	  threadFen += new WMainEvents(oldv, this, 0)
	}
	
	def maxThread(it : Int, v : ArrayBuffer[Views]) = {
	  threadMin(it) = false
	  threadFen(it).max(v)
	}
	
	def reduce(it : Int) = {
	  val v = threadFen(it).getviews
	  threadFen(it).close
	  threadMin(it) = true
	  threadFen(0).addTab(it, v)
	}
	
	def getTables(it : Int) = {
	  val v = new ArrayBuffer[String]
	  
	  v+= "Stack"
	  val tmpst = Controler.getThread(it).stack
	  
	  for(i<-0 to tmpst.getSp-1) {
	    if(tmpst.get(i).isInstanceOf[ZAM.Zamblock]) {
	      v += "Stack : " + i 
	    }
	  }
	  
	  val tmpacc = Controler.getThread(it).getaccu
	  if(tmpacc.isInstanceOf[ZAM.Zamblock]) {
	      v += "Accu"
	    }
	  
	  val tmpenv = Controler.getThread(it).getTabenv
	  if(tmpenv.sizeArray > 0) v+= "Env"
	    
	  v += "Glob"
	  val tmpglob = Controler.getEnv.getglob
	  
	  tmpglob.foreach(ent=>{
	    if(ent._2.isInstanceOf[ZAM.Zamblock]) v += "Glob : " + ent._1})
	    
	  v
	}
	
	def addBrk(v : Int) = {
	  var i = 0
	  var tmp = 0
	  var vadd = 0
	  while( (i < BreakPoints.size) && (v > BreakPoints(i)) ) {
	    i = i + 1
	  }
	  vadd = v
	  for(j<-i to BreakPoints.size-1) {
	    tmp = BreakPoints(j)
	    BreakPoints(j) = vadd
	    vadd = tmp
	  }
	  BreakPoints += vadd
	}
	
	def remBrk(v : Int) = {
	  var i = 0
	  while(v != BreakPoints(i)) {
	    i = i + 1
	  }
	  BreakPoints.remove(i)
	}
	
	def MajView(it : Int) {
	  threadFen(it).MajView
	}
	
	def MajPan = threadFen(0).MajPan
	
	def Example1 = {
	  if(Controler.isReady) {
		  restart
	  }
	  Controler.setPath("Example1.zam")
	  prepare(1)
	  MajView(0)
	  //ajout d'une deuxieme thread
	threadName += "T1"
	threadMin += false
	threadFen += new WMainEvents(null, this, 1)
	  
	}
	
	def Example2 = {
	  if(Controler.isReady) {
		  restart
	  }
	  Controler.setPath("Example2.zam")
	  prepare(2)
	   MajView(0)
	  //ajout d'une deuxieme thread
	threadName += "T1"
	threadMin += false
	threadFen += new WMainEvents(null, this, 1)
	  //ajout d'une troisième thread
	threadName += "T2"
	threadMin += false
	threadFen += new WMainEvents(null, this, 2)
	}
	
	def Example3 = {
	  if(Controler.isReady) {
		  restart
	  }
	  Controler.setPath("Example3.zam")
	  prepare(3)
	  MajView(0)
	  //ajout d'une deuxieme thread
	threadName += "T1"
	threadMin += false
	threadFen += new WMainEvents(null, this, 1)
	  //ajout d'une troisième thread
	threadName += "T2"
	threadMin += false
	threadFen += new WMainEvents(null, this, 2)
	}
	
	def Fin = {
	  //sauver Code ?
	  println("A bientot")
	  System.exit(0)
	}
}