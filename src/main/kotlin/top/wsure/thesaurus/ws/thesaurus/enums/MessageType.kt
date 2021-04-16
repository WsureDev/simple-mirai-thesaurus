package top.wsure.thesaurus.ws.thesaurus.enums

enum class MessageType(val type: Int,val desc:String) {
    PRECISE(0,"精确问"),
    FUZZY(1,"模糊问")
    ;
    companion object{
        fun valueOf(type: Int):MessageType?{
            return values().find { it.type == type }
        }
    }

}