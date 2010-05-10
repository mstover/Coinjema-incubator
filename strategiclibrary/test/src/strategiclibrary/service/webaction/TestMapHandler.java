package strategiclibrary.service.webaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;

public class TestMapHandler extends TestCase {

    public TestMapHandler() {
        super();
    }

    public TestMapHandler(String arg0) {
        super(arg0);
    }
    
    public void testParameters() throws Exception
    {
        MultiMap request = new MultiHashMap();
        Map<String,Serializable> user = new HashMap<String,Serializable>();
        Map<String,Object> app = new HashMap<String,Object>();
        request.put("param_1","value_1");
        request.put("param_2","value_2a");
        request.put("param_2","value_2b");
        MapHandlerData info = new MapHandlerData(request,user,app);
        assertEquals("value_1",info.getParameter("param_1"));
        assertEquals("value_2a",info.getParameter("Param_2"));
        assertEquals("value_2b",info.getParameterValues("param_2")[1]);
        assertEquals(2,info.getParameterValuesAsList("param_2").size());
        info.setRequestBean("param_3","value_3");
        assertEquals("value_3",info.getParameter("param_3"));
        assertEquals("value_3",info.getBeanMap().get("param_3"));
    }
    
    public void testActionList() throws Exception
    {
    	MultiMap request = new MultiHashMap();
    	Map<String,Serializable> user = new HashMap<String,Serializable>();
    	Map<String,Object> app = new HashMap<String,Object>();
    	request.put("actionzzz","do_action_1");
    	request.put("actionzzz","do_action_2");
    	request.put("ServletPath","do_action_3/do_action_4/do_action_5.jsp");
    	MapHandlerData info = new MapHandlerData(request,user,app);
    	info.getActions();
    	assertTrue(info.containsAction("do_action_4"));
    	assertTrue(info.containsAction("do_action_5"));
    }

}
