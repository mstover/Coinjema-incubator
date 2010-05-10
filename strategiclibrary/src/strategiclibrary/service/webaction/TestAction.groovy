package strategiclibrary.service.webaction;

public class TestAction extends AbstractWebAction
{
	public String getName() 
	{ 
		return "test_action";
	}
	
	public void performAction(HandlerData actionInfo)
	{ 
		getLogger().info("Test Action Called!"); 
	}
}