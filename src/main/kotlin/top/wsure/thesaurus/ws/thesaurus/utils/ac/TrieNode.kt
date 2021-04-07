//package top.wsure.thesaurus.ws.thesaurus.utils.ac
//
//import java.util.HashMap
//
///**
// * FileName: TrieNode
// * Author:   wsure
// * Date:     2021/4/7 4:30 下午
// * Description:
// */
//data class TrieNode(var key:String?,var children: MutableMap<Char, TrieNode>) {
//
//    constructor():this(null,HashMap())
//
//    fun setChild(character: Char, child: TrieNode) {
//        children[character] = child
//    }
//
//    fun getChild(character: Char): TrieNode? {
//        return children[character]
//    }
//}