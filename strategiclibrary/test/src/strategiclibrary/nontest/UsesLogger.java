package strategiclibrary.nontest;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

@CoinjemaObject
public class UsesLogger {

    public UsesLogger() {
        
    }
    
    private Logger log;

    @CoinjemaDependency(alias="log4j")
    public void setLog(Logger l)
    {
        log = l;
    }
    
    public Logger getLog()
    {
        return log;
    }

}
