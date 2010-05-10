/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.coinjema.collections.HashTree;
import org.coinjema.collections.HashTreeTraverser;

/**
 * @author Michael Stover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeSorter implements HashTreeTraverser {
	
	Object key;
	Comparator comper;
	
	
	public TreeSorter(Object key,Comparator comper){
		this.key = key;
		this.comper = comper;
	}
   
   public TreeSorter(Comparator comper)
   {
      this.comper = comper;
   }
   
   public TreeSorter()
   {      
   }
   
   public TreeSorter(Object key)
   {
      this.key = key;
   }

	/* (non-Javadoc)
	 * @see org.apache.jorphan.collections.HashTreeTraverser#addNode(java.lang.Object, org.apache.jorphan.collections.HashTree)
	 */
	public void addNode(Object arg0, HashTree arg1) {
		if(key == null || arg0.equals(key))
		{
			Collection coll = arg1.list();
			if(coll instanceof List)
			{
            if(comper != null)
			   {
               Collections.sort((List)coll,comper);
            }
            else
            {
               Collections.sort((List)coll);
            }
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.jorphan.collections.HashTreeTraverser#subtractNode()
	 */
	public void subtractNode() {

	}

	/* (non-Javadoc)
	 * @see org.apache.jorphan.collections.HashTreeTraverser#processPath()
	 */
	public void processPath() {

	}

}
