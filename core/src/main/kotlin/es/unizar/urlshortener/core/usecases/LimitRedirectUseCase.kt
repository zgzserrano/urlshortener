package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 *  Returns the clicks number for the current day from a url identified by it [id]
 */
interface LimitRedirectUseCase {
    fun limitRedirectByDay(hash: String) : Boolean
}

/**
 * Implementation of [GetClicksDayUseCase].
 */
class LimitRedirectUseCaseImpl (
    private val shortUrlRepositoryService: ShortUrlRepositoryService
) : LimitRedirectUseCase {
    var LIMIT_BY_DAY = 10;
    override fun limitRedirectByDay(hash: String): Boolean {
        var available = false
        val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE
        val shortUrl = shortUrlRepositoryService.findByKey(hash)
        if (shortUrl != null && shortUrl.redirectCount!! < LIMIT_BY_DAY) {
            val now = OffsetDateTime.now()
            if (shortUrl.lastRedirect?.format(dateFormat).equals(now.format(dateFormat))){
                shortUrl.redirectCount = shortUrl.redirectCount?.plus(1)
            }
            else
                shortUrl.redirectCount = 0
            shortUrl.lastRedirect = now
            shortUrlRepositoryService.save(shortUrl)
            available = true
            println("------"+shortUrl.redirectCount+"-----"+shortUrl.lastRedirect)

        }
        return available
    }
}