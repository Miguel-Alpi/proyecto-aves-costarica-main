package com.universidad.avescr;

import org.jpl7.*;

public class PrologConnector {
    public static void main(String[] args) {
        // Ruta relativa desde la raíz del proyecto
        String plFile = "prolog/base_conocimiento.pl";

        Query consult = new Query("consult", new Term[] { new Atom(plFile) });
        System.out.println("Carga del archivo Prolog: " + (consult.hasSolution() ? "OK" : "FALLÓ"));

        Query q = new Query("buscar_ave_flex(Ave, rojo, cualquiera, cualquiera)");
        while (q.hasMoreSolutions()) {
            System.out.println("Ave encontrada: " + q.nextSolution().get("Ave"));
        }
    }
}
