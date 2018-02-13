package br.com.alura.loja.modelo;

import com.thoughtworks.xstream.XStream;

public class Model {

	public String toXML() {
		return new XStream().toXML(this);
	}

}