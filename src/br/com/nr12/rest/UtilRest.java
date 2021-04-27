package br.com.nr12.rest;

import java.io.File;
import java.io.StringWriter;				// Classe utilizada para criar uma cadeia de caracteres.

import javax.ws.rs.core.Response;			/* Define o contrato entre uma instância e voltou a tempo de 
											 * execução quando um aplicativo precisa fornecer metadados 
											 * para o tempo de execução. */
import javax.ws.rs.core.Response.ResponseBuilder;		/* Uma classe usada para construir exemplos de 
														 * resposta que contêm metadados, em vez de ou 
														 * em adição a uma entidade. */

import org.codehaus.jackson.map.ObjectMapper;		/* Classe ObjectMapper fornece funcionalidades para 
													 * converter objetos Java para JSON*/


public class UtilRest {

	/*
	 * Abaixo o método responsável por enviar a resposta ao cliente sobre 
	 * a transação realizada, inclusão, consulta, edição ou exclusão,
	 * realização com sucesso.
	 * Repare que o método em questão aguarda que seja repassado um 
	 * conteúdo que será referenciado por um objeto chamado result.
	 */
	public Response buildResponse(Object result){
		
		/*
		 * Cria a instância da Classe StringWriter para o objeto fw.
		 * Isto que este objeto é quem estará referenciando o conteúdo
		 * repassado como resposta para o lado cliente.
		 */
		StringWriter fw = new StringWriter(); 
		
		try{
			/*
			 * Cria a instância da classe ObjectMapper para o objeto
			 * mapper.
			 */
			ObjectMapper mapper = new ObjectMapper();
			
			/*
			 * Acessa o método writeValue, por meio do objeto mapper,
			 * passando como parâmetro o objeto fw e o conteúdo do 
			 * objeto result, na realidade está criando um mapeamento
			 * de dados onde o objeto fw é a chave do valor de um 
			 * conteúdo referenciado pelo objeto result.
			 * result pode conter a mensagem, "Cadastro efetuado com 
			 * sucesso", ou "Exclusão efetuada com sucesso" ou outra
			 * qualquer dependendo da transação realizada.
			 */
			mapper.writeValue(fw, result); // converte objeto java para json
			
			/*
			 * Monta o objeto de resposta com status 200 (ok), junto 
			 * com o objeto result convertido para JSON pelo objeto fw
			 * para o cliente no formato String. 
			 */
			
			return Response.ok(fw.toString()).build();
		}catch(Exception ex){
			return this.buildErrorResponse(ex.getMessage());
		}
		
	}
	
	public Response buildResponse(File result){
		
		try{
			return Response.ok((Object)result).build();
		}catch(Exception ex){
			return this.buildErrorResponse(ex.getMessage());
		}
		
	}
	
	/*	public Response buildResponse (JRExporter result){
		
		try{
			return Response.ok((Object)result).build();
		}catch(Exception ex){
			return this.buildErrorResponse(ex.getMessage());
		}
		
	}*/
	
	
	
	/*
	 * Abaixo o método responsável por enviar a resposta ao cliente sobre
	 * a transação realizada, inclusão, consulta, edição ou exclusão, ao 
	 * cliente, não realizadas com sucesso, ou seja, que contenha algum erro.
	 * Repare que o método em questão aguarda que seja repassado um conteúdo 
	 * que será refereciado pelo por um objeto chamado rb.
	 */
	public Response buildErrorResponse(String str){
		
		/*
		 * Abaixo o objeto rb recebe o status do erro.
		 */
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		
		/*
		 * Define a entidade (objeto de resposta), que nesse caso é uma mensagem
		 * que será retornado para o cliente.
		 */
		rb = rb.entity(str); //define o objeto de resposta
		
		/*
		 * Define o tipo de retorno desta entidade(objeto), no
		 * caso é definido como texto simples.
		 */
		rb = rb.type("text/plain");
		
		/*
		 * Retorna o objeto de resposta com status 500 (erro),
		 * junto com a String contendo a mensagem de erro.
		 */
		return rb.build();
	}

	// utilizar o metodo abaixo => buildErrorValidationResponse()
	/*
	public Response buildErrorResponse(Object result){
		
		StringWriter fw = new StringWriter(); 
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(fw, result); // converte objeto java para json
			
			ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			rb = rb.entity(fw.toString());
			rb = rb.type("text/plain");
			return rb.build();
			
			//return Response.ok(fw.toString()).build();
		}catch(Exception ex){
			return this.buildErrorResponse(ex.getMessage());
		}
	}
	*/
	
	public Response buildErrorValidationResponse(Object result){
		
		StringWriter fw = new StringWriter(); 
		
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(fw, result); // converte objeto java para json
			
			ResponseBuilder rb = Response.status(Response.Status.BAD_REQUEST);
			rb = rb.entity(fw.toString());
			rb = rb.type("text/plain");
			return rb.build();
			
		}catch(Exception ex){
			return this.buildErrorResponse(ex.getMessage());
		}
	}
}
