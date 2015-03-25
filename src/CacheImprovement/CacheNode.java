
package CacheImprovement;

public interface CacheNode
{

    void setValue(Object value);


    Object getValue();


    boolean isExpired();
}