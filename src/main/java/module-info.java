module es.medac.gymfit.interfacestarea67gymfit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens es.medac.gymfit.interfacestarea67gymfit to javafx.fxml;
    exports es.medac.gymfit.interfacestarea67gymfit;
}