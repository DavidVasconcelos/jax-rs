package br.com.alura.loja;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


public class Servidor {
	
	/**
	 * Cria o servidor para expor o serviço, executa como Java App
	 * Para interromper é só dar enter no console
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ResourceConfig config = new ResourceConfig().packages("br.com.alura.loja");
		URI uri = URI.create("http://localhost:8080/");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri,config);
		System.out.println("Servidor rodando");
		System.in.read();
		server.stop();		

	}

}
