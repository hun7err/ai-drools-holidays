package com.sample;

import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");
			// go !
			Holiday holiday = new Holiday();
			ksession.insert(holiday);
			
			Options options = new Options();
			ksession.insert(options);

			ksession.fireAllRules();
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	public static class ChosenContinents {
		private Vector<String> names;
		public ChosenContinents(Vector<String> v) {
			this.names = (Vector <String>) v.clone();
		}
	}
	
	public static class ChosenCountries {
		private Vector <String> names;
	}
	
	public static class ChosenAge {
		private String age;
		public ChosenAge(String a) {
			this.age = a;
		}
	}
	
	public static class ChosenDiseases {
		private Vector <String> names;
		public ChosenDiseases(Vector<String> v) {
			this.names = (Vector <String>) v.clone();
		}
	}

	public static class ChosenSkills {
		private Vector <String> names;
		public ChosenSkills(Vector<String> v) {
			this.names = (Vector <String>) v.clone();
		}
		/*public Vector <String> getSkills() {
			return this.names;
		}
		public int getSkillsNumber() {
			return this.names.size();
		}*/
		public String [] generateWillingness() {
			int j = 0;
			Options o = new Options();
			String [] options = new String [o.skills.length - names.size()];
			for(int i=0; i<o.skills.length; ++i) {
				if (! ((Vector <String>) this.names).contains(o.skills[i])) {
					options[j] = o.skills[i]; 
					++j;
				}
			}
			return options;
		}
	}

	public static class ChosenWillingness {
		private Vector <String> names;
		public ChosenWillingness(Vector<String> v) {
			this.names = (Vector <String>) v.clone();
		}
	}
	
	public static class ChosenInterests {
		private Vector <String> names;
		public ChosenInterests(Vector<String> v) {
			this.names = (Vector <String>) v.clone();
		}
	}
	
	public static class ChosenWayOfSpendingTime {
		private String name;
		public ChosenWayOfSpendingTime(String s) {
			this.name = s;
		}
	}
	
	public static class ChosenBudget {
		private int budget;
		public ChosenBudget (int b) {
			this.budget = b;
		}
	}
	
	public static class ChosenLengthOfHoliday {
		private int days;
		public ChosenLengthOfHoliday (int d) {
			this.days = d;
		}
	}
	
	
	
	public static class Options {
		public String [] continents = new String []
			{"Afryka", "Ameryka południowa", "Ameryka północna", "Azja", "Europa" };
		//public String [] countriesAfryka = new String []
		//		{ "Algieria", "Egipt", "Madagaskar", "Nigeria", "Senegal"};
		public String [] skills = new String [] 
				{"Jazda na nartach", "Nurkowanie"};
		public String [] diseases = new String [] 
				{"Złe znoszenie wysokich temperatur", "Choroba lokomocyjna", "Strach przed lataniem samolotem"};
		public String [] age = new String []
				{"dzieci / młodzież", "rodzice z dziećmi", "dorośli", "osoby w starszym wieku"};
		public String [] wayOfSpendingTime = new String [] 
				{"Aktywna", "Bierna"};
		public String [] interests = new String [] 
				{"historia", "kultura"};
			
	}
	
	public static class Holiday {

		/** 
		 * window used to get budget and length of the stay
		 * - one textfield (not empty, integer)
		 * */
		public int windowInt(String title, String label, String errorNull, String errorNotInt, int min, String errorTooSmall) {
			
			boolean correctData;
			String data;
			do {
				correctData = true; 
				data = JOptionPane.showInputDialog (null, label, title, JOptionPane.PLAIN_MESSAGE);

				if (data.isEmpty()) {
					JOptionPane.showMessageDialog(null, errorNull, "Invalid data", JOptionPane.ERROR_MESSAGE); 
					correctData = false;
				}
				
				for (int i=0; (i<data.length()) && (correctData == true); ++i) {
					if (!Character.isDigit(data.charAt(i))) {
						JOptionPane.showMessageDialog(null, errorNotInt, "Invalid data", JOptionPane.ERROR_MESSAGE); 
						correctData = false;
					}
				}
				
				if ((correctData == true) && (Integer.parseInt(data) < min)) {
					JOptionPane.showMessageDialog(null, errorTooSmall, "Invalid data", JOptionPane.ERROR_MESSAGE); 
					correctData = false;
				}
				
			} while (correctData == false);

			return Integer.parseInt(data);
		}

		/** 
		 * window used to get continents, counties etc
		 * - checkbox
		 * */
		public Vector <String> windowCheckbox(String [] options, String title, String message, boolean acceptEmpty) {
			Vector <String> result = new Vector<String>();
	    	boolean correctData;
			
	    	JCheckBox [] checkbox = new JCheckBox [options.length];
	    	for (int i=0; i<options.length; ++i)
	    		checkbox[i] = new JCheckBox(options[i]);
		     
		    Object [] params = new Object [(options.length)+1]; 
		    params[0] = message;
		    for (int i=0; i<options.length; ++i)
		    	params[i+1] = checkbox[i];
		    Object[] ok = {"OK"};
		    
		    do {
		    	correctData = false;
		    	JOptionPane.showOptionDialog(null, params, title, JOptionPane.OK_OPTION, 
		    			JOptionPane.PLAIN_MESSAGE, null, ok, ok[0]);
			
		    	//checking if at least one item is selected
				for (int i=0; i<options.length; ++i) {
				    if (checkbox[i].isSelected()) {
				    	result.add(options[i]);
				    	correctData = true;
				    }
				}
			
				if(correctData == false) {
					if (acceptEmpty == true) {
						Object[] opt = { "OK", "CANCEL" };
						int decision = JOptionPane.showOptionDialog(null, "Nie zaznaczono żadnej opcji. Czy na pewno chcesz kontynuować?", 
								"Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opt, opt[0]);
						if (decision == 0) {
							correctData = true;
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "You have to check at least one option", 
								"Invalid data", JOptionPane.ERROR_MESSAGE); 
					}
				}
		    } while (correctData == false);
		    
			return result;
		}
		
		/** 
		 * window used to get age
		 * - list (one choice)
		 * */
		public String windowChooseOne(String [] options, String title, String message, int defaultOption) {
			Object selectedValue = JOptionPane.showInputDialog(null, message, title,
			        JOptionPane.INFORMATION_MESSAGE, null, options, options[defaultOption]);
		    
			return (String) selectedValue;
		}
		
		
	}


}

