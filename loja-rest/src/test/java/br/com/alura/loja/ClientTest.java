package br.com.alura.loja;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ClientTest {
	
	private HttpServer server;
	
	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
	}
	
	@After
	public void mataServidor() {
		this.server.stop();
	}
	
	@Test
	public void testaConexaoComServidor () {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		
	}
	
	@Test
	public void testaQueBuscaUmCarrinhoTrazEsperado () {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
		
	}
	
	@Test
    public void testaQueAConexaoComOServidorFuncionaNoPathDeProjetos() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        String conteudo = target.path("/projetos/1").request().get(String.class);
        Assert.assertTrue(conteudo.contains("<nome>Minha loja"));


    }
	
	@Test
	public void testaQueBuscaUmProjetoTrazEsperado () {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		assertEquals("Minha loja", projeto.getNome());
		
	}

}
