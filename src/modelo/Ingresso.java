package modelo;
import modelo.Convidado;

public class Ingresso {
	// Atributtes
	private String codigo;
	private String telefone;
	private Evento evento;
	private Participante participante;
	
	// Constructors
	public Ingresso(String codigo,String telefone,Evento evento,Participante participante) {
		this.codigo = codigo;
		this.telefone = telefone;
		this.evento = evento;
		this.participante = participante;
	}
	
	// Getters and Setters
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public Evento getEvento() {
		return evento;
	}
	
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	
	public Participante getParticipante() {
		return participante;
	}
	
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}
	
	// Methods
	public double calcularPreco() {
	    double price = evento.getPreco();
	    int idade = participante.calcularIdade();

	   

	    if (participante instanceof Convidado) {
	        
	      
	        if (idade < 18) {
	            price *= 0.4;
	        } else if (idade >= 18 && idade < 60) {
	            price *= 0.5;
	        } else if (idade >= 60) {
	            price *= 0.3;
	        }
	    } else if (participante instanceof Participante) {
	       
	 
	        if (idade < 18) {
	            price *= 0.9;
	        } else if (idade >= 60) {
	            price *= 0.8;
	        }
	    }
	    return price;
	}

	
	
	@Override
	public String toString() {
		return "Ingresso [codigo=" + codigo + ", telefone=" + telefone + ", Evento=[ capacidade=" + evento.getCapacidade()+", data=" + evento.getData()+
				  ",id=" + evento.getId() + ", preço=" + evento.getPreco() + "]"+
				 ", Participante [cpf=" + participante.getCpf() + ", nascimento=" + participante.getNascimento() + "]\n"; 

				 
	//}
}

}