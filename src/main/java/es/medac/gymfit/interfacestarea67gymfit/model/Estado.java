package es.medac.gymfit.interfacestarea67gymfit.model;

/** Estado que ve el usuario en la lista, siempre con texto y no solo con color. */
public enum Estado {
    AL_DIA("Al día", "Al día"),
    VENCE_PRONTO("Vence pronto", "Vencen pronto"),
    VENCIDO("Vencido", "Vencidos"),
    CONGELADO("Congelado", "Congelados"),
    BAJA("De baja", "De baja");

    private final String etiqueta;
    private final String etiquetaFiltro;

    Estado(String etiqueta, String etiquetaFiltro) {
        this.etiqueta = etiqueta;
        this.etiquetaFiltro = etiquetaFiltro;
    }

    public String etiqueta() {
        return etiqueta;
    }

    public String etiquetaFiltro() {
        return etiquetaFiltro;
    }
}
