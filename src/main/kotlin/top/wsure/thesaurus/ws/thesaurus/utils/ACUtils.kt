package top.wsure.thesaurus.ws.thesaurus.utils

/**
 * FileName: ACUtils
 * Author:   wsure
 * Date:     2021/4/7 11:38 上午
 * Description:
 */
class ACUtils {
    companion object{
        fun distinctCharSet(patterns: List<String>):List<Char>{
            return patterns.map { it.toCharArray().asList() }.flatten().distinct().sorted()
        }
    }
}