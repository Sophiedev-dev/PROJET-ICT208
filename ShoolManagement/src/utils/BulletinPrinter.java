package utils;

import model.Bulletin;

public class BulletinPrinter {

    public static void imprimerBulletin(Bulletin bulletin) {
        System.out.println("\n==============================");
        System.out.println("🏫 BULLETIN SCOLAIRE");
        System.out.println("------------------------------");
        System.out.println("Élève : " + bulletin.getEleve().getNom());
        System.out.println("Classe : " + bulletin.getClasse().getNom());
        System.out.println("Trimestre : " + bulletin.getTrimestre());
        System.out.println("------------------------------");
        System.out.printf("Moyenne : %.2f\n", bulletin.getMoyenne());
        System.out.println("Rang : " + bulletin.getRang());
        System.out.println("Mention : " + bulletin.getMention());
        System.out.println("==============================\n");
    }
}
