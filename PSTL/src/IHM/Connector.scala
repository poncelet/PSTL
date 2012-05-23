package IHM
import scala.collection.mutable.ArrayBuffer

class Connector {
	val Controler = new control.Controler
	val threadFen = new ArrayBuffer[WMainEvents]
	
	val threadMin = new ArrayBuffer[Boolean]
	val threadName = new ArrayBuffer[String]
	
	val BreakPoints = new ArrayBuffer[Int]
	BreakPoints += 5
	BreakPoints += 15
	
	
	threadName += "main"
	threadMin += true
	
	threadFen += new WMainEvents(null, this, 0)
	
	def getThreadName(it : Int) = threadName(it)
	
	def step(it : Int) = {
	  if(Controler.getpc(it) < Controler.getASTSize - 1)
	    Controler.Avancer(it, 1)
	}
	
	def backstep(it : Int) = {
	  if(Controler.getpc(it) > 0)
	    Controler.Reculer(it, 1)
	}
	def avance(it : Int) = {
	  val pc = Controler.getpc(it)
	  var nextstop = 0
	  var i = 0 
	  while( (pc > nextstop) & (i < BreakPoints.size)) {
	    nextstop = BreakPoints(i)
	    i = i + 1
	  }
	  if(pc > nextstop) nextstop = Controler.getASTSize - 1
	  Controler.Avancer(it, nextstop - pc)
	}
	def prepare = Controler.prepare
	  
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
	  threadName += "main"
	  threadMin += true
	  threadFen += new WMainEvents(oldv, this, 0)
	}
	
	def maxThread(it : Int, v : ArrayBuffer[Int]) = {
	  val v = threadFen(it).getviews
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
	  v+= "Accu"
	  v+= "Env"
	  v+= "Glob"
	    
	  v
	}
	
	def Fin = {
	  //sauver Code ?
	  println("A bientot")
	  System.exit(0)
	}
}