//package top.wsure.thesaurus.ws.thesaurus.utils.ac
//
//import top.wsure.thesaurus.ws.thesaurus.utils.ACUtils
//import java.util.ArrayList
//
///**
// * FileName: AhoCorasickMatcher
// * Author:   wsure
// * Date:     2021/4/7 3:10 下午
// * Description:
// */
//class AhoCorasickMatcher : AbstractMultipleExactStringMatcher {
//
//
//    override fun match(text: String, vararg patterns: String): List<MatchingResult> {
//        val data = constructACAutomaton(patterns.distinct())
//        var v :TrieNode? = data.root
//        val n = text.length
//        val resultList: MutableList<MatchingResult> = ArrayList()
//        for (j in 0 until n) {
//            while (v != null && v.getChild(text[j]) == null) {
//                v = data.fail[v]
//            }
//            if(v != null){
//                v = v.getChild(text[j])
//                for (i in data.patterns[v]!!) {
//                    println(data.patterns[v])
//                    resultList.add(MatchingResult(i, j,patterns[i]))
//                }
//            } else{
//                v = data.root
//            }
//        }
//        return resultList
//    }
//
//    override fun match(text: String, automaton: Automaton): List<MatchingResult> {
//
//       TODO()
//    }
//
//
//    override fun constructACAutomaton(patterns: List<String>): Automaton {
//        val ret = Automaton()
//        constructTrie(ret, patterns)
//        computeFailureFunction(ret, ACUtils.distinctCharSet(patterns))
//        return ret
//    }
//
//    private fun constructTrie(automaton: Automaton, patterns: List<String>) {
//        require(patterns.isNotEmpty()) { "No patterns given." }
//        val root = TrieNode()
//        for (i in patterns.indices) {
//            var v: TrieNode? = root
//            var j = 0
//            val patternLength = patterns[i].length
//            while (j < patternLength && v!!.getChild(patterns[i][j]) != null) {
//                v = v.getChild(patterns[i][j])
//                ++j
//            }
//            while (j < patternLength) {
//                val u = TrieNode()
//                v!!.setChild(patterns[i][j], u)
//                v = u
//                ++j
//            }
//            val list: MutableList<Int> = ArrayList()
//            list.add(i)
//            automaton.patterns[v!!] = list
//        }
//        automaton.patterns[root] = ArrayList()
//        automaton.root = root
//    }
//
//    private fun computeFailureFunction(automaton: Automaton,charSet:List<Char>) {
//        val fallbackNode = TrieNode()
//        charSet.forEach {
//            fallbackNode.setChild(it, automaton.root)
//        }
//        automaton.fail[automaton.root] = fallbackNode
//        val queue: ArrayDeque<TrieNode?> = ArrayDeque()
//        queue.addLast(automaton.root)
//
//
//        while (!queue.isEmpty()) {
//            val u = queue.removeFirst()
//            var it = 0
//            while (it < charSet.size) {
//                if (u!!.getChild(charSet[it]) == null) {
//                    ++it
//                    continue
//                }
//                val v = u.getChild(charSet[it])
//                var w = automaton.fail[u]
//                while (w!!.getChild(charSet[it]) == null) {
//                    w = automaton.fail[w]
//                }
//                automaton.fail[v!!] = w.getChild(charSet[it])!!
//                val list = automaton.patterns[automaton.fail[v]]
//                if (automaton.patterns[v] == null) {
//                    automaton.patterns[v] = list!!
//                } else {
//                    automaton.patterns[v]!!.addAll(list!!)
//                }
//                queue.addLast(v)
//                ++it
//            }
//        }
//        automaton.patterns[automaton.root] = ArrayList()
//    }
//}