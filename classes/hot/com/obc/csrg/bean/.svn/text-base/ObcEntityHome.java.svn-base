package com.obc.csrg.bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import com.obc.csrg.constants.GenderEnum;
import com.obc.csrg.constants.BooleanValueEnum;
import com.obc.csrg.constants.PeriodTypeEnum;
import com.obc.csrg.constants.GeographicAreaTypeEnum;
import com.obc.csrg.constants.Constants;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.Config;
import com.obc.csrg.model.DataChangeModel;
import com.obc.csrg.model.DataChangeProperty;
import com.obc.csrg.model.DataChangeValue;
import com.obc.csrg.model.ParentsModel;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.DBFile;
import com.obc.csrg.util.DataChangeUtil;
import com.obc.csrg.util.EmailUtil;
import com.obc.csrg.util.InputFileController;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.events.ModelEventQueue;

public class ObcEntityHome<E extends Model> extends EntityHome<E>{
	
	@In
	protected EntityManager entityManager;
	
	@Logger 
	protected Log log;
	
	@In
	protected FacesMessages facesMessages;
	
	@In
	protected Map<String,String> messages;
	
	@In
	protected Locale locale;
	
	@In(required=false)
	protected StateBeanLocal stateBean;
	
	@RequestParameter 
	protected Long injectedId;
	
