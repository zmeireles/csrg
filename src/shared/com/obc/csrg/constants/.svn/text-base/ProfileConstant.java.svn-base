package com.obc.csrg.constants;

//import org.jboss.seam.annotations.Name;

import java.util.List;
import java.util.ArrayList;

import com.obc.csrg.lib.NameValueConstant;

public final class ProfileConstant 
	extends NameValueConstant{

	private static final long serialVersionUID = -7671257456521131491L;

	private int[] includes = null;
	private boolean base = true;
	private boolean aplicavel = true;
	private boolean entidade = false;
	
    public static final ProfileConstant USER   					= new ProfileConstant(0,false, Constants.PROFILE_USER,new int[]{3});
    public static final ProfileConstant ADMIN     				= new ProfileConstant(1,false, Constants.PROFILE_ADMIN,new int[]{3,4,5});
    public static final ProfileConstant PUBLIC_USER	 			= new ProfileConstant(2,false, Constants.PROFILE_PUBLIC_USER,new int[]{3});
    public static final ProfileConstant PERSON 					= new ProfileConstant(3,false, Constants.PROFILE_PERSON,false);
    public static final ProfileConstant CONTENT_MANAGER 		= new ProfileConstant(4,false, Constants.PROFILE_CONTENT_MANAGER,new int[]{3});
    public static final ProfileConstant ACCESS_MANAGER 			= new ProfileConstant(5,false, Constants.PROFILE_ACCESS_MANAGER,new int[]{3});
    public static final ProfileConstant ONLINE_SUPPORT			= new ProfileConstant(6,false, Constants.PROFILE_ONLINE_SUPPORT,new int[]{3});
    public static final ProfileConstant SYS_ADMIN				= new ProfileConstant(7,false, Constants.PROFILE_SYS_ADMIN,new int[]{3,4,5});
    
    private ProfileConstant(final int type, boolean entidade, final String name)
    {
        super(type, name);
        this.entidade = entidade;
    }
    private ProfileConstant(final int type, boolean entidade, final String name,boolean aplicavel){
        super(type, name);
        this.entidade = entidade;
        this.aplicavel = aplicavel;
    }
    
    private ProfileConstant(final int type, boolean entidade, final String name,int[] includes){
        super(type, name);
        this.entidade = entidade;
        this.includes = includes;
    }
    private ProfileConstant(final int type, boolean entidade, final String name,int[] includes,boolean aplicavel){
        super(type, name);
        this.entidade = entidade;
        this.includes = includes;
        this.aplicavel = aplicavel;
    }

    public static ProfileConstant valueOf(final int type){
        return valueOf(ProfileConstant.class, type);
    }

	public int[] getIncludes() {
		return includes;
	}

	public void setIncludes(int[] includes) {
		this.includes = includes;
	}
    public boolean includes(ProfileConstant profile){
    	if(this.equals(profile))
    		return true;
    	if(this.getIncludes()==null)
    		return false;
    	for(int i = 0;i<this.getIncludes().length;i++){
    		int type =this.getIncludes()[i];
    		final ProfileConstant p = ProfileConstant.valueOf(type);
    		if(p.equals(profile))
    			return true;
    		else{
    			if(p.includes(profile))
    				return true;
    		}
    	}
    	return false;
    }

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public boolean isAplicavel() {
		return aplicavel;
	}

	public void setAplicavel(boolean aplicavel) {
		this.aplicavel = aplicavel;
	}
	public List<String> getChildrenList(){
		List<String> lista = new ArrayList<String>();
		lista.add(Integer.toString(this.getType()));
		if(this.getIncludes()==null)
			return lista;
		for(int i:this.getIncludes()){
			lista.add(Integer.toString(i));
			ProfileConstant.valueOf(i).addChildrenList(lista);
		}
		return lista;
	}
	private void addChildrenList(List<String> lista){
		if(this.getIncludes()==null)
			return;
		for(int i:this.getIncludes()){
			lista.add(Integer.toString(i));
			ProfileConstant.valueOf(i).addChildrenList(lista);
		}
	}
	public List<ProfileConstant> getPerfilChildren(){
		List<ProfileConstant> lista = new ArrayList<ProfileConstant>();
		lista.add(this);
		if(this.getIncludes()==null)
			return lista;
		for(int i:this.getIncludes()){
			lista.add(ProfileConstant.valueOf(i));
			ProfileConstant.valueOf(i).addPerfilChildrenList(lista);
		}
		return lista;
	}
	private void addPerfilChildrenList(List<ProfileConstant> lista){
		if(this.getIncludes()==null)
			return;
		for(int i:this.getIncludes()){
			lista.add(ProfileConstant.valueOf(i));
			ProfileConstant.valueOf(i).addPerfilChildrenList(lista);
		}
	}
	public boolean isEntidade() {
		return entidade;
	}
	public void setEntidade(boolean entidade) {
		this.entidade = entidade;
	}
	
	public static List<Integer> getUserTypes(){
		List<Integer> result = new ArrayList<Integer>();
		//all are users
		result.add(0);
		result.add(1);
		result.add(2);
		result.add(4);
		result.add(5);
		result.add(6);
		return result;
	}
	
}
