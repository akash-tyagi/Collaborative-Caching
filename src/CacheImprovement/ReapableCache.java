package CacheImprovement;





/**
 * A managed cache is one that can receive callbacks asking it to remove
 * any expired objects (based on time timeoutMilliSeconds).
 * The CacheReaper is typically responsible for performing the callback.
 *
 * @see org.shiftone.cache.util.reaper.CacheReaper
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.1 $
 */
public interface ReapableCache extends Cache
{

    /**
     * Method removeExpiredElements
     */
    void removeExpiredElements();
}