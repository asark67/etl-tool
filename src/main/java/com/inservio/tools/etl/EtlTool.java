package com.inservio.tools.etl;

import scriptella.execution.EtlExecutor;
import scriptella.execution.ExecutionStatistics;
import scriptella.util.CollectionUtils;
import scriptella.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by asark on 17/09/2015.
 */
public class EtlTool {
  private static final Logger log = Logger.getLogger(EtlTool.class.getName());

  private Properties properties = new Properties();

  public EtlTool(Properties properties) throws Exception {
    File file =  new File("config/config.properties");
    if (file.exists()) {
      this.properties.load(new FileInputStream(file));
    }
    String userDefinedProperties = properties.getProperty("properties");
    if (userDefinedProperties != null) {
      File userFile = new File(userDefinedProperties);
      if (userFile.exists()) this.properties.load(new FileInputStream(userFile));
    }
    this.properties.putAll(properties);
  }

  public void process(File template) throws Exception {
    if (template==null) throw new Exception("template file not specified");
    if (!template.exists()) throw new FileNotFoundException("cannot find "+template.getAbsolutePath());
    EtlExecutor executor = EtlExecutor.newExecutor(IOUtils.toUrl(template) , CollectionUtils.asMap(properties));
    ExecutionStatistics results = executor.execute();
    log.info(results.toString());
  }

  public static void main(String args[]) throws Exception {
    Properties props = new Properties();
    File template = null;

    for (String arg : args) {
      if (arg.startsWith("/")) {
        String[] elements = arg.substring(1).split("=");
        log.fine(elements[0] + "=" + elements[1]);
        if (elements[0].equalsIgnoreCase("TEMPLATE"))
          template = new File("etl/"+elements[1]);
        else
          props.setProperty(elements[0], elements[1]);
      }
    }

    EtlTool tool = new EtlTool(props);

    if (template != null) {
      tool.process(template);
    } else {
      System.out.println("usage: "+tool.getClass().getName()+" /template=[template-name] {[/property_name=property_value] [/property_name=property_value] ...}");
      System.out.println("\ttemplate-name lives in the etl folder");
      System.exit(-1);
    }

  }
}
