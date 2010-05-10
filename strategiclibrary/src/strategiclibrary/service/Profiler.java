package strategiclibrary.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

@CoinjemaObject(type="profiler")
public class Profiler {
    Logger profileLog = Logger.getLogger(Profiler.class);
    int interval = 500;
    ThreadLocal localTimers = new ThreadLocal() {
        protected Object initialValue() {
            return new HashMap();
        }
    };
    Map profileInfo = Collections.synchronizedMap(new TreeMap());
    int count = 0;
    
    public Profiler()
    {}
    
    public Profiler(CoinjemaContext cc)
    {}

    @CoinjemaDependency(method="log4j",hasDefault=true)
    public void setProfileLog(Logger l)
    {
        profileLog = l;
    }
    
    Logger getProfileLog()
    {
        return profileLog;
    }
    
    @CoinjemaDependency(hasDefault=true,method="interval")
    public void setProfileInterval(int interval)
    {
        this.interval = interval;
    }
    
    public void beginTiming(String category)
    {
        Map timeMaps = (Map)localTimers.get();
        pushTiming(timeMaps, category,new long[]{System.currentTimeMillis()});
    }
    
    void pushTiming(Map catMap,String category,long[] time)
    {
        LinkedList ll = (LinkedList)catMap.get(category);
        if(ll == null)
        {
            ll = new LinkedList();
            catMap.put(category,ll);
        }
        ll.addFirst(time);
    }
    
    public synchronized void endTiming(String category)
    {
        Map timeMaps = (Map)localTimers.get();
       long[] time = popTime(timeMaps,category);
       time[0] = System.currentTimeMillis() - time[0];
       if(time[0] < 0 || time[0] > 1000000)
       {
          return;
       }
       long[] total = (long[]) profileInfo.get(category);
       if(total == null)
       {
          total = new long[2];
          total[0] = 0;
          total[1] = 0;
          profileInfo.put(category, total);
       }
       count++;
       total[0] += time[0];
       total[1]++;
       if(count % interval == 0)
       {
          if(getProfileLog().isInfoEnabled())
          {
             logProfileInfo(this.getClass().getName() + " Profiling Info");
          }
       }
    }
    
    long[] popTime(Map catMap,String category)
    {
        long[] ret = null;
        LinkedList ll = (LinkedList)catMap.get(category);
        if(ll != null)
        {
            if(ll.size() > 0) ret = (long[])ll.removeFirst();
            else ret = new long[]{0};
            if(ll.size() == 0) catMap.remove(category);
        }
        else ret = new long[]{0};
        return ret;
    }
    
    synchronized void logProfileInfo(String label)
    {
       Logger log = getProfileLog();
       log.info("=================== " + label + " ================");
       log.info("Number of executions: " + count);
       log.info("Category,total Time (seconds),Execution count,Average time (ms)");
       Iterator profileKeys = profileInfo.keySet().iterator();
       long overallTotal = 0;
       while(profileKeys.hasNext())
       {
          String query = (String) profileKeys.next();
          long[] time = (long[]) profileInfo.get(query);
          log.info(query + "," + (float) ((float)time[0] / (float)1000) + "," + time[1] + "," + (float)((float)time[0]/(float)time[1]));
          overallTotal += time[0];
       }
       log.info("Total time spent: " + (float) ((float)overallTotal / (float)1000) + " seconds");
       log.info("Average time/query = " + (float) ((float)overallTotal / (float)count) + " milliseconds");
       log.info("================== End Profile Information ==================");
    }

}
