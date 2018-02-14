package br.com.alura.loja;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ClientTest {

	private HttpServer server;
	private Client client;
	private WebTarget target;

	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());
        this.client = ClientBuilder.newClient(config);
	}

	@After
	public void mataServidor() {
		this.server.stop();
	}

	@Test
	public void testaConexaoComServidor() {

		this.target = client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));

	}

	@Test
	public void testaQueBuscaUmCarrinhoTrazEsperado() {

		this.target = client.target("http://localhost:8080");
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());

	}

	@Test
	public void testaQueAConexaoComOServidorFuncionaNoPathDeProjetos() {

		this.target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<nome>Minha loja"));

	}

	@Test
	public void testaQueBuscaUmProjetoTrazEsperado() {

		this.target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		assertEquals("Minha loja", projeto.getNome());

	}

	@Test
	public void testaAdicionaCarrinhoViaPost() {

		this.target = client.target("http://localhost:8080");
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
		carrinho.setRua("Rua Vergueiro");
		carrinho.setCidade("Sao Paulo");
		String xml = carrinho.toXML();

		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("/carrinhos").request().post(entity);
		Assert.assertEquals("http://localhost:8080/carrinhos/2", response.getHeaderString("Location"));

	}

	@Test
	public void testaDeletaProdutoCarrinhoViaDelete() {

		this.target = client.target("http://localhost:8080");

		Response response = target.path("/carrinhos/1/produtos/6237").request().delete();
		Assert.assertEquals(200, response.getStatus());

	}
	
	@Test
	public void testaAlteraProdutoCarrinhoViaPut() {

		this.target = client.target("http://localhost:8080");
		Produto produto = new Produto(3467L,5);
		
		String xml = produto.toXML();

		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("/carrinhos/1/produtos/3467/quantidade").request().put(entity);
		Assert.assertEquals(200, response.getStatus());

	}

}
