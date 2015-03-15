package CacheImprovement;




/**
 * Class FifoNode
 *
 *
 * @author <a href="mailto:jeff@shiftone.org">Jeff Drost</a>
 * @version $Revision: 1.1 $
 */
class FifoNode implements CacheNode
{

    Object         key         = null;
    Object         value       = null;
    LinkedListNode fifoNode    = null;
    long           timeoutTime = 0;

    public final boolean isExpired()
    {

        long timeToGo = timeoutTime - System.currentTimeMillis();

        return (timeToGo <= 0);
    }


    public final Object getValue()
    {
        return this.value;
    }


    public final void setValue(Object value)
    {
        this.value = value;
    }


    public String toString()
    {
        return "(fifo-" + String.valueOf(key) + ")";
    }
}
