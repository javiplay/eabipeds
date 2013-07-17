package org.geneura.javiplay.ga.bipeds;

import javax.swing.JFrame;

import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedMain;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedSetting;
import org.jbox2d.testbed.framework.TestbedSetting.SettingType;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class FirstGeneticBiped {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 	TestbedMain.main(null);
		TestbedModel model = new TestbedModel();         // create our model
		// add tests
		//TestList.populateModel(model);                   // populate the provided testbed tests
		model.addCategory("My Super Tests");             // add a category
		TestbedTest test = new BipedSimulator();
		
		model.addTest(test);                // add our test						
		
		
		// add our custom setting "My Range Setting", with a default value of 10, between 0 and 20
		model.getSettings().addSetting(new TestbedSetting("My Range Setting", SettingType.ENGINE, 10, 0, 20));
		
		

		TestbedPanel panel = new TestPanelJ2D(model);    // create our testbed panel

		JFrame testbed = new TestbedFrame(model, panel); // put both into our testbed frame
		
		// etc
		testbed.setVisible(true);

		int testNumber = 0;
		for (int i=0; i<model.getTestsSize(); i++){
			if (model.isTestAt(i)) {
				if (test.getTestName().equals(model.getTestAt(i).getTestName())) 
					{
					    testNumber = i;
						break;
					}
			}
		}
			
			
		model.setCurrTestIndex(testNumber);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
