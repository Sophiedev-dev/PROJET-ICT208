package view;

public class MainConsole {
    public static void main(String[] args) {
        while (true) {
            new LoginView().afficher();
            System.out.println("Retourner au menu de connexion ? (O/N)");
            if (!new java.util.Scanner(System.in).nextLine().equalsIgnoreCase("O")) {
                break;
            }
        }
        System.out.println("Merci d’avoir utilisé le système.");
    }
}
