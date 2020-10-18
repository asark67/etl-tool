package com.inservio.tools.etl.driver.mysql;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import scriptella.jdbc.JdbcConnection;
import scriptella.jdbc.JdbcException;
import scriptella.spi.ConnectionParameters;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asark on 18/09/2015.
 */
public class SecureDriver extends scriptella.driver.mysql.Driver {
  private final static Logger log = Logger.getLogger(SecureDriver.class.getName());
  private final static String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";
  private final static String IVGENERATOR_CLASS_NAME = "org.jasypt.iv.RandomIvGenerator";
  private final static String PASSWORD_KEY_ENV_VARIABLE = System.getProperty("key.env.var","ETLTOOL_KEY");

  private static StandardPBEStringEncryptor enc;

  public SecureDriver() {
    super();
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

  @Override
  public JdbcConnection connect(ConnectionParameters params) {
    try {
      Pattern pattern = Pattern.compile("(?i)ENC\\((.*)\\)");
      Properties props = new Properties();
      props.putAll(params.getProperties());

      if (params.getUser() != null) {
        props.put("user", params.getUser());
      }
      if (params.getPassword() != null) {
        Matcher matcher = pattern.matcher(params.getPassword());
        if (matcher.find()) {
          try {
            props.put("password", enc.decrypt(matcher.group(1)));
          } catch (EncryptionOperationNotPossibleException e) {
            log.fine("Bad password supplied");
            throw e;
          }
        } else {
          props.put("password", params.getPassword());
        }
      }
      log.fine("Executing connect for user "+params.getUser()+"...");
      return connect(params, props);
    } catch (SQLException e) {
      log.fine(e.getLocalizedMessage());
    }
    throw new JdbcException("Unable to obtain connection for URL " + params.getUrl());
  }
}
