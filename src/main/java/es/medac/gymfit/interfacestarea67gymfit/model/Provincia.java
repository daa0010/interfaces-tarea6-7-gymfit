package es.medac.gymfit.interfacestarea67gymfit.model;

/** Provincias con su código INE: el usuario elige por nombre y el código se guarda por detrás. */
public enum Provincia {
    ALAVA("01", "Álava"), ALBACETE("02", "Albacete"), ALICANTE("03", "Alicante"), ALMERIA("04", "Almería"),
    AVILA("05", "Ávila"), BADAJOZ("06", "Badajoz"), BALEARES("07", "Illes Balears"), BARCELONA("08", "Barcelona"),
    BURGOS("09", "Burgos"), CACERES("10", "Cáceres"), CADIZ("11", "Cádiz"), CASTELLON("12", "Castellón"),
    CIUDAD_REAL("13", "Ciudad Real"), CORDOBA("14", "Córdoba"), CORUNA("15", "A Coruña"), CUENCA("16", "Cuenca"),
    GIRONA("17", "Girona"), GRANADA("18", "Granada"), GUADALAJARA("19", "Guadalajara"), GIPUZKOA("20", "Gipuzkoa"),
    HUELVA("21", "Huelva"), HUESCA("22", "Huesca"), JAEN("23", "Jaén"), LEON("24", "León"),
    LLEIDA("25", "Lleida"), RIOJA("26", "La Rioja"), LUGO("27", "Lugo"), MADRID("28", "Madrid"),
    MALAGA("29", "Málaga"), MURCIA("30", "Murcia"), NAVARRA("31", "Navarra"), OURENSE("32", "Ourense"),
    ASTURIAS("33", "Asturias"), PALENCIA("34", "Palencia"), LAS_PALMAS("35", "Las Palmas"), PONTEVEDRA("36", "Pontevedra"),
    SALAMANCA("37", "Salamanca"), TENERIFE("38", "Santa Cruz de Tenerife"), CANTABRIA("39", "Cantabria"), SEGOVIA("40", "Segovia"),
    SEVILLA("41", "Sevilla"), SORIA("42", "Soria"), TARRAGONA("43", "Tarragona"), TERUEL("44", "Teruel"),
    TOLEDO("45", "Toledo"), VALENCIA("46", "Valencia"), VALLADOLID("47", "Valladolid"), BIZKAIA("48", "Bizkaia"),
    ZAMORA("49", "Zamora"), ZARAGOZA("50", "Zaragoza"), CEUTA("51", "Ceuta"), MELILLA("52", "Melilla");

    private final String codigoIne;
    private final String nombre;

    Provincia(String codigoIne, String nombre) {
        this.codigoIne = codigoIne;
        this.nombre = nombre;
    }

    public String codigoIne() {
        return codigoIne;
    }

    public String nombre() {
        return nombre;
    }

    public static Provincia desdeCodigo(String codigo) {
        for (Provincia p : values()) {
            if (p.codigoIne.equals(codigo)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Código de provincia desconocido: " + codigo);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
