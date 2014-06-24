package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("dbFile")
@Scope(SESSION)
@Table(name = "db_file")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DBFile extends Model implements Serializable {

	private Long id;
	private byte[] data = null;
	private String filename = "";
	private String contentType = "";
	private Date creationDate = new Date();
	private Date changedDate = new Date();
	private String creationUsername = null;//username that created the file
	private String changedUsername = null;//username that changed the file
	
	//business functions
	public File obtemFile(String header){
		File file = new File(header.trim().replace(" ", "_") + "_" + this.getId() + 
				"." + this.getFileExtension(this.filename)); //+ "_" + this.nomeFicheiro);
		//File file = new File(header.trim().replace(" ", "_") + "_" + this.getId() + "_" + this.nomeFicheiro);
		try{
			OutputStream os = new FileOutputStream(file);
			os.write(this.data);
			os.close();
			return file;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public String getFileExtension(String filename){
		String ext = "";
		String[] parts = filename.split("\\.");
		if(parts.length>0)
			return parts[parts.length-1];
		return ext;
	}
	//getters and setters
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "db_file_id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "data")
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		//System.out.println("user photo - setData");
		if(fileIsValid(data)){
			//System.out.println("user photo - setData - data is valid");
			this.data = data;
		}
	}
	@Length(max=255)
	@Column(name = "filename")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	@Length(max=255)
	@Column(name = "content_type")
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@Column(name = "changed_date")
	public Date getChangedDate() {
		return changedDate;
	}
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}
	@Length(max=255)
	@Column(name = "creation_username")
	public String getCreationUsername() {
		return creationUsername;
	}
	public void setCreationUsername(String creationUsername) {
		this.creationUsername = creationUsername;
	}
	@Length(max=255)
	@Column(name = "changed_username")
	public String getChangedUsername() {
		return changedUsername;
	}
	public void setChangedUsername(String changedUsername) {
		this.changedUsername = changedUsername;
	}
	
	
	/*
	 * BUSINESS LOGIC
	 */
	
	@Override
	public String toString(){
		if(filename.contains("\\")) return filename.substring(filename.lastIndexOf("\\")+1, filename.length());
		return (!filename.equals(""))? filename : "file";
	}
	
	//actualiza o userName na alteracao de informacao
	private void saveUserNameString(String username){
		if(this.creationUsername != null && !this.creationUsername.equals("")) this.creationUsername = username;
		this.changedUsername = username;
	}
	
	//actualiza a date de modificacao da informacao
	private void updateDate(){
		this.changedDate = new Date();
	}
	
	//verifica se a informacao é valida para este objecto
	public static boolean fileIsValid(final byte[] file){
		if(file != null && file.length >0 ) return true;
		return false;
	}
	public boolean fileIsValido(){
		return fileIsValid(this.data);
	}
	
	//guarda a informacao
	public boolean save(byte[] data, String filename, String contentType, String username){
		if(fileIsValid(data)){
			this.setData(data);
			this.setFilename(filename);
			this.setContentType(contentType);
			this.updateDate();
			this.saveUserNameString(username);
			return true;
		}
		return false;
	}
	
	//apaga a informacao
	public void clean(String username){
		this.data = null;
		this.filename = "";
		this.contentType = "";
		this.updateDate();
		this.saveUserNameString(username);
	}
	
	//responde se este objecto tem informacao
	public boolean eqEmpty(){
		return !(data != null && data.length >0 );
	}
	
	//KEY USADA NO LINK PARA AUMENTAR A SEGURANCA NOS DOWNLOADS
	//é um codigo simples para evitar ser facil fazer downloads de ficheiros do qual nao tem o link
	public String obterCert(){
		Long key = 1L;
		if(this.getId() != null && !this.getId().equals(0L)) key += this.id;
		key *= this.getFilename().hashCode();
		key *= this.getCreationDate().hashCode();
		key *= this.getChangedDate().hashCode();
		return String.valueOf(key);
	}
	public boolean valorIsCert(String key){
		return this.obterCert().equals(key);
	}
	
}
