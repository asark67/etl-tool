package com.inservio.tools;

import com.inservio.tools.etl.driver.oracle.SecureDriver;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.util.logging.Logger;

/**
 * Created by asark on 18/09/2015.
 */
public class PasswordTool {
  private final static Logger log = Logger.getLogger(PasswordTool.class.getName());
  private final static String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";
  private final static String IVGENERATOR_CLASS_NAME = "org.jasypt.iv.RandomIvGenerator";
  private final static String PASSWORD_KEY_ENV_VARIABLE = System.getProperty("key.env.var","ETLTOOL_KEY");

  private static StandardPBEStringEncryptor enc;

  public PasswordTool() {
    if (enc==null) {
      log.fine("Secure Driver using algorithm: "+ALGORITHM+", ivGeneratorClassName: "+IVGENERATOR_CLASS_NAME+", key_env_variable: "+PASSWORD_KEY_ENV_VARIABLE);
      EnvironmentStringPBEConfig e = new EnvironmentStringPBEConfig();
      e.setAlgorithm(ALGORITHM);
      e.setIvGeneratorClassName(IVGENERATOR_CLASS_NAME);
      e.setPasswordEnvName(PASSWORD_KEY_ENV_VARIABLE);
      enc = new StandardPBEStringEncryptor();
      enc.setConfig(e);
    }
  }

  public String enc(String password) {
    return enc.encrypt(password);
  }

  public static void main(String args[]) throws Exception {
    PasswordTool tool = new PasswordTool();
    if (args.length >= 1) {
      System.out.println(args[0] + " => " + tool.enc(args[0]));
    } else {
      System.out.println("usage: "+tool.getClass().getName()+" [unencrypted password]");
      System.out.println("\treturns encrypted password");
    }
  }
}
