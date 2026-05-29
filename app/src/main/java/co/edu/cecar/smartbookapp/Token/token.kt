package co.edu.cecar.smartbookapp.Token

object SessionManager{
    var token:String? =null
        private set

    fun guardarSession(nuevoToken: String){
        token=nuevoToken
    }
    fun estaLogeado(): Boolean{
        return !token.isNullOrBlank()
    }
    fun cerrarSession(){
        token=null
    }
}