package regras_negocio;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import modelo.*;
import repositorio.*;

public class Fachada{
	private static Repositorio repositorio = new Repositorio();
	
	private Fachada() {};
	
	public static void criarEvento(String data, String descricao, int capacidade, double preco ) throws Exception {
		if (preco < 0) 
			throw new Exception("Preço não deve ser negativo\n");
		else if (capacidade < 2)
			throw new Exception("Capacidade deve ser maior ou igual a 2\n");
		else if (data == null)
			throw new Exception("Data não deve ser vazia\n");
		else if (descricao == null)
			throw new Exception("Descrição não deve ser vazia\n");
			
		Evento evento = new Evento(repositorio.gerarId(), data, descricao, capacidade, preco);
		repositorio.adicionar(evento);
		//repositorio.salvarObjetos();
		
	}
	
	public static void criarParticipante(String cpf ,String nascimento ) throws Exception{
		if(repositorio.localizarParticipante(cpf) != null)
			throw new Exception("Participante já existe\n");
		
		Participante participante = new Participante(cpf, nascimento);
		repositorio.adicionar(participante);
		//repositorio.salvarObjetos();
	}
	
	public static void criarConvidado(String cpf ,String nascimento, String empresa) throws Exception{
		if(repositorio.localizarParticipante(cpf) != null)
			throw new Exception("Convidado já existe\n");
		
		Convidado convidado = new Convidado(cpf, nascimento, empresa);
		repositorio.adicionar(convidado);
		//repositorio.salvarObjetos();
	}
	
	public static void criarIngresso(int id, String cpf, String telefone) throws Exception{
		
		if (repositorio.localizarEvento(id) == null)
			throw new Exception("Evento não existe\n");
		else {
			Evento evento = repositorio.localizarEvento(id);
			if(evento.getIngressos().size() >= evento.getCapacidade())
				throw new Exception("Evento atingiu sua capacidade máxima de ingressos\n");
		}
			
		if (repositorio.localizarParticipante(cpf) == null)
			throw new Exception("Participante não existe\n");
		else if (telefone == null)
			throw new Exception("Telefone não deve ser vazio\n");
		
		Participante participante = repositorio.localizarParticipante(cpf);
		Evento evento = repositorio.localizarEvento(id);
	// ------------------------------------------------------------------------------	
		String codigo = id + "-" + cpf;
		Ingresso ingresso = new Ingresso(codigo, telefone, evento, participante);
		repositorio.adicionar(ingresso); 
	// ------------------------------------------------------------------------------
		ArrayList<Ingresso> ingressosDoParticipante = participante.getIngressos();
		Ingresso ingressoLocalizado = repositorio.localizarIngresso(codigo);
		ingressosDoParticipante.add(ingressoLocalizado);
		// participante.getIngressos().add(ingresso);
    // ------------------------------------------------------------------------------
		ArrayList<Ingresso> ingressosDoEvento = evento.getIngressos();
		ingressosDoEvento.add(ingressoLocalizado);
		// evento.getIngressos().add(ingresso);
	// ------------------------------------------------------------------------------
		
		
	}
	
	public static void apagarEvento(int id) throws Exception{
		if(repositorio.localizarEvento(id) == null)
			throw new Exception("Evento não existe\n");
		else {
			Evento evento = repositorio.localizarEvento(id);
			if(evento.getIngressos().size() > 0)
				throw new Exception("Evento possui ingressos registrados\n");
		}
		
		Evento evento = repositorio.localizarEvento(id);
		repositorio.remover(evento);
		//repositorio.salvarObjetos();
	}
	
	public static void apagarParticipante(String cpf) throws Exception{


		// Verifica a existência do participante.

		if(repositorio.localizarParticipante(cpf) == null)
			throw new Exception("Participante não existe\n");
		else {

			Participante participante = repositorio.localizarParticipante(cpf); // Resgata o participante atrelado ao CPF.
			ArrayList<Ingresso> ingressosDoParticipante = participante.getIngressos(); // Resgata a lista de ingressos do participante.
			int tamanho = ingressosDoParticipante.size(); // Descobrir a quantidade de ingressos.
			if (tamanho >0) { // Verifica se o participante tem ingressos.
				Ingresso ultimoIngresso = ingressosDoParticipante.get(tamanho-1); // Resgatar o ultimo ingresso do participante.
				Evento eventoDoIngresso = ultimoIngresso.getEvento(); // Resgatar o evento atrelado ao ingresso do participante.
				String dataEventoDoIngresso = eventoDoIngresso.getData(); // Resgatar a data do evento atralelado ao ingresso e atrelado ao participante.
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formatar a data para dia/mês/ano.
				LocalDate dataTransformada = LocalDate.parse(dataEventoDoIngresso, formatter); // Transformando a String em um objeto LocalDate
				LocalDate dataAtual = LocalDate.now(); // Resgatando o horário atual



				// Caso a data do evento vinculado ao ingresso estiver ultrapassada, entrará nessa condição.
				if(dataTransformada.isBefore(dataAtual)){
					ingressosDoParticipante.clear(); // Apaga todos os ingressos atrelado ao participante.

					// Resgata todos os ingressos e, em seguida, remove todos os ingressos associados ao CPF especificado.
					
					ArrayList<Ingresso> ingressosApagar = repositorio.getIngressos(); // Resgata todos os ingressos do repositório.
					
				
					for (int i = ingressosApagar.size()- 1; i >= 0; i--) {
						Ingresso ingressoBase = ingressosApagar.get(i);
						String codigo = ingressoBase.getCodigo();
						String[] partes = codigo.split("-");
						if (partes[1].equals(cpf)) {
							ingressosApagar.remove(i);
						}
					}

					// Resgata todos os eventos e, para cada evento, verifica na lista de ingressos do evento e remove o CPF especificado.


					ArrayList <Evento> eventosApagar = repositorio.getEventos();

					for (int i = eventosApagar.size()-1 ; i >= 0; i--){
						Evento eventoBase = eventosApagar.get(i);
						ArrayList<Ingresso> ingressosApagar1 = eventoBase.getIngressos();

						for (int j = ingressosApagar1.size()-1; j >=0;j--){
							Ingresso ingressoBase = ingressosApagar1.get(j);
							String codigo = ingressoBase.getCodigo();
							String[] partes = codigo.split("-");
							if (partes[1].equals(cpf)) {
								ingressosApagar1.remove(j);
						}
					}
						
				}

				repositorio.remover(participante);

			}
			// Lance uma exceção se a data não foi ultrapassada.
				else{
					throw new Exception("Não é possível apagar, pois o último ingresso não foi ultrapassado: " +
					ultimoIngresso.getCodigo());
				
					}

				}
			else{
				// Se o participante não tiver ingressos, ele já será apagado de uma vez.
				repositorio.remover(participante);
			}
			
		}
	}
	
	
}