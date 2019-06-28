package com.inservio.tools;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.util.logging.Logger;

/**
 * Created by asark on 18/09/2015.
 */
public class PasswordTool {
  private final static String ALGORITHM = "PBEWithMD5AndDES";
  private final static String PASSWORD_KEY_ENV_VARIABLE = System.getProperty("key.env.var","ETLTOOL-KEY");

  private static StandardPBEStringEncryptor enc;

  public PasswordTool() {
    if (enc==null) {
      EnvironmentStringPBEConfig e = new EnvironmentStringPBEConfig();
      e.setAlgorithm(ALGORITHM);
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
