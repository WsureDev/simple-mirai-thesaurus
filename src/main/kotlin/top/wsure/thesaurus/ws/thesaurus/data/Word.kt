package top.wsure.thesaurus.ws.thesaurus.data

import top.wsure.thesaurus.ws.thesaurus.enums.MessageType

data class Word(
    var question:String,
    var answer:String,
    var type:MessageType,
){
    constructor(question:String,answer:String,type: Int):this(question,answer, MessageType.valueOf(type)!!)
    constructor(question:String,type: MessageType):this(question,"", type)
}
