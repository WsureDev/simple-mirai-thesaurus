package top.wsure.thesaurus.ws.thesaurus.utils.ac

import top.wsure.thesaurus.ws.thesaurus.utils.ACUtils
import java.util.ArrayList
import java.util.Deque
import java.util.HashMap

/**
 * FileName: AhoCorasickMatcher
 * Author:   wsure
 * Date:     2021/4/7 3:10 下午
 * Description:
 */
class AhoCorasickMatcherCopy : AbstractMultipleExactStringMatcher {
    override fun match(text: String, vararg patterns: String): List<MatchingResult> {
        require(patterns.isNotEmpty()) { "No patterns given." }
        val data: Automaton = constructACAutomaton(patterns.distinct())
        var v = data.root
        val n = text.length
        val resultList: MutableList<MatchingResult> = ArrayList<MatchingResult>()
        for (j in 0 until n) {
            while (v!!.getChild(text[j]) == null) {
                v = data.fail[v]
            }
            v = v.getChild(text[j])
            for (i in data.patterns[v]!!) {
                resultList.add(MatchingResult(i, j,patterns[i]))
            }
        }
        return resultList
    }

    override fun match(text: String, automaton: Automaton): List<MatchingResult> {
        TODO("Not yet implemented")
    }

    class TrieNode {
        private val children: MutableMap<Char, TrieNode?> = HashMap()
        fun setChild(character: Char, child: TrieNode?) {
            children[character] = child
        }

        fun getChild(character: Char): TrieNode? {
            return children[character]
        }
    }

    override fun constructACAutomaton(patterns: List<String>): Automaton {
        val ret = Automaton()
        constructTrie(ret, patterns)
        computeFailureFunction(ret)
        return ret
    }

    private fun constructTrie(automaton: Automaton, patterns: List<String>) {
        val root = TrieNode()
        val k = patterns.size
        for (i in 0 until k) {
            var v: TrieNode? = root
            var j = 0
            val patternLength = patterns[i].length
            while (j < patternLength
                && v!!.getChild(patterns[i][j]) != null
            ) {
                v = v.getChild(patterns[i][j])
                ++j
            }
            while (j < patternLength) {
                val u = TrieNode()
                v!!.setChild(patterns[i][j], u)
                v = u
                ++j
            }
            val list: MutableList<Int> = ArrayList()
            list.add(i)
            automaton.patterns[v] = list
        }
        automaton.patterns[root] = ArrayList()
        automaton.root = root
    }

    private fun computeFailureFunction(automaton: Automaton) {
        val fallbackNode = TrieNode()
        var c = 'a'
        while (c <= 'z') {
            fallbackNode.setChild(c, automaton.root)
            ++c
        }
        automaton.fail[automaton.root] = fallbackNode
        val queue: ArrayDeque<TrieNode?> = ArrayDeque()
        queue.addLast(automaton.root)
        while (!queue.isEmpty()) {
            val u = queue.removeFirst()
            var c = 'a'
            while (c <= 'z') {
                if (u!!.getChild(c) == null) {
                    ++c
                    continue
                }
                val v = u.getChild(c)
                var w = automaton.fail[u]
                while (w!!.getChild(c) == null) {
                    w = automaton.fail[w]
                }
                automaton.fail[v] = w.getChild(c)
                val list = automaton.patterns[automaton.fail[v]]
                if (automaton.patterns[v] == null) {
                    automaton.patterns[v] = list
                } else {
                    automaton.patterns[v]!!.addAll(list!!)
                }
                queue.addLast(v)
                ++c
            }
        }
        automaton.patterns[automaton.root] = ArrayList()
    }

    class Automaton {
        var root: TrieNode? = null
        var fail: MutableMap<TrieNode?, TrieNode?> = HashMap()
        var patterns: MutableMap<TrieNode?, MutableList<Int>?> = HashMap()
    }
}