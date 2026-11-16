package es.medac.gymfit.interfacestarea67gymfit.model;

public enum Sexo {
    MUJER("Mujer"), HOMBRE("Hombre"), OTRO("Otro");

    private final String nombre;

    Sexo(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
