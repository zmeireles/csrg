package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Entity
@Name("faxAttachment")
@Scope(SESSION)
@Table(name = "fax_attachment")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class FaxAttachment extends Model implements Serializable {

	private Long id;
	private byte[] data = null;
	private String filename = "";
	private String contentType = "";
	private FaxLog faxLog;
	
	/*
	 * BUSINESS LOGIC
	 */
	
	//verifica se a informacao é valida para este objecto
	public static boolean ficheiroIsValido(final byte[] ficheiro){
		if(ficheiro != null && ficheiro.length >0 ) return true;
		return false;
	}
	public boolean ficheiroIsValido(){
		return ficheiroIsValido(this.data);
	}
	
	//guarda a informacao
	public boolean save(byte[] data, String filename, String contentType){
		if(ficheiroIsValido(data)){
			this.setData(data);
			this.setFilename(filename);
			this.setContentType(contentType);
			return true;
		}
		return false;
	}
	
	//apaga a informacao
	public void clean(String userNameString){
		this.data = null;
		this.filename = "";
		this.contentType = "";
	}
	
	//responde se este objecto tem informacao
	public boolean eqEmpty(){
		return !(data != null && data.length >0 );
	}
	
	//getters and setters
	@Override
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "fax_attachment_id", nullable = false)
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
		if(ficheiroIsValido(data))
			this.data = data;
	}
	
	@Length(max=255)
	@Column(name = "content_type")
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Length(max=255)
	@Column(name = "filename")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fax_log_id")
	public FaxLog getFaxLog() {
		return faxLog;
	}
	public void setFaxLog(FaxLog faxLog) {
		this.faxLog = faxLog;
	}
	
}
