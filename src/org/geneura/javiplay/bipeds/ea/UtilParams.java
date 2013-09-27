package org.geneura.javiplay.bipeds.ea;

import java.io.FileInputStream;
import java.util.Properties;


import es.ugr.osgiliath.util.impl.HashMapParameters;

public class UtilParams {

	public static HashMapParameters LoadParamsFromFile(String filename) {
		HashMapParameters params = new HashMapParameters();

		Properties defaultProps = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(filename);
			defaultProps.load(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		params.setup(defaultProps);
		return params;

	}

}
