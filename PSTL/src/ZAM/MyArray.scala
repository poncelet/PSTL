package ZAM
import scala.collection.mutable.ArrayBuffer

class MyArray(size : Int, nfuncs : Int = 0) {
	val tab = new ArrayBuffer[Value](size)
	var offset = 0
	
	//init
	for(i<-0 to size-1) tab += null
	
	def at(i : Int) = {
	  if(nfuncs == 0) tab(i)
	  else { //gestion des fonctions rec
	    tab( (nfuncs - offset) + i)
	  }
	}
	
	def update(n : Int, v : Value) = tab.update(n, v)
	
	def add(v : Value) = tab += v
	
	def clear = tab.clear
	
	def setoffset(n : Int) = offset = offset + n // Attention ordre inversé : offrec -2 => offset + 2  

	def sizeArray = tab.size
}