	//GUARDA FICHEIROS TEMPORARIAMENTE
	//contentType do upload da foto da tag geral e outros files
    //usado para filtrar o tipo do ficheiro
    private DBFile priFile;
    private DBFile secFile;
    //
    protected static String[] curriculoType = {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/pdf"};
    protected static String[] imageType = {"image/pjpeg","image/jpg","image/jpeg","image/gif","image/x-png","image/png"};
    //protected static String[] ficheiroAnexoType = {"image/pjpeg","image/jpg","image/jpeg","application/pdf"};
    protected static String[] attachFileType = {"image/tif","image/jpg","image/jpeg","image/tiff",
		"application/pdf","application/msword","application/excel","application/mspowerpoint","application/powerpoint",
		"application/x-mspowerpoint","text/plain","application/octet-stream",
		"application/odt","application/sxw","application/ods","application/odp", //?
		"application/rtf","application/x-rtf","text/richtext","application/postscript"};

    public InputFileController inputFileController= new InputFileController();
    
    //model listener. should be handled by sub classes
    protected ModelEventListener modelEventListener;
    
    //popup panel stuff
    private Effect availablePopupItemsEffect;
	private Effect selectedPopupItemsEffect;
	private boolean showPopup = false;
	protected DataChangeUtil<E> dataChange;
	
	protected List<DataChangeUtil> dataChangeList = new ArrayList<DataChangeUtil>();
	
	/*
     * Metodos
     */
	@Override
    public Object getId(){
		if (injectedId==null){
        	return super.getId();
        }else{
        	return injectedId;
        }
    }
	@Override
	@Create
	@Begin(join=true)
    public void create(){
		//stateBean.log4Debug(this.toString(), "[create]");
		super.create();
		if(this.getInstance()!=null){
			//LOAD METHODS
			this.loadAfterCreate();
		}
		
    }
	
	@Destroy
	public void destroy(){
		
	}
	protected void loadAfterCreate(){
		
		// build simple effects for Drag and Drop
		selectedPopupItemsEffect = new Highlight("#fda505");
		selectedPopupItemsEffect.setFired(true);
		availablePopupItemsEffect = new Highlight("#fda505");
		availablePopupItemsEffect.setFired(true);
		dataChange = new DataChangeUtil<E>(this.instance,entityManager,log,stateBean.getUser());
		dataChange.replicateValues();
		dataChangeList.add(dataChange);
	}
	
	@Override
    public String persist(){
		//log.info("[persist] name:#0", instance.toString());
		String result = null;
		if(this.verifyData()){
			Long id = persistData();
			//log.info("[persist] id:#0", id);
			if(id==null)
				result = super.persist();
			else
				result = super.update();
			//stateBean.log4Debug(this.toString(), "[persist] id: #0", this.getInstance().getId());
			//stateBean.injectId(this.getInstance().getId());
			//stateBean.log4Debug(this.toString(), "[persist] injectedId: #0", this.getInstance().getId());
		}
		//log.info("[persist] result:#0", result);
		if(result!=null){
			//LOAD METHODS
			
			this.loadAfterPersist();
			entityManager.flush();
			ModelEventQueue.fireNewModelEvent(this.instance);
		}else{
			//facesMessages.add(messages.get("home.persistFailed"));
		}
		return stringReturnMethodPersistUpdate(result);
	}
	@Override
	public String update(){
		String result = null;
		//log.info("[update]");
		if(this.verifyData()){
			this.compareDataChanges();
			//dataChange.compareChanges();
			updateData();
			//log.info("[update] before super.update()");
			result = super.update();
			//stateBean.log4Debug(this.toString(), "[update] id: #0", this.getInstance().getId());
		}
		//log.info("[persist] result:#0", result);
		if(result!=null){
			//LOAD METHODS
			this.loadAfterUpdate();
			this.handleChangedValues();
			entityManager.flush();
			ModelEventQueue.fireModelUpdatedEvent(this.instance);
		}else{
			//facesMessages.add(messages.get("home.updateFailed"));
		}
		return stringReturnMethodPersistUpdate(result);
	}
	protected boolean verifyData(){
		return true;
	}
	protected void updateData(){}
	
	protected Long persistData(){return null;}
	
	protected void loadAfterPersist(){
		//log.info("[loadAfterPersist] fire newModel event for:#0 of class:#1", this.instance,instance.getClass().toString());
		//ModelEventQueue.fireNewModelEvent(this.instance);
	}
	
	protected void loadAfterUpdate(){
		//log.info("[loadAfterPersist] fire modelUpdated event for:#0 of class:#1", this.instance,instance.getClass().toString());
		//ModelEventQueue.fireModelUpdatedEvent(this.instance);
	}
	
	protected String stringReturnMethodPersistUpdate(String result){
		//stateBean.log4Debug(this.toString(), "[stringReturnMethodPersistUpdate] result: #0", result);
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see org.jboss.seam.framework.EntityHome#remove()
	 */
	@Override
	public String remove(){
		String result = null;
		//add tarefas gerais (logs, regras, ...)
		result = super.remove();
		//stateBean.log4Debug(this.toString(), "[remove] id: #0", this.getInstance().getId());
		//stateBean.log4Debug(this.toString(), "[remove] result: #0", result);
		return stateBean.popBackLinkList();
	}
	/*
	 * function used to remove models. Must be overridden by extended classes
	 */
	public boolean mayRemove(){
		//apenas dizer true quando esta está
		//this.isManaged()
		//e outras regras
		return false;
	}
	
	private void compareDataChanges(){
		for(DataChangeUtil dcu:this.dataChangeList){
			dcu.compareChanges();
		}
	}
	/**
	 * checks for changed values that demand notification/reporting
	 * builds a report and sends by email
	 */
	protected void handleChangedValues(){
		Config config = stateBean.getConfig();
		if(config.getDataChangeNotificationEmail()!=null &&
				!config.getDataChangeNotificationEmail().trim().equals("") &&
				dataChange!=null && dataChange.getDataChangeModel()!=null){
			String subject = "Alterações na tabela: " + dataChange.getDataChangeModel().getAlias();
			String report = "Registo: [" + this.instance + "] alterado<br/>";
			boolean anyReportableChange=false;
			for(DataChangeUtil dcu:this.dataChangeList){
				DataChangeUtil dataChange = dcu;
				log.info("[handleChangedValues] model:#0", dcu.getInstance());
				for(Object m:dcu.getChangedValues()){
					DataChangeProperty p = ((DataChangeValue)m).getDataChangeProperty();
					if(p.isNotifyOnChange()){
						anyReportableChange = true;
						String change = "Campo: [" + p.getAlias() +
										"] alterado. Antigo valor: [" + dataChange.getOldValue(p)+
										"], novo valor: [" + dataChange.getNewValue(p) + "]";
						report += "<br/>" + change;
					}
				}
			}
			//log.info("[handleChangedValues] report:#0", report);
			if(anyReportableChange){
				//send email
				EmailUtil email = new EmailUtil(config.getSmtpHost(),config.getSmtpPort(),
						Boolean.toString(config.getSmtpAuth()),
						config.getSmtpAuthUser(),config.getSmtpAuthPwd());
				try{
				email.send(config.getNotificationSourceName(), config.getNotificationSourceEmail(),
						config.getDataChangeNotificationEmail(), config.getDataChangeNotificationEmail(), 
						subject, report);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * formats a string list of file types to a string, separating the types by colons
	 */
	public String acceptFileType(final String[] types){
		StringBuffer result = new StringBuffer("");
		boolean first = true;
		for(String str : types){
			if(first) first = false;
			else result.append(",");
			result.append(str);
		}
		return result.toString();
	}
	
	/**
	 * verifica se o contentType esta de acordo com o desejado nos imageType
	 */
	protected static boolean fileIsImage(final DBFile file){
		boolean hasTypeImage = fileHasType(file, imageType);
		return hasTypeImage;
	}
	
	/**
	 * verifica se o contentType esta de acordo com o desejado nos curriculoType
	 */
	protected static boolean fileIsCurriculum(final DBFile file){
		boolean hasTypeImage = fileHasType(file, curriculoType);
		return hasTypeImage;
	}
	
	/**
	 * verifica se o contentType esta de acordo com o desejado nos types
	 */
	protected static boolean fileHasType(final DBFile file, final String[] types){
		if(file==null)
			return false;
		return fileHasType(file.getContentType(), types);
	}
	
	/**
	 * verifica se o contentType esta de acordo com o desejado nos types
	 */
	protected static boolean fileHasType(final String fileContentType, final String[] types){
		System.out.print("[home] [ficheiroHasType] "+fileContentType);
		boolean hasType = false;
		if(attachFileType==types) return true;
		for(String t:types){
			if(t.equals(fileContentType)) hasType = true;
		}
		return hasType;
	}
	
	/**
	 * usado para dar string com formato monetario 
	 */
	public static String getCurrencyString(final double val){
		Currency currency = Currency.getInstance("EUR");
		int decimais = currency.getDefaultFractionDigits();
		StringBuffer format = new StringBuffer("0.");
		for(int i=0;i<decimais;i++){
			format.append("0");
		}
		DecimalFormat df = new DecimalFormat(format.toString());
		return currency.getSymbol() + " " + df.format(val);
	}
	
	/**
	 * usado para criar combo de tipo de areas geograficas
	 */
	public static List<SelectItem> getTiposAreaGeografica(final Map<String,String> messages) {
		List<SelectItem> result = new ArrayList<SelectItem>();
		for(GeographicAreaTypeEnum tp: GeographicAreaTypeEnum.values()){
			result.add(new SelectItem(tp,messages.get(tp.getName())));
		}
		return result;
	}
	
	/**
	 * usado para criar combo de lista generos
	 */
	public static List<SelectItem> getGeneros(final Map<String,String> messages) {
		List<SelectItem> generos = new ArrayList<SelectItem>();
		for(GenderEnum g:GenderEnum.values()){
			generos.add(new SelectItem(g,messages.get(g.getName())));
		}
		return generos;
	}
	
	/**
	 * usado para criar combo de lista de valor boolean
	 */
	public static List<SelectItem> getBooleanValue(final Map<String,String> messages) {
		List<SelectItem> booleanValue = new ArrayList<SelectItem>();
		for(BooleanValueEnum bv : BooleanValueEnum.values()){
			booleanValue.add(new SelectItem(bv,messages.get(bv.getName())));
		}
		return booleanValue;
	}
	
	/**
	 * usado para criar combo de lista de tipo de periodo
	 */
	public static List<SelectItem> getTipoPeriodo(final Map<String,String> messages) {
		List<SelectItem> tipoPeriodo = new ArrayList<SelectItem>();
		for(PeriodTypeEnum tp : PeriodTypeEnum.values()){
			tipoPeriodo.add(new SelectItem(tp,messages.get(tp.getName())));
		}
		return tipoPeriodo;
	}
	
	/**
	 * retorna um string com caracteres lower e alguns literais
	 */
	protected static String toSearchRegExp(final String s){
		if(s==null || s.equals("")) return null;
		String literal = "0123456789bcçdfghjklmnpqrstvwxyz *%";
		char[] sl = s.trim().toLowerCase().toCharArray();
		StringBuffer resultado = new StringBuffer("");
		for(char c: sl){
			if(literal.indexOf(c)==-1){
				resultado.append('_');
			}else{
				resultado.append(c);
			}
		}
		return "%" + resultado.toString().replace("*", "%").replace(" ", "%") + "%";
	}
	

	/* 
	 * ParentsModel management
	 */
	/**
	 * remove old parents
	 */
	protected static <T extends ParentsModel<U>, U extends Model> void removeOldParents(EntityManager entityManager, U model){
		Set<T> oldParentList = model.getMyAsc();
		Set<T> childrenList = model.getMyDesc();
		for(T children : childrenList){
			Set<T> childrenParentList = children.getDesc().getMyAsc();
			Set<T> childrenParentListToRemove = new HashSet();
			for(T childrenParent : childrenParentList){
				for(T parentToRemove : oldParentList){
					if(childrenParent.getAsc().getId().equals(parentToRemove.getAsc().getId())){
						childrenParentListToRemove.add(childrenParent);
						entityManager.remove(childrenParent);
					}
				}
			}
			children.getDesc().getMyAsc().removeAll(childrenParentListToRemove);
		}
		for(T parentToRemove : oldParentList){
			entityManager.remove(parentToRemove);
		}
		model.getMyAsc().clear();
	}
	/**
	 * add new parents
	 */
	protected static <T extends ParentsModel<U>, U extends Model> void addNewParents(EntityManager entityManager, U model, U newParent){
		//System.err.println("[BqecomEntityHome] [addNewParents] ");
		Set<T> childrenList = model.getMyDesc();
		Set<T> newParentParentList = newParent.getMyAsc();
		Set<ParentsModel> childrenParentList;
		for(T parentToAdd : newParentParentList){
			ParentsModel novoParentModel = model.newInstanceOfParentModel(parentToAdd.getAsc());
			model.getMyAsc().add(novoParentModel);
			entityManager.persist(novoParentModel);
		}
		ParentsModel novoParentModel = model.newInstanceOfParentModel(newParent);
		model.getMyAsc().add(novoParentModel);
		entityManager.persist(novoParentModel);
		//actualizar children
		Set<T> newParentList = model.getMyAsc();
		for(T children : childrenList){
			childrenParentList = children.getDesc().getMyAsc();
			for(T parentToAdd : newParentList){
				ParentsModel novoParentModelC = children.getDesc().newInstanceOfParentModel(parentToAdd.getAsc());
				childrenParentList.add(novoParentModelC);
				entityManager.persist(novoParentModelC);
			}
		}
	}
	/**
	 * change parents
	 */
	protected static <T extends ParentsModel<U>, U extends Model> void changeParents(EntityManager entityManager, U model, U newParent){
		removeOldParents(entityManager, model);
		entityManager.flush();
		addNewParents(entityManager, model, newParent);
	}
	//file upload
	public InputFileController getInputFileController() {
		return inputFileController;
	}
	
	public void setInputFileController(InputFileController inputFileController) {
		this.inputFileController = inputFileController;
	}
	public void uploadFile(ActionEvent event){
		this.inputFileController.uploadFile(event);
		if(this.inputFileController.getCurrentFile()==null)
			return;
		log.info("[uploadFile] currentFile:#0", this.inputFileController.getCurrentFile().toString());
		try {
			this.priFile.setData(getBytesFromFile(this.inputFileController.getCurrentFile().getFile()));
			log.info("[uploadFile] priFile:#0", this.priFile.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}
	
	//popup stuff
	public void togglePopup(ActionEvent event) {
		this.showPopup = !this.showPopup;
	}
	//drag and drop utilities
	public boolean isAvailableTarget(String targetId){
		if(targetId.indexOf(Constants.DND_AVAILABLE_ITEMS_DROP_BOX)!=-1) return true;
		if(targetId.indexOf(Constants.DND_EMPTY_AVAILABLE_ITEMS_DROP_BOX)!=-1) return true;
		return false;
	}
	public boolean isSelectionTarget(String targetId){
		if(targetId.indexOf(Constants.DND_SELECTED_ITEMS_DROP_BOX)!=-1) return true;
		if(targetId.indexOf(Constants.DND_EMPTY_SELECTED_ITEMS_DROP_BOX)!=-1) return true;
		return false;
	}
	//Getters and setters
	
	public DBFile getPriFile() {
		return priFile;
	}
	public void setPriFile(DBFile priFile) {
		this.priFile = priFile;
	}
	public DBFile getSecFile() {
		return secFile;
	}
	public void setSecFile(DBFile secFile) {
		this.secFile = secFile;
	}
	public Effect getAvailablePopupItemsEffect() {
		return availablePopupItemsEffect;
	}
	public void setAvailablePopupItemsEffect(Effect availablePopupItemsEffect) {
		this.availablePopupItemsEffect = availablePopupItemsEffect;
	}
	public Effect getSelectedPopupItemsEffect() {
		return selectedPopupItemsEffect;
	}
	public void setSelectedPopupItemsEffect(Effect selectedPopupItemsEffect) {
		this.selectedPopupItemsEffect = selectedPopupItemsEffect;
	}
	public boolean isShowPopup() {
		return showPopup;
	}
	public void setShowPopup(boolean showPopup) {
		this.showPopup = showPopup;
	}

}
