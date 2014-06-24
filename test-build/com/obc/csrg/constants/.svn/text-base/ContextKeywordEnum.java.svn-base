package com.obc.csrg.constants;

public enum ContextKeywordEnum {
	areaGeografica,idioma,cnp,dadosDirectos,conhecimentosProfissionais,profissao,formacao,cae,nivelAcademico,nivelFormacao,
	dadosDiscretos,horarioTrabalho,tipoOferta,idade,mesesExperiencia;
	
	public String getName(){
		switch(this){
		case areaGeografica:return Constants.CT_KW_AREA_GEOGRAFICA;
		case idioma:return Constants.CT_KW_IDIOMA;
		case cnp:return Constants.CT_KW_CNP;
		case dadosDirectos:return Constants.CT_KW_DADOS_DIRECTOS;
		case conhecimentosProfissionais:return Constants.CT_KW_CONHECIMENTOS_PROFISSIONAIS;
		case profissao:return Constants.CT_KW_PROFISSAO;
		case formacao:return Constants.CT_KW_FORMACAO;
		case cae:return Constants.CT_KW_CAE;
		case nivelAcademico:return Constants.CT_KW_NIVEL_ACADEMICO;
		case nivelFormacao:return Constants.CT_KW_NIVEL_FORMACAO;
		case dadosDiscretos:return Constants.CT_KW_DADOS_DISCRETOS;//usado para pôr keywords específicas
		case horarioTrabalho:return Constants.CT_KW_HORARIO_TRABALHO;
		case tipoOferta:return Constants.CT_KW_TIPO_OFERTA;
		case idade :return Constants.CT_KW_IDADE;
		case mesesExperiencia:return Constants.CT_KW_MESES_EXPERIENCIA;
		default :return Constants.CT_KW_AREA_GEOGRAFICA;
		}
	}
	public int getWeight(){
		switch(this){
		case areaGeografica:return 10;
		case idioma:return 10;
		case cnp:return 40;
		case dadosDirectos:return 30;
		case conhecimentosProfissionais:return 30;
		case profissao:return 30;
		case formacao:return 30;
		case cae:return 10;
		case nivelAcademico:return 30;
		case nivelFormacao:return 30;
		case dadosDiscretos:return 30;
		case horarioTrabalho:return 20;
		case idade:return 30;
		case mesesExperiencia:return 30;
		default:return 10;
		}
	}
	public boolean isDemanding(){
		switch(this){
		case areaGeografica:return false;
		case idioma:return false;
		case cnp:return true;
		case dadosDirectos:return false;
		case conhecimentosProfissionais:return false;
		case profissao:return false;
		case formacao:return false;
		case cae:return false;
		case nivelAcademico:return false;
		case nivelFormacao:return false;
		case dadosDiscretos:return false;
		case horarioTrabalho:return false;
		case idade:return false;
		case mesesExperiencia:return false;
		default:return false;
		}
	}
	
}
