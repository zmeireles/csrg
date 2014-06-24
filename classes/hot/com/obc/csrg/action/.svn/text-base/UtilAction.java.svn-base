/**
 * 
 */
package com.obc.csrg.action;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.UtilActionLocal;
/**
 * @author jmeireles
 *
 */

@Stateless
@Name("utilAction")
public class UtilAction implements UtilActionLocal{

	@Logger 
    private Log log;
	
	@In(required=false)
    private StateBeanLocal stateBean;
	
	public String sayHello(String name) {
		//log.info("[say hello] hello #0",stateBean.getUser().getUsername());
		log.info("[say hello] hello #0",name);
		return "Hello, " + name;
	}
}
