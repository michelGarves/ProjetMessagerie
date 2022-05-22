/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.noswing.messagerie;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import plum.console.Clavier;

/**
 *
 * @author thbogusz
 */
public class GestionMessage {

    public GestionMessage(String identifiant) throws SQLException {
        // Création de l'objet jdbc permettant d'administrer les utilisateurs
        JdbcMessage jdbc = new JdbcMessage();
        
        String champ [] = {"destinataireUsers", "objet", "message"};
        String titre = "MESSAGES";

        String[] menu = {"Voir les messages", "Envoyer un message",
            "Supprimer un message", "Répondre à un message"};
        String [] idTab = {identifiant};
        String[] reponse ;
        
        int rep;
        do {
            rep = GMenu.show(titre, menu);


            switch (rep) {
                case 1:
                    ResultSet rs = jdbc.getAllMessages(identifiant);
                    
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String origine = rs.getString("origineUsers");
                            String destinataire = rs.getString("destinataireUsers");
                            String objet  = rs.getString("objet");
                            String message = rs.getString("message");
                            Date dateSend = rs.getDate("dateEnvoi");
                            String etat = rs.getString("etat");

                            // print the results
                            System.out.print("----------------------\n");
                            System.out.format("ID MESSAGE : %s \n" +  "ID ORIGINE : %s \n" +  "ID DESTINATAIRE : %s \n" +  "OBJET : %s\n" 
                                    + "MESSAGE : %s\n" +  "DATE D'ENVOI : %s\n" +  "ETAT : %s\n",
                                    id, origine, destinataire, objet, message, dateSend, etat);
                            System.out.print("----------------------\n");
                        }
                        
                    
                    
                    break;

                case 2:
                    
                    reponse = GForm.show("Envoyer un message", champ, null);
                    
                    int lenId = idTab.length;
                    int lenReponse = reponse.length;
                    
                    String[] concate = new String[lenId + lenReponse];
                    System.arraycopy(idTab, 0, concate, 0, lenId);  
                    System.arraycopy(reponse, 0, concate, lenId, lenReponse);
                    
                    if(concate != null){
                        jdbc.insertMessage(concate);
                    }
                    
                    break;

                case 3:
                    int id =Clavier.lireInt( "Id du message que vous voulez supprimer ? : ");
                    jdbc.deleteMessage(id);
                    
                    break;

                case 4:

                    break;

            }
        } while (rep != 0);

    }
}
