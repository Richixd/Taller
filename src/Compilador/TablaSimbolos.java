package Compilador;

public class TablaSimbolos {

    

    private String id;
    private String tipoDato;
    private Object valor;
    // Agrega aquí los atributos 

    // Constructor
    public TablaSimbolos(String id, String tipoDato, Object valor) {
        this.id = id;
        this.tipoDato = tipoDato;
        this.valor = valor;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    // Agrega aquí los métodos adicionales que necesites
}

    
    

