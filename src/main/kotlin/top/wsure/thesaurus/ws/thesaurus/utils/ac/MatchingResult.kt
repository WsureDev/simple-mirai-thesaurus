package top.wsure.thesaurus.ws.thesaurus.utils.ac

import kotlinx.serialization.Serializable

/**
 * FileName: MatchingResult
 * Author:   wsure
 * Date:     2021/4/7 4:24 下午
 * Description:
 */
@Serializable
data class MatchingResult(
    /**
     * The index of the pattern being matched.
     */
    var patternIndex: Int,
    /**
     * The index of the last character in a pattern indexed by
     * `patternIndex`.
     */
    var concludingIndex: Int,

    var pattern: String
)