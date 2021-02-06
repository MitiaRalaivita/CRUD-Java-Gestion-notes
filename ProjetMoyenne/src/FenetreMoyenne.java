
import javax.swing.*;




import java.sql.*; 
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FenetreMoyenne extends JFrame {
		
	protected String titre = "";
	String nom;
	String avg; //la moyenne de l'eleve
	double somme=0; //somme des notes
	int n; //nbre de note	
	double count=10; //compte le nombre de clic mais affiche egalement la note
	JLabel note; //designe la note de l'eleve
	JButton buttonPlus;
	JButton buttonMoins;
	JScrollPane scroll;
	JButton buttonAfficher;
	JButton buttonCalcul;
	JButton buttonLecture;
	JButton buttonSuppr;
	JTextArea areaAffichage;
	JTextField fieldMoyenne;
	JTextField fieldNom;
	JButton buttonReset;
	private static final long serialVersionUID = 1L;
	
    //Mon construceur
	public FenetreMoyenne(String titre) throws HeadlessException {
		super();		
		this.setTitle(titre);		
		JPanel csp = this.getChampsSaisiePanel();
		JPanel ca = this.getChampAffichage();
			
		//ajout d'un listener au bouton
		MyActionListener mal = new MyActionListener();
		this.buttonPlus.addActionListener(mal);	
		this.buttonMoins.addActionListener(mal);
		this.buttonAfficher.addActionListener(mal);
		this.buttonCalcul.addActionListener(mal);
		this.buttonLecture.addActionListener(mal);
		this.buttonSuppr.addActionListener(mal);
		this.buttonReset.addActionListener(mal);
		this.getContentPane().add(csp, BorderLayout.CENTER);
		this.getContentPane().add(ca, BorderLayout.EAST);
		this.pack();
		this.setLocationRelativeTo(null); // centrer la fenêtre
		//this.setResizable(false); //empeche le redimensionnement avec false
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Termine le processus
		//lorsqu'on clique sur la croix rouge
		
	}

	protected JPanel getChampsSaisiePanel(){
		// création des composants
		JPanel panelChampsNote 	= new JPanel();
		JLabel labelNom 		= new JLabel("Nom:");
		JLabel labelNote 		= new JLabel("Note:");
		fieldNom            	= new JTextField("");
		this.note 	            = new JLabel("10"); //revoir
		this.buttonPlus         = new JButton("+");
		this.buttonMoins        = new JButton("-");
		buttonAfficher          = new JButton("Afficher la note saisie");
		buttonCalcul            = new JButton("Calculer moyenne et insérer");
		buttonLecture           = new JButton("Lire les données de la base");
		buttonSuppr 			= new JButton("Supprimer une ligne");
		JLabel  labelMoyenne    = new JLabel("Moyenne");
		fieldMoyenne = new JTextField();
		buttonReset 			= new JButton("Réinitialiser l'affichage");
		

		// Définition du layout
		panelChampsNote.setLayout(new GridLayout(7,2));

		// ajout des composants
		panelChampsNote.add(labelNom);
		panelChampsNote.add(fieldNom);
		panelChampsNote.add(labelNote);
		panelChampsNote.add(note);
		panelChampsNote.add(buttonPlus);
		panelChampsNote.add(buttonMoins);
		panelChampsNote.add(buttonAfficher);
		panelChampsNote.add(buttonCalcul);
		panelChampsNote.add(buttonLecture);
		panelChampsNote.add(buttonSuppr);
		panelChampsNote.add(labelMoyenne);
		panelChampsNote.add(fieldMoyenne);
		panelChampsNote.add(buttonReset);
		return panelChampsNote;
	}
	protected JPanel getChampAffichage(){
		// création des composants
		JPanel panelChampAffichage = new JPanel(new BorderLayout()); 	
		areaAffichage              = new JTextArea(20,40);
		this.scroll                =new JScrollPane(areaAffichage);
		areaAffichage.setEditable(false);
		// ajout des composants

		panelChampAffichage.add(scroll,BorderLayout.CENTER);
		return panelChampAffichage;

	}
	// methode qui enregistre le nom et la moyenne dans la bdd
	public static void enregistrer(String nom,String avg){
			String url = "jdbc:mysql://localhost:3306/bdd_eleves";
			String utilisateur = "root";
			String motDePasse = "";
			Connection con = null;

		try{  						
			con = DriverManager.getConnection( url,utilisateur,motDePasse); //connexion a la bdd
			System.out.println("Connexion etablie");
			//requete insertion
			String sql = "INSERT INTO eleves (nom,moyenne) VALUES(\""+nom+"\",\""+avg+"\")"; 
			Statement statement = con.createStatement(); //état correspondant à une requete
			int rowsInserted = statement.executeUpdate(sql);
			if (rowsInserted > 0) {
			    System.out.println("Vous avez ajouté une ligne!");
			}			
		}
		catch(SQLException e){
			e.printStackTrace();
		}		
		finally {
		    if ( con != null )
		        try {
		            /* Fermeture de la connexion */
		            con.close();
		        } catch ( SQLException ignore ) {
		            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
		        }
		}
	}
	
	
	// methode qui affiche toutes les lignes dans la bdd
		public  void demander(){
				String url = "jdbc:mysql://localhost:3306/bdd_eleves";
				String utilisateur = "root";
				String motDePasse = "";
				Connection con = null;

			try{  						
				con = DriverManager.getConnection( url,utilisateur,motDePasse); //connexion a la bdd
				System.out.println("Connexion etablie");
				//requete insertion
				String sql = "SELECT * FROM eleves"; 
				Statement statement = con.createStatement(); //état correspondant à une requete
				ResultSet result = statement.executeQuery(sql);
				/* Récupération des données du résultat de la requête de lecture */
				
				while ( result.next() ) {
				    int idEleve = result.getInt( "id" );
				    String nomEleve = result.getString( "nom" );
				    String moyenneEleve = result.getString( "moyenne" );
				    String NewL = System.getProperty("line.separator");

				    /* Traiter ici les valeurs récupérées. */
				    
				    System.out.println(idEleve +": " + nomEleve+" "  + moyenneEleve);
				    areaAffichage.append(idEleve +": " + nomEleve+" a une moyenne de "  + moyenneEleve + NewL);				    
				}				
			}
			catch(SQLException e){
				e.printStackTrace();
			}		
			finally {
			    if ( con != null )
			        try {
			            /* Fermeture de la connexion */
			            con.close();
			        } catch ( SQLException ignore ) {
			            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
			        }
			}
		}
		
		// methode qui supprime la ligne correspondante au nom entré en parametre
		public static void supprimer(String nom){
				String url = "jdbc:mysql://localhost:3306/bdd_eleves";
				String utilisateur = "root";
				String motDePasse = "";
				Connection con = null;

			try{  						
				con = DriverManager.getConnection( url,utilisateur,motDePasse); //connexion a la bdd
				System.out.println("Connexion etablie");
				//requete insertion
				String sql = "DELETE FROM eleves WHERE nom =\""+nom+"\""; 
				Statement statement = con.createStatement(); //état correspondant à une requete
				int rowsDeleted = statement.executeUpdate(sql);
				if (rowsDeleted > 0) {
				    System.out.println("Vous avez supprimé une ligne!");
				}			
			}
			catch(SQLException e){
				e.printStackTrace();
			}		
			finally {
			    if ( con != null )
			        try {
			            /* Fermeture de la connexion */
			            con.close();
			        } catch ( SQLException ignore ) {
			            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
			        }
			}
		}
	
	
	
	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == buttonPlus){
				if(count<20)
				count++;
				note.setText(count+"");
			}
			else if (event.getSource() == buttonMoins){
				if(count>0)
				count--;
				note.setText(count+"");
			}
			else if (event.getSource() == buttonAfficher){
				somme = somme + count;
				n++;
				areaAffichage.append(count+"\n");
				note.setText("10");
				count=10;
				
			}
			else if (event.getSource() == buttonCalcul){
				double moy=somme/n;
				System.out.println(moy);
				fieldMoyenne.setText(moy+"");
				nom = fieldNom.getText();
				avg= moy+"";
				enregistrer(nom,avg);
				//reinitialisons n et somme apres l'enregistrement dans la base de donnee
				n=0;
				somme=0;			
				areaAffichage.setText("");
				avg= 0+"";
			}
			else if (event.getSource() == buttonLecture){
				areaAffichage.setText("");
				demander();								
			}
			else if (event.getSource() == buttonSuppr){
				areaAffichage.setText("");
				nom = fieldNom.getText();				
				supprimer(nom);
				//reinitialisons n et somme apres l'enregistrement dans la base de donnee			
				areaAffichage.setText(nom + " a été supprimé de la bdd");
			}
			else if (event.getSource() == buttonReset){
				areaAffichage.setText("");							
			}
		}
	}

}


