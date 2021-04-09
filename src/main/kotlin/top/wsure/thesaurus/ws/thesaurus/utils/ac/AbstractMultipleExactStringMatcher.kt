package top.wsure.thesaurus.ws.thesaurus.utils.ac


/**
 * Copyright (C), 上海维跃信息科技有限公司
 * FileName: AbstractMultipleExactStringMatcher
 * Author:   wsure
 * Date:     2021/4/7 10:38 上午
 * Description:
 */
interface AbstractMatcher<T> {
    fun match(text: String,patterns: Collection<T>): List<MatchingResult<T>>

    fun constructACAutomaton(patterns: Collection<T>): AcNode<T>

    fun match(text: String,automaton: AcNode<T>): List<MatchingResult<T>>
}