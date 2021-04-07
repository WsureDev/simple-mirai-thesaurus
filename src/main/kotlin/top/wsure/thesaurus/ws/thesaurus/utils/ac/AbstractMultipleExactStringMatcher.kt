package top.wsure.thesaurus.ws.thesaurus.utils.ac


/**
 * Copyright (C), 上海维跃信息科技有限公司
 * FileName: AbstractMultipleExactStringMatcher
 * Author:   wsure
 * Date:     2021/4/7 10:38 上午
 * Description:
 */
interface AbstractMultipleExactStringMatcher {
    fun match(text: String, vararg patterns: String): List<MatchingResult>

    /**
     * This class represents a match.
     */
    fun constructACAutomaton(patterns: List<String>): AhoCorasickMatcherCopy.Automaton

    fun match(text: String,automaton: AhoCorasickMatcherCopy.Automaton): List<MatchingResult>
}