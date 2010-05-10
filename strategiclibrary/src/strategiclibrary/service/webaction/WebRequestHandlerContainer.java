/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.collections.ConfigurationTree;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.Profiler;
import strategiclibrary.service.classfinder.ClassFinderService;
import strategiclibrary.service.webaction.beans.ErrorsBean;
import strategiclibrary.service.webaction.error.Redirector;

/**
 * Sample Configuration: <service
 * role="strategiclibrary.service.webaction.WebRequestHandler"
 * class="strategiclibrary.service.webaction.WebRequestHandlerContainer"> <property
 * name="user.dir">/home/user/lib/actions.jar </property> <property
 * name="site_title>PM Tool </property> <requiresLogin>add_todo </requiresLogin>
 * <loginProof>user </loginProof> <autoAction>set_context </autoAction>
 * </service>
 */
@CoinjemaObject(type="webRequestService")
public class WebRequestHandlerContainer
        implements WebRequestHandler, ActionExecutor {
    public static final String SKIP = "SKIP_";

    public static final String SKIP_ACTIONS = "SKIP_ACTIONS";

    Map handlers;
    
    ClassFinderService classFinder;
    
    Profiler prof = new Profiler();
    
    Redirector errorRedirector;

    ConfigurationTree actionMapping;

    Properties properties;

    Set<String> actionsRequiringLogin = new HashSet<String>();

    Set<String> actionsRequiringNoLogin = new HashSet<String>();

    String loginProofBean;

    Set<String> automaticActions = new HashSet<String>();

    Set<String> initialActions = new HashSet<String>();

    private boolean first = true;

    private boolean loginStrict = false;

    private boolean checkLogin(HandlerData actionInfo) {
        return (loginProofBean == null)
                || (actionInfo.getBean(loginProofBean) != null);
    }

    protected Redirector getErrorRedirector() {
        return errorRedirector;
    }

    protected void storeSkipActions(HandlerData actions) throws ActionException {
        Set skipActions = (Set) actions.getUserBean(SKIP_ACTIONS, HashSet.class);
        Iterator iter = actions.getActions().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            if (name.startsWith(SKIP)) {
                skipActions.add(name.substring(SKIP.length()));
            }
        }
    }
    
    @CoinjemaDependency(type="redirectorService")
    public void setErrorRedirector(Redirector er)
    {
        this.errorRedirector = er;
    }

    /**
     * Will find the requested actions in the HandlerData and execute them.
     */
    public void performAction(HandlerData actionInfo) throws ActionException {
        initErrors(actionInfo);
        storeRedirectingInfo(actionInfo);
        Iterator iter = getAllActions(actionInfo);
        while (iter.hasNext()) {
            String item = (String) iter.next();
            if (!executeAction(item, actionInfo))
            {
            	actionInfo.flashErrorRequest();
                break;
            }
        }
        if (!actionInfo.hasParam("redirecting")) {
            if (actionInfo.getBean(SKIP_ACTIONS) != null) {
                ((Set) actionInfo.getBean(SKIP_ACTIONS)).clear();
            }
        }
        ErrorsBean errors = (ErrorsBean) actionInfo.getUserBean(ErrorsBean.NAME,ErrorsBean.class);
        if(!errors.isRedirect())
        {
	        String redirect = getErrorRedirector().getLogicalRedirect(actionInfo);
	        if(redirect != null)
	        {
	        	errors.setRedirect(true);
	        	errors.setRedirectPage(redirect);
	        }
        }
	        
        ((ServletHandlerData)actionInfo).copyCachedToRequestScope();
        first = false;
    }

    /**
     * @param item
     * @param actionInfo
     * @throws ActionException
     * @throws ServiceException
     */
    public boolean executeAction(String item, HandlerData actionInfo)
            throws ActionException {
    	getLog().debug("Executing action " + item);
        if (isSkipped(actionInfo, item)) {
            return true;
        }
        WebRequestHandler actionHandler = (WebRequestHandler) handlers
                .get(item);
        if (actionHandler == null) {
        	getLog().debug("Couldn't find action");
            return true;
        }
        try {
        	getLog().debug("Running action");
            if (passesLoginCheck(actionInfo, actionHandler)) {
            	getLog().debug("Passed login check");
                prof.beginTiming(actionHandler.getName());
                actionHandler.performAction(actionInfo);
            }
            else
            {
            	getLog().debug("Didn't pass login check");
            }
        } catch (ActionException e) {
            if (e.isFatal()) {
                getLog().error("Fatal error performing action: " + item, e);
                addError(actionInfo, e);
                return false;
            } else {
                getLog().debug("Problem performing action: " + item, e);
                addError(actionInfo, e);
                if (analyzeErrors(actionInfo))
                    return true;
                else
                    return false;
            }
        } catch (Exception e) {
            getLog().error("Service error performing action: " + item, e);
            addError(actionInfo, e);
            if (analyzeErrors(actionInfo))
                return true;
            else
                return false;
        } finally {
            prof.endTiming(actionHandler.getName());
        }
        return true;
    }

    /**
     * @param actionInfo
     * @param item
     * @return
     * @throws ActionException
     */
    protected boolean isSkipped(HandlerData actionInfo, String item)
            throws ActionException {
        return item == null
                || item.startsWith(SKIP)
                || actionInfo.containsAction(SKIP + item)
                || ((Set) actionInfo.getUserBean(SKIP_ACTIONS, HashSet.class)).contains(item);
    }

    /**
     * @param actionInfo
     * @param actionHandler
     * @return
     */
    private boolean passesLoginCheck(HandlerData actionInfo,
            WebRequestHandler actionHandler) {
        return checkLogin(actionInfo)
                || (!loginStrict && !actionsRequiringLogin
                        .contains(actionHandler.getName()))
                || actionsRequiringNoLogin.contains(actionHandler.getName());
    }

    /**
     * @param actionInfo
     * @return
     */
    private Iterator getAllActions(HandlerData actionInfo) {
        List<String> actions = actionInfo.getActions();
        actions.addAll(0, automaticActions);
        getLog().debug("is first request: " + first);
        if (first) {
            actions.addAll(0, initialActions);
        }
        if(getLog().isDebugEnabled())
            getLog().debug("actions contains: " + actions);
        LinkedList<String> newList = new LinkedList<String>(actions);
        getLog().debug("Executing requested actions");
        return newList.iterator();
    }

    /**
     * @param actionInfo
     * @param errors
     */
    private void initErrors(HandlerData actionInfo) throws ActionException {
        ErrorsBean errors = (ErrorsBean) actionInfo.getUserBean(ErrorsBean.NAME,ErrorsBean.class);
        boolean redirecting = actionInfo.getUserBean("redirecting") != null;
        if (errors != null && !errors.isRedirect() && !redirecting) {
            errors.clear();
            actionInfo.clearFlash();
            actionInfo.removeUserBean("messages");
            getLog().debug("Removing errorbean and messages");
        } else if (errors != null) {
            errors.setRedirectPage(null);
            errors.setRedirect(false);
            actionInfo.removeUserBean("redirecting");
            getLog().debug("Doing a redirect, saving errors and messages");
        }
    }

    /**
     * @param actionInfo
     */
    private void storeRedirectingInfo(HandlerData actionInfo)
            throws ActionException {
        boolean redirecting;
        redirecting = actionInfo.hasParam("redirecting");
        if (redirecting) {
            actionInfo.setUserBean("redirecting", new Boolean(redirecting));
            storeSkipActions(actionInfo);
        }
    }

    /**
     * @param actionInfo
     * @throws ServiceException
     */
    private boolean analyzeErrors(HandlerData actionInfo) {
        ErrorsBean errors;
        String redirectPage = null;
        boolean canContinue = true;
        if ((redirectPage = getErrorRedirector().getRedirectPage(actionInfo)) == null) {
            canContinue = true;
        } else {
            errors = (ErrorsBean) actionInfo.getUserBean(ErrorsBean.NAME);
            errors.setRedirect(true);
            errors.setRedirectPage(redirectPage);
            canContinue = false;
        }
        return canContinue;
    }

    /**
     * Add the given error to the errors bean.
     * 
     * @param actionInfo
     * @param e
     *            void
     */
    private void addError(HandlerData actionInfo, Exception e) {
        ErrorsBean errors = (ErrorsBean) actionInfo.getBean(ErrorsBean.NAME);
        if (errors == null) {
            errors = new ErrorsBean();
            actionInfo
                    .setUserBean(ErrorsBean.NAME, errors);
        }
        errors.addError(e);
    }

    private void getActionHandlers() throws Exception {
        List foundHandlers = classFinder
                .findClassesThatExtend(new Class[] { WebRequestHandler.class });
        Iterator iter = foundHandlers.iterator();
        Class item = null;
        getLog().debug("web properties = " + properties);
        while (iter.hasNext()) {
            try {
                item = (Class) iter.next();
                if(!item.equals(WebRequestHandlerContainer.class))
                {
	                WebRequestHandler action = (WebRequestHandler) item
	                        .newInstance();
	                if (action instanceof AbstractWebAction) {
	                    ((AbstractWebAction)action).setContainer(this);
	                    ((AbstractWebAction)action).setProps(properties);
	                    getLog().debug(
	                            "Adding action handler: " + action.getName());
	                    handlers.put(action.getName(), action);
	                }
                }
            } catch (InstantiationException e1) {
                getLog().info("Couldn't instantiate " + item, e1);
            } catch (IllegalAccessException e1) {
                getLog().info("Illegal access: " + item, e1);
            } catch(Exception e)
            {
            	getLog().error("Something bad happened",e);
            }
        }
    }
    
    @CoinjemaDependency(type="classfinder")
    public void setClassFinder(ClassFinderService f)
    {
        this.classFinder = f;
    }
    
    @CoinjemaDependency(method="config",order=CoinjemaDependency.Order.LAST)
    public void setConfig(Properties props)
    {
        this.properties = props;
        loginProofBean = properties.getProperty("loginProof");
        loginStrict = "true".equals(properties.getProperty("loginStrict"));
        initialize();
    }
    
    @CoinjemaDependency(method="actionsRequiringLogin")
    public void setActionsRequiringLogin(Set<String> actions)
    {
        actionsRequiringLogin = actions;
    }
    
    @CoinjemaDependency(method="actionsRequiringNoLogin")
    public void setActionsRequiringNoLogin(Set<String> actions)
    {
        actionsRequiringNoLogin = actions;
    }
    
    @CoinjemaDependency(method="autoActions")
    public void setAutoActions(Set<String> actions)
    {
        automaticActions = actions;
    }
    
    @CoinjemaDependency(method="initialActions")
    public void setInitialActions(Set<String> actions)
    {
        initialActions = actions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() {
        handlers = new HashMap();
        try {
            getActionHandlers();
        } catch (Exception e) {
            throw new RuntimeException("ClassFinder error finding action handlers",e);
        }
        first = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.WebRequestHandler#getName()
     */
    public String getName() {
        return null;
    }
    
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

	public WebRequestHandlerContainer(CoinjemaContext ctx) {
	}

	public WebRequestHandlerContainer() {
		super();
	}
}