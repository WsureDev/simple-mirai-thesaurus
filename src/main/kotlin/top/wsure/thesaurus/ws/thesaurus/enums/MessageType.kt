package top.wsure.thesaurus.ws.thesaurus.enums

enum class MessageType(val type: Int) {
    PRECISE(0),
    FUZZY(1)
    ;
    companion object{
        fun valueOf(type: Int):MessageType?{
            return values().find { it.type == type }
        }
    }

}