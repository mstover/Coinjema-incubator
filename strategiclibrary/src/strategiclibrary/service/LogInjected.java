package strategiclibrary.service;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

@CoinjemaObject
public abstract class LogInjected {
    private Logger log;

    @CoinjemaDependency(alias="log4j")
    public void setLog(Logger l)
    {
        log = l;
    }
    
    protected Logger getLog()
    {
        return log;
    }

}